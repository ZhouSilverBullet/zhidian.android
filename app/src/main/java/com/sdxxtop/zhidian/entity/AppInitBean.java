package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：APP_SDK初始化实体
 */

public class AppInitBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"apk_url":"http:://cdn.sdxxtop.com/apk/test.apk","version_code":"10100","force_update":0}
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
        /**
         * apk_url : http:://cdn.sdxxtop.com/apk/test.apk
         * version_code : 10100
         * force_update : 0
         */

        private String apk_url;
        private String version_code;
        private int force_update;

        public String getApk_url() {
            return apk_url;
        }

        public void setApk_url(String apk_url) {
            this.apk_url = apk_url;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public int getForce_update() {
            return force_update;
        }

        public void setForce_update(int force_update) {
            this.force_update = force_update;
        }
    }

    @Override
    public String toString() {
        return "AppInitBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
