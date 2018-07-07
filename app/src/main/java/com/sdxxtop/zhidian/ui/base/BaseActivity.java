package com.sdxxtop.zhidian.ui.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sdxxtop.zhidian.App;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.activity.SplashActivity;
import com.sdxxtop.zhidian.utils.ApkUtil;
import com.sdxxtop.zhidian.utils.DeviceUtil;
import com.sdxxtop.zhidian.utils.DialogUtil;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.StatusBarUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：15:24
 * 邮箱：15010104100@163.com
 * 描述：视图控制基类
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    public static final int PERMISSION_FOR_PHONE_STATE = 50;
    public static final int REQUEST_PERMISSION_SETTING = 51;
    public static final int REQUEST_PERMISSION_READ_STORAGE = 52;
    public static final int REQUEST_PERMISSION_CAMRE = 53;
    public static final int REQUEST_PERMISSION_LOACTION = 54;
    public static final int REQUEST_PERMISSION_WIFI = 55;
    public static final int REQUEST_PERMISSION_CONTACT = 56;


    public BaseActivity mContext;
    protected Dialog progressDialog;
    private AlertDialog alertDialog;

    protected String[] perms = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE};
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置Activity背景颜色?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (StatusBarUtil.MIUISetStatusBarLightMode(getWindow(), true)) {//小米MIUI系统
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android6.0以上系统
                    StatusBarUtil.android6_SetStatusBarLightMode(getWindow());
                    StatusBarUtil.compat(this);
                } else {
                    StatusBarUtil.compat(this);
                }
            } else if (StatusBarUtil.FlymeSetStatusBarLightMode(getWindow(), true)) {//魅族flyme系统
                StatusBarUtil.compat(this);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android6.0以上系统
                StatusBarUtil.android6_SetStatusBarLightMode(getWindow());
                StatusBarUtil.compat(this);
            }
        }
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bkcolor);
        this.getWindow().setBackgroundDrawable(drawable);
        setContentView(getActivityView());
        ButterKnife.bind(this);
        mContext = this;
        initByRestart(savedInstanceState);
        initVariables();
        initView();
        initData();
        initEvent();

    }

    protected void initByRestart(Bundle savedInstanceState) {

    }

    protected void showToast(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        ToastUtil.show(value);
    }

    protected void registeredEvent() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void initVariables() {
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        boolean showGuide = DataStorage.getGuideShow(WelcomeActivity.this);
//        initSdk();
//        new AdLogoutLogic().getAd();
//        if (showGuide) { //出现过引导页，才访问splash图片
//            new SplashLogic().getSplashSave();
//        }
        if (requestCode == PERMISSION_FOR_PHONE_STATE) {
            DeviceUtil.getDeviceNo(this);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, final List<String> perms) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("没有权限, 你需要去设置中开启读取手机权限");
        boolean b = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE);
        if (!b) {
            builder.setNegativeButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    ApkUtil.skipAppMessage(BaseActivity.this, REQUEST_PERMISSION_SETTING);
                }
            });
        } else {
            builder.setNegativeButton("去允许", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    alertDialog.dismiss();
                    EasyPermissions.requestPermissions(BaseActivity.this, "请开启获取手机状态权限", PERMISSION_FOR_PHONE_STATE, BaseActivity.this.perms);
                }
            });
        }
        alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 初始化界面
     */
    protected abstract int getActivityView();

    /**
     * 初始化数据
     */
    protected void initView() {
    }

    ;

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    ;

    /**
     * 设置监听及回掉
     */
    protected void initEvent() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 展示对话框 不可取消
     *
     * @param message 展示的消息
     */
    public void showProgressDialog(String message) {
        if (null != mContext && !mContext.isFinishing()) {
            if (progressDialog == null) {
                progressDialog = DialogUtil.createLoadingDialog(mContext, message, false);
            }
            progressDialog.show();
        }
    }

    protected void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onClick(View view) {

    }

    protected void saveFragment(Bundle outState, Fragment fragment) {
        try {
            if (fragment.isAdded()) {
                getSupportFragmentManager().putFragment(outState,
                        fragment.getClass().getSimpleName(), fragment);
            }
        } catch (Exception e) {
            LogUtils.e("saveFragment error:" + e.getMessage());
        }
    }

    protected <T extends Fragment> T getFragment(Bundle savedState, Class cls) {
        return (T) getSupportFragmentManager().getFragment(savedState,
                cls.getSimpleName());
    }

    public int getVersionCode() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            return 10000;
        }
    }

    /**
     * 清楚所有通知栏通知
     */
    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        MiPushClient.clearNotification(getApplicationContext());
    }

    public boolean isVersionMoreKitkat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return false;
        }
    }

    public void statusBar(boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android6.0以上系统
            StatusBarUtil.setDarkStatusIcon(getWindow(), isDark);
        }
    }

    public void openSplashActivity(Context context) {
        if (App.getAppContext() != null) {
            Activity curActivity = App.getAppContext().getCurActivity();
            int indexOf = App.getAppContext().store.indexOf(curActivity);
            if (indexOf == 0) {
                Intent intent = new Intent(context, SplashActivity.class);
                context.startActivity(intent);
            }
        } else if (!App.getAppContext().isRun()) {
            Intent intent = new Intent(context, SplashActivity.class);
            context.startActivity(intent);
        }
    }
}
