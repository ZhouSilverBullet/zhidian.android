package com.sdxxtop.zhidian.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;

/**
 * Created by Administrator on 2018/5/8.
 */

public class SubTitleView extends RelativeLayout {

    private View leftLayout;
    private TextView leftText;
    private TextView titleText;
    private TextView rightText;
    private int layoutBgColor;
    private boolean leftNotShow;
    private boolean rightTextIsShow;
    private boolean rightImageIsShow;
    private Drawable rightImageDrawable;

    private String leftTextValue;
    private int leftTextColor;
    private String subTitleValue;
    private boolean lineIsShow;

    private String rightTextValue;
    private int rightTextColor;
    private View bottomLine;
    private ImageView rightImg;

    public SubTitleView(Context context) {
        this(context, null);
    }

    public SubTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SubTitleView, defStyleAttr, 0);
        layoutBgColor = a.getColor(R.styleable.SubTitleView_layoutBg, context.getResources().getColor(R.color.white));
        leftNotShow = a.getBoolean(R.styleable.SubTitleView_leftIsNotShow, false);
        rightTextIsShow = a.getBoolean(R.styleable.SubTitleView_rightTextIsShow, false);
        rightImageIsShow = a.getBoolean(R.styleable.SubTitleView_rightImageIsShow, false);
        leftTextValue = a.getString(R.styleable.SubTitleView_leftText);
        leftTextColor = a.getColor(R.styleable.SubTitleView_leftTextColor, context.getResources().getColor(R.color.textcolor));
        rightTextValue = a.getString(R.styleable.SubTitleView_rightText);
        rightImageDrawable = a.getDrawable(R.styleable.SubTitleView_rightImage);
        rightTextColor = a.getColor(R.styleable.SubTitleView_rightTextColor, context.getResources().getColor(R.color.blue));
        subTitleValue = a.getString(R.styleable.SubTitleView_subTitle);
        lineIsShow = a.getBoolean(R.styleable.SubTitleView_bottomLineIsShow, true);

        a.recycle();
        init();
    }

    private void init() {
        setFitsSystemWindows(true);
        setClipToPadding(true);

        LayoutInflater.from(getContext()).inflate(R.layout.view_sub_title, this, true);
        leftLayout = findViewById(R.id.view_sub_title_left_layout);
        leftText = (TextView) findViewById(R.id.view_sub_title_left_text);
        titleText = (TextView) findViewById(R.id.view_sub_title_title_view);
        rightText = (TextView) findViewById(R.id.view_sub_title_right_text);
        rightImg = (ImageView) findViewById(R.id.view_sub_title_right_img);
        bottomLine = findViewById(R.id.view_sub_title_bottom_line);
        if (lineIsShow) {
            bottomLine.setVisibility(VISIBLE);
        } else {
            bottomLine.setVisibility(GONE);
        }
        titleText.setText(subTitleValue);
        if (TextUtils.isEmpty(leftTextValue)) {
            leftTextValue = "返回";
        }
        leftText.setText(leftTextValue);
        leftText.setTextColor(leftTextColor);

        if (rightTextIsShow) {
            rightText.setVisibility(VISIBLE);
            rightText.setText(rightTextValue);
            rightText.setTextColor(rightTextColor);
        } else {
            rightText.setVisibility(GONE);
        }

        if (rightImageIsShow) {
            rightImg.setVisibility(VISIBLE);
            if (rightImageDrawable != null) {
                rightImg.setImageDrawable(rightImageDrawable);
            }
        } else {
            rightImg.setVisibility(GONE);
        }

        if (leftNotShow) {
            leftLayout.setVisibility(GONE);
        }

        setBackgroundColor(layoutBgColor);

        leftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });
    }

    public void setTitleValue(String value) {
        if (titleText != null) {
            titleText.setText(value);
        }
    }

    public TextView getRightText() {
        return rightText;
    }

    public ImageView getRightImg() {
        return rightImg;
    }

    public TextView getLeftText() {
        return leftText;
    }

    public TextView getTitleText() {
        return titleText;
    }
}
