package com.sdxxtop.zhidian.utils;

import android.text.TextUtils;
import android.util.SparseArray;

import com.sdxxtop.zhidian.entity.ColorTextBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class ColorTextUtil {
    private static SparseArray<String> departMap;
    private static SparseArray<String> userMap;

//    private final static String[] colorList = new String[]{
//            "#3296FA", "#9396FA"
//            , "#9D81FF", "#81A4F9"
//            , "#FCBA28", "#AD8EA3"
//            , "#4E76E4", "#9D7DFA"
//            , "#D37FF1", "#4D97F0"
//            , "#FC716C", "#55BEF5"};

    private final static String[] colorList = new String[]{
            "#52C67A","#3296FA", "#9396FA", "#FCBA28", "#AD8EA3", "#4E76E4", "#9D7DFA", "#D37FF1", "#4D97F0", "#FC716C", "#55BEF5"};

    public static ColorTextBean getDepartmentValue(int key) {

        if (departMap == null) {
            departMap = new SparseArray<>();
            departMap.put(0, "人员出勤");
            departMap.put(1, "今日迟到");
            departMap.put(2, "今日旷工");
            departMap.put(3, "今日漏打卡");
            departMap.put(4, "今日外勤");
            departMap.put(5, "月迟到图");
            departMap.put(6, "月旷工图");
            departMap.put(7, "月平均工时图");
            departMap.put(8, "月加班图");
            departMap.put(9, "月漏打卡图");
            departMap.put(10, "月外勤统计图");
        }

        String textValue = departMap.get(key);
        String bgColor = colorList[key];

        return new ColorTextBean(key, bgColor, textValue);
    }

    public static List<ColorTextBean> getUserValue(String user_stat) {
        List<ColorTextBean> colorTextList = new ArrayList<>();
        if (TextUtils.isEmpty(user_stat)) {
            return colorTextList;
        }
        String[] userStat = user_stat.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : userStat) {
            if (!TextUtils.isEmpty(s)) {
                list.add(Integer.parseInt(s));
            }
        }

        for (Integer integer : list) {
            colorTextList.add(getUsermentValue(integer));
        }

        return colorTextList;
    }

    public static ColorTextBean getUsermentValue(int key) {
        if (userMap == null) {
            userMap = new SparseArray<>();
            userMap.put(1, "今日迟到榜");
            userMap.put(2, "今日旷工榜");
            userMap.put(3, "今日漏打卡榜");
            userMap.put(4, "今日打卡记录");
            userMap.put(5, "月迟到榜");
            userMap.put(6, "月旷工榜");
            userMap.put(7, "月工时榜");
            userMap.put(8, "月加班榜");
            userMap.put(9, "月漏打卡榜");
            userMap.put(10, "月外勤榜");
        }

        String textValue = userMap.get(key);
        String bgColor = colorList[key];

        return new ColorTextBean(key, bgColor, textValue);
    }


    public static List<ColorTextBean> getPartValue(String part_stat) {
        List<ColorTextBean> colorTextList = new ArrayList<>();
        if (TextUtils.isEmpty(part_stat)) {
            return colorTextList;
        }
        String[] userStat = part_stat.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : userStat) {
            if (!TextUtils.isEmpty(s)) {
                list.add(Integer.parseInt(s));
            }
        }

        for (Integer integer : list) {
            colorTextList.add(getDepartmentValue(integer));
        }

        return colorTextList;
    }

}
