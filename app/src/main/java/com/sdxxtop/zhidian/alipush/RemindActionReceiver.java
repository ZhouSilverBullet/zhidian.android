package com.sdxxtop.zhidian.alipush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.activity.MainActivity;

/**
 * Created by Administrator on 2018/6/29.
 */

public class RemindActionReceiver extends BroadcastReceiver {
    private Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if ("zhidian.intent_action_reamind".equals(action)) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent2 = new Intent();
            intent2.setClass(context, MainActivity.class);//点击通知需要跳转的activity
            intent2.putExtra(MainActivity.MAIN_SKIP, 1);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent2,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            notification = builder.setContentTitle(intent.getStringExtra("title"))
                    .setContentText(intent.getStringExtra("contentText"))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(contentIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            if (notificationManager != null) {
                notificationManager.notify(0, notification);
            }
        }
    }
}
