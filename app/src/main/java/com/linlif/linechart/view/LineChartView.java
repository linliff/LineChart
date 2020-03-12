package com.linlif.linechart.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.linlif.linechart.DensityUtil;
import com.linlif.linechart.ListUtil;
import com.linlif.linechart.NumberUtil;
import com.linlif.linechart.R;
import com.linlif.linechart.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by linlif on 2019-12-01
 */
public class LineChartView extends View implements View.OnTouchListener {

    //文本和折线点数据
    private List<String> XLabels = new ArrayList<>();
    private List<Integer> YLabels = new ArrayList<>();
    private List<Float> data = new ArrayList<>();

    //记录每个折线点
    private List<Float> XPoint = new ArrayList<>();
    private List<Float> YPoint = new ArrayList<>();

    private float dottedNum;
    private String dottedDesc;

    //折线画笔
    private PointLinePath linePath;
    //虚线参考价画笔
    private DottedLinePath dottedLinePath;
    //网格线画笔
    private GridLinePaint gridPaint;
    //XY轴文本画笔
    private TextPaint textPaint;

    //横线的数量
    private int horizontalLineSize = 7;

    //竖线的数量
    private int verticalLine = 7;

    //每个单元格的长度
    private int xScale = (Util.getScreenWidth() - DensityUtil.dip2px(53f)) / 6;

    private float yToXWeight = (float) 0.5;
    //每个单元格的高度
    private int yScale = (int) (xScale * yToXWeight);

    //Y轴多出来的部分
    private int yOffSet = 0;

    //Y轴刻度的长度
    private int yNumLength = yScale * (horizontalLineSize - 1);

    //Y轴的长度
    private int yLength = yNumLength;

    //X轴的长度
    private int xLength = xScale * (verticalLine - 1);

    //Y轴文本和线间距
    private int yTextSpan = DensityUtil.dip2px(5f);
    private int yTextPadding = DensityUtil.dip2px(4f);

    //X轴文本 和 线间距
    private int xTextSpan = DensityUtil.dip2px(15f);

    //起始点
    private int startX = DensityUtil.dip2px(40f);
    //起始点
    private int startY = 0;

    //起始点距离边线的间距
    private int pointEndInterval = 0;

    //手势滑动和点击位置
    private int touchPointX;
    private int touchPointY;

    //折线的起始点
    private float lineStartX;
    private float lineEndX;

    //第一个有效数据的位置
    int firstDataPosition;

    //折线点的圆形大小
    private float circleRadius = DensityUtil.dip2px(4f);

    private OnTouchMoveListener mListener;

    private Path dst;
    private float animatedValue;
    private PathMeasure pathMeasure;
    private ValueAnimator animator;

    private boolean showMaxY;
    private boolean showMinY;

