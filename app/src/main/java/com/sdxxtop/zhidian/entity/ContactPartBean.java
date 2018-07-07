package com.sdxxtop.zhidian.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：17:14
 * 邮箱：15010104100@163.com
 * 描述：查看部门信息
 */

public class ContactPartBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"part":[{"part_id":18,"company_id":"","part_name":""}],"userinfo":[{"userid":50000028,"name":"","position":"","img":"#aa76ba","user_status":0}],"nav":[{"part_id":17,"part_name":"技术部","parent_id":""}]}
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

    public static class DataEntity implements Serializable {
        private List<PartEntity> part;
        private List<UserinfoEntity> userinfo;
        private List<NavEntity> nav;

        public List<PartEntity> getPart() {
            return part;
        }

        public void setPart(List<PartEntity> part) {
            this.part = part;
        }

        public List<UserinfoEntity> getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(List<UserinfoEntity> userinfo) {
            this.userinfo = userinfo;
        }

        public List<NavEntity> getNav() {
            return nav;
        }

        public void setNav(List<NavEntity> nav) {
            this.nav = nav;
        }

        public static class PartEntity implements Serializable {
            /**
             * part_id : 18
             * company_id :
             * part_name :
             */

            private int part_id;
            private String company_id;
            private String part_name;
            private boolean isCheck;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getPart_id() {
                return part_id;
            }

            public void setPart_id(int part_id) {
                this.part_id = part_id;
            }

            public String getCompany_id() {
                return company_id;
            }

            public void setCompany_id(String company_id) {
                this.company_id = company_id;
            }

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }
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
            private boolean isCheck;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

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

        public static class NavEntity implements Serializable {
            /**
             * part_id : 17
             * part_name : 技术部
             * parent_id :
             */

            private int part_id;
            private String part_name;
            private String parent_id;

            public int getPart_id() {
                return part_id;
            }

            public void setPart_id(int part_id) {
                this.part_id = part_id;
            }

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }
        }
    }

    @Override
    public String toString() {
        return "ContactPartBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
