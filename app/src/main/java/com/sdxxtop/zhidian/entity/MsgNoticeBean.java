package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class MsgNoticeBean extends BaseModel<MsgNoticeBean.DataBean> {

    public static class DataBean {
        /**
         * num : 10
         * notice : [{"notice_id":193,"title":"不我","content":"讨论组我们","img":"http://cdn.sdxxtop.com/app/notice/20180511174646501520.jpg,http://cdn.sdxxtop.com/app/notice/20180511174646138062.jpg,http://cdn.sdxxtop.com/app/notice/20180511174646859525.png","add_time":"2018-05-11 17:46:46"},{"notice_id":190,"title":"刷新","content":"你老婆的就这样","img":"http://cdn.sdxxtop.com/app/notice/20180511160441821605.jpg,http://cdn.sdxxtop.com/app/notice/20180511160441141235.jpg,http://cdn.sdxxtop.com/app/notice/20180511160441423344.jpg","add_time":"2018-05-11 16:04:41"},{"notice_id":181,"title":"测试","content":"OMG你","img":"http://cdn.sdxxtop.com/app/notice/20180509152433839360.png,http://cdn.sdxxtop.com/app/notice/20180509152433192970.png,http://cdn.sdxxtop.com/app/notice/20180509152433432904.png","add_time":"2018-05-09 15:24:33"},{"notice_id":180,"title":"测试","content":"测试","img":"http://cdn.sdxxtop.com/app/notice/20180509144910517049.png,http://cdn.sdxxtop.com/app/notice/20180509144910756831.png,http://cdn.sdxxtop.com/app/notice/20180509144910621515.png","add_time":"2018-05-09 14:49:10"},{"notice_id":179,"title":"测试","content":"而你好吗","img":"http://cdn.sdxxtop.com/app/notice/20180509143044565994.png,http://cdn.sdxxtop.com/app/notice/20180509143044879561.png,http://cdn.sdxxtop.com/app/notice/20180509143044534729.png","add_time":"2018-05-09 14:30:44"},{"notice_id":178,"title":"测试","content":"测试功能","img":"http://cdn.sdxxtop.com/app/notice/20180509142718844358.png,http://cdn.sdxxtop.com/app/notice/20180509142718326081.png","add_time":"2018-05-09 14:27:18"},{"notice_id":175,"title":"moonlight","content":"心敏敏","img":"","add_time":"2018-05-08 22:06:05"},{"notice_id":174,"title":"放假通知","content":"放假三天","img":"","add_time":"2018-05-08 22:04:28"},{"notice_id":173,"title":"标题标题","content":"英雄","img":"","add_time":"2018-05-08 22:03:43"},{"notice_id":172,"title":"标题标题","content":"你哦","img":"","add_time":"2018-05-08 22:03:00"}]
         */

        private int num;
        private List<NoticeBean> notice;

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
             * notice_id : 193
             * title : 不我
             * content : 讨论组我们
             * img : http://cdn.sdxxtop.com/app/notice/20180511174646501520.jpg,http://cdn.sdxxtop.com/app/notice/20180511174646138062.jpg,http://cdn.sdxxtop.com/app/notice/20180511174646859525.png
             * add_time : 2018-05-11 17:46:46
             */

            private int notice_id;
            private String title;
            private String content;
            private String img;
            private String add_time;

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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }
        }
    }
}
