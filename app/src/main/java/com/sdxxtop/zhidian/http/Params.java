package com.sdxxtop.zhidian.http;

import android.content.Context;
import android.text.TextUtils;

import com.sdxxtop.zhidian.App;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.utils.DeviceUtil;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.StringUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/7.
 */

public class Params {
    protected Context context;
    protected HashMap<String, String> map;

    public Params() {
        map = new HashMap<>();
        context = App.getContext();
        defaultValue();
    }

    private void defaultValue() {
        map.put("ci", PreferenceUtils.getInstance(context).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(context).getStringParam(ConstantValue.USER_ID));
    }

    public String getUserId() {
        String ui = map.get("ui");
        if (TextUtils.isEmpty(ui)) {
            ui = PreferenceUtils.getInstance(context).getStringParam(ConstantValue.USER_ID);
        }
        return ui;
    }

    public String getCompanyId() {
        String ci = map.get("ci");
        if (TextUtils.isEmpty(ci)) {
            ci = PreferenceUtils.getInstance(context).getStringParam(ConstantValue.COMPANY_ID);
        }
        return ci;
    }

    public void removeKey(String key) {
        if (map.containsKey(key)) {
            map.remove(key);
        }
    }

    public void put(String key, String value) {
        map.put(key, StringUtil.stringNotNull(value));
    }

    public void put(String key, long value) {
        map.put(key, value + "");
    }

    public void put(String key, int value) {
        map.put(key, value + "");
    }

    public String getData() {
        return NetUtil.getBase64Data(map);
    }

    public void putDeviceNo() {
        map.put("dn", DeviceUtil.getDeviceNo(context));
    }
}
