package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sdxxtop.zhidian.R;

/**
 * Created by Administrator on 2018/5/15.
 */

public class SwipeItemView extends LinearLayout {
    public SwipeItemView(Context context) {
        this(context, null);
    }

    public SwipeItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_swipe_text_and_text, this, true);

    }
}
