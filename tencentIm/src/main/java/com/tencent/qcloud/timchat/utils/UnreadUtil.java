package com.tencent.qcloud.timchat.utils;

/**
 * Created by Administrator on 2018/8/24.
 */

public class UnreadUtil {
    private static UnreadUtil unreadUtil;
    private long messageCount;

    private UnreadUtil() {
    }

    public static UnreadUtil getInstance() {
        if (unreadUtil == null) {
            synchronized (UnreadUtil.class) {
                if (unreadUtil == null) {
                    unreadUtil = new UnreadUtil();
                }
            }
        }
        return unreadUtil;
    }

    public void setMessageCount(long messageCount) {
        this.messageCount = messageCount;
    }

    public long getMessageCount() {
        return messageCount;
    }

    public String getUnreadMessageStringNum() {
        long count = messageCount;
        if (count <= 0) {
            return "";
        }
        if (count > 99) {
            return "99+";
        }
        return count + "";
    }
}
