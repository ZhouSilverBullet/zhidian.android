package com.sdxxtop.zhidian.entity;

import android.text.TextUtils;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class StatMonthBean extends BaseModel<StatMonthBean.DataBean> {

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
             * part_id : 18
             * userid : 50000039
             * num : 2
             * part_name : 技术部
             * img : http://cdn.sdxxtop.com/app/face/20180508222401386756.png
             * name : 周洋-0522
             */

            private int part_id;
            private int userid;
            private String num;
            private String ave;
            private String sign_time;
            private String part_name;
            private String img;
            private String name;
            //用于排序判断
            private boolean isAveSort;
            //是否正排序
            private boolean isSquareSort;

            public int getPart_id() {
                return part_id;
            }

            public void setPart_id(int part_id) {
                this.part_id = part_id;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAve() {
                return ave;
            }

            public void setAve(String ave) {
                this.ave = ave;
            }

            public String getSign_time() {
                return sign_time;
            }

            public void setSign_time(String sign_time) {
                this.sign_time = sign_time;
            }

//            @Override
//            public int compareTo(@NonNull StatBean o) {
//                if (isAveSort) {
//                    if (isSquareSort) { //正排序
//                        if (parse(this.getAve()) > parse(o.getAve())) {
//                            return 1;
//                        } else {
//                            return 0;
//                        }
//                    } else { //反排序
//                        if (parse(o.getAve()) > parse(this.getAve())) {
//                            return 1;
//                        } else {
//                            return 0;
//                        }
//                    }
//                } else {
//                    if (isSquareSort) {
//                        if (parse(this.getNum()) > parse(o.getNum())) {
//                            return 1;
//                        } else {
//                            return 0;
//                        }
//                    } else {
//                        if (parse(o.getNum()) > parse(this.getNum())) {
//                            return 1;
//                        } else {
//                            return 0;
//                        }
//                    }
//                }
//            }

            public boolean isAveSort() {
                return isAveSort;
            }

            public void setAveSort(boolean sumSort) {
                isAveSort = sumSort;
            }

            public boolean isSquareSort() {
                return isSquareSort;
            }

            public void setSquareSort(boolean squareSort) {
                isSquareSort = squareSort;
            }

            public Float parse(String value) {
                Float v = 0f;
                if (!TextUtils.isEmpty(value)) {
                    v = Float.valueOf(value);
                }
                return v;
            }
        }
    }
}
