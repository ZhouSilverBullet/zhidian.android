package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * Created by Administrator on 2018/7/18.
 */

public class HomeworkDetailBean extends BaseModel<HomeworkDetailBean.DataBean> {

    public static class DataBean {
        /**
         * task : {"task_id":8,"content":"测试","student_id":"一班","class_id":"","add_time":"2018-07-18 18:23:53","img":"","status":1,"title":"作业"}
         */

        private TaskBean task;

        public TaskBean getTask() {
            return task;
        }

        public void setTask(TaskBean task) {
            this.task = task;
        }

        public static class TaskBean {
            /**
             * task_id : 8
             * content : 测试
             * student_id : 一班
             * class_id :
             * add_time : 2018-07-18 18:23:53
             * img :
             * status : 1
             * title : 作业
             */

            private int task_id;
            private String content;
            private String student_id;
            private String class_id;
            private String add_time;
            private String img;
            private int status;
            private String title;

            public int getTask_id() {
                return task_id;
            }

            public void setTask_id(int task_id) {
                this.task_id = task_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getStudent_id() {
                return student_id;
            }

            public void setStudent_id(String student_id) {
                this.student_id = student_id;
            }

            public String getClass_id() {
                return class_id;
            }

            public void setClass_id(String class_id) {
                this.class_id = class_id;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
