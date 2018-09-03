package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */

public class StudentAttendanceBean extends BaseModel<StudentAttendanceBean.DataBean> {

    public static class DataBean {
        private List<SignBean> sign;

        public List<SignBean> getSign() {
            return sign;
        }

        public void setSign(List<SignBean> sign) {
            this.sign = sign;
        }

        public static class SignBean {

            private int sign_id;
            private int student_id;
            private String student_name;
            private int is_notice;
            private int is_arrive;
            private String sign_arrive;
            private String sign_leave;

            public int getSign_id() {
                return sign_id;
            }

            public void setSign_id(int sign_id) {
                this.sign_id = sign_id;
            }

            public int getStudent_id() {
                return student_id;
            }

            public void setStudent_id(int student_id) {
                this.student_id = student_id;
            }

            public String getStudent_name() {
                return student_name;
            }

            public void setStudent_name(String student_name) {
                this.student_name = student_name;
            }

            public int getIs_notice() {
                return is_notice;
            }

            public void setIs_notice(int is_notice) {
                this.is_notice = is_notice;
            }

            public String getSign_arrive() {
                return sign_arrive;
            }

            public void setSign_arrive(String sign_arrive) {
                this.sign_arrive = sign_arrive;
            }

            public String getSign_leave() {
                return sign_leave;
            }

            public void setSign_leave(String sign_leave) {
                this.sign_leave = sign_leave;
            }

            public int getIs_arrive() {
                return is_arrive;
            }

            public void setIs_arrive(int is_arrive) {
                this.is_arrive = is_arrive;
            }
        }
    }
}
