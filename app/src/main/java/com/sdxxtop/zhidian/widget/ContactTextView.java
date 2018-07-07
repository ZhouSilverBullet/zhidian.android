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
 * Created by Administrator on 2018/5/30.
 */

public class ContactTextView extends LinearLayout {

    private TextView nameText;
    private TextView valueText;
    private View line;
    private String name;
    private String value;
    private boolean isShow;

    public ContactTextView(Context context) {
        this(context, null);
    }

    public ContactTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContactTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ContactTextView, defStyleAttr, 0);
        name = a.getString(R.styleable.ContactTextView_ctv_text_name);
        value = a.getString(R.styleable.ContactTextView_ctv_text_value);
        isShow = a.getBoolean(R.styleable.ContactTextView_ctv_line_is_show, true);
        a.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_text, this, true);
        nameText = (TextView) findViewById(R.id.contact_text_name);
        valueText = (TextView) findViewById(R.id.contact_text_value);
        line = findViewById(R.id.contact_text_line);

        if (isShow) {
            line.setVisibility(VISIBLE);
        } else {
            line.setVisibility(GONE);
        }

        nameText.setText(name);
        valueText.setText(value);

    }

    public TextView getValueText() {
        setVisibility(VISIBLE);
        return valueText;
    }
}
