package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class ReportColorTextBean extends BaseModel<ReportColorTextBean.DataBean> {

    public static class DataBean {
        private List<PartListBean> part_list;
        private List<UserListBean> user_list;

        public List<PartListBean> getPart_list() {
            return part_list;
        }

        public void setPart_list(List<PartListBean> part_list) {
            this.part_list = part_list;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        public static class PartListBean {
            /**
             * bgColor : #3296FA
             * textValue : 今日迟到
             * type : 1
             * dec : 统计各部门每天的迟到次数
             */

            private String bgColor;
            private String textValue;
            private String type;
            private String dec;
            private boolean isOpen;
            private int index;

            public String getBgColor() {
                return bgColor;
            }

            public void setBgColor(String bgColor) {
                this.bgColor = bgColor;
            }

            public String getTextValue() {
                return textValue;
            }

            public void setTextValue(String textValue) {
                this.textValue = textValue;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDec() {
                return dec;
            }

            public void setDec(String dec) {
                this.dec = dec;
            }

            public boolean isOpen() {
                return isOpen;
            }

            public void setOpen(boolean open) {
                isOpen = open;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }
        }

        public static class UserListBean {
            /**
             * bgColor : #3296FA
             * textValue : 今日迟到榜
             * type : 1
             * dec : 统计每天迟到员工榜单
             */

            private String bgColor;
            private String textValue;
            private String type;
            private String dec;
            private boolean isOpen;
            private int index;

            public String getBgColor() {
                return bgColor;
            }

            public void setBgColor(String bgColor) {
                this.bgColor = bgColor;
            }

            public String getTextValue() {
                return textValue;
            }

            public void setTextValue(String textValue) {
                this.textValue = textValue;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDec() {
                return dec;
            }

            public void setDec(String dec) {
                this.dec = dec;
            }

            public boolean isOpen() {
                return isOpen;
            }

            public void setOpen(boolean open) {
                isOpen = open;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }
        }
    }


//    public int type;
//    public String bgColor;
//    public String textValue;
//    public boolean isOpen;
//    public String dec;
//
//    public ReportColorTextBean(int type, String bgColor, String textValue, boolean isOpen, String dec) {
//        this.type = type;
//        this.bgColor = bgColor;
//        this.textValue = textValue;
//        this.isOpen = isOpen;
//        this.dec = dec;
//    }
}
