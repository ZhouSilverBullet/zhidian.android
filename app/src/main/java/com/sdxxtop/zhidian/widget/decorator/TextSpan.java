package com.sdxxtop.zhidian.widget.decorator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.text.style.LineBackgroundSpan;
import android.util.TypedValue;

import com.sdxxtop.zhidian.App;

/**
 * Created by Administrator on 2018/5/14.
 */

public class TextSpan implements LineBackgroundSpan {
    private int color;
    private String mText;
    public static String EXCEPTION = "1";//
    public static String NORMAL = "0";
    private String mode = "-1";

    public TextSpan(int color, String text) {
        this.color = color;
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        this.mText = text;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        int oldColor = p.getColor();
        float textSize = p.getTextSize();
        p.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, App.getAppContext().getResources().getDisplayMetrics()));
        float center = (right + left) / 2;
        float textLength = this.mText.length() * p.getTextSize();
        p.setColor(color);
        c.drawText(mText, center - textLength / 2, bottom + (baseline / 2) + 5, p);
        p.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, App.getAppContext().getResources().getDisplayMetrics()));
        p.setColor(oldColor);
    }
}