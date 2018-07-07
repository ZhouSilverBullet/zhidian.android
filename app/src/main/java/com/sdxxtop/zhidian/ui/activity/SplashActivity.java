package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NormalLoginBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.PreferenceUtils;

public class SplashActivity extends BaseActivity {

    protected long totalTime;

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
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TIME_TEMP, data.getExpire_time());
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TOKEN, data.getAuto_token());
                    final String company_id = data.getCompany_id();
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_ID, company_id);
                    AppSession.getInstance().setCompanyId(company_id);

                    long tempTotalTime = System.currentTimeMillis() - totalTime;
                    long current = 1500 - tempTotalTime;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if ("0".equals(company_id)) {  //进入创建公司
                                Intent intent = new Intent(mContext, LoginSuccessActivity.class);
                                intent.putExtra("splash", 1);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                    }, current < 0 ? 10 : current);

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

    @Override
    public void onBackPressed() {
    }
}
