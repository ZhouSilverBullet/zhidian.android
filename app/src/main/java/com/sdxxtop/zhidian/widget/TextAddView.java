package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.utils.ViewUtil;

/**
 * Created by Administrator on 2018/5/18.
 */

public class TextAddView extends LinearLayout {

    private Context context;

    public TextAddView(Context context) {
        this(context, null);
    }

    public TextAddView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextAddView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);

        doAdd();
        doAdd();
        doAddaddView();
    }

    private void doAddaddView() {

    }

    private void doAdd() {
        LayoutInflater.from(context).inflate(R.layout.view_text_add, this, true);
        addView(lineView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(context, 1));
        findViewById(R.id.btnDelete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });
//        SwipeMenuLayout swipeMenuLayout = new SwipeMenuLayout(context);
//        EditText editText = new EditText(context);
//        swipeMenuLayout.addView(editText, MarginLayoutParams.MATCH_PARENT, ViewUtil.dp2px(context, 45));
//        swipeMenuLayout.addView(lineView(), MarginLayoutParams.MATCH_PARENT, ViewUtil.dp2px(context, 45));
//        addView(swipeMenuLayout, MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT);
    }

    private View lineView() {
        View view = new View(context);
        view.setBackgroundColor(getSimpleColor(R.color.recycler_text_item_line_color));
        return view;
    }

    private int getSimpleColor(int color) {
        return getResources().getColor(color);
    }
}
