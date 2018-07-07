package com.sdxxtop.zhidian;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.sdxxtop.zhidian.alipush.CrashHandler;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.PreferenceUtils;

import java.util.Stack;


/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：15:24
 * 邮箱：15010104100@163.com
 * 描述：
 */
public class App extends MultiDexApplication {
    public static final String TAG = "MultiDex App";

    private static App mApp;
    public Stack<Activity> store;
    private String jinDu;
    private String weiDu;
    private boolean isRun;

    /**
     * 全局 Context
     */
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        if (isZhidianProcess(getCurProcessName())) {
            mApp = this;
            store = new Stack<>();
            CrashHandler.getInstance().init(this);
            mContext = getApplicationContext();
        }
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
        FaceSDKManager.getInstance().initialize(this, "zhidian-face-android", "idl-license.face-android");
        initCloudChannel(this);
    }

    public boolean isZhidianProcess(String process) {
        return getPackageName().equals(process);
    }

    public String getCurProcessName() {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            LogUtils.e("getCurProcessName:" + e.getMessage());
        }
        return getPackageName();
    }

    public static App getAppContext() {
        return mApp;
    }

    public static Context getContext() {
        return mContext;
    }

    private class SwitchBackgroundCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            store.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }


    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurActivity() {
        return store.lastElement();
    }

    public String getJinDu() {
        if (TextUtils.isEmpty(jinDu)) {
            jinDu = PreferenceUtils.getInstance(this).getStringParam(ConstantValue.MAP_JING_DU);
        }
        return jinDu;
    }

    public String getWeiDu() {
        if (TextUtils.isEmpty(weiDu)) {
            weiDu = PreferenceUtils.getInstance(this).getStringParam(ConstantValue.MAP_WEI_DU);
        }
        return weiDu;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.backgroundcolor, R.color.notice_contentcolor);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter classicsFooter = new ClassicsFooter(context);
                classicsFooter.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundcolor));
                return classicsFooter.setDrawableSize(20);
            }
        });
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {

        MiPushRegister.register(applicationContext, "2882303761517799604", "5531779955604");
        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(applicationContext);
//        //GCM/FCM辅助通道注册
//        GcmRegister.register(this, sendId, applicationId); //sendId/applicationId为步骤获得的参数

        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.e(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtils.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppSession.getInstance().clearSession();
    }
}
