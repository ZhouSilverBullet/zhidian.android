package com.sdxxtop.zhidian.utils;

import android.util.SparseArray;

/**
 * Created by Administrator on 2018/5/11.
 */

public class LeaveUtil {
    static SparseArray<String> leaveMap;
    static SparseArray<String> overTimeTypeMap; //加班类型
    static SparseArray<String> overTimePositonMap; //加班地点
    static SparseArray<String> weekMap; //日期兑换



    public static String getName(int key) {
        if (key == 0 || key > 13) {
            return "";
        }
        if (leaveMap == null) {
            leaveMap = new SparseArray<>();
            leaveMap.put(1, "事假");
            leaveMap.put(2, "病假");
            leaveMap.put(3, "年假");
            leaveMap.put(4, "学习");
            leaveMap.put(5, "婚假");
            leaveMap.put(6, "产检假");
            leaveMap.put(7, "产假");
            leaveMap.put(8, "陪产假");
            leaveMap.put(9, "哺乳假");
            leaveMap.put(10, "工伤假");
            leaveMap.put(11, "丧假");
            leaveMap.put(12, "探亲假");
            leaveMap.put(13, "其他");
        }

        return leaveMap.get(key);

    }

    public static String getOverTimeType(int key) {
        if (overTimeTypeMap == null) {
            overTimeTypeMap = new SparseArray<>();
            overTimeTypeMap.put(1, "正常加班");
            overTimeTypeMap.put(2, "预加班");
        }
        return overTimeTypeMap.get(key);
    }
//1:在家加班 2:出差加班 3:外出加班 4:办公室加班
    public static String getOverTimePosition(int key) {
        if (overTimePositonMap == null) {
            overTimePositonMap = new SparseArray<>();
            overTimePositonMap.put(1, "在家加班");
            overTimePositonMap.put(2, "出差加班");
            overTimePositonMap.put(3, "外出加班");
            overTimePositonMap.put(4, "办公室加班");
        }
        return overTimePositonMap.get(key);
    }
    public static String getWeekName(int key) {
        if (weekMap == null) {
            weekMap = new SparseArray<>();
            weekMap.put(1, "一");
            weekMap.put(2, "二");
            weekMap.put(3, "三");
            weekMap.put(4, "四");
            weekMap.put(5, "五");
            weekMap.put(6, "六");
            weekMap.put(7, "日");
        }
        return weekMap.get(key);
    }



}
