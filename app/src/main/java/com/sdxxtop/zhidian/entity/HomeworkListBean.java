package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class HomeworkListBean extends BaseModel<HomeworkListBean.DataBean> {

    public static class DataBean {
        /**
         * num : 6
         * task : [{"task_id":7,"content":"好好干活","student_id":"","class_id":"8,9","add_time":"2018-07-18 17:04:46","title":"一班,二班作业"},{"task_id":6,"content":"测试11","student_id":"20000005,20000006","class_id":"8","add_time":"2018-07-17 22:27:40","title":"一班,漩涡鸣人5,漩涡鸣人6作业"},{"task_id":4,"content":"测试11","student_id":"20000005,20000006","class_id":"8","add_time":"2018-07-17 22:08:37","title":"一班,漩涡鸣人5,漩涡鸣人6作业"},{"task_id":3,"content":"测试11","student_id":"20000005,20000006","class_id":"8","add_time":"2018-07-17 22:07:36","title":"一班,漩涡鸣人5,漩涡鸣人6作业"},{"task_id":2,"content":"测试11","student_id":"20000005,20000006","class_id":"8","add_time":"2018-07-17 22:04:48","title":"一班,漩涡鸣人5,漩涡鸣人6作业"},{"task_id":1,"content":"测试11","student_id":"20000005,20000006","class_id":"8","add_time":"2018-07-17 22:04:25","title":"一班,漩涡鸣人5,漩涡鸣人6作业"}]
         */

        private int num;
        private List<TaskBean> task;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public List<TaskBean> getTask() {
            return task;
        }

        public void setTask(List<TaskBean> task) {
            this.task = task;
        }

        public static class TaskBean {
            /**
             * task_id : 7
             * content : 好好干活
             * student_id :
             * class_id : 8,9
             * add_time : 2018-07-18 17:04:46
             * title : 一班,二班作业
             */

            private int task_id;
            private String content;
            private String student_id;
            private String class_id;
            private String add_time;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
