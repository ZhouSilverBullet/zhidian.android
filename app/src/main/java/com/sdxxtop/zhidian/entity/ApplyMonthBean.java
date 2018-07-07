package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public class ApplyMonthBean extends BaseModel<ApplyMonthBean.DataBean> {
    /**
     * data : {"abnormal":["2017-04-11","2017-05-10"]}
     */
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
