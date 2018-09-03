package com.sdxxtop.zhidian.utils;

import android.text.TextUtils;
import android.util.Log;

import com.sdxxtop.zhidian.model.ConstantValue;

import static com.sdxxtop.zhidian.model.ConstantValue.isDebug;

/**
 * 作者: ZhangHD
 * 日期: 2018/3/21 时间: 13:37
 */
public class LogUtils {



    static String mTag = "Log_ZhiDian";

    //for error log
    public static void error(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.e(mTag, msg);
        }
    }

    //for warming log
    public static void warn(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.w(mTag, msg);
        }
    }

    //for info log
    public static void info(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.i(mTag, msg);
        }
    }

    //for debug log
    public static void debug(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.d(mTag, msg);
        }
    }

    //for verbose log
    public static void verbose(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.v(mTag, msg);
        }
    }

    //for error log
    public static void e(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.e(mTag, msg);
        }
    }

    //for warming log
    public static void w(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.w(mTag, msg);
        }
    }

    //for info log
    public static void i(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.i(mTag, msg);
        }
    }

    //for debug log
    public static void d(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.d(mTag, msg);
        }
    }

    //for verbose log
    public static void v(String msg) {
        if (ConstantValue.isDebug) {
            msg = handleMsg(msg);
            Log.v(mTag, msg);
        }
    }


    //for warming log
    public static void w(String tag, String msg) {
        if (ConstantValue.isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            msg = handleMsg(msg);
            Log.w(tag, msg);
        }
    }

    //for info log
    public static void i(String tag, String msg) {
        if (ConstantValue.isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            msg = handleMsg(msg);
            Log.i(tag, msg);
        }
    }

    //for debug log
    public static void d(String tag, String msg) {
        if (ConstantValue.isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            msg = handleMsg(msg);
            Log.d(tag, msg);
        }
    }

    //for verbose log
    public static void v(String tag, String msg) {
        if (ConstantValue.isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            msg = handleMsg(msg);
            Log.v(tag, msg);
        }
    }

    //for verbose log
    public static void e(String tag, String msg) {
        if (ConstantValue.isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            msg = handleMsg(msg);
            Log.e(tag, msg);
        }
    }

    private static String handleMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        return msg;
    }


    /**
     * 点击Log跳转到指定源码位置
     */
    public static void showLog(String tag, String msg) {
        if (isDebug && !TextUtils.isEmpty(msg)) {
            if (TextUtils.isEmpty(tag)) tag = mTag;
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            int currentIndex = -1;
            for (int i = 0; i < stackTraceElement.length; i++) {
                if (stackTraceElement[i].getMethodName().compareTo("showLog") == 0) {
                    currentIndex = i + 1;
                    break;
                }
            }
            if (currentIndex >= 0) {
                String fullClassName = stackTraceElement[currentIndex].getClassName();
                String className = fullClassName.substring(fullClassName
                        .lastIndexOf(".") + 1);
                String methodName = stackTraceElement[currentIndex].getMethodName();
                String lineNumber = String
                        .valueOf(stackTraceElement[currentIndex].getLineNumber());

                Log.i(tag, msg + "\n  ---->at " + className + "." + methodName + "("
                        + className + ".java:" + lineNumber + ")");
            } else {
                Log.i(tag, msg);
            }
        }
    }
}
