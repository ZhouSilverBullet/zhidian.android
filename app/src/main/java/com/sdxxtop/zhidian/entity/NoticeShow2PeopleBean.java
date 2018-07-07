package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：公告阅读人详情实体
 */

public class NoticeShow2PeopleBean extends BaseModel<NoticeShow2PeopleBean.DataBean> {

    public static class DataBean {
        private List<ReadBean> read;
        private List<NotReadBean> not_read;

        public List<ReadBean> getRead() {
            return read;
        }

        public void setRead(List<ReadBean> read) {
            this.read = read;
        }

        public List<NotReadBean> getNot_read() {
            return not_read;
        }

        public void setNot_read(List<NotReadBean> not_read) {
            this.not_read = not_read;
        }

        public static class ReadBean implements Serializable {
            /**
             * name : 周洋
             * img : #aa76ba
             */

            private String name;
            private String img;

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

        public static class NotReadBean {
            /**
             * name : 周洋
             * img : #aa76ba
             */

            private String name;
            private String img;

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