    //颜色配置
    private int bgColorStart;
    private int bgColorEnd;
    private int lineColor;
    private int verticalLineColor;
    private int textColor;
    private int dottedLineColor;
    private int dottedTextColor;
    private int gridLineColor;
    private int textSize;
    private boolean textBold;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (context != null && attrs != null) {
            @SuppressLint("CustomViewStyleable")
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChart);
            bgColorStart = typedArray.getColor(R.styleable.LineChart_bgColorStart, 0xFFFFFFFF);
            bgColorEnd = typedArray.getColor(R.styleable.LineChart_bgColorEnd, 0xFFFFFFFF);
            gridLineColor = typedArray.getColor(R.styleable.LineChart_gridLineColor, 0xFFFFFFFF);
            lineColor = typedArray.getColor(R.styleable.LineChart_lineColor, 0xFFFFFFFF);
            verticalLineColor = typedArray.getColor(R.styleable.LineChart_verticalLineColor, 0xFFFFFFFF);
            textColor = typedArray.getColor(R.styleable.LineChart_textColor, 0xFFFFFFFF);
            textSize = typedArray.getDimensionPixelSize(R.styleable.LineChart_xyTextSize, DensityUtil.dip2px(11f));
            textBold = typedArray.getBoolean(R.styleable.LineChart_textBold, false);
            dottedLineColor = typedArray.getColor(R.styleable.LineChart_dottedLineColor, 0xFFFFFFFF);
            dottedTextColor = typedArray.getColor(R.styleable.LineChart_dottedTextColor, 0xFFFFFFFF);
            typedArray.recycle();
        }

        gridPaint = new GridLinePaint(gridLineColor);
        textPaint = new TextPaint(textColor, textSize, textBold);
        linePath = new PointLinePath(lineColor, verticalLineColor);
        dottedLinePath = new DottedLinePath(dottedLineColor, dottedTextColor);

        dst = new Path();
        pathMeasure = new PathMeasure();

        setOnTouchListener(this);
        setClickable(true);

        //关闭硬件加速可以显示虚线
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    public LineChartView(Context context, AttributeSet attrs, int defStyle) {
        this(context, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

            float currentY = event.getY();
            int scrollY = touchPointY == 0 ? 0 : (int) (currentY - touchPointY);

            touchPointY = (int) event.getY();

            if (scrollY != 0 && (scaledTouchSlop < scrollY || scrollY < -scaledTouchSlop)) {
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }

        }

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {

            //小于1不处理
            if (-1 < touchPointX - event.getX() && touchPointX - event.getX() < 1) {
                return false;
            }

            touchPointX = (int) event.getX();

            getParent().requestDisallowInterceptTouchEvent(true);

            invalidate();

            if (mListener != null) {
                mListener.onTouchEvent(event);
            }

            return true;
        }

        getParent().requestDisallowInterceptTouchEvent(false);

        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int finaDensityHeight = yLength + xTextSpan * 2;

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        setMeasuredDimension(sizeWidth, (modeHeight == MeasureSpec.UNSPECIFIED) ? finaDensityHeight : sizeHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //绘制背景色
        drawBackgroundColor(canvas);

        //竖线 和 X轴文本
        drawXText(canvas);

        //横线
        for (int i = 0; i < horizontalLineSize; i++) {

            if (i == 0) {
                canvas.drawLine(startX, startY, startX + xLength, startY, gridPaint.getGridPaint());
            } else if (i == horizontalLineSize - 1) {
                canvas.drawLine(startX, startY + i * yScale + yOffSet * 2, startX + xLength, startY + i * yScale + yOffSet * 2, gridPaint.getGridPaint());
            } else {
                canvas.drawLine(startX, startY + i * yScale + yOffSet, startX + xLength, startY + i * yScale + yOffSet, gridPaint.getGridPaint());
            }
        }

        //Y轴文本
        for (int i = 0; i < YLabels.size(); i++) {
            String yLabel = String.valueOf(YLabels.get(i));
            if (!showMaxY && i == YLabels.size() - 1) {
                //不显示Y轴最大值
            } else if (!showMinY && i == 0) {
                //不显示Y轴最小值
            } else {
                canvas.drawText(yLabel, startX - (yLabel.length() * yTextSpan + DensityUtil.dip2px(10f)), startY + yLength - (i * yScale + yOffSet) + yTextPadding, textPaint.getTextPaint());
            }

        }

        int max = getMaxTmp(); //取Y轴最大数
        int min = getMinTmp(); //取Y轴最小数
        int diff = max > min ? max - min : 1;

        float yInterval = (float) yNumLength / diff; //平分Y轴
        float xInterval = data.size() > 0 ? (float) (xLength - pointEndInterval) / (data.size() - 1) : 0; //平分X轴

        //绘制价格折线
        drawPolyLine(canvas, max, xInterval, yInterval);

        //绘制手势跟随的线
        drawTouchLine(canvas, xInterval);

        super.onDraw(canvas);
    }

    private void drawXText(Canvas canvas) {

        int dataInterval = (XLabels.size() - 1) / 6;

        if (dataInterval == 0) {
            dataInterval = 1;
        }

        float XInterval = (float) (xLength - pointEndInterval) / (XLabels.size() - 1);

        int dataOffset = 0;
        if (XLabels.size() >= 7) {
            dataOffset = (XLabels.size() - 1) % 6;
        }

        int xOffset = 0;

        for (int i = 0; i < XLabels.size() - dataOffset; i++) {

            if ((i % dataInterval) == 0) {

                if (i != 0 && dataOffset != 0 && xOffset < dataOffset) {
                    xOffset++;
                }

                int realPos = i + xOffset;

                if (realPos >= XLabels.size()) {
                    realPos = XLabels.size() - 1;
                }

                float xLine = xLength - pointEndInterval + startX - realPos * XInterval;

                String xLabel = XLabels.get(realPos);

                if (realPos == 0) {
                    canvas.drawLine(xLine + pointEndInterval, startY, xLine + pointEndInterval, startY + yLength, gridPaint.getGridPaint());
                } else {
                    canvas.drawLine(xLine, startY, xLine, startY + yLength, gridPaint.getGridPaint());
                }

                if (realPos == 0) {
                    canvas.drawText(xLabel, xLine - xTextSpan, startY + yLength + xTextSpan, textPaint.getTextPaint());
                } else if (realPos != XLabels.size() - 1) {
                    canvas.drawText(xLabel, xLine - xTextSpan, startY + yLength + xTextSpan, textPaint.getTextPaint());
                }

            }

        }
    }

    private void drawBackgroundColor(Canvas canvas) {

        if (canvas == null) {
            return;
        }

        Paint paint = new Paint();
        LinearGradient backGradient = new LinearGradient(0, 0, 0, yLength, new int[]{bgColorStart, bgColorEnd}, null, Shader.TileMode.CLAMP);
        paint.setShader(backGradient);
        canvas.drawRect(startX, startY, xLength + startX, yLength + startY, paint);
    }

    private void drawTouchLine(Canvas canvas, float xInterval) {

        int position = -1;
        boolean isFirstDara = false;

        if (touchPointX == 0) {
            return;
        }

        if (touchPointX < lineStartX) {
            drawPointCircle(canvas, XPoint.get(firstDataPosition), YPoint.get(firstDataPosition));
            if (mListener != null) {
                if (firstDataPosition > 0 && data.get(firstDataPosition - 1) <= 0) {
                    isFirstDara = true;
                }
                mListener.onTouchMove((int) lineStartX, startY, firstDataPosition, isFirstDara);
            }
            return;
        }

        if (touchPointX > lineEndX) {
            drawPointCircle(canvas, ListUtil.getLastItem(XPoint), ListUtil.getLastItem(YPoint));
            if (mListener != null) {
                mListener.onTouchMove((int) lineEndX, startY, XPoint.size() - 1, false);
            }
            return;
        }

        //绘制滑动轨迹线
        for (int i = 0; i < XPoint.size(); i++) {

            if (XPoint.get(i) - xInterval < touchPointX && touchPointX < XPoint.get(i) + xInterval) {
                position = i;
                if (position > 0 && data.get(position - 1) <= 0) {
                    isFirstDara = true;
                }
                drawPointCircle(canvas, XPoint.get(i), YPoint.get(i));
                touchPointX = Math.round(XPoint.get(i));
                break;

            }
        }

        if (mListener != null) {
            mListener.onTouchMove(touchPointX, startY, position, isFirstDara);
        }
    }

    private void drawPointCircle(Canvas canvas, Float x, Float y) {
        canvas.drawLine(x, startY, x, startY + yLength, linePath.getVerticalLinePaint());
        canvas.drawCircle(x, y, circleRadius, linePath.getStrokePaint());
        canvas.drawCircle(x, y, circleRadius, linePath.getFillPaint());
    }

    private void drawPolyLine(Canvas canvas, int max, float xInterval, float yInterval) {

        if (canvas == null) {
            return;
        }

        float pointX = 0, pointY = 0; //每个价格点的坐标

        boolean isFirstPoint = false;
        XPoint.clear();
        YPoint.clear();
        for (int i = 0; i < data.size(); i++) {

            pointX = startX + i * xInterval;
            pointY = yOffSet + startY + (max - data.get(i)) * yInterval;

            XPoint.add(pointX);
            YPoint.add(pointY);

            if (data.get(i) != 0 && !isFirstPoint) {//设置折线起点
                isFirstPoint = true;
                firstDataPosition = i;
                lineStartX = pointX;
                linePath.moveTo(pointX, pointY);
                canvas.drawLine(startX, pointY, pointX, pointY, linePath.getDottedLinePaint());
            } else if (i == data.size() - 1 && data.get(i) != 0) {
                linePath.lineTo(pointX, pointY);
                lineEndX = pointX;
            } else if (data.get(i) != 0) {
                linePath.lineTo(pointX, pointY);
            }

        }

        //绘制折线
        if (linePath.getLinePath() != null) {
//            canvas.drawPath(linePath.getLinePath(), linePath.getLinePaint());

            pathMeasure.setPath(linePath.getLinePath(), false);
            pathMeasure.getSegment(0, animatedValue * pathMeasure.getLength(), dst, true);
            canvas.drawPath(dst, linePath.getLinePaint());

            dst.reset();
            linePath.getLinePath().reset();

        }
        if (touchPointX == 0) {
            String text = NumberUtil.formartPointTwoZero(ListUtil.getLastItem(data));

            float diffY = pointY - startY;
            int xOff = 8;
            if (text.length() > 5) {
                xOff = 11;
            }
            if (diffY < 50) {
                canvas.drawText(text, pointX - text.length() * xOff, pointY + 50, textPaint.getTextPaint());
            } else {
                canvas.drawText(text, pointX - text.length() * xOff, pointY - 20, textPaint.getTextPaint());
            }
        }

        //绘制参考价虚线
        if (dottedLinePath != null && dottedNum != 0) {
            float dottedPointY = startY + (max - dottedNum) * yInterval + yOffSet;
            if (dottedDesc != null) {

                float diffY = (startY + yLength) - dottedPointY;
                if (diffY < 50) {
                    canvas.drawText(dottedDesc, startX + 10, dottedPointY - 25, dottedLinePath.getTextPaint());
                } else {
                    canvas.drawText(dottedDesc, startX + 10, dottedPointY + 50, dottedLinePath.getTextPaint());
                }
            }
            canvas.drawLine(startX, dottedPointY, startX + xLength - pointEndInterval, dottedPointY, dottedLinePath.getDottedLinePaint());
        }

        if (touchPointX == 0) {
            canvas.drawCircle(pointX, pointY, circleRadius, linePath.getStrokePaint());
            canvas.drawCircle(pointX, pointY, circleRadius, linePath.getFillPaint());
        }

    }

    private int getMaxTmp() {
        if (YLabels.size() > 0) {
            return Collections.max(YLabels);
        }
        return 0;
    }

    private int getMinTmp() {
        if (YLabels.size() > 0) {
            return Collections.min(YLabels);
        }
        return 0;
    }

    public void setDootedNum(Float dootedNum, String dottedDesc) {

        this.dottedNum = dootedNum;
        this.dottedDesc = dottedDesc;
    }

    public void setyOffSet(int yOffSet) {

        this.yOffSet = yOffSet;
        this.yLength = yNumLength + yOffSet * 2;
    }

    public void setyToXWeight(float yToXWeight) {
        this.yToXWeight = yToXWeight;
        this.yScale = (int) (xScale * yToXWeight);
        this.yNumLength = yScale * (horizontalLineSize - 1);
        this.yLength = yNumLength + yOffSet * 2;
    }

    public int getStartY() {

        return startY;
    }

    public void setShowMaxY(boolean showMaxY) {
        this.showMaxY = showMaxY;
    }

    public void setShowMinY(boolean showMinY) {
        this.showMinY = showMinY;
    }

    public void setPointEndInterval(int pointEndInterval) {
        this.pointEndInterval = pointEndInterval;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void setData(List<String> XLabels, List<Integer> YLabels, List<Float> data, boolean needAnimator, int touchPointX) {

        if (!ListUtil.isEmpty(XLabels)) {
            this.XLabels.clear();
            this.XLabels.addAll(XLabels);
        }

        if (!ListUtil.isEmpty(YLabels)) {
            this.YLabels.clear();
            this.YLabels.addAll(YLabels);
        }

        if (!ListUtil.isEmpty(data)) {
            this.data.clear();
            this.data.addAll(data);
        }

        this.touchPointX = touchPointX;

        if (needAnimator) {
            initAnimator();
        } else {
            animatedValue = 1;
            invalidate();
        }

    }

    private void initAnimator() {

        abortAnimator();

        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (float) animation.getAnimatedValue();
                LineChartView.this.invalidate();
            }
        });
        animator.start();
    }

    public void abortAnimator() {

        if (animator != null) {
            animator.cancel();
            animator = null;
        }

    }

    public float getStartX() {
        return startX;
    }

    public void setOnTouchListener(OnTouchMoveListener listener) {
        mListener = listener;
    }

    public interface OnTouchMoveListener {

        void onTouchMove(int x, int y, int position, boolean isFirstData);

        void onTouchEvent(MotionEvent event);
    }

}
