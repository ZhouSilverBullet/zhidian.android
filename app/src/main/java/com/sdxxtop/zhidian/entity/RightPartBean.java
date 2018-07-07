package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class RightPartBean extends BaseModel<RightPartBean.DataBean> {

    public static class DataBean {
        private List<PartBean> part;

        public List<PartBean> getPart() {
            return part;
        }

        public void setPart(List<PartBean> part) {
            this.part = part;
        }

        public static class PartBean {
            /**
             * part_id : 18
             * company_id : 1000035
             * part_name : 技术部
             */

            private int part_id;
            private int company_id;
            private String part_name;
            private boolean isCheck;

            public int getPart_id() {
                return part_id;
            }

            public void setPart_id(int part_id) {
                this.part_id = part_id;
            }

            public int getCompany_id() {
                return company_id;
            }

            public void setCompany_id(int company_id) {
                this.company_id = company_id;
            }

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }
        }
    }
}
