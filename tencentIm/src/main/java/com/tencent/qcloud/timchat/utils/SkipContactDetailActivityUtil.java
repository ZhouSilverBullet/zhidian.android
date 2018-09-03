package com.tencent.qcloud.timchat.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/8/4.
 */

public class SkipContactDetailActivityUtil {
    public static void skip(Context context, String identifier) {
        String userId;
        String studentId;
        boolean isTeacher = identifier.startsWith("t");
        if (isTeacher) {
            userId = identifier.substring(8, identifier.length());
            studentId = "";
        } else {
            studentId = identifier.substring(1, 9);
            userId = identifier.substring(9, identifier.length());
        }

        Intent intent = new Intent();
        intent.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.ui.activity.ContactDetailActivity");

        if (isTeacher) {
            intent.putExtra("userId", userId);
            intent.putExtra("type", 11);
        } else {
            intent.putExtra("userId", userId);
            intent.putExtra("studentId", studentId);
            intent.putExtra("type", 10);
        }
        context.startActivity(intent);
    }

}
