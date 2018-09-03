package com.sdxxtop.zhidian.utils;

/**
 * Created by Administrator on 2018/7/17.
 */

public class PartIconColorUtil {
    private static String[] partColors = {"#3296fa", "#9396fa", "#9d81ff", "#81a4f9", "#d9d6ec", "#4e76e4", "#ad8ea3"};

    public static String getColor(int position) {
        return partColors[position % 7];
    }
}
