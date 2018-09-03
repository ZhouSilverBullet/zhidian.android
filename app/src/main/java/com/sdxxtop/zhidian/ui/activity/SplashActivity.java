package com.sdxxtop.zhidian.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NormalLoginBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.im.IMLoginCallback;
import com.sdxxtop.zhidian.im.IMLoginHelper;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ApkUtil;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.PreferenceUtils;

import io.reactivex.functions.Consumer;
import zhangphil.iosdialog.widget.AlertDialog;

public class SplashActivity extends BaseActivity {

    protected long totalTime;
    private final static String TAG = "SplashActivity";
    private String company_id;

    @Override
    protected int getActivityView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearNotification();
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
    }

    @Override
    protected void initData() {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    toInitData();
                } else {
                    requestMustPermission();
                }
            }
        });
    }

    private void requestMustPermission() {
        requestDialog();
    }

    private void requestDialog() {
        final boolean b = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE);
        String btnDec = "";
        if (b) {
            btnDec = "去允许";
        } else {
            btnDec = "去设置";
        }

        new AlertDialog(this)
                .builder()
                .setMsg("没有权限, 您需要去设置中开启知点必须要的权限，才能正常使用")
                .setTitle("提示").setPositiveButton(btnDec, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b) {
                    initData();
                } else {
                    ApkUtil.skipAppMessage(mContext, REQUEST_PERMISSION_SETTING);
                }
            }
        }).show();
    }

    private void toInitData() {
        boolean isShowGuide = PreferenceUtils.getInstance(this).getBooleanParam(ConstantValue.GUIDE_IS_SHOW, false);
        int guideShowVersion = PreferenceUtils.getInstance(this).getIntParam(ConstantValue.GUIDE_SHOW_VERSION);
        if (!isShowGuide && guideShowVersion < getVersionCode()) { //显示guide页面
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
        } else {
            //算时间
            totalTime = System.currentTimeMillis();
            int timeTemp = PreferenceUtils.getInstance(mContext).getIntParam(ConstantValue.LOGIN_TIME_TEMP);
            if (timeTemp != -1 && timeTemp < System.currentTimeMillis()) {
                //测试用暂关此功能
                postAutoLogin();
            } else { //没有自动登陆去登陆界面
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, NormalLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        }
    }

    /**
     * 调取自动登陆请求
     */
    protected void postAutoLogin() {
        Params params = new Params();
        params.putDeviceNo();
        params.put("at", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.LOGIN_TOKEN));
        params.put("pi", ConstantValue.pi);
        params.put("dm", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.DEVICE_NAME));
        RequestUtils.createRequest(2500).postAutoLogin(params.getData()).enqueue(new RequestCallback<>(this, new IRequestListener<NormalLoginBean>() {
            @Override
            public void onSuccess(NormalLoginBean normalLoginBean) {
                NormalLoginBean.DataEntity data = normalLoginBean.getData();
                if (data != null) {
                    handleLoginSuccess(data);
                }
            }

            @Override
            public void onFailure(int code, final String errorMsg) {
                long tempTotalTime = System.currentTimeMillis() - totalTime;
                long current = 1500 - tempTotalTime;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast(errorMsg);
                        Intent intent = new Intent(mContext, NormalLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, current < 0 ? 10 : current);
            }
        }));
    }

    private void handleLoginSuccess(NormalLoginBean.DataEntity data) {
        PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TIME_TEMP, data.getExpire_time());
        PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TOKEN, data.getAuto_token());
        company_id = data.getCompany_id();
        AppSession.getInstance().setCompanyId(company_id);

        //创建公司的优先级最高
        if ("0".equals(company_id) || TextUtils.isEmpty(company_id)) {  //进入创建公司
            Intent intent = new Intent(mContext, LoginSuccessActivity.class);
            intent.putExtra("splash", 1);
            startActivity(intent);
            finish();
        } else {
            String signature = data.getSignature();
            if (!TextUtils.isEmpty(signature)) {
                tlsLogin(company_id, data.getUserid() + "", signature);
            }
        }
    }

    private void tlsLogin(String company_id, String userid, final String signature) {
        IMLoginHelper.getInstance().tIMLogin(company_id, userid, signature, new IMLoginCallback() {
            @Override
            public void onError(int i, String s) {
                showToast(s + "i");
                LogUtils.e("i " + i + "\ns = " + s);
                Intent intent = new Intent(mContext, NormalLoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSuccess() {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
