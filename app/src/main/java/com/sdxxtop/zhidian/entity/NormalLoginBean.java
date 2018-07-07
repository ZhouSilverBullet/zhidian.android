package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：15:58
 * 邮箱：15010104100@163.com
 * 描述：普通登陆实体
 */

public class NormalLoginBean extends BaseModel<NormalLoginBean.DataEntity> {

    public static class DataEntity {
        /**
         * auto_token : 014311E95AD9ADA368C633E65D103EF4
         * expire_time : 1522586198
         * userid : 50000000
         * msg : 该账号已和iPhone SE手机绑定,不能在此设备签到,其他功能正常
         * company_id : 1000035
         */

        private String auto_token;
        private int expire_time;
        private int userid;
        private String msg;
        private String company_id;

        public String getAuto_token() {
            return auto_token;
        }

        public void setAuto_token(String auto_token) {
            this.auto_token = auto_token;
        }

        public int getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(int expire_time) {
            this.expire_time = expire_time;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }
    }

}
