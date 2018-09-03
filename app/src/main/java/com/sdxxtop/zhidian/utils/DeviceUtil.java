package com.sdxxtop.zhidian.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

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

    public static String getNetworkType(Context context) {
        String strNetworkType = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager == null) {
            return "";
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
            }
        }

        return strNetworkType;
    }

}
