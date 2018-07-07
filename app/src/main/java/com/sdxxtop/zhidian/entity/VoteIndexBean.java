package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：10:46
 * 邮箱：15010104100@163.com
 * 描述：民主评议主页实体
 */

public class VoteIndexBean extends BaseModel<VoteIndexBean.DataEntity> {


    public static class DataEntity {
        /**
         * is_self : 2
         * num : 10
         * vote : [{"vote_id":18,"vote_type":1,"title":"放假","name":"周洋","score":100,"end_time":"2018-03-27 23:22:00","add_time":"2018-03-17 23:22:00","option_name":["上海","山东"]}]
         */

        private int is_self;
        private int num;
        private List<VoteEntity> vote;

        public int getIs_self() {
            return is_self;
        }

        public void setIs_self(int is_self) {
            this.is_self = is_self;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public List<VoteEntity> getVote() {
            return vote;
        }

        public void setVote(List<VoteEntity> vote) {
            this.vote = vote;
        }

        public static class VoteEntity {
            /**
             * vote_id : 18
             * vote_type : 1
             * title : 放假
             * name : 周洋
             * score : 100
             * end_time : 2018-03-27 23:22:00
             * add_time : 2018-03-17 23:22:00
             * option_name : ["上海","山东"]
             */

            private int vote_id;
            private int vote_type;
            private String title;
            private String name;
            private int score;
            private String end_time;
            private String add_time;
            private List<String> option_name;

            public int getVote_id() {
                return vote_id;
            }

            public void setVote_id(int vote_id) {
                this.vote_id = vote_id;
            }

            public int getVote_type() {
                return vote_type;
            }

            public void setVote_type(int vote_type) {
                this.vote_type = vote_type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public List<String> getOption_name() {
                return option_name;
            }

            public void setOption_name(List<String> option_name) {
                this.option_name = option_name;
            }

        }
    }
}
