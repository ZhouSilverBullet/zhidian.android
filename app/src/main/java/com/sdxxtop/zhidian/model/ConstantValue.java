package com.sdxxtop.zhidian.model;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：控制常量值
 */

public class ConstantValue {

    public static String BASE_URL = "http://app.sdxxtop.com/api/";

    /**
     * sp地址
     */
    public static String CONFIG = "config";

    // TODO: 2018/3/21  发版切换
    public static boolean isDebug = true;

    /**
     * 用户id
     */
    public static String USER_ID = "user_id";

    /**
     * 用户名
     */
    public static String USER_NAME = "username";

    /**
     * 用户密码
     */
    public static String PASSWORD = "password";

    /**
     * 当前设备名称
     */
    public static String DEVICE_NAME = "device_name";

    /**
     * 当前设备的IMEI
     */
    public static String DEVICE_NO = "device_no";

    /**
     * 登陆标记
     */
    public static String IS_LOGIN = "ISLOGIN";

    /**
     * 自动登陆实现时间戳
     */
    public static String LOGIN_TIME_TEMP = "login_timetemp";

    /**
     * 历史账号密码
     */
    public static String HISTORY_ACCOUNT = "HISTORY_ACCOUNT";
    /**
     * 自动登陆token
     */
    public static String LOGIN_TOKEN = "login_token";

    /**
     * 所属公司id
     */
    public static String COMPANY_ID = "company_id";

    /**
     * 平台类型(1:安卓 2:IOS)
     */
    public static String pi = "1";//平台id

    /**
     * NetUtil中base64编码用
     */
    public static String STR_SPLICE_SYMBOL = "&";
    public static String STR_EQUAL_OPERATION = "=";

    /**
     * NetUtil中APP_KEY
     */
    public static String APP_KEY = "60487FE91A0577ED60C4DC56A9EF3DB5";

    /**
     * 提醒打卡时间
     */
    public static String REMIND_MIN = "remind_min";

    /**
     * 消息免打扰开始时间
     */
    public static String BEGIN_TIME = "begin_time";

    /**
     * 消息免打扰关闭时间
     */
    public static String END_TIME = "end_time";

    /**
     * 消息接收checkbox状态
     */
    public static String NOTICEISCHECKED = "noticeIsChecked";

    /**
     * 消息接收checkbox状态
     */
    public static String REJECTIONISCHECKED = "rejectionIsChecked";

    /**
     * 打卡状态
     */
    public static String TAKE_CARD_STATE = "take_card_state";

    /**
     * 定位longitude 经度
     */
    public static String MAP_JING_DU = "longitude";

    /**
     * 定位longitude 纬度
     */
    public static String MAP_WEI_DU = "latitude";
    /**
     * 定位 地址
     */
    public static String MAP_ADDRESS = "address";
    /**
     * 创建公司，公司规模
     */
    public static String COMPANY_SIZE = "company_size";

    public static String GPS_SETTING_SIZE = "gps_setting_size";

    public static String COMPANY_JIN_WEIDU = "company_jin_weidu";

    /**
     * guide 出现
     */
    public static String GUIDE_IS_SHOW = "guide_is_show";

    /**
     * 当前版本 版本高出现guide
     */
    public static String GUIDE_SHOW_VERSION = "guide_show_version";
}
