package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.alipush.AnalyticsHome;
import com.sdxxtop.zhidian.entity.NormalLoginBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.eventbus.ChangeMobileEvent;
import com.sdxxtop.zhidian.eventbus.ChangePasswordEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DeviceUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：11:39
 * 邮箱：15010104100@163.com
 * 描述：密码账号管理界面
 */
public class AccountPwdActivity extends BaseActivity {

    @BindView(R.id.tv_login_phone)
    TextView tvLoginPhone;
    @BindView(R.id.iv_modify_password)
    ImageView ivModifyPassword;
    private String mobile;

    @Override
    protected int getActivityView() {
        return R.layout.activity_account_pwd;
    }

    @Override
    protected void initView() {
        registeredEvent();

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        tvLoginPhone.setText(mobile);
    }

    @OnClick({ R.id.rl_login_phone, R.id.rl_modify_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_login_phone:
                Intent intentPhone = new Intent(mContext, ModifyMobileActivity.class);
                intentPhone.putExtra("mobile", mobile);
                startActivity(intentPhone);
                break;
            case R.id.rl_modify_password:
                Intent intentPassword = new Intent(mContext, ModifyPasswordActivity.class);
                intentPassword.putExtra("mobile", mobile);
                startActivity(intentPassword);
                break;
        }
    }

    @Subscribe
    public void notifyChangeMobileSuccess(ChangeMobileEvent event) {
        if (event != null) {
            tvLoginPhone.setText(event.phone);
            postNormalLogin(PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.PASSWORD));
            notifyMineFragment();
        }
    }

    @Subscribe
    public void notifyChangePasswordSuccess(ChangePasswordEvent event) {
        if (event != null) {
            postNormalLogin(event.password);
        }
    }

    //改变手机号的时候刷新一下我的界面
    public void notifyMineFragment() {
        EventBus.getDefault().post(new ChangeCompanyEvent());
    }

    /**
     * 调取普通登陆请求
     */
    private void postNormalLogin(final String password) {
        String phone = tvLoginPhone.getText().toString();
        final String spacePhone = StringUtil.textPhoneValue(phone);
        Params params = new Params();
        params.removeKey("ui");
        params.removeKey("ci");
        //登陆要查询本地的ci
        params.put("ci", AppSession.getInstance().getCompanyId());
        params.put("un", phone);
        params.put("pw", password);
        params.put("pi", ConstantValue.pi);
        params.put("dn", DeviceUtil.getDeviceNo(mContext));
        params.put("dm", DeviceUtil.getDeviceName());
        showProgressDialog("");

        RequestUtils.createRequest().postNormalLogin(params.getData()).enqueue(new RequestCallback<NormalLoginBean>(new IRequestListener<NormalLoginBean>() {
            @Override
            public void onSuccess(NormalLoginBean loginBean) {
                closeProgressDialog();
                NormalLoginBean.DataEntity data = loginBean.getData();
                if (data == null) {
                    return;
                }

                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.USER_NAME, spacePhone);
                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.PASSWORD, password);
                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TIME_TEMP, data.getExpire_time());
                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TOKEN, data.getAuto_token());
                String company_id = data.getCompany_id();
                if (TextUtils.isEmpty(company_id)) {
                    company_id = "";
                }

//                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_ID, company_id);

                AnalyticsHome.bindAccount(spacePhone);

                int userid = data.getUserid();

                //登陆完成之后进行存储相关登陆数据
                AppSession.getInstance().addOrRefreshAccountBean(spacePhone, PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.PASSWORD), company_id, userid + "");
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                ToastUtil.show(errorMsg);
            }
        }));
    }
}
