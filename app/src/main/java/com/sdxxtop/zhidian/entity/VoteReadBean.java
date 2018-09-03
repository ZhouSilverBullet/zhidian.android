package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：10:56
 * 邮箱：15010104100@163.com
 * 描述：浏览民主评议
 */

public class VoteReadBean extends BaseModel<VoteReadBean.DataBean> {
    /**
     * data : {"vote_id":213,"title":"呼呼呼","operator_id":50000000,"operator_name":"周洋1","rest_time":"6天23时35分","second":603356,"people_num":0,"vote_desc":"VB宝贝","is_only":1,"is_hide":2,"is_show":2,"is_bind":2,"is_vote":2,"score":5,"score_img":"http://cdn.sdxxtop.com/app/vote/20180519151905290414.png","part_name":["技术部","运营部","行政部","人事部","法务部","财务部","上海总部","市场部","小卖部","演艺部","旭兴科技 ","旭兴科技","市场部-企划部","测试1.1.1"],"name":["熊熊","草地","斗战胜佛","lizhaobin","噜噜噜","李昭斌斌","李晓斌","熊大","熊大","爸","李昭斌","啊啊啊","老二","大娃","门面","熊二","周周","lizhaobin","噜噜噜","李晓斌","1333666999999","啊啊啊","简单账号","122","122"],"sum":0,"option":[{"option_id":476,"option_name":"","img":"","num":0,"is_vote":2,"score":0}]}
     */


    public static class DataBean {
        /**
         * vote_id : 213
         * title : 呼呼呼
         * operator_id : 50000000
         * operator_name : 周洋1
         * rest_time : 6天23时35分
         * second : 603356
         * people_num : 0
         * vote_desc : VB宝贝
         * is_only : 1
         * is_hide : 2
         * is_show : 2
         * is_bind : 2
         * is_vote : 2
         * score : 5
         * score_img : http://cdn.sdxxtop.com/app/vote/20180519151905290414.png
         * part_name : ["技术部","运营部","行政部","人事部","法务部","财务部","上海总部","市场部","小卖部","演艺部","旭兴科技 ","旭兴科技","市场部-企划部","测试1.1.1"]
         * name : ["熊熊","草地","斗战胜佛","lizhaobin","噜噜噜","李昭斌斌","李晓斌","熊大","熊大","爸","李昭斌","啊啊啊","老二","大娃","门面","熊二","周周","lizhaobin","噜噜噜","李晓斌","1333666999999","啊啊啊","简单账号","122","122"]
         * sum : 0
         * option : [{"option_id":476,"option_name":"","img":"","num":0,"is_vote":2,"score":0}]
         */

        private int vote_id;
        private String title;
        private int operator_id;
        private String operator_name;
        private String rest_time;
        private int second;
        private int people_num;
        private String vote_desc;
        private int is_only;
        private int is_hide;
        private int is_show;
        private int is_bind;
        private int is_vote;
        private int score;
        private String score_img;
        private int sum;
        private List<String> part_name;
        private List<String> name;
        private List<OptionBean> option;

        public int getVote_id() {
            return vote_id;
        }

        public void setVote_id(int vote_id) {
            this.vote_id = vote_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getOperator_id() {
            return operator_id;
        }

        public void setOperator_id(int operator_id) {
            this.operator_id = operator_id;
        }

        public String getOperator_name() {
            return operator_name;
        }

        public void setOperator_name(String operator_name) {
            this.operator_name = operator_name;
        }

        public String getRest_time() {
            return rest_time;
        }

        public void setRest_time(String rest_time) {
            this.rest_time = rest_time;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public int getPeople_num() {
            return people_num;
        }

        public void setPeople_num(int people_num) {
            this.people_num = people_num;
        }

        public String getVote_desc() {
            return vote_desc;
        }

        public void setVote_desc(String vote_desc) {
            this.vote_desc = vote_desc;
        }

        public int getIs_only() {
            return is_only;
        }

        public void setIs_only(int is_only) {
            this.is_only = is_only;
        }

        public int getIs_hide() {
            return is_hide;
        }

        public void setIs_hide(int is_hide) {
            this.is_hide = is_hide;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public int getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(int is_bind) {
            this.is_bind = is_bind;
        }

        public int getIs_vote() {
            return is_vote;
        }

        public void setIs_vote(int is_vote) {
            this.is_vote = is_vote;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getScore_img() {
            return score_img;
        }

        public void setScore_img(String score_img) {
            this.score_img = score_img;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public List<String> getPart_name() {
            return part_name;
        }

        public void setPart_name(List<String> part_name) {
            this.part_name = part_name;
        }

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }

        public List<OptionBean> getOption() {
            return option;
        }

        public void setOption(List<OptionBean> option) {
            this.option = option;
        }

        public static class OptionBean {
            /**
             * option_id : 476
             * option_name :
             * img :
             * num : 0
             * is_vote : 2
             * score : 0
             */

            private int option_id;
            private String option_name;
            private String img;
            private int num;
            private int is_vote;
            private int score;
            private String average;
            //自己加入
            private boolean isCheck;

            public int getOption_id() {
                return option_id;
            }

            public void setOption_id(int option_id) {
                this.option_id = option_id;
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

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getIs_vote() {
                return is_vote;
            }

            public void setIs_vote(int is_vote) {
                this.is_vote = is_vote;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }
        }
    }

    /*{
        "code" : 200,
            "msg":"成功",
            "data" :{
        "vote_id" : 2,                           //民主评议id
                "title":"放假",                          //标题
                "operator_name":"周洋",                //发布人
                "vote_desc": "这是民主评议描述"         //民主评议内容
        "rest_time":"1天11时2分"             //剩余时间
        "people_num" : 1                     //参与人数
        "is_vote":1                          //当前登录用户是否已投票(1:已投 2:未投)
        "is_only":1                          //单选多选(1:单选 2:多选) 用于投票
        "is_show":1                         //结果展示(1:结束后展示 2:发布可见)
        "is_hide":1                        //是否匿名(1:公开 2:匿名)
        "score":100                       //打分分数  用于打分
        "score_img"                      //打分图片  没有时为空
        "sum" : 20                       //已投票数和  用于投票
        "name":{      //民主评议指定的用户,一维数组
            "周洋",
                    "六七"
        }
        "part_name":{  //民主评议指定的部门,一维数组
            "技术部",
                    "运营部"
        }
        "option":[    //选项二维数组
        {
            "option_id" : 2,
                "option_name" :    //打分没有指定打分对象时 此处为空
            "img":            //选项图片
            "num"            //民主评议为打分是 是选项的最优分 为投票是为选项的票数
        }
]

    }
    }*/
}
