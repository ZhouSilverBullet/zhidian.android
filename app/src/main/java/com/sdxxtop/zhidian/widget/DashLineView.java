package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.utils.ViewUtil;

/**
 * Created by Administrator on 2018/5/26.
 */

public class DashLineView extends View {

    private Paint mPaint;
    private Path mPath;

    public DashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.lv));
        // 需要加上这句，否则画不出东西
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(ViewUtil.dp2px(getContext(), 3));
        mPaint.setPathEffect(new DashPathEffect(new float[]{15, 5}, 0));

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        int centerY = getHeight() / 2;
//        mPath.reset();
//        mPath.moveTo(0, centerY);
//        mPath.lineTo(getWidth(), centerY);
//        canvas.drawPath(mPath, mPaint);

        int centerX = getWidth() / 2;
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(0, getHeight());
        canvas.drawPath(mPath, mPaint);
    }
}