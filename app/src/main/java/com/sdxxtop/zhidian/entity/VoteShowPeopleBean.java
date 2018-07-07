package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：11:00
 * 邮箱：15010104100@163.com
 * 描述：显示参与人详情实体
 */

public class VoteShowPeopleBean extends BaseModel<VoteShowPeopleBean.DataEntity> {

    /**
     * code : 200
     * msg : 成功
     * data : {"vote_type":1,"userinfo":[{"userid":50000000,"option_id":4,"score":80,"is_hide":1,"name":"周洋","option_name":"上海"}]}
     */

    public static class DataEntity {
        /**
         * vote_type : 1
         * userinfo : [{"userid":50000000,"option_id":4,"score":80,"is_hide":1,"name":"周洋","option_name":"上海"}]
         */

        private int vote_type;
        private List<UserinfoEntity> userinfo;

        public int getVote_type() {
            return vote_type;
        }

        public void setVote_type(int vote_type) {
            this.vote_type = vote_type;
        }

        public List<UserinfoEntity> getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(List<UserinfoEntity> userinfo) {
            this.userinfo = userinfo;
        }

        public static class UserinfoEntity {
            /**
             * userid : 50000000
             * option_id : 4
             * score : 80
             * is_hide : 1
             * name : 周洋
             * option_name : 上海
             */

            private int userid;
            private int option_id;
            private int score;
            private int is_hide;
            private String name;
            private String option_name;
            private String img;

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public int getOption_id() {
                return option_id;
            }

            public void setOption_id(int option_id) {
                this.option_id = option_id;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getIs_hide() {
                return is_hide;
            }

            public void setIs_hide(int is_hide) {
                this.is_hide = is_hide;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOption_name() {
                return option_name;
            }

            public void setOption_name(String option_name) {
                this.option_name = option_name;
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
