package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.sdxxtop.zhidian.R;

public class SegmentedGroup extends RadioGroup {

    private int mMarginDp;
    private Resources resources;
    private int mTintColor;
    private int mCheckedTextColor = Color.WHITE;
    private LayoutSelector mLayoutSelector;
    private Float mCornerRadius;

    public SegmentedGroup(Context context) {
        super(context);
        resources = getResources();
        mTintColor = resources.getColor(R.color.radio_button_selected_color);
        mMarginDp = (int) getResources().getDimension(R.dimen.radio_button_stroke_border);
        mCornerRadius = getResources().getDimension(R.dimen.radio_button_conner_radius);
        mLayoutSelector = new LayoutSelector(mCornerRadius);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SegmentedGroup,
                0, 0);

        try {
            mMarginDp = (int) typedArray.getDimension(
                    R.styleable.SegmentedGroup_sc_border_width,
                    getResources().getDimension(R.dimen.radio_button_stroke_border));

            mCornerRadius = typedArray.getDimension(
                    R.styleable.SegmentedGroup_sc_corner_radius,
                    getResources().getDimension(R.dimen.radio_button_conner_radius));

            mTintColor = typedArray.getColor(
                    R.styleable.SegmentedGroup_sc_tint_color,
                    getResources().getColor(R.color.radio_button_selected_color));

            mCheckedTextColor = typedArray.getColor(
                    R.styleable.SegmentedGroup_sc_checked_text_color,
                    getResources().getColor(android.R.color.white));

        } finally {
            typedArray.recycle();
        }
    }

    public SegmentedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        resources = getResources();
        mTintColor = resources.getColor(R.color.radio_button_selected_color);
        mMarginDp = (int) getResources().getDimension(R.dimen.radio_button_stroke_border);
        mCornerRadius = getResources().getDimension(R.dimen.radio_button_conner_radius);
        initAttrs(attrs);
        mLayoutSelector = new LayoutSelector(mCornerRadius);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateBackground();
    }

    public void setTintColor(int tintColor) {
        mTintColor = tintColor;
        updateBackground();
    }

    public void setTintColor(int tintColor, int checkedTextColor) {
        mTintColor = tintColor;
        mCheckedTextColor = checkedTextColor;
        updateBackground();
    }

    public void updateBackground() {
        int count = super.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            updateBackground(child);

            if (i == count - 1){ break;}

            LayoutParams initParams = (LayoutParams) child.getLayoutParams();
            LayoutParams params = new LayoutParams(initParams.width, initParams.height, initParams.weight);
            if (getOrientation() == LinearLayout.HORIZONTAL) {
                params.setMargins(0, 0, -mMarginDp, 0);
            } else {
                params.setMargins(0, 0, 0, -mMarginDp);
            }
            child.setLayoutParams(params);
        }
    }

    private void updateBackground(View view) {
        int checked = mLayoutSelector.getSelected();
        int unchecked = mLayoutSelector.getUnselected();
        //Set text color
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                {android.R.attr.state_pressed},
                {-android.R.attr.state_pressed, -android.R.attr.state_checked},
                {-android.R.attr.state_pressed, android.R.attr.state_checked}},
                new int[]{Color.GRAY, mTintColor, mCheckedTextColor});
        ((Button) view).setTextColor(colorStateList);

        Drawable checkedDrawable = resources.getDrawable(checked).mutate();
        Drawable uncheckedDrawable = resources.getDrawable(unchecked).mutate();
        ((GradientDrawable) checkedDrawable).setColor(mTintColor);
        ((GradientDrawable) checkedDrawable).setStroke(mMarginDp, mTintColor);
        ((GradientDrawable) uncheckedDrawable).setStroke(mMarginDp, mTintColor);
        ((GradientDrawable) checkedDrawable).setCornerRadii(mLayoutSelector.getChildRadii(view));
        ((GradientDrawable) uncheckedDrawable).setCornerRadii(mLayoutSelector.getChildRadii(view));

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, uncheckedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkedDrawable);

        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(stateListDrawable);
        } else {
            view.setBackgroundDrawable(stateListDrawable);
        }
    }

    private class LayoutSelector {

        private int children;
        private int child;
        private final int SELECTED_LAYOUT = R.drawable.radio_checked;
        private final int UNSELECTED_LAYOUT = R.drawable.radio_unchecked;

        private float r;
        private final float r1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , 0.1f, getResources().getDisplayMetrics());
        private final float[] rLeft;
        private final float[] rRight;
        private final float[] rMiddle;
        private final float[] rDefault;
        private final float[] rTop;
        private final float[] rBot;
        private float[] radii;

        public LayoutSelector(float cornerRadius) {
            children = -1;
            child = -1;
            r = cornerRadius;
            rLeft = new float[]{r, r, r1, r1, r1, r1, r, r};
            rRight = new float[]{r1, r1, r, r, r, r, r1, r1};
            rMiddle = new float[]{r1, r1, r1, r1, r1, r1, r1, r1};
            rDefault = new float[]{r, r, r, r, r, r, r, r};
            rTop = new float[]{r, r, r, r, r1, r1, r1, r1};
            rBot = new float[]{r1, r1, r1, r1, r, r, r, r};
        }

        private int getChildren() {
            return SegmentedGroup.this.getChildCount();
        }

        private int getChildIndex(View view) {
            return SegmentedGroup.this.indexOfChild(view);
        }

        private void setChildRadii(int newChildren, int newChild) {


            if (children == newChildren && child == newChild){
                return;
            }



            children = newChildren;
            child = newChild;


            if (children == 1) {
                radii = rDefault;
            } else if (child == 0) {
                radii = (getOrientation() == LinearLayout.HORIZONTAL) ? rLeft : rTop;
            } else if (child == children - 1) {
                radii = (getOrientation() == LinearLayout.HORIZONTAL) ? rRight : rBot;
            } else {
                radii = rMiddle;
            }
        }


        public int getSelected() {
            return SELECTED_LAYOUT;
        }


        public int getUnselected() {
            return UNSELECTED_LAYOUT;
        }


        public float[] getChildRadii(View view) {
            int newChildren = getChildren();
            int newChild = getChildIndex(view);
            setChildRadii(newChildren, newChild);
            return radii;
        }
    }
}