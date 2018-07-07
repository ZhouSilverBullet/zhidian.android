package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：18:05
 * 邮箱：15010104100@163.com
 * 描述：浏览公告
 */

public class NoticeReadBean extends BaseModel<NoticeReadBean.DataEntity> {

    public static class DataEntity {
        /**
         * read_num : 3
         * not_num : 9
         * notice : {"notice_id":18,"title":"放假","name":"周洋","content":"","add_time":"2018-03-17 23:22:00","img":""}
         * name : ["周洋","六七"]
         * part_name : ["技术部","运营部"]
         */

        private int read_num;
        private int not_num;
        private NoticeEntity notice;
        private List<String> name;
        private List<String> part_name;

        public int getRead_num() {
            return read_num;
        }

        public void setRead_num(int read_num) {
            this.read_num = read_num;
        }

        public int getNot_num() {
            return not_num;
        }

        public void setNot_num(int not_num) {
            this.not_num = not_num;
        }

        public NoticeEntity getNotice() {
            return notice;
        }

        public void setNotice(NoticeEntity notice) {
            this.notice = notice;
        }

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }

        public List<String> getPart_name() {
            return part_name;
        }

        public void setPart_name(List<String> part_name) {
            this.part_name = part_name;
        }

        public static class NoticeEntity {
            /**
             * notice_id : 18
             * title : 放假
             * name : 周洋
             * content :
             * add_time : 2018-03-17 23:22:00
             * img :
             */

            private int is_bind;
            private int notice_id;
            private String title;
            private String name;
            private String content;
            private String add_time;
            private String img;
            private String operator_id;

            public int getIs_bind() {
                return is_bind;
            }

            public void setIs_bind(int is_bind) {
                this.is_bind = is_bind;
            }

            public int getNotice_id() {
                return notice_id;
            }

            public void setNotice_id(int notice_id) {
                this.notice_id = notice_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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

            public String getOperator_id() {
                return operator_id;
            }

            public void setOperator_id(String operator_id) {
                this.operator_id = operator_id;
            }
        }
    }
}
