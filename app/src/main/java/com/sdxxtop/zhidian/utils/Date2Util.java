package com.sdxxtop.zhidian.utils;

import android.text.TextUtils;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class Date2Util {
    /**
     * @param dayDate 2018年1月5日
     * @param day     前一天是负值
     */
    public static String[] getUpOrDownDay(String dayDate, int day) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat dft2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dft.parse(dayDate);
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            instance.add(Calendar.DAY_OF_MONTH, day);
            Date time = instance.getTime();
            String upOrDownDay = dft.format(time);
            String format = dft2.format(time);
            return new String[]{upOrDownDay, format};
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param dayDate 2018年1月5日
     */
    public static String getUpOrDownMonth(String dayDate) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy年MM月");
        SimpleDateFormat dft2 = new SimpleDateFormat("yyyy-MM");
        try {
            Date date = dft.parse(dayDate);
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            Date time = instance.getTime();
            String format = dft2.format(time);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSubmissionTime(String sign_time) {
        try {
            Date date = new Date(DateUtil.convertTimeToLong(sign_time, "yyyy-MM-dd HH:mm:ss"));
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            int hour = instance.get(Calendar.HOUR_OF_DAY);
            int minute = instance.get(Calendar.MINUTE);
            String hourString = "";
            String minuteString = "";
            if (hour < 10) {
                hourString = "0" + hour;
            } else {
                hourString = "" + hour;
            }
            if (minute < 10) {
                minuteString = "0" + minute;
            } else {
                minuteString = "" + minute;
            }
            return hourString + ":" + minuteString;
        } catch (Exception e) {
        }
        return "";
    }


    public static List getWeekendInMonth(int year, int month) {
        List<Integer> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);// 不设置的话默认为当年
        calendar.set(Calendar.MONTH, month - 1);// 设置月份
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为当月第一天
        int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 当月最大天数
        for (int i = 0; i < daySize; i++) {
            calendar.add(Calendar.DATE, 1);//在第一天的基础上加1
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            if (week == Calendar.SATURDAY || week == Calendar.SUNDAY) {// 1代表周日，7代表周六 判断这是一个星期的第几天从而判断是否是周末
                list.add(calendar.get(Calendar.DAY_OF_MONTH));// 得到当天是一个月的第几天
            }
        }
        return list;
    }


    public static List<String> getWeekendInMonth(String[] split, int year, int month) {
        List<Integer> weekList = new ArrayList<>();
        for (String s : split) {
            if (!TextUtils.isEmpty(s)) {
                weekList.add(Integer.parseInt(s));
            }
        }

        List<Integer> weekDayList = new ArrayList<>();
        for (Integer integer : weekList) {
            switch (integer) {
                case 1:
                    weekDayList.add(Calendar.MONDAY);
                    break;
                case 2:
                    weekDayList.add(Calendar.TUESDAY);
                    break;
                case 3:
                    weekDayList.add(Calendar.WEDNESDAY);
                    break;
                case 4:
                    weekDayList.add(Calendar.THURSDAY);
                    break;
                case 5:
                    weekDayList.add(Calendar.FRIDAY);
                    break;
                case 6:
                    weekDayList.add(Calendar.SATURDAY);
                    break;
                case 7:
                    weekDayList.add(Calendar.SUNDAY);
                    break;
            }
        }

        //用于存放没有上班的日期
        List<String> dateList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);// 不设置的话默认为当年
        calendar.set(Calendar.MONTH, month - 1);// 设置月份
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为当月第一天

        String stringMonth;
        if (month < 10) {
            stringMonth = "0" + month;
        } else {
            stringMonth = "" + month;
        }


        int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 当月最大天数
        for (int i = 0; i < daySize; i++) {
            calendar.add(Calendar.DATE, 1);//在第一天的基础上加1
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            if (!weekDayList.contains(week)) { //不包含这一天就进行添加
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String stringDay;
                if (day < 10) {
                    stringDay = "0" + day;
                } else {
                    stringDay = "" + day;
                }
                String tempStringDate = year + "-" + stringMonth + "-" + stringDay;
                dateList.add(tempStringDate);
            }

        }
        return dateList;
    }

    /**
     * 获取本月的最后一天
     * @return
     */
    public static Date getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDayOfMonth = calendar.getTime();
        return lastDayOfMonth;
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static Date getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return c.getTime();
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static Date getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return c.getTime();
    }

    /**
     * 后面日期不进行选择
     *
     * @param date
     * @return
     */
    public static boolean compareToday2(CalendarDay date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR); //年
        int month = calendar.get(Calendar.MONTH); //月
        int day = calendar.get(Calendar.DAY_OF_MONTH); //日
        int year1 = date.getYear();
        int month1 = date.getMonth();
        int day1 = date.getDay();
        if (year1 > year) {
            return true;
        } else if (year1 == year && month1 > month) {
            return true;
        } else if (year1 == year && month1 == month && day1 > day) {
            return true;
        }

        return false;
    }
}
