package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：16:00
 * 邮箱：15010104100@163.com
 * 描述：自动登录实体
 */

public class AutoLoginBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"userid":50000021,"auto_token":"2B8454948396505EB3220388A1A14D86","company_id":"100000","expire_time":"1521708027"}
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
         * userid : 50000021
         * auto_token : 2B8454948396505EB3220388A1A14D86
         * company_id : 100000
         * expire_time : 1521708027
         */

        private int userid;
        private String auto_token;
        private String company_id;
        private String expire_time;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getAuto_token() {
            return auto_token;
        }

        public void setAuto_token(String auto_token) {
            this.auto_token = auto_token;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }
    }

    @Override
    public String toString() {
        return "AutoLoginBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
