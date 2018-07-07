package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/4/19  时间：11:26
 * 邮箱：15010104100@163.com
 * 描述：信息通知主页实体
 */

public class UcenterNoticeIndexBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"userid":12,"is_push":1,"is_disturb":1,"begin_time":"23:00","end_time":"06:00"}
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
         * userid : 12
         * is_push : 1
         * is_disturb : 1
         * begin_time : 23:00
         * end_time : 06:00
         */

        private int userid;
        private int is_push;
        private int is_disturb;
        private String begin_time;
        private String end_time;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getIs_push() {
            return is_push;
        }

        public void setIs_push(int is_push) {
            this.is_push = is_push;
        }

        public int getIs_disturb() {
            return is_disturb;
        }

        public void setIs_disturb(int is_disturb) {
            this.is_disturb = is_disturb;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }

    @Override
    public String toString() {
        return "UcenterNoticeIndexBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
