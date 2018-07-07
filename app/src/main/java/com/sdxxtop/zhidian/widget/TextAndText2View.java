package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;

/**
 * Created by Administrator on 2018/5/15.
 */

public class TextAndText2View extends LinearLayout {

    private TextView textNameText;
    private TextView textRightText;
    private TextView textRightImage;
    private View textLine;
    private boolean lineIsShow;
    private boolean imgIsShow;
    private String textViewValue;
    private String textRightValue;
    private TextView textHintText;

    public TextAndText2View(Context context) {
        this(context, null);
    }

    public TextAndText2View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextAndText2View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextAndTextView, defStyleAttr, 0);
        lineIsShow = a.getBoolean(R.styleable.TextAndTextView_tatv_line_is_show, true);
        imgIsShow = a.getBoolean(R.styleable.TextAndTextView_tatv_text_img_is_show, true);
        textViewValue = a.getString(R.styleable.TextAndTextView_tatv_text_view);
        textRightValue = a.getString(R.styleable.TextAndTextView_tatv_text_right_value);
        a.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_text_and_text2, this, true);
        textNameText = (TextView) findViewById(R.id.text_and_text_name);
        textRightText = (TextView) findViewById(R.id.text_and_text_right);
        textHintText = (TextView) findViewById(R.id.text_and_text_hint);
        textRightImage = (TextView) findViewById(R.id.text_and_text_right_image);
        textLine = findViewById(R.id.text_and_text_line);

        if (!lineIsShow) {
            textLine.setVisibility(GONE);
        }

        if (!imgIsShow) {
            textRightImage.setVisibility(GONE);
        }

        textNameText.setText(textViewValue);
        textRightText.setText(textRightValue);
    }

    public TextView getTextRightImage() {
        return textRightImage;
    }

    public TextView getTextRightText() {
        return textRightText;
    }

    public TextView getTextHintText() {
        textHintText.setVisibility(VISIBLE);
        return textHintText;
    }
}
