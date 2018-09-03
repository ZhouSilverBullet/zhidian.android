package com.sdxxtop.zhidian.alipush;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.sdxxtop.zhidian.utils.LogUtils;

/**
 * Created by Administrator on 2018/5/23.
 */

public class AnalyticsHome {
    public static final String TAG = "AnalyticsHome";
    private static CloudPushService pushService;

    private static void init() {
        if (pushService == null) {
            pushService = PushServiceFactory.getCloudPushService();
        }
    }

    public static void bindAccount(final String userId) {
        init();
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) { //先解除绑定
                LogUtils.e(TAG, "unbindAccount s= " + s);
                bind(userId);
            }

            @Override
            public void onFailed(String s, String s1) {
                bind(userId);
                LogUtils.e(TAG, " onFailed bindAccount s= " + s);
                LogUtils.e(TAG, " onFailed bindAccount s1= " + s1);
            }
        });


    }

   private static void bind(final String userId) {
        pushService.bindAccount(userId, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                LogUtils.e(TAG, "unbindAccount s= " + s);
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtils.e(TAG, " onFailed unbindAccount s= " + s);
                LogUtils.e(TAG, " onFailed unbindAccount s1= " + s1);
            }
        });
    }

    public static void unbindAccount() {
        init();
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                LogUtils.e(TAG, "bindAccount s= " + s);
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtils.e(TAG, " onFailed bindAccount s= " + s);
                LogUtils.e(TAG, " onFailed bindAccount s1= " + s1);
            }
        });

    }
}
