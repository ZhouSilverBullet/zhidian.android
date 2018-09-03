package com.tencent.qcloud.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/7/31.
 */

public class TextAndSwitchView extends LinearLayout {

    private String textValue;
    private boolean lineIsShow;
    private TextView textView;
    private CheckBox checkBox;
    private boolean isCheck;
    private View line;

    public TextAndSwitchView(Context context) {
        this(context, null);
    }

    public TextAndSwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextAndSwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TextAndSwitchView, defStyleAttr, 0);
        textValue = typedArray.getString(R.styleable.TextAndSwitchView_tasv_text_view);
        isCheck = typedArray.getBoolean(R.styleable.TextAndSwitchView_tasv_text_is_check, false);
        lineIsShow = typedArray.getBoolean(R.styleable.TextAndSwitchView_tasv_line_is_show, true);

        typedArray.recycle();

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_text_and_switch, this, true);
        textView = (TextView) findViewById(R.id.view_text_and_switch_value);
        checkBox = (CheckBox) findViewById(R.id.view_text_and_switch_check);
        line = findViewById(R.id.view_text_and_switch_line);

        textView.setText(textValue);
        if (lineIsShow) {
            line.setVisibility(VISIBLE);
        } else {
            line.setVisibility(GONE);
        }
        checkBox.setChecked(isCheck);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
