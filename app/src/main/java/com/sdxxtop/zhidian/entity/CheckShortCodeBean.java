package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：16:25
 * 邮箱：15010104100@163.com
 * 描述：校验公司短码
 */

public class CheckShortCodeBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"company_name":"旭兴科技2","short_code":"387869"}
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
         * company_name : 旭兴科技2
         * short_code : 387869
         */

        private String company_name;
        private String short_code;
        private String company_id;

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getShort_code() {
            return short_code;
        }

        public void setShort_code(String short_code) {
            this.short_code = short_code;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }
    }

    @Override
    public String toString() {
        return "CheckShortCodeBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
