package com.sdxxtop.zhidian.entity;

import java.util.List;

/**
 * 作者：CaiCM
 * 日期：2018/4/20  时间：16:19
 * 邮箱：15010104100@163.com
 * 描述：用户加入的公司列表实体
 */

public class ShowCompanyBean {

    /**
     * code : 200
     * msg : 成功
     * data : {"data":[{"company_id":1000035,"company_name":"旭兴科技公司"},{"company_id":1000091,"company_name":"旭兴科技有限公司"},{"company_id":1000092,"company_name":"蔡"},{"company_id":1000094,"company_name":"。"},{"company_id":1000095,"company_name":"，"},{"company_id":1000096,"company_name":"，"},{"company_id":1000098,"company_name":"111"},{"company_id":1000099,"company_name":"，，，"},{"company_id":1000100,"company_name":"抛弃"},{"company_id":1000101,"company_name":"12"},{"company_id":1000126,"company_name":"1"},{"company_id":1000155,"company_name":"，"},{"company_id":1000156,"company_name":"。"},{"company_id":1000157,"company_name":"？"},{"company_id":1000158,"company_name":"。"},{"company_id":1000159,"company_name":"。"},{"company_id":1000163,"company_name":"，"},{"company_id":1000164,"company_name":"，"},{"company_id":1000165,"company_name":"八"},{"company_id":1000166,"company_name":"。"},{"company_id":1000167,"company_name":"w"},{"company_id":1000168,"company_name":"9"},{"company_id":1000171,"company_name":"，"},{"company_id":1000172,"company_name":"。"},{"company_id":1000173,"company_name":"，"},{"company_id":1000174,"company_name":"，"},{"company_id":1000175,"company_name":"，"},{"company_id":1000176,"company_name":"，"},{"company_id":1000177,"company_name":"。"},{"company_id":1000178,"company_name":"。"},{"company_id":1000179,"company_name":"？"},{"company_id":1000180,"company_name":"，"},{"company_id":1000181,"company_name":"，"},{"company_id":1000182,"company_name":"，"},{"company_id":1000183,"company_name":"，"},{"company_id":1000184,"company_name":"，"},{"company_id":1000185,"company_name":"，"},{"company_id":1000186,"company_name":"，"},{"company_id":1000187,"company_name":"，"},{"company_id":1000188,"company_name":"。"},{"company_id":1000189,"company_name":"，"},{"company_id":1000190,"company_name":"你"},{"company_id":1000192,"company_name":"看看"},{"company_id":1000193,"company_name":"我"},{"company_id":1000194,"company_name":"旭兴"},{"company_id":1000195,"company_name":"，"},{"company_id":1000196,"company_name":"旭兴科技大楼"},{"company_id":1000207,"company_name":"我"},{"company_id":1000208,"company_name":"1"},{"company_id":1000209,"company_name":"1"},{"company_id":1000210,"company_name":"1"},{"company_id":1000220,"company_name":"你妹夫死机"},{"company_id":1000221,"company_name":"煞笔"},{"company_id":1000222,"company_name":"我们"},{"company_id":1000223,"company_name":"坤音娱乐公司"},{"company_id":1000224,"company_name":"们"},{"company_id":1000232,"company_name":"好"},{"company_id":1000233,"company_name":"好"},{"company_id":1000243,"company_name":"北京信息"}]}
     */

    private int code;
    private String msg;
    private DataEntityX data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataEntityX getData() {
        return data;
    }

    public void setData(DataEntityX data) {
        this.data = data;
    }

    public static class DataEntityX {
        private List<DataEntity> data;

        public List<DataEntity> getData() {
            return data;
        }

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public static class DataEntity {
            /**
             * company_id : 1000035
             * company_name : 旭兴科技公司
             */

            private int company_id;
            private String company_name;

            public int getCompany_id() {
                return company_id;
            }

            public void setCompany_id(int company_id) {
                this.company_id = company_id;
            }

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }
        }
    }

    @Override
    public String toString() {
        return "ShowCompanyBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
