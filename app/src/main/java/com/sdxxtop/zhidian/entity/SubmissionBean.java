package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * Created by Administrator on 2018/5/11.
 */

public class SubmissionBean extends BaseModel<SubmissionBean.DataBean> {

    public static class DataBean {
        /**
         * apply_id : 7891
         * apply_type : 1
         * userid : 50000000
         * company_id : 1000035
         * group_id :
         * group_userid :
         * group_part :
         * approver_id : 50000000,50000028
         * send_id : 50000477
         * witness_id :
         * reviewer_id : 0
         * opinion :
         * reason : 不将就
         * img :
         * leave_type : 1
         * space :
         * overtime_type : 0
         * overtime_position : 0
         * extra :
         * status : 0
         * time : 1020
         * review_time : 1000-01-01 00:00:00
         * apply_date : 1000-01-01
         * start_time : 2018-05-11 00:00:00
         * end_time : 2018-05-11 17:00:00
         * add_time : 2018-05-11 18:23:28
         * update_time : 2018-05-11 18:23:28
         * name : 周洋1
         * send_name : 熊大
         * witness_name :
         * userinfo : {"userid":50000000,"name":"周洋1","img":"http://cdn.sdxxtop.com/app/face/20180510214628366041.png"}
         * approver_name : {"userid":50000000,"name":"周洋1,王五","img":"http://cdn.sdxxtop.com/app/face/20180510214628366041.png"}
         */

        private int apply_id;
        private int apply_type;
        private int userid;
        private int company_id;
        private String group_id;
        private String group_userid;
        private String group_user;
        private String group_part;
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
        private String name;
        private String send_name;
        private String witness_name;
        private UserinfoBean userinfo;
        private ApproverNameBean approver_name;

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

        public String getGroup_user() {
            return group_user;
        }

        public void setGroup_user(String group_user) {
            this.group_user = group_user;
        }

        public void setGroup_userid(String group_userid) {
            this.group_userid = group_userid;
        }

        public String getGroup_part() {
            return group_part;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSend_name() {
            return send_name;
        }

        public void setSend_name(String send_name) {
            this.send_name = send_name;
        }

        public String getWitness_name() {
            return witness_name;
        }

        public void setWitness_name(String witness_name) {
            this.witness_name = witness_name;
        }

        public UserinfoBean getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserinfoBean userinfo) {
            this.userinfo = userinfo;
        }

        public ApproverNameBean getApprover_name() {
            return approver_name;
        }

        public void setApprover_name(ApproverNameBean approver_name) {
            this.approver_name = approver_name;
        }

        public static class UserinfoBean {
            /**
             * userid : 50000000
             * name : 周洋1
             * img : http://cdn.sdxxtop.com/app/face/20180510214628366041.png
             */

            private int userid;
            private String name;
            private String img;

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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }

        public static class ApproverNameBean {
            /**
             * userid : 50000000
             * name : 周洋1,王五
             * img : http://cdn.sdxxtop.com/app/face/20180510214628366041.png
             */

            private int userid;
            private String name;
            private String img;

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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
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
        //"my_class_id" : 1,
//        "my_class_name": "春节时间",
//        "my_start_date":"2018-04-28",
//        "my_end_date":"2018-04-31",
//        "ex_class_id" : 2,
//        "ex_class_name" : "秋季时间",
//        "ex_start_date":"2018-05-01",
//        "ex_end_date":"2018-05-04",
//        "sign_id":"1,2",//打卡id多个用逗号隔开
//        "sign_name":"上班,下班",//打卡名称
//        "sign_time":"09:00,12:00",//漏打卡时间
//        "sign_status":"5,6" //类型5迟到 6早退
//        "late_time":"1,2"//迟到早退时间
//        "device_name":"iPhone SE"
    }
}
