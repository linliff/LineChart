package com.linlif.linechart.view;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by linlif on 2019-12-01
 */
public class GridLinePaint {

    private Paint gridPaint;

    public GridLinePaint() {

        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#FFFFFF"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(2);
    }


    public Paint getGridPaint() {

        return gridPaint;
    }
}
