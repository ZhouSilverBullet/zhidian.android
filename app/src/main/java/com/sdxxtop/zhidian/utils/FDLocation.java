package com.sdxxtop.zhidian.utils;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.sdxxtop.zhidian.App;
import com.sdxxtop.zhidian.model.ConstantValue;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhousaito on 2017/11/26.
 */

public class FDLocation implements AMapLocationListener {
    private static FDLocation location;
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private static double latitude; //精度
    private static double longitude; //维度
    private String address;

    private FDLocation() {

    }

    public static FDLocation getInstance() {
        if (location == null) {
            location = new FDLocation();
        }
        return location;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                address = amapLocation.getAddress();
                latitude = amapLocation.getLatitude();//获取纬度
                longitude = amapLocation.getLongitude();//获取经度
                if (latitude == 0) {
                    PreferenceUtils.getInstance(App.getContext()).saveParam(ConstantValue.MAP_JING_DU, "");
                    PreferenceUtils.getInstance(App.getContext()).saveParam(ConstantValue.MAP_WEI_DU, "");
                } else {
                    PreferenceUtils.getInstance(App.getContext()).saveParam(ConstantValue.MAP_JING_DU, longitude + "");
                    PreferenceUtils.getInstance(App.getContext()).saveParam(ConstantValue.MAP_WEI_DU, latitude + "");
                }
                String tempAddress = address == null ? "" : address;
                PreferenceUtils.getInstance(App.getContext()).saveParam(ConstantValue.MAP_ADDRESS, tempAddress);

                amapLocation.getAccuracy();//获取精度信息
                Log.e("onLocationChanged: ", "latitude: " + latitude + "longitude" + longitude);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

        if (listener != null) {
            listener.onAddress(address);
        }

        if (locationCompanyListener != null) {
            locationCompanyListener.onAddress(amapLocation);
        }
    }

    public void location() {
        mlocationClient = new AMapLocationClient(App.getAppContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mLocationOption.setWifiScan(true);
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        // 启动定位
        mlocationClient.startLocation();
    }


    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public interface LocationAddressListener {
        void onAddress(String address);
    }

    private LocationAddressListener listener;

    public void setLocationAddressListener(LocationAddressListener listener) {
        this.listener = listener;
    }

    public void removeAddressListener() {
        listener = null;
    }


    public interface LocationCompanyListener {
        void onAddress(AMapLocation address);
    }

    private LocationCompanyListener locationCompanyListener;

    public void setLocationCompanyListener(LocationCompanyListener listener) {
        this.locationCompanyListener = listener;
    }

    public void removeLocationCompanyListener() {
        locationCompanyListener = null;
    }
}
