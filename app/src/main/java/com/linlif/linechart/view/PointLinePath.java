package com.linlif.linechart.view;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by linlif on 2019-12-01
 */
public class PointLinePath {

    private Paint linePaint;
    private Paint verticalLinePaint;
    private Paint strokePaint;
    private Paint fillPaint;
    private Paint dottedLinePaint;

    private Path linePath;

    public PointLinePath(int lineColor, int verticalLineColor) {

        linePath = new Path();

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(5);

        verticalLinePaint = new Paint();
        verticalLinePaint.setColor(verticalLineColor);
        verticalLinePaint.setStyle(Paint.Style.STROKE);
        verticalLinePaint.setAntiAlias(true);
        verticalLinePaint.setStrokeWidth(3);

        strokePaint = new Paint();
        strokePaint.setColor(Color.parseColor("#FB0000"));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(4);

        fillPaint = new Paint();
        fillPaint.setColor(Color.parseColor("#FFF5EE"));
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);

        dottedLinePaint = new Paint();
        dottedLinePaint.setStyle(Paint.Style.STROKE);
        dottedLinePaint.setAntiAlias(true);
        dottedLinePaint.setStrokeWidth(5);
        dottedLinePaint.setColor(lineColor);
        dottedLinePaint.setPathEffect(new DashPathEffect(new float[]{15, 15}, 0));

    }


    public void moveTo(float x, float y) {

        if (linePath != null) {
            linePath.moveTo(x, y);
        }

    }

    public void lineTo(float x, float y) {

        if (linePath != null) {
            linePath.lineTo(x, y);
        }

    }

    public Path getLinePath() {

        return linePath;
    }

    public Paint getLinePaint() {

        return linePaint;
    }

    public Paint getVerticalLinePaint() {
        return verticalLinePaint;
    }

    public Paint getStrokePaint() {

        return strokePaint;
    }

    public Paint getFillPaint() {
        return fillPaint;
    }

    public Paint getDottedLinePaint() {
        return dottedLinePaint;
    }
}
