package com.sdxxtop.zhidian.entity;

import com.google.gson.annotations.SerializedName;
import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public class ApproverIndexBean extends BaseModel<ApproverIndexBean.DataBean> {

    public static class DataBean {
        /**
         * holiday_time : 10
         * name : 周洋1
         * device_name : iPhone SE
         * start_time : 2018-04-29 09:00
         * end_time : 2018-04-29 18:00
         * approver : [{"userid":50000004,"name":"李四","img":"#9396fa"}]
         */

        private int holiday_time;
        private String name;
        private String device_name;
        @SerializedName("statr_time")
        private String start_time;
        private String end_time;
        private List<ApproverBean> approver;

        public int getHoliday_time() {
            return holiday_time;
        }

        public void setHoliday_time(int holiday_time) {
            this.holiday_time = holiday_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
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

        public List<ApproverBean> getApprover() {
            return approver;
        }

        public void setApprover(List<ApproverBean> approver) {
            this.approver = approver;
        }

        public static class ApproverBean {
            /**
             * userid : 50000004
             * name : 李四
             * img : #9396fa
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
}
