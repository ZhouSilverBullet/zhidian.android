package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：21:53
 * 邮箱：15010104100@163.com
 * 描述：账号信息
 */

public class UcenterUserinfoBean extends BaseModel<UcenterUserinfoBean.DataEntity> {

    public static class DataEntity {
        /**
         * userid : 50000028
         * sex : 1
         * mobile : 17608474053
         * name :
         * position :
         * img : #aa76ba
         * email :
         * company_name : 旭兴科技
         * device_name : VOVI X5
         */

        private int userid;
        private int sex;
        private long mobile;
        private String name;
        private String position;
        private String img;
        private String email;
        private String company_name;
        private String device_name;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public long getMobile() {
            return mobile;
        }

        public void setMobile(long mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }
    }
}
