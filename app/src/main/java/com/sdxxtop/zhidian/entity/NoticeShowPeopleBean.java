package com.sdxxtop.zhidian.entity;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：公告阅读人详情实体
 */

public class NoticeShowPeopleBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"read":[{"name":"周洋","img":"#aa76ba"}],"not_read":[{"name":"周洋","img":"#aa76ba"}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

        public static class ReadBean {
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

    @Override
    public String toString() {
        return "NoticeShowPeopleBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
