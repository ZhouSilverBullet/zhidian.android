package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class MsgIndexBean extends BaseModel<List<MsgIndexBean.DataBean>> {

    public static class DataBean {
        /**
         * title : 测试
         * update_time : 2018-07-17 15:32:39
         * notice_num : 1
         * type : 2
         */

        private String title;
        private String update_time;
        private int notice_num;
        private int type;
        private int apply_num;
        private int report_num;
        private int num;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getNotice_num() {
            return notice_num;
        }

        public void setNotice_num(int notice_num) {
            this.notice_num = notice_num;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getApply_num() {
            return apply_num;
        }

        public void setApply_num(int apply_num) {
            this.apply_num = apply_num;
        }

        public int getReport_num() {
            return report_num;
        }

        public void setReport_num(int report_num) {
            this.report_num = report_num;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
