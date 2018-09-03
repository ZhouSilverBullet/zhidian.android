package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：16:55
 * 邮箱：15010104100@163.com
 * 描述：通讯录主页实体
 */

public class ContactIndexBean extends BaseModel<ContactIndexBean.DataEntity> {

    public static class DataEntity {
        /**
         * colloect_user : [{"userid":50000028,"name":"王五","position":"","img":"#9d81ff","user_status":1},{"userid":50000029,"name":"程六","position":"","img":"#81a4f9","user_status":1},{"userid":50000037,"name":"王昭君","position":"","img":"#9396fa","user_status":1},{"userid":50000000,"name":"周洋1","position":"PHP工程师","img":"http://cdn.sdxxtop.com/app/face/1.jpg","user_status":1},{"userid":50000027,"name":"李四","position":"","img":"#9396fa","user_status":1},{"userid":50000031,"name":"赵九","position":"","img":"#4e76e4","user_status":1},{"userid":50000033,"name":"虞姬","position":"","img":"#9d81ff","user_status":1},{"userid":50000035,"name":"赵云","position":"","img":"#9396fa","user_status":1},{"userid":50000036,"name":"阿珂","position":"","img":"#9d81ff","user_status":1},{"userid":50000247,"name":"阿斯蒂芬","position":"","img":"#9396fa","user_status":1}]
         * part_user : [{"userid":50000037,"name":"王昭君","position":"","img":"#9396fa","user_status":1},{"userid":50000000,"name":"周洋1","position":"PHP工程师","img":"http://cdn.sdxxtop.com/app/face/1.jpg","user_status":1}]
         * part_name : 旭兴科技
         * is_invite : 1
         */

        private String part_name;
        private int is_invite;
        private List<ColloectUserEntity> colloect_user;
        private List<ColloectUserEntity> part_user;
        /**
         *收索的时候用的
         */
        private List<ColloectUserEntity> userinfo;

        public String getPart_name() {
            return part_name;
        }

        public void setPart_name(String part_name) {
            this.part_name = part_name;
        }

        public int getIs_invite() {
            return is_invite;
        }

        public void setIs_invite(int is_invite) {
            this.is_invite = is_invite;
        }

        public List<ColloectUserEntity> getColloect_user() {
            return colloect_user;
        }

        public void setColloect_user(List<ColloectUserEntity> colloect_user) {
            this.colloect_user = colloect_user;
        }

        public List<ColloectUserEntity> getPart_user() {
            return part_user;
        }

        public void setPart_user(List<ColloectUserEntity> part_user) {
            this.part_user = part_user;
        }

        public List<ColloectUserEntity> getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(List<ColloectUserEntity> userinfo) {
            this.userinfo = userinfo;
        }

        public static class ColloectUserEntity {
            /**
             * userid : 50000028
             * name : 王五
             * position :
             * img : #9d81ff
             * user_status : 1
             */

            private int userid;
            private String name;
            private String position;
            private String img;
            private int user_status;
            private String student_id;
            private String student_name;
            /**
             * 用户类型 1:教师 2:家长
             */
            private int user_type;

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

            public int getUser_type() {
                return user_type;
            }

            public void setUser_type(int user_type) {
                this.user_type = user_type;
            }

            /**
             * true:教师 false:家长
             *
             * 为0的时候可能是没有获取到数据造成的
             */
            public boolean isTeacher() {
                return user_type == 1 || user_type == 0;
            }

            public String getStudent_id() {
                return student_id;
            }

            public void setStudent_id(String student_id) {
                this.student_id = student_id;
            }

            public String getStudent_name() {
                return student_name;
            }

            public void setStudent_name(String student_name) {
                this.student_name = student_name;
            }
        }
    }
}
