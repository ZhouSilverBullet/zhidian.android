package com.sdxxtop.zhidian.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：CaiCM
 * 日期：2018/3/24  时间：10:24
 * 邮箱：15010104100@163.com
 * 描述：判断电话号码是否符合格式.
 */

public class RegexUtils {

    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }
}