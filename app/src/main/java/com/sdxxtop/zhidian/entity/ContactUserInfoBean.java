package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：17:34
 * 邮箱：15010104100@163.com
 * 描述：查看个人信息
 */

public class ContactUserInfoBean extends BaseModel<ContactUserInfoBean.DataBean> {

    public static class DataBean {
        /**
         * is_collect : 1
         * part_name :
         * is_manager : 1
         * is_self : 2
         * device_name :
         * userinfo : {"userid":50000431,"name":"lizhaobin","position":"","sex":0,"email":"","img":"#81a4f9","mobile":"18063162693","user_extra":"","company_id":"","qq":"","wechat":"","national":"","work_place":"","home_place":"","id_card":"","birthday":"","marriage":"","address":"","contact":"","add_time":"","update_time":""}
         */

        private int is_collect;
        private String part_name;
        private int is_manager;
        private int is_self;
        private String device_name;
        private UserinfoBean userinfo;

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public String getPart_name() {
            return part_name;
        }

        public void setPart_name(String part_name) {
            this.part_name = part_name;
        }

        public int getIs_manager() {
            return is_manager;
        }

        public void setIs_manager(int is_manager) {
            this.is_manager = is_manager;
        }

        public int getIs_self() {
            return is_self;
        }

        public void setIs_self(int is_self) {
            this.is_self = is_self;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public UserinfoBean getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserinfoBean userinfo) {
            this.userinfo = userinfo;
        }

        public static class UserinfoBean {
            /**
             * userid : 50000431
             * name : lizhaobin
             * position :
             * sex : 0
             * email :
             * img : #81a4f9
             * mobile : 18063162693
             * user_extra :
             * company_id :
             * qq :
             * wechat :
             * national :
             * work_place :
             * home_place :
             * id_card :
             * birthday :
             * marriage :
             * address :
             * contact :
             * add_time :
             * update_time :
             */

            private int userid;
            private String name;
            private String position;
            private int sex;
            private String email;
            private String img;
            private String mobile;
            private String user_extra;
            private String company_id;
            private String qq;
            private String wechat;
            private String national;
            private String work_place;
            private String home_place;
            private String id_card;
            private String birthday;
            private String marriage;
            private String address;
            private String contact;
            private String add_time;
            private String update_time;
            private int is_hide_mobile;

            /**
             * "relation":"父子", //与学生关系
             * "student_name":"漩涡鸣人1",//学生姓名
             * "part_name":"学校名称",
             * "grade_name":"一年级",
             * "class_name":"一班",
             */
            private String relation;
            private String part_id;
            private String grade_id;
            private String class_id;
            private String student_name;
            private String part_name;
            private String grade_name;
            private String class_name;

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

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getUser_extra() {
                return user_extra;
            }

            public void setUser_extra(String user_extra) {
                this.user_extra = user_extra;
            }

            public String getCompany_id() {
                return company_id;
            }

            public void setCompany_id(String company_id) {
                this.company_id = company_id;
            }

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }

            public String getWechat() {
                return wechat;
            }

            public void setWechat(String wechat) {
                this.wechat = wechat;
            }

            public String getNational() {
                return national;
            }

            public void setNational(String national) {
                this.national = national;
            }

            public String getWork_place() {
                return work_place;
            }

            public void setWork_place(String work_place) {
                this.work_place = work_place;
            }

            public String getHome_place() {
                return home_place;
            }

            public void setHome_place(String home_place) {
                this.home_place = home_place;
            }

            public String getId_card() {
                return id_card;
            }

            public void setId_card(String id_card) {
                this.id_card = id_card;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getMarriage() {
                return marriage;
            }

            public void setMarriage(String marriage) {
                this.marriage = marriage;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getContact() {
                return contact;
            }

            public void setContact(String contact) {
                this.contact = contact;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public int getIs_hide_mobile() {
                return is_hide_mobile;
            }

            public void setIs_hide_mobile(int is_hide_mobile) {
                this.is_hide_mobile = is_hide_mobile;
            }

            public String getRelation() {
                return relation;
            }

            public void setRelation(String relation) {
                this.relation = relation;
            }

            public String getPart_id() {
                return part_id;
            }

            public void setPart_id(String part_id) {
                this.part_id = part_id;
            }

            public String getGrade_id() {
                return grade_id;
            }

            public void setGrade_id(String grade_id) {
                this.grade_id = grade_id;
            }

            public String getClass_id() {
                return class_id;
            }

            public void setClass_id(String class_id) {
                this.class_id = class_id;
            }

            public String getStudent_name() {
                return student_name;
            }

            public void setStudent_name(String student_name) {
                this.student_name = student_name;
            }

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }

            public String getGrade_name() {
                return grade_name;
            }

            public void setGrade_name(String grade_name) {
                this.grade_name = grade_name;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }
        }
    }
}
