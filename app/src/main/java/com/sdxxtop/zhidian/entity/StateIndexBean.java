package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * Created by Administrator on 2018/5/16.
 */

public class StateIndexBean extends BaseModel<StateIndexBean.DataBean> {

    public static class DataBean {
        /**
         * part_stat : 2,3,4,5,6,7,8,9,10
         * user_stat : 1,2,3,4,5,6,7,8,9,10
         */

        private String part_stat;
        private String user_stat;

        public String getPart_stat() {
            return part_stat;
        }

        public void setPart_stat(String part_stat) {
            this.part_stat = part_stat;
        }

        public String getUser_stat() {
            return user_stat;
        }

        public void setUser_stat(String user_stat) {
            this.user_stat = user_stat;
        }
    }
}
