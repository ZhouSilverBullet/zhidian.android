package com.sdxxtop.zhidian.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2018/5/10.
 */

public class PicUtil {
    public static void displayImage(Context context, String url, ImageView view) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Glide.with(context).load(url).into(view);
    }
}
