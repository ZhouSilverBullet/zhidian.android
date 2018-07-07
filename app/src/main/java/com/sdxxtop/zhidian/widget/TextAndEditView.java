package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;

/**
 * Created by Administrator on 2018/5/15.
 */

public class TextAndEditView extends LinearLayout {

    private TextView editNameText;
    private EditText editText;
    private View editLine;
    private String textViewValue;
    private String editHintValue;
    private boolean isShow;

    public TextAndEditView(Context context) {
        this(context, null);
    }

    public TextAndEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextAndEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextAndEditView, defStyleAttr, 0);
        textViewValue = a.getString(R.styleable.TextAndEditView_taev_text_view);
        editHintValue = a.getString(R.styleable.TextAndEditView_taev_edit_text_hint);
        isShow = a.getBoolean(R.styleable.TextAndEditView_taev_line_is_show, true);
        a.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_text_and_edit, this, true);
        editNameText = (TextView) findViewById(R.id.text_and_edit_name);
        editText = (EditText) findViewById(R.id.text_and_edit_edit);
        editLine = findViewById(R.id.text_and_edit_line);

        if (!isShow) {
            editLine.setVisibility(GONE);
        }

        editNameText.setText(textViewValue);
        editText.setHint(editHintValue);
    }

    public EditText getEditText() {
        return editText;
    }
}
