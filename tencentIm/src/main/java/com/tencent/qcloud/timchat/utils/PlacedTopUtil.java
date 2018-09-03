package com.tencent.qcloud.timchat.utils;

import android.text.TextUtils;

import com.tencent.qcloud.timchat.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/3.
 * <p>
 * 管理帖子是否置顶
 */

public class PlacedTopUtil {

    //老师
    public static void saveTopTeacherIdentify(String companyId, String userId, String identify) {
        String key = "t" + companyId + userId;
        saveTopIdentify(key, identify);
    }

    //家长
    public static void saveTopParentIdentify(String studentId, String userId, String identify) {
        String key = "p" + studentId + userId;
        saveTopIdentify(key, identify);
    }


    public static void saveTopIdentify(String key, String identify) {
        String placeTop = PreferenceUtils.getInstance(MyApplication.getContext()).getStringParam(key);
        if (TextUtils.isEmpty(placeTop)) {
            placeTop = identify;
        } else {
            placeTop = identify + "," + placeTop;
        }
        //最后保存起来
        PreferenceUtils.getInstance(MyApplication.getContext()).saveParam(key, placeTop);
    }

    public static List<String> getTopTeacherIdentify(String companyId, String userId) {
        String key = "t" + companyId + userId;
        return getTopIdentify(key);
    }

    public static List<String> getTopParentIdentify(String studentId, String userId) {
        String key = "p" + studentId + userId;
        return getTopIdentify(key);
    }

    public static List<String> getTopIdentify(String key) {
        List<String> list = new ArrayList<>();
        String placeTop = PreferenceUtils.getInstance(MyApplication.getContext()).getStringParam(key);
        if (!TextUtils.isEmpty(placeTop)) {
            String[] split = placeTop.split(",");
            for (String s : split) {
                list.add(s);
            }
        }
        return list;
    }

    public static void removeIdentify(String key, String identify) {
        List<String> topIdentify = getTopIdentify(key);
        if (topIdentify == null || topIdentify.size() == 0) {
            return;
        }

        if (topIdentify.contains(identify)) {
            topIdentify.remove(identify);
        }

        String temp = "";
        for (String s : topIdentify) {
            temp = temp + s + ",";
        }

        if (temp.length() > 0) {
            temp = temp.substring(0, temp.length() - 1);
        }

        PreferenceUtils.getInstance(MyApplication.getContext()).saveParam(key, temp);

    }

    public static boolean hasInTopIdentify(String selfIdentify, String identify) {
        List<String> topIdentify = getTopIdentify(selfIdentify);
        if (topIdentify == null || topIdentify.size() == 0) {
            return false;
        }

        boolean isIn = false;
        for (String s : topIdentify) {
            if (s.equals(identify)) {
                isIn = true;
                break;
            }
        }

        return isIn;
    }

}
