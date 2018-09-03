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
        private int parent_num;
        private List<ApplyBean> apply;
        private List<ApplyBean> parent_apply;
        private List<ApplyBean> all_apply;

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

        public List<ApplyBean> getParent_apply() {
            return parent_apply;
        }

        public void setParent_apply(List<ApplyBean> parent_apply) {
            this.parent_apply = parent_apply;
        }

        public int getParent_num() {
            return parent_num;
        }

        public void setParent_num(int parent_num) {
            this.parent_num = parent_num;
        }

        public List<ApplyBean> getAll_apply() {
            return all_apply;
        }

        public void setAll_apply(List<ApplyBean> all_apply) {
            this.all_apply = all_apply;
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
            //是否是家长申请
            private boolean isParentApply;
            private boolean check;

            private String visit_time;
            private String student_name;

            public String getVisit_time() {
                return visit_time;
            }

            public void setVisit_time(String visit_time) {
                this.visit_time = visit_time;
            }

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

            public boolean isParentApply() {
                return isParentApply;
            }

            public void setParentApply(boolean parentApply) {
                isParentApply = parentApply;
            }

            public String getStudent_name() {
                return student_name;
            }

            public void setStudent_name(String student_name) {
                this.student_name = student_name;
            }
        }

        public static class ParentApplyBean {
            /**
             * apply_id : 10000022
             * apply_type : 22
             * leave_type : 0
             * userid : 30000001
             * company_id : 1000153
             * student_id : 20000000
             * approver_id : 50001063
             * send_id : 50001063
             * reviewer_id : 0
             * opinion :
             * reason : 测试
             * img : #9396fa
             * status : 0
             * review_time : 1000-01-01 00:00:00
             * time : 0
             * visit_time : 2018-08-06 17:09:00
             * start_time : 1000-01-01 00:00:00
             * end_time : 1000-01-01 00:00:00
             * add_time : 2018-08-06 16:09:39
             * update_time : 2018-08-06 16:09:39
             * name : 波风水门
             */

            private int apply_id;
            private int apply_type;
            private int leave_type;
            private int userid;
            private int company_id;
            private int student_id;
            private String approver_id;
            private String send_id;
            private int reviewer_id;
            private String opinion;
            private String reason;
            private String img;
            private int status;
            private String review_time;
            private int time;
            private String visit_time;
            private String start_time;
            private String end_time;
            private String add_time;
            private String update_time;
            private String name;
            private String check;

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

            public int getLeave_type() {
                return leave_type;
            }

            public void setLeave_type(int leave_type) {
                this.leave_type = leave_type;
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

            public int getStudent_id() {
                return student_id;
            }

            public void setStudent_id(int student_id) {
                this.student_id = student_id;
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getReview_time() {
                return review_time;
            }

            public void setReview_time(String review_time) {
                this.review_time = review_time;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public String getVisit_time() {
                return visit_time;
            }

            public void setVisit_time(String visit_time) {
                this.visit_time = visit_time;
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

            public String getCheck() {
                return check;
            }

            public void setCheck(String check) {
                this.check = check;
            }

            public ApplyBean getApplyBean() {
               /* private int apply_id;
                private int apply_type;
                private int leave_type;
                private int userid;
                private int company_id;
                private int student_id;
                private String approver_id;
                private String send_id;
                private int reviewer_id;
                private String opinion;
                private String reason;
                private String img;
                private int status;
                private String review_time;
                private int time;
                private String visit_time;
                private String start_time;
                private String end_time;
                private String add_time;
                private String update_time;
                private String name;
                private String check;*/
                ApplyBean applyBean = new ApplyBean();
                applyBean.setApply_id(apply_id);
                applyBean.setApply_type(apply_type);
                applyBean.setLeave_type(leave_type);
                applyBean.setUserid(userid);
                applyBean.setCompany_id(company_id);
                applyBean.setApprover_id(approver_id);
                applyBean.setSend_id(send_id);
                applyBean.setReviewer_id(reviewer_id);
                applyBean.setOpinion(opinion);
                applyBean.setReason(reason);
                applyBean.setImg(img);
                applyBean.setStatus(status);
                applyBean.setReview_time(review_time);
                applyBean.setTime(time);
                applyBean.setVisit_time(visit_time);
                applyBean.setStart_time(start_time);
                applyBean.setEnd_time(end_time);
                applyBean.setAdd_time(add_time);
                applyBean.setUpdate_time(update_time);
                applyBean.setName(name);

                return null;
            }
        }
    }
}
