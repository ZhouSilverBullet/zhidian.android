package com.tencent.qcloud.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/7/31.
 */

public class FixedWidthLayout extends LinearLayout {
    private int widthPixels;
    private boolean isLayout = true;

    public FixedWidthLayout(Context context) {
        this(context, null);
    }

    public FixedWidthLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedWidthLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
    }

    /**
     * 可以进行添加子View吗
     *
     * @param child
     * @param imageLayoutWidth
     * @return
     */
    public boolean canToAddView(View child, int viewWidth, int imageLayoutWidth, int imageWidth) {
        if (!isLayout) { //不能进行添加了
            return false;
        }
        //获取屏幕的宽度
        if (widthPixels == 0) {
            widthPixels = getResources().getDisplayMetrics().widthPixels;
        }
        int childCount = getChildCount();
        int width = 0;
        for (int i = 0; i < childCount; i++) {
            width = width + viewWidth;
        }

        //加上如果宽度大于了屏幕宽度就显示点点
        if (width + viewWidth + imageLayoutWidth > widthPixels) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.more2);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageLayoutWidth, imageLayoutWidth);
            layoutParams.width = imageWidth;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            addView(imageView, layoutParams);
            //设置不能添加的标识，这样就在前面直接返回了，不会重新测量了
            isLayout = false;
            return false;
        }
        addView(child);
        return true;
    }

    public void canLayout(boolean layout) {
        isLayout = layout;
    }
}
