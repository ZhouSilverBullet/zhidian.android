package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 */

public class ApplyListBean extends BaseModel<ApplyListBean.DataBean> {

    public static class DataBean {

        private UserinfoBean userinfo;
        private int num;
        private List<ApplyBean> apply;

        public UserinfoBean getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserinfoBean userinfo) {
            this.userinfo = userinfo;
        }

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

        public static class UserinfoBean {
            /**
             * name : 周洋
             * img : http://cdn.sdxxtop.com/app/face/20180508190840854207.jpg
             */

            private String name;
            private String img;

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

        public static class ApplyBean {

            private int apply_id;
            private int apply_type;
            private int userid;
            private int company_id;
            private String group_id;
            private String group_userid;
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
            private String extra;
            private int status;
            private int time;
            private String review_time;
            private String apply_date;
            private String start_time;
            private String end_time;
            private String add_time;
            private String update_time;
            private String name;
            private boolean check;

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

            public String getExtra() {
                return extra;
            }

            public void setExtra(String extra) {
                this.extra = extra;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
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

            public boolean isCheck() {
                return check;
            }

            public void setCheck(boolean check) {
                this.check = check;
            }
        }
    }
}
