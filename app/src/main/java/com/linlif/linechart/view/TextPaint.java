package com.linlif.linechart.view;

import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by linlif on 2019-12-01
 */
public class TextPaint {

    private Paint textPaint;

    public TextPaint(int color, int textSize, boolean isBlod) {

        textPaint = new Paint();
        textPaint.setColor(color);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        if (isBlod) {
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

    }

    public Paint getTextPaint() {

        return textPaint;
    }
}
