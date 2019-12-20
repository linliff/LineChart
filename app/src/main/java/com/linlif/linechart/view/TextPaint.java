package com.linlif.linechart.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.linlif.linechart.DensityUtil;

/**
 * Created by linlif on 2019-12-01
 */
public class TextPaint {

    private Paint textPaint;

    public TextPaint() {

        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#313134"));
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.dip2px(11f));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    public Paint getTextPaint() {

        return textPaint;
    }
}
