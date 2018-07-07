package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/4/19  时间：10:02
 * 邮箱：15010104100@163.com
 * 描述：外勤权限主页实体
 */

public class UcenterOutIndexBean extends BaseModel<UcenterOutIndexBean.DataEntity> {

    public static class DataEntity {
        private List<PartEntity> part;
        private List<List<UserinfoEntity>> userinfo;

        public List<PartEntity> getPart() {
            return part;
        }

        public void setPart(List<PartEntity> part) {
            this.part = part;
        }

        public List<List<UserinfoEntity>> getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(List<List<UserinfoEntity>> userinfo) {
            this.userinfo = userinfo;
        }

        public static class PartEntity {

            public PartEntity(int part_id, String part_name) {
                this.part_id = part_id;
                this.part_name = part_name;
            }

            private int part_id;
            private String part_name;
            private String no_prefix;

            public int getPart_id() {
                return part_id;
            }

            public void setPart_id(int part_id) {
                this.part_id = part_id;
            }

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }

            public String getNo_prefix() {
                return no_prefix;
            }

            public void setNo_prefix(String no_prefix) {
                this.no_prefix = no_prefix;
            }
        }

        public static class UserinfoEntity {
            /**
             * part_name : 技术部
             * userid : 50000028
             * name : 王五
             * img : #9d81ff
             * is_out : 2
             */

            private String part_name;
            private int userid;
            private int part_id;
            private String name;
            private String img;
            private int is_out;

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

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

            public int getIs_out() {
                return is_out;
            }

            public void setIs_out(int is_out) {
                this.is_out = is_out;
            }

            public int getPart_id() {
                return part_id;
            }

            public void setPart_id(int part_id) {
                this.part_id = part_id;
            }
        }
    }

}
