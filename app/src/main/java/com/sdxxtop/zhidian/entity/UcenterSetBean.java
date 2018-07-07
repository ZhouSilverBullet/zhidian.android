package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/4/19  时间：09:33
 * 邮箱：15010104100@163.com
 * 描述：我的设置主页
 */

public class UcenterSetBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"mobile":17608474053,"company_name":"公司名称","is_manager":2,"is_device":1,"is_invite":1,"is_out":2,"remind_min":5}
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
         * mobile : 17608474053
         * company_name : 公司名称
         * is_manager : 2
         * is_device : 1
         * is_invite : 1
         * is_out : 2
         * remind_min : 5
         */

        private long mobile;
        private String company_name;
        private int is_manager;
        private int is_device;
        private int is_invite;
        private int is_out;
        private int remind_min;

        public long getMobile() {
            return mobile;
        }

        public void setMobile(long mobile) {
            this.mobile = mobile;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public int getIs_manager() {
            return is_manager;
        }

        public void setIs_manager(int is_manager) {
            this.is_manager = is_manager;
        }

        public int getIs_device() {
            return is_device;
        }

        public void setIs_device(int is_device) {
            this.is_device = is_device;
        }

        public int getIs_invite() {
            return is_invite;
        }

        public void setIs_invite(int is_invite) {
            this.is_invite = is_invite;
        }

        public int getIs_out() {
            return is_out;
        }

        public void setIs_out(int is_out) {
            this.is_out = is_out;
        }

        public int getRemind_min() {
            return remind_min;
        }

        public void setRemind_min(int remind_min) {
            this.remind_min = remind_min;
        }
    }

    @Override
    public String toString() {
        return "UcenterSetBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
