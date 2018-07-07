package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * Created by Administrator on 2018/5/8.
 */

public class ApplyIndexBean extends BaseModel<ApplyIndexBean.DataBean> {

    public static class DataBean {
        int num;
        int is_out;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getIs_out() {
            return is_out;
        }

        public void setIs_out(int is_out) {
            this.is_out = is_out;
        }
    }
}
