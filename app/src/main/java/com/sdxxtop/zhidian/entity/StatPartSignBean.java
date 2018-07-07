package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class StatPartSignBean extends BaseModel<StatPartSignBean.DataBean> {

    public static class DataBean {
        private List<StatBean> stat;

        public List<StatBean> getStat() {
            return stat;
        }

        public void setStat(List<StatBean> stat) {
            this.stat = stat;
        }

        public static class StatBean {
            /**
             * part_id : 434
             * num : 1
             * part_name : 上海分部
             */

            private int part_id;
            private float num;
            private String part_name;

            public int getPart_id() {
                return part_id;
            }

            public void setPart_id(int part_id) {
                this.part_id = part_id;
            }

            public float getNum() {
                return num;
            }

            public void setNum(float num) {
                this.num = num;
            }

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }
        }
    }
}
