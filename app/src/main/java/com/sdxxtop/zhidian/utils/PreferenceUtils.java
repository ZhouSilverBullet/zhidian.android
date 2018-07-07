package com.sdxxtop.zhidian.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.model.ConstantValue;


/**
 * 作者: CaiCM
 * 日期: 2018/3/21 时间: 13:37
 * 邮箱：15010104100@163.com
 * 描述：SharedPreference工具类
 */
public class PreferenceUtils {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor shareEditor;

    private static PreferenceUtils preferenceUtils = null;

    public static final String NOTE_TYPE_KEY = "NOTE_TYPE_KEY";

    private PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(ConstantValue.CONFIG, Context.MODE_PRIVATE);
        shareEditor = sharedPreferences.edit();
    }

    public static PreferenceUtils getInstance(Context context) {
        if (preferenceUtils == null) {
            synchronized (PreferenceUtils.class) {
                if (preferenceUtils == null) {
                    preferenceUtils = new PreferenceUtils(context.getApplicationContext());
                }
            }
        }
        return preferenceUtils;
    }

    public String getStringParam(String key) {
        if (ConstantValue.COMPANY_ID.equals(key)) {
            return AppSession.getInstance().getCompanyId();
        }

        if (ConstantValue.USER_ID.equals(key)) {
            return AppSession.getInstance().getUserId();
        }

        return getStringParam(key, "");
    }

    public String getStringParam(String key, String defaultString) {
        return sharedPreferences.getString(key, defaultString);
    }

    public void saveParam(String key, String value) {
        shareEditor.putString(key, value).commit();
    }

    public boolean getBooleanParam(String key) {
        return getBooleanParam(key, false);
    }

    public boolean getBooleanParam(String key, boolean defaultBool) {
        return sharedPreferences.getBoolean(key, defaultBool);
    }

    public void saveParam(String key, boolean value) {
        shareEditor.putBoolean(key, value).commit();
    }

    public int getIntParam(String key) {
        return getIntParam(key, -1);
    }

    public int getIntParam(String key, int defaultInt) {
        return sharedPreferences.getInt(key, defaultInt);
    }

    public void saveParam(String key, int value) {
        shareEditor.putInt(key, value).commit();
    }

    public long getLongParam(String key) {
        return getLongParam(key, 0);
    }

    public long getLongParam(String key, long defaultInt) {
        return sharedPreferences.getLong(key, defaultInt);
    }

    public void saveParam(String key, long value) {
        shareEditor.putLong(key, value).commit();
    }
}
