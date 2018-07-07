package com.sdxxtop.zhidian.entity;

import com.google.gson.annotations.SerializedName;
import com.sdxxtop.zhidian.http.BaseModel;

/**
 * Created by Administrator on 2018/5/16.
 */

public class ReadClassBean extends BaseModel<ReadClassBean.DataBean>{

    public static class DataBean {
        /**
         * class : {"class_id":164,"name":"测试妹子你好","short_name":"好","sign_num":8,"sign_name":"手下留情,温柔点,手下留情,温柔点,手下留情,温柔点,手下留情,温柔点","sign_date":"00:00,06:00,06:00,12:00,12:00,18:00,18:00,00:00","sign_time":15,"level_time":0}
         */

        @SerializedName("class")
        private ClassBean classX;

        public ClassBean getClassX() {
            return classX;
        }

        public void setClassX(ClassBean classX) {
            this.classX = classX;
        }

        public static class ClassBean {
            /**
             * class_id : 164
             * name : 测试妹子你好
             * short_name : 好
             * sign_num : 8
             * sign_name : 手下留情,温柔点,手下留情,温柔点,手下留情,温柔点,手下留情,温柔点
             * sign_date : 00:00,06:00,06:00,12:00,12:00,18:00,18:00,00:00
             * sign_time : 15
             * level_time : 0
             */

            private int class_id;
            private String name;
            private String short_name;
            private int sign_num;
            private String sign_name;
            private String sign_date;
            private int sign_time;
            private int level_time;

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

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public int getSign_num() {
                return sign_num;
            }

            public void setSign_num(int sign_num) {
                this.sign_num = sign_num;
            }

            public String getSign_name() {
                return sign_name;
            }

            public void setSign_name(String sign_name) {
                this.sign_name = sign_name;
            }

            public String getSign_date() {
                return sign_date;
            }

            public void setSign_date(String sign_date) {
                this.sign_date = sign_date;
            }

            public int getSign_time() {
                return sign_time;
            }

            public void setSign_time(int sign_time) {
                this.sign_time = sign_time;
            }

            public int getLevel_time() {
                return level_time;
            }

            public void setLevel_time(int level_time) {
                this.level_time = level_time;
            }
        }
    }
}
