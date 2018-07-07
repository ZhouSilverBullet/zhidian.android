package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class MsgApprovalBean extends BaseModel<MsgApprovalBean.DataBean> {

    public static class DataBean {
        /**
         * num : 3
         * apply : [{"apply_id":8136,"apply_type":1,"userid":50000042,"company_id":1000035,"group_id":"","group_userid":"","group_part":"","approver_id":"50000042,50000502,50000503,50000505,50000506,50000507,50000508","send_id":"50000039","witness_id":"","reviewer_id":0,"opinion":"","reason":"你摸","img":"#4e76e4","leave_type":8,"space":"","overtime_type":0,"overtime_position":0,"extra":"","status":0,"time":540,"review_time":"1000-01-01 00:00:00","apply_date":"1000-01-01","start_time":"2018-05-14 09:30:00","end_time":"2018-05-14 18:30:00","add_time":"2018-05-14 10:09:02","update_time":"2018-05-14 10:09:02","title":"lizhaobin的请假申请"},{"apply_id":8121,"apply_type":8,"userid":50000000,"company_id":1000035,"group_id":"","group_userid":"","group_part":"","approver_id":"50000042","send_id":"50000554,50000435,50000235,50000650,50000440,50000687,50000480,50000217,50000609,50000477,50000478,50000431,50000434,50000561,50000558,50000236,50000762","witness_id":"","reviewer_id":0,"opinion":"","reason":"爸爸","img":"http://cdn.sdxxtop.com/app/face/20180510214628366041.png","leave_type":0,"space":"","overtime_type":0,"overtime_position":0,"extra":{"my_start_date":"2018-05-13","my_end_date":"2018-05-13","my_class_id":"9","my_class_name":"早九晚五","ex_class_id":"160","ex_class_name":"春季时间","ex_start_date":"2018-05-13","ex_end_date":"2018-05-13"},"status":0,"time":0,"review_time":"1000-01-01 00:00:00","apply_date":"1000-01-01","start_time":"1000-01-01 00:00:00","end_time":"1000-01-01 00:00:00","add_time":"2018-05-12 12:06:06","update_time":"2018-05-12 12:06:06","title":"周洋1的调班申请"},{"apply_id":7841,"apply_type":1,"userid":50000042,"company_id":1000035,"group_id":"","group_userid":"","group_part":"","approver_id":"50000042,50000502,50000503,50000505,50000506,50000507,50000508","send_id":"50000431,50000434,50000440,50000478,50000477,50000236,50000480,50000217,50000435,50000554,50000561,50000235,50000609,50000650,50000558,50000687,50000762","witness_id":"","reviewer_id":50000042,"opinion":"","reason":"我会","img":"#4e76e4","leave_type":1,"space":"","overtime_type":0,"overtime_position":0,"extra":"","status":1,"time":570,"review_time":"2018-05-08 21:57:15","apply_date":"1000-01-01","start_time":"2018-05-08 09:00:00","end_time":"2018-05-08 18:30:00","add_time":"2018-05-08 21:55:36","update_time":"2018-05-08 21:57:15","title":"lizhaobin的请假申请","reviewer_name":"lizhaobin"}]
         */

        private int num;
        private List<ApplyBean> apply;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public List<ApplyBean> getApply() {
            return apply;
        }

        public void setApply(List<ApplyBean> apply) {
            this.apply = apply;
        }

        public static class ApplyBean {
            /**
             * apply_id : 8136
             * apply_type : 1
             * userid : 50000042
             * company_id : 1000035
             * group_id :
             * group_userid :
             * group_part :
             * approver_id : 50000042,50000502,50000503,50000505,50000506,50000507,50000508
             * send_id : 50000039
             * witness_id :
             * reviewer_id : 0
             * opinion :
             * reason : 你摸
             * img : #4e76e4
             * leave_type : 8
             * space :
             * overtime_type : 0
             * overtime_position : 0
             * extra :
             * status : 0
             * time : 540
             * review_time : 1000-01-01 00:00:00
             * apply_date : 1000-01-01
             * start_time : 2018-05-14 09:30:00
             * end_time : 2018-05-14 18:30:00
             * add_time : 2018-05-14 10:09:02
             * update_time : 2018-05-14 10:09:02
             * title : lizhaobin的请假申请
             * reviewer_name : lizhaobin
             */

            private int apply_id;
            private int apply_type;
            private int userid;
            private int company_id;
            private String group_id;
            private String group_userid;
            private String group_part;
            private String group_user;
            private String approver_id;
            private String send_id;
            private String witness_id;
            private int reviewer_id;
            private String opinion;
            private String reason;
            private String img;
            private int leave_type;
            private String space;
            private int overtime_type;
            private int overtime_position;
            private ExtraBean extra;
            private int status;
            private String time;
            private String review_time;
            private String apply_date;
            private String start_time;
            private String end_time;
            private String add_time;
            private String update_time;
            private String title;
            private String reviewer_name;
            private String approver_name;

            public int getApply_id() {
                return apply_id;
            }

            public void setApply_id(int apply_id) {
                this.apply_id = apply_id;
            }

            public int getApply_type() {
                return apply_type;
            }

            public void setApply_type(int apply_type) {
                this.apply_type = apply_type;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public int getCompany_id() {
                return company_id;
            }

            public void setCompany_id(int company_id) {
                this.company_id = company_id;
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getGroup_userid() {
                return group_userid;
            }

            public void setGroup_userid(String group_userid) {
                this.group_userid = group_userid;
            }

            public String getGroup_part() {
                return group_part;
            }

            public String getGroup_user() {
                return group_user;
            }

            public void setGroup_user(String group_user) {
                this.group_user = group_user;
            }

            public void setGroup_part(String group_part) {
                this.group_part = group_part;
            }

            public String getApprover_id() {
                return approver_id;
            }

            public void setApprover_id(String approver_id) {
                this.approver_id = approver_id;
            }

            public String getSend_id() {
                return send_id;
            }

            public void setSend_id(String send_id) {
                this.send_id = send_id;
            }

            public String getWitness_id() {
                return witness_id;
            }

            public void setWitness_id(String witness_id) {
                this.witness_id = witness_id;
            }

            public int getReviewer_id() {
                return reviewer_id;
            }

            public void setReviewer_id(int reviewer_id) {
                this.reviewer_id = reviewer_id;
            }

            public String getOpinion() {
                return opinion;
            }

            public void setOpinion(String opinion) {
                this.opinion = opinion;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getLeave_type() {
                return leave_type;
            }

            public void setLeave_type(int leave_type) {
                this.leave_type = leave_type;
            }

            public String getSpace() {
                return space;
            }

            public void setSpace(String space) {
                this.space = space;
            }

            public int getOvertime_type() {
                return overtime_type;
            }

            public void setOvertime_type(int overtime_type) {
                this.overtime_type = overtime_type;
            }

            public int getOvertime_position() {
                return overtime_position;
            }

            public void setOvertime_position(int overtime_position) {
                this.overtime_position = overtime_position;
            }

            public ExtraBean getExtra() {
                return extra;
            }

            public void setExtra(ExtraBean extra) {
                this.extra = extra;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getReview_time() {
                return review_time;
            }

            public void setReview_time(String review_time) {
                this.review_time = review_time;
            }

            public String getApply_date() {
                return apply_date;
            }

            public void setApply_date(String apply_date) {
                this.apply_date = apply_date;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getReviewer_name() {
                return reviewer_name;
            }

            public void setReviewer_name(String reviewer_name) {
                this.reviewer_name = reviewer_name;
            }

            public String getApprover_name() {
                return approver_name;
            }

            public void setApprover_name(String approver_name) {
                this.approver_name = approver_name;
            }
        }
    }

    public static class ExtraBean {
        private int my_class_id;
        private String my_class_name;
        private String my_start_date;
        private String my_end_date;
        private int ex_class_id;
        private String ex_class_name;
        private String ex_start_date;
        private String ex_end_date;
        private String sign_id;
        private String sign_name;
        private String sign_time;
        private String sign_status;
        private String late_time;
        private String device_name;

        public int getMy_class_id() {
            return my_class_id;
        }

        public void setMy_class_id(int my_class_id) {
            this.my_class_id = my_class_id;
        }

        public String getMy_class_name() {
            return my_class_name;
        }

        public void setMy_class_name(String my_class_name) {
            this.my_class_name = my_class_name;
        }

        public String getMy_start_date() {
            return my_start_date;
        }

        public void setMy_start_date(String my_start_date) {
            this.my_start_date = my_start_date;
        }

        public String getMy_end_date() {
            return my_end_date;
        }

        public void setMy_end_date(String my_end_date) {
            this.my_end_date = my_end_date;
        }

        public int getEx_class_id() {
            return ex_class_id;
        }

        public void setEx_class_id(int ex_class_id) {
            this.ex_class_id = ex_class_id;
        }

        public String getEx_class_name() {
            return ex_class_name;
        }

        public void setEx_class_name(String ex_class_name) {
            this.ex_class_name = ex_class_name;
        }

        public String getEx_start_date() {
            return ex_start_date;
        }

        public void setEx_start_date(String ex_start_date) {
            this.ex_start_date = ex_start_date;
        }

        public String getEx_end_date() {
            return ex_end_date;
        }

        public void setEx_end_date(String ex_end_date) {
            this.ex_end_date = ex_end_date;
        }

        public String getSign_id() {
            return sign_id;
        }

        public void setSign_id(String sign_id) {
            this.sign_id = sign_id;
        }

        public String getSign_name() {
            return sign_name;
        }

        public void setSign_name(String sign_name) {
            this.sign_name = sign_name;
        }

        public String getSign_time() {
            return sign_time;
        }

        public void setSign_time(String sign_time) {
            this.sign_time = sign_time;
        }

        public String getSign_status() {
            return sign_status;
        }

        public void setSign_status(String sign_status) {
            this.sign_status = sign_status;
        }

        public String getLate_time() {
            return late_time;
        }

        public void setLate_time(String late_time) {
            this.late_time = late_time;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }
    }

}
