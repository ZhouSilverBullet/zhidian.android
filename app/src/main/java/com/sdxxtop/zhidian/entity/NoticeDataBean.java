package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * 作者: CaiCM
 * 日期: 2018/3/24 时间: 11:05
 * 描述:公告主页实体
 */

public class NoticeDataBean extends BaseModel<NoticeDataBean.DataBean> {


    /**
     * code : 200
     * msg : 成功
     * data : {"is_add":1,"is_read":2,"num":10,"notice":[{"notice_id":18,"title":"放假","name":"周洋","add_time":"2018-03-17 23:22:00","img":""}]}
     */

    public static class DataBean {
        /**
         * is_add : 1
         * is_read : 2
         * num : 10
         * notice : [{"notice_id":18,"title":"放假","name":"周洋","add_time":"2018-03-17 23:22:00","img":""}]
         */

        private int is_add;
        private int is_read;
        private int num;
        private List<NoticeBean> notice;

        public int getIs_add() {
            return is_add;
        }

        public void setIs_add(int is_add) {
            this.is_add = is_add;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public List<NoticeBean> getNotice() {
            return notice;
        }

        public void setNotice(List<NoticeBean> notice) {
            this.notice = notice;
        }

        public static class NoticeBean {
            /**
             * notice_id : 18
             * title : 放假
             * name : 周洋
             * add_time : 2018-03-17 23:22:00
             * img :
             */

            private int notice_id;
            private String title;
            private String name;
            private String add_time;
            private String img;

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
        }
    }
}
