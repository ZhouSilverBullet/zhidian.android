package com.sdxxtop.zhidian.entity;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/4/19  时间：09:37
 * 邮箱：15010104100@163.com
 * 描述：设备管理主页
 */

public class DeviceIndexBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"wifi":[{"device_id":32,"name":"北京","wifi_name":"xuxingkeji","status":1,"bssid":"94:d9:b3:8f:29:97"},{"device_id":26,"name":"旭兴科技","wifi_name":"xuxingkeji","status":1,"bssid":"94:d9:b3:8f:29:97"}],"gps":[{"device_id":27,"name":"旭兴GPS","address":"北京市海淀区上地街道上地东路35号颐泉汇","status":1,"longitude":"116.314455,40.035922","sign_range":300}],"device":[]}
     */

    private int code;
    private String msg;
    private DataEntity data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private List<WifiEntity> wifi;
        private List<GpsEntity> gps;
        private List<DeviceEntity> device;

        public List<WifiEntity> getWifi() {
            return wifi;
        }

        public void setWifi(List<WifiEntity> wifi) {
            this.wifi = wifi;
        }

        public List<GpsEntity> getGps() {
            return gps;
        }

        public void setGps(List<GpsEntity> gps) {
            this.gps = gps;
        }

        public List<DeviceEntity> getDevice() {
            return device;
        }

        public void setDevice(List<DeviceEntity> device) {
            this.device = device;
        }

        public static class WifiEntity {
            /**
             * device_id : 32
             * name : 北京
             * wifi_name : xuxingkeji
             * status : 1
             * bssid : 94:d9:b3:8f:29:97
             */

            private int device_id;
            private String name;
            private String wifi_name;
            private int status;
            private String bssid;

            public int getDevice_id() {
                return device_id;
            }

            public void setDevice_id(int device_id) {
                this.device_id = device_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getWifi_name() {
                return wifi_name;
            }

            public void setWifi_name(String wifi_name) {
                this.wifi_name = wifi_name;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getBssid() {
                return bssid;
            }

            public void setBssid(String bssid) {
                this.bssid = bssid;
            }
        }

        public static class GpsEntity {
            /**
             * device_id : 27
             * name : 旭兴GPS
             * address : 北京市海淀区上地街道上地东路35号颐泉汇
             * status : 1
             * longitude : 116.314455,40.035922
             * sign_range : 300
             */

            private int device_id;
            private String name;
            private String address;
            private int status;
            private String longitude;
            private int sign_range;

            public int getDevice_id() {
                return device_id;
            }

            public void setDevice_id(int device_id) {
                this.device_id = device_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public int getSign_range() {
                return sign_range;
            }

            public void setSign_range(int sign_range) {
                this.sign_range = sign_range;
            }
        }

        public static class DeviceEntity {
            /**
             * device_id : 32
             * name : 北京
             * wifi_name : xuxingkeji
             * status : 1
             */

            private int device_id;
            private String name;
            private String wifi_name;
            private int status;

            public int getDevice_id() {
                return device_id;
            }

            public void setDevice_id(int device_id) {
                this.device_id = device_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getWifi_name() {
                return wifi_name;
            }

            public void setWifi_name(String wifi_name) {
                this.wifi_name = wifi_name;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }

    @Override
    public String toString() {
        return "DeviceIndexBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
