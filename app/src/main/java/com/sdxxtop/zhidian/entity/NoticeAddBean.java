package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：18:03
 * 邮箱：15010104100@163.com
 * 描述：添加公告
 */

public class NoticeAddBean extends BaseModel<NoticeAddBean.DataEntity>{

    public static class DataEntity {
        private int notice_id;

        public int getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(int notice_id) {
            this.notice_id = notice_id;
        }
    }
}
