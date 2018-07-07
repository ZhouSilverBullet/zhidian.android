package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：16:53
 * 邮箱：15010104100@163.com
 * 描述：手机号添加员工实体
 */

public class JoinByMobileBean {

    /**
     * code : 200
     * msg : 成功
     * data : {}
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
    }

    @Override
    public String toString() {
        return "JoinByMobileBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
