package com.sdxxtop.zhidian.entity;

import com.google.gson.annotations.SerializedName;
import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class ShowClassBean extends BaseModel<ShowClassBean.DataBean> {

    public static class DataBean {
        @SerializedName("class")
        private List<ClassBean> classX;

        public List<ClassBean> getClassX() {
            return classX;
        }

        public void setClassX(List<ClassBean> classX) {
            this.classX = classX;
        }

        public static class ClassBean {
            /**
             * class_id : 9
             * name : 早九晚五
             * sign_date : 09:00,12:30,15:00,18:30
             * range : 09:00-18:30
             * is_select : 2
             */

            private int class_id;
            private String name;
            private String sign_date;
            private String range;
            private int is_select;
            private boolean isCheck;

            public int getClass_id() {
                return class_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSign_date() {
                return sign_date;
            }

            public void setSign_date(String sign_date) {
                this.sign_date = sign_date;
            }

            public String getRange() {
                return range;
            }

            public void setRange(String range) {
                this.range = range;
            }

            public int getIs_select() {
                return is_select;
            }

            public void setIs_select(int is_select) {
                this.is_select = is_select;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }
        }
    }
}
