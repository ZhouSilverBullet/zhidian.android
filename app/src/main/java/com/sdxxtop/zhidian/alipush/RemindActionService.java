package com.sdxxtop.zhidian.alipush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.activity.MainActivity;

/**
 * Created by Administrator on 2018/5/24.
 */

public class RemindActionService extends Service {
    private Context mContext;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent2 = new Intent();
        intent2.setClass(this, MainActivity.class);//点击通知需要跳转的activity
        intent2.putExtra(MainActivity.MAIN_SKIP, 1);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification = mBuilder.setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("contentText"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
        return START_REDELIVER_INTENT;
    }


}
