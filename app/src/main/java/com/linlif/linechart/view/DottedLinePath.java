package com.linlif.linechart.view;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.linlif.linechart.DensityUtil;

/**
 * Created by linlif on 2019-12-01
 */
public class DottedLinePath {

    private Paint textPaint;
    private Paint dottedLinePaint;

    public DottedLinePath(int dottedLineColor, int dottedTextColor) {

        dottedLinePaint = new Paint();
        dottedLinePaint.setStyle(Paint.Style.STROKE);
        dottedLinePaint.setAntiAlias(true);
        dottedLinePaint.setStrokeWidth(2);
        dottedLinePaint.setColor(dottedLineColor);
        dottedLinePaint.setPathEffect(new DashPathEffect(new float[]{15, 15}, 0));

        textPaint = new Paint();
        textPaint.setColor(dottedTextColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.dip2px(11f));

    }


    public Paint getDottedLinePaint() {

        return dottedLinePaint;
    }

    public Paint getTextPaint() {

        return textPaint;
    }
}
