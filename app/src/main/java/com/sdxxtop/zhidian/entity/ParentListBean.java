package com.sdxxtop.zhidian.entity;

import android.text.TextUtils;

import com.sdxxtop.zhidian.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */

public class ParentListBean extends BaseModel<ParentListBean.DataBeanX> implements Serializable {

    public static class DataBeanX implements Serializable {
        /**
         * is_parent : 2
         * data : [{"part_id":10000664,"part_name":"开发专用","type_id":10000664,"name":"开发专用"},{"part_id":10000665,"part_name":"总经办","type_id":10000665,"name":"总经办"},{"part_id":10000666,"part_name":"运营部","type_id":10000666,"name":"运营部"},{"part_id":10000667,"part_name":"视觉部","type_id":10000667,"name":"视觉部"},{"part_id":10000668,"part_name":"人事部","type_id":10000668,"name":"人事部"},{"part_id":10000669,"part_name":"财务部","type_id":10000669,"name":"财务部"},{"part_id":10000670,"part_name":"研发部","type_id":10000670,"name":"研发部"},{"part_id":10000671,"part_name":"前端","type_id":10000671,"name":"前端"},{"part_id":10000672,"part_name":"服务端","type_id":10000672,"name":"服务端"},{"part_id":10000673,"part_name":"移动端","type_id":10000673,"name":"移动端"},{"part_id":10000674,"part_name":"中心校","type_id":10000674,"name":"中心校"}]
         */

        private int is_parent;
        private List<DataBean> data;
        private List<NavBean> nav;

        public List<NavBean> getNav() {
            return nav;
        }

        public void setNav(List<NavBean> nav) {
            this.nav = nav;
        }

        public int getIs_parent() {
            return is_parent;
        }

        public void setIs_parent(int is_parent) {
            this.is_parent = is_parent;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        /**
         * 是否是教师
         * 家长信息,1是 2不是
         *
         * @return
         */
        public boolean isParent() {
            return is_parent == 1;
        }

        public static class DataBean implements Serializable {
            /**
             * part_id : 10000664
             * part_name : 开发专用
             * type_id : 10000664
             * name : 开发专用
             */
            private int part_id;
            private String part_name;
            private int type_id;
            private String name;
            private String img;
            private int userid;
            private int student_id;
            private int user_status;
            private String student_name;
            private boolean check;
            //一年级一班
            private String gradeValue;

            public String getGradeValue() {
                return gradeValue;
            }

            public void setGradeValue(String gradeValue) {
                this.gradeValue = gradeValue;
            }

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

            public int getType_id() {
                return type_id;
            }

            public void setType_id(int type_id) {
                this.type_id = type_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            /**
             * 如果 img 是个空的就是部门
             *
             * @return
             */
            public boolean isPart() {
                return TextUtils.isEmpty(img);
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public int getStudent_id() {
                return student_id;
            }

            public void setStudent_id(int student_id) {
                this.student_id = student_id;
            }

            public int getUser_status() {
                return user_status;
            }

            public void setUser_status(int user_status) {
                this.user_status = user_status;
            }

            public String getStudent_name() {
                return student_name;
            }

            public void setStudent_name(String student_name) {
                this.student_name = student_name;
            }

            public boolean isCheck() {
                return check;
            }

            public void setCheck(boolean check) {
                this.check = check;
            }

            public String getUserStudentId() {
                return userid + "|" + student_id;
            }
        }

        public static class NavBean {
            /**
             * type_id : 1000153
             * type : 1
             * name : 所有家长
             */

            private int type_id;
            private int type;
            private String name;
            private boolean isTeacher;

            //teacher
            private int part_id;
            private String part_name;
            private String parent_id;

            public int getType_id() {
                return type_id;
            }

            public void setType_id(int type_id) {
                this.type_id = type_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isTeacher() {
                return isTeacher;
            }

            public void setTeacher(boolean teacher) {
                isTeacher = teacher;
            }

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
}
