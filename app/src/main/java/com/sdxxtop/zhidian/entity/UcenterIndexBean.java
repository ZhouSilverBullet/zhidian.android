package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：21:47
 * 邮箱：15010104100@163.com
 * 描述：我的模块主页实体
 */

public class UcenterIndexBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"is_manager":2,"is_stat":10,"is_class":1,"is_out":2,"userinfo":{"userid":50000028,"mobile":17608474053,"name":"","position":"","img":"#aa76ba"}}
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
         * is_manager : 2
         * is_stat : 10
         * is_class : 1
         * is_out : 2
         * userinfo : {"userid":50000028,"mobile":17608474053,"name":"","position":"","img":"#aa76ba"}
         */

        private int is_manager;
        private int is_stat;
        private int is_class;
        private int is_out;
        private UserinfoEntity userinfo;

        public int getIs_manager() {
            return is_manager;
        }

        public void setIs_manager(int is_manager) {
            this.is_manager = is_manager;
        }

        public int getIs_stat() {
            return is_stat;
        }

        public void setIs_stat(int is_stat) {
            this.is_stat = is_stat;
        }

        public int getIs_class() {
            return is_class;
        }

        public void setIs_class(int is_class) {
            this.is_class = is_class;
        }

        public int getIs_out() {
            return is_out;
        }

        public void setIs_out(int is_out) {
            this.is_out = is_out;
        }

        public UserinfoEntity getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserinfoEntity userinfo) {
            this.userinfo = userinfo;
        }

        public static class UserinfoEntity {
            /**
             * userid : 50000028
             * mobile : 17608474053
             * name :
             * position :
             * img : #aa76ba
             */

            private int userid;
            private long mobile;
            private String name;
            private String position;
            private String img;

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
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
        }
    }

    @Override
    public String toString() {
        return "UcenterIndexBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
