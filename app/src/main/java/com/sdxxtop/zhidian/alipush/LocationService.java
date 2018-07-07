package com.sdxxtop.zhidian.alipush;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/29.
 */

public class LocationService extends Service {
    public static final String SIGN_DATE_LIST = "sign_date_list";
    public static final String ALL_GOTO_WORK = "all_goto_work";
    public static final String REMIND_MIN = "remind_min";
    public static final String LOG_LONG_DATE = "log_long_date";
    private long logLongDate;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            ArrayList<String> list = (ArrayList<String>) intent.getSerializableExtra(SIGN_DATE_LIST);
            boolean isAllGoToWork = intent.getBooleanExtra(ALL_GOTO_WORK, false);
            long currentRemindMin = intent.getLongExtra(REMIND_MIN, 0);
            logLongDate = intent.getLongExtra(LOG_LONG_DATE, 0);
            LocationPushHelper.getInstance(this).setLocation(list, isAllGoToWork, currentRemindMin, logLongDate);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocationBind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationPushHelper.getInstance(this).removeAlarm();
        //是否内存
        LocationPushHelper.getInstance(this).destroy();
    }

    public static void startLocationService(Context context, List<String> signDateList,
                                            boolean isAllGoToWork, long currentRemindMin, long logLongDate) {
        Intent intent = new Intent(context, LocationService.class);
        ArrayList<String> list = new ArrayList<>();
        list.addAll(signDateList);
        intent.putExtra(SIGN_DATE_LIST, list);
        intent.putExtra(ALL_GOTO_WORK, isAllGoToWork);
        intent.putExtra(REMIND_MIN, currentRemindMin);
        intent.putExtra(LOG_LONG_DATE, logLongDate);
        context.startService(intent);
    }

    public class LocationBind extends Binder {

        public void clearCache() {
            LocationPushHelper.getInstance(LocationService.this).clearCache();
        }
    }
}
