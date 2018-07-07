package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * 作者：CaiCM
 * 日期：2018/4/21  时间：11:11
 * 邮箱：15010104100@163.com
 * 描述：公司信息展示
 */

public class CompanyShowInfoBean extends BaseModel<CompanyShowInfoBean.DataEntity> {

    public static class DataEntity {
        /**
         * is_manager : 2
         * is_edit : 10
         * company : {"company_name":"公司名称","short_name":"公司简称","short_code":"公司短码","classify_id":"1","longitude":"经纬度","address":"山东","area":"地区"}
         */

        private int is_manager;
        private int is_edit;
        private CompanyEntity company;

        public int getIs_manager() {
            return is_manager;
        }

        public void setIs_manager(int is_manager) {
            this.is_manager = is_manager;
        }

        public int getIs_edit() {
            return is_edit;
        }

        public void setIs_edit(int is_edit) {
            this.is_edit = is_edit;
        }

        public CompanyEntity getCompany() {
            return company;
        }

        public void setCompany(CompanyEntity company) {
            this.company = company;
        }

        public static class CompanyEntity {
            /**
             * company_name : 公司名称
             * short_name : 公司简称
             * short_code : 公司短码
             * classify_id : 1
             * longitude : 经纬度
             * address : 山东
             * area : 地区
             */

            private String company_name;
            private String short_name;
            private String short_code;
            private String classify_id;
            private String longitude;
            private String address;
            private String area;

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public String getShort_code() {
                return short_code;
            }

            public void setShort_code(String short_code) {
                this.short_code = short_code;
            }

            public String getClassify_id() {
                return classify_id;
            }

            public void setClassify_id(String classify_id) {
                this.classify_id = classify_id;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }
        }
    }
}
