package com.sdxxtop.zhidian.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.sdxxtop.zhidian.model.ConstantValue;

/**
 * Created by Administrator on 2018/5/5.
 */

public class DeviceUtil {

    public static String getDeviceNo(Context context) {
        if (context == null) {
            return "";
        }

        PreferenceUtils.getInstance(context).saveParam(ConstantValue.DEVICE_NAME, Build.MODEL);
        String device_no = PreferenceUtils.getInstance(context).getStringParam(ConstantValue.DEVICE_NO);
        if (!TextUtils.isEmpty(device_no)) {
            return device_no;
        }

        try {
            //获取当前设备的IMEI
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            device_no = telephonyManager.getDeviceId();
            PreferenceUtils.getInstance(context).saveParam(ConstantValue.DEVICE_NO, device_no);
        } catch (Exception e) {
            device_no = "";
        }


        return device_no == null ? "" : device_no;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String getWifiId(Context context) {
        if (context == null) {
            return "";
        }
        String wifiMac = "";
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            wifiMac = info.getBSSID();
            if (!TextUtils.isEmpty(wifiMac)) {
                return wifiMac;
            }
        } catch (Exception e) {
            wifiMac = "";
        }

        return wifiMac == null ? "" : wifiMac;
    }

    public static String getWifiName(Context context) {
        if (context == null) {
            return "";
        }
        String wifiName = "";
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            wifiName = info.getSSID();
            if (!TextUtils.isEmpty(wifiName)) {
                wifiName = wifiName.substring(1, wifiName.length() - 1);
                return wifiName;
            }
        } catch (Exception e) {
            wifiName = "";
        }

        return wifiName == null ? "" : wifiName;
    }

}
