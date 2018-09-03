package com.tencent.qcloud.sdk;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * 常量
 */
public class Constant {

    public static final int ACCOUNT_TYPE = 32102;
    //sdk appid 由腾讯分配
    public static final int SDK_APPID = 1400116947;

    public static final String USER_SIG_KEY = "eJxlkE1Pg0AQhu-8CsJVY1iWhcWkB2ir-RCTxlrpaYPdpU5wAZcVRON-14JJSZzDXJ4neeedL8M0TWt793CVHg7le6GZ7iphmdemZVuXZ1hVwFmqGVb8HxQfFSjB0kwL1UOHBI5tjxXgotCQwZ*giT0MOi1MRmbNc9an9SJyfx3kBa4-VuDYw3i*mS4XKt*HSRm8Pm8FuRVl8eLLVddGM7iYyqrbea308jfHDzsvhIjT8BPie7e5WS9pkMiZ-4Qf2yie0xJnG9WsvXTPabKqF8fJZBSpQQ5PQQQ7xMcUjw9qhKqhLIbuNiIIoeBUzDK*jR9x9l4Z";

    private static boolean getBuildConfigDebug(String clazzName) {
        try {
            Class<?> aClass = Class.forName(clazzName);
            Field debug = aClass.getField("DEBUG");
            boolean b = (boolean) debug.get(aClass);
            Log.e("Constant", "get debug value =  " + b);
            return b;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Log.e("Constant", "get debug value =  出现异常");
        return false;
    }
}
