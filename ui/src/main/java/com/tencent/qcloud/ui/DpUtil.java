package com.tencent.qcloud.ui;

import android.content.Context;

/**
 * Created by Administrator on 2018/8/24.
 */

public class DpUtil {
    public static int dp2px(Context ctx, int dp) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
