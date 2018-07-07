package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.sdxxtop.zhidian.utils.ViewUtil;

/**
 * Created by Administrator on 2018/5/29.
 */

public class MaxHeightScrollView extends ScrollView {
    private Context mContext;

    public MaxHeightScrollView(Context context) {
        super(context);
        init(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            //最大高度显示为屏幕内容高度的一半
//            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
//            DisplayMetrics d = new DisplayMetrics();
//            display.getMetrics(d);
            //此处是关键，设置控件高度不能超过屏幕高度一半（d.heightPixels / 2）（在此替换成自己需要的高度）
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(ViewUtil.dp2px(mContext, 150), MeasureSpec.AT_MOST);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}