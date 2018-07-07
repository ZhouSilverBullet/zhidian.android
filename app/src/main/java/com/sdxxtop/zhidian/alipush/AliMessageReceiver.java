package com.sdxxtop.zhidian.alipush;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

/**
 * Created by Administrator on 2018/5/23.
 */

public class AliMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        handleClickData(context, title, summary, extraMap);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
    }

    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        super.onMessage(context, cPushMessage);
    }

    private void handleClickData(final Context context, String title, String summary, String extraMap) {

        PushCenterActivity.startActivityToReceiver(context, extraMap);

//        try {
//            JSONObject jsonObject = new JSONObject(extraMap);
//            int company_id = jsonObject.optInt("company_id");
//            String company_name = jsonObject.optString("company_name");
//            if (jsonObject.has("notice_id")) { //公告的
//
//                final int notice_id = jsonObject.optInt("notice_id");
////                Intent intent = new Intent(context, NoticeDetail2Activity.class);
//                Intent intent = new Intent();
//                intent.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.ui.activity.NoticeDetail2Activity");
//                intent.putExtra("ni", notice_id);
//                intent.putExtra("company_id", company_id);
////                if (!App.getAppContext().isRun()) {
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                }
//                context.startActivity(intent);
//
//            } else if (jsonObject.has("apply_id")) { //申请
//
//                final int apply_id = jsonObject.optInt("apply_id");
//                final int at = jsonObject.optInt("apply_type");
////                Intent intent = new Intent(context, SubmissionActivity.class);
//                Intent intent = new Intent();
//                intent.setClassName("com.sdxxtop.zhidian", "com.sdxxtop.zhidian.ui.activity.SubmissionActivity");
//                intent.putExtra("at", at);
//                intent.putExtra("apply_id", apply_id + "");
//                intent.putExtra("company_id", company_id);
////                if (!App.getAppContext().isRun()) {
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                }
//                context.startActivity(intent);
//
//            }
//        } catch (JSONException e) {
//            Log.e("MyMessageReceiver", e.getMessage());
//            e.printStackTrace();
//        }
    }
}
