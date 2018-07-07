package com.sdxxtop.zhidian.alipush;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.sdxxtop.zhidian.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/28.
 */

public class LocationPushHelper {
    public static final String TAG = "LocationPushHelper";
    //    ArrayList<>
    //缓存一个开始的时间
    private long startTakeCardCurrentTime;
    private List<Long> currentTimeList = new ArrayList<>();
    private List<AlarmManager> alarmManagerList = new ArrayList<>();
    private List<PendingIntent> pendingIntentList = new ArrayList<>();
    private Context mContext;
    private int alarmId;

    private static LocationPushHelper instance;
    private List<String> signDateList;
    private boolean isAllGoToWork;
    private long currentRemindMin;
    private long logLongDate;

    public static LocationPushHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (LocationPushHelper.class) {
                instance = new LocationPushHelper(context);
            }
        }
        return instance;
    }

    private LocationPushHelper() {
    }

    public void setLocation(List<String> signDateList, boolean isAllGoToWork,
                            long currentRemindMin, long logLongDate) {
        this.signDateList = signDateList;
        this.isAllGoToWork = isAllGoToWork;
        this.currentRemindMin = currentRemindMin;
        this.logLongDate = logLongDate;

        for (int i = 0; i < signDateList.size(); i++) {
            String time = signDateList.get(i);
            long longTime = DateUtil.convertTimeToLong(time, "yyyy-MM-dd HH:mm:ss");
            //日志的打卡时间后面的打卡时间才能进行打卡
            if (logLongDate < longTime) {
                if (i % 2 == 0 || isAllGoToWork) {
                    startLocationPush(longTime - currentRemindMin, "上班", "上班提醒打卡");
                } else {
                    startLocationPush(longTime + currentRemindMin, "下班", "下班提醒打卡");
                }
            }
        }
    }

    private LocationPushHelper(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    public void startLocationPush(long minute, String title, String content) {
        if (currentTimeList.contains(minute)) {
            return;
        }

        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            Log.e(TAG, "AlarmManager is null return");
            return;
        }

        if (minute < System.currentTimeMillis()) {
            Log.e(TAG, "minute is large time return");
            return;
        }
        currentTimeList.add(minute);

        Intent intent = new Intent(mContext, RemindActionService.class);
        intent.putExtra("id", 10);
        intent.putExtra("title", title);
        intent.putExtra("contentText", content);
        if (alarmId == Integer.MAX_VALUE) {
            alarmId = 0;
        }
        alarmId++;
        PendingIntent pendingIntent = PendingIntent.getService(mContext, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, minute, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, minute, pendingIntent);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, minute, pendingIntent);
        }
        pendingIntentList.add(pendingIntent);
        alarmManagerList.add(am);
    }

    public void clearCache() {
        startTakeCardCurrentTime = 0; //切换公司，重置一下这个时间
        if (currentTimeList != null) {
            currentTimeList.clear();
        }
        removeAlarm();

    }

    public void removeAlarm() {
        try {
            if (pendingIntentList != null && alarmManagerList != null) {
                if (alarmManagerList.size() == pendingIntentList.size()) {
                    for (int i = 0; i < alarmManagerList.size(); i++) {
                        alarmManagerList.get(i).cancel(pendingIntentList.get(i));
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void destroy() {
        if (mContext != null) {
            mContext = null;
            instance = null;
        }
    }
}
