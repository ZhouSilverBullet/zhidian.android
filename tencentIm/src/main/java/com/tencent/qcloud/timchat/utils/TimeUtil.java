package com.tencent.qcloud.timchat.utils;

import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间转换工具
 */
public class TimeUtil {


    private TimeUtil(){}

    /**
     * 时间转化为显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp){
        if (timeStamp==0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp*1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        if (calendar.before(inputTime)){
            //今天23:59在输入时间之前，解决一些时间误差，把当天时间显示到这里
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + MyApplication.getContext().getResources().getString(R.string.time_year)+"MM"+MyApplication.getContext().getResources().getString(R.string.time_month)+"dd"+MyApplication.getContext().getResources().getString(R.string.time_day));
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        if (calendar.before(inputTime)){
            return MyApplication.getContext().getResources().getString(R.string.time_yesterday);
        }else{
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)){
                SimpleDateFormat sdf = new SimpleDateFormat("M"+MyApplication.getContext().getResources().getString(R.string.time_month)+"d"+MyApplication.getContext().getResources().getString(R.string.time_day));
                return sdf.format(currenTimeZone);
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + MyApplication.getContext().getResources().getString(R.string.time_year)+"MM"+MyApplication.getContext().getResources().getString(R.string.time_month)+"dd"+MyApplication.getContext().getResources().getString(R.string.time_day));
                return sdf.format(currenTimeZone);

            }

        }

    }

    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(long timeStamp){
        if (timeStamp==0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp*1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        if (!calendar.after(inputTime)){
            //当前时间在输入时间之前
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + MyApplication.getContext().getResources().getString(R.string.time_year)+"MM"+MyApplication.getContext().getResources().getString(R.string.time_month)+"dd"+MyApplication.getContext().getResources().getString(R.string.time_day));
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        if (calendar.before(inputTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return MyApplication.getContext().getResources().getString(R.string.time_yesterday)+" "+sdf.format(currenTimeZone);
        }else{
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)){
                SimpleDateFormat sdf = new SimpleDateFormat("M"+MyApplication.getContext().getResources().getString(R.string.time_month)+"d"+MyApplication.getContext().getResources().getString(R.string.time_day)+" HH:mm");
                return sdf.format(currenTimeZone);
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+MyApplication.getContext().getResources().getString(R.string.time_year)+"MM"+MyApplication.getContext().getResources().getString(R.string.time_month)+"dd"+MyApplication.getContext().getResources().getString(R.string.time_day)+" HH:mm");
                return sdf.format(currenTimeZone);
            }

        }

    }

    /**
     * 产品需要修改的时间
     * 举例如下图（3号是今天）：
     * <p>
     * 1小时内：23分钟前
     * 超过一小时：20:23
     * 2号发的：昨天
     * 6.27号~7.1号：星期三~星期日
     * 26号及之前：7/26
     * 去年及去年之前：2017/7/25
     *
     * @param time
     * @return
     */
    public static String getWeixinShowTime0(Date time) {
        try {
            Calendar currentTime = Calendar.getInstance();
            currentTime.setTime(time);
            Calendar nowTime = Calendar.getInstance();

            int nowYear = nowTime.get(Calendar.YEAR);
            int nowMonth = nowTime.get(Calendar.MONTH);
            int nowDay = nowTime.get(Calendar.DAY_OF_MONTH);
            int nowHour = nowTime.get(Calendar.HOUR_OF_DAY);
            int nowMinute = nowTime.get(Calendar.MINUTE);

            int currentYear = currentTime.get(Calendar.YEAR);
            int currentMonth = currentTime.get(Calendar.MONTH);
            int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);
            int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentTime.get(Calendar.MINUTE);

            if (nowYear != currentYear) {
                //不是同一年  6月30日 20：10
                return currentYear + "/" + getZeroTime(currentMonth + 1) + "/" + getZeroTime(currentDay) + "日" /*+ " " + getZeroTime(currentHour) + ":" + getZeroTime(currentMinute)*/;
            }

            if (nowMonth != currentMonth) {
                //不是同一月  07/30
                return getZeroTime(currentMonth + 1) + "/" + getZeroTime(currentDay) /*+ "日" + " " + getZeroTime(currentHour) + ":" + getZeroTime(currentMinute)*/;
            }

            int totalDay = Math.abs(nowDay - currentDay);

            if (totalDay > 7) {
                return getZeroTime(currentMonth + 1) + "/" + getZeroTime(currentDay);
            } else {
                //小于等于六天 要带星期几
                //如：星期四  20：10  不能是昨天和今天
                if (totalDay <= 6 && totalDay >= 2) {
                    int i = currentTime.get(Calendar.DAY_OF_WEEK);
                    String tempWeekName = "";
                    switch (i) {
                        case Calendar.MONDAY:
                            tempWeekName = "星期一";
                            break;

                        case Calendar.TUESDAY:
                            tempWeekName = "星期二";
                            break;

                        case Calendar.WEDNESDAY:
                            tempWeekName = "星期三";
                            break;

                        case Calendar.THURSDAY:
                            tempWeekName = "星期四";
                            break;

                        case Calendar.FRIDAY:
                            tempWeekName = "星期五";
                            break;

                        case Calendar.SATURDAY:
                            tempWeekName = "星期六";
                            break;
                        default:
                            //Calendar.SUNDAY
                            tempWeekName = "星期日";
                            break;
                    }

                    return tempWeekName /*+ " " + getZeroTime(currentHour) + ":" + getZeroTime(currentMinute)*/;
                }

                if (totalDay == 1) {
                    return "昨天" /*+ " " + getZeroTime(currentHour) + ":" + getZeroTime(currentMinute)*/;
                }

                if (totalDay == 0) {
                    int abs = Math.abs(currentMinute - nowMinute);
                    if (currentHour - nowHour == 0 && abs < 1) {
                        return "刚刚";
                    }
                    if (abs < 60) {
                        return abs + "分钟前";
                    }
                    return getZeroTime(currentHour) + ":" + getZeroTime(currentMinute);
                }
            }

            //不可能有这种情况了,暂时写着容错处理 和 最开始的那种相同
            return getZeroTime(currentMonth + 1) + "月" + getZeroTime(currentDay) + "日" + " " + getZeroTime(currentHour) + ":" + getZeroTime(currentMinute);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getZeroTime(int time) {
        String value = "";
        if (time >= 10) {
            value = time + "";
        } else {
            value = "0" + time;
        }
        return value;
    }
}
