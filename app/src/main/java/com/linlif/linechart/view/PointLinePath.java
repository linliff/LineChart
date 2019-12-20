package com.linlif.linechart.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by linlif on 2019-12-01
 */
public class PointLinePath {

    private Paint linePaint;
    private Paint verticaLinePaint;
    private Paint strokePaint;
    private Paint fillPaint;

    private Path linePath;

    public PointLinePath() {

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#FB0000"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(5);

        verticaLinePaint = new Paint();
        verticaLinePaint.setColor(Color.parseColor("#FB0000"));
        verticaLinePaint.setStyle(Paint.Style.STROKE);
        verticaLinePaint.setAntiAlias(true);
        verticaLinePaint.setStrokeWidth(3);

        strokePaint = new Paint();
        strokePaint.setColor(Color.parseColor("#FB0000"));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(4);

        fillPaint = new Paint();
        fillPaint.setColor(Color.parseColor("#FFF5EE"));
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);
        linePath = new Path();

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

    public Paint getVerticaLinePaint() {
        return verticaLinePaint;
    }

    public Paint getStrokePaint() {

        return strokePaint;
    }

    public Paint getFillPaint() {
        return fillPaint;
    }
}
