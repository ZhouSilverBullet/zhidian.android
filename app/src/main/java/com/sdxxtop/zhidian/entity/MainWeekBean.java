package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class MainWeekBean extends BaseModel<MainWeekBean.DataBean> {

    public static class DataBean {
        private List<String> abnormal;

        public List<String> getAbnormal() {
            return abnormal;
        }

        public void setAbnormal(List<String> abnormal) {
            this.abnormal = abnormal;
        }
    }
}
