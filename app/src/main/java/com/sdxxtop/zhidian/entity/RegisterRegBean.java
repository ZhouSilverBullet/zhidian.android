package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：15:51
 * 邮箱：15010104100@163.com
 * 描述：提交注册
 */

public class RegisterRegBean {
    /**
     * code : 200
     * data : {"userid":50000206}
     * msg : 成功
     */

    private int code;
    private DataEntity data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataEntity {
        /**
         * userid : 50000206
         */

        private int userid;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

    }

    @Override
    public String toString() {
        return "RegistSuccBean{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
