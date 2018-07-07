package com.sdxxtop.zhidian.entity;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：16:57
 * 邮箱：15010104100@163.com
 * 描述：通讯录搜索
 */

public class ContactSearchBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"userinfo":[{"userid":50000028,"name":"","position":"","img":"#aa76ba","user_status":0}]}
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
        private List<UserinfoEntity> userinfo;

        public List<UserinfoEntity> getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(List<UserinfoEntity> userinfo) {
            this.userinfo = userinfo;
        }

        public static class UserinfoEntity {
            /**
             * userid : 50000028
             * name :
             * position :
             * img : #aa76ba
             * user_status : 0
             */

            private int userid;
            private String name;
            private String position;
            private String img;
            private int user_status;

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
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

            public int getUser_status() {
                return user_status;
            }

            public void setUser_status(int user_status) {
                this.user_status = user_status;
            }
        }
    }
}
