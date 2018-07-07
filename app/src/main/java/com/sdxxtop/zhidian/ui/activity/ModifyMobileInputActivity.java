package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.eventbus.ChangeMobileEvent;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：CaiCM
 * 日期：2018/4/20  时间：9:39
 * 邮箱：15010104100@163.com
 * 描述：更换手机账号界面（输入当前登陆密码）
 */
public class ModifyMobileInputActivity extends BaseActivity {

    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    protected int getActivityView() {
        return R.layout.activity_modify_mobile_input;
    }

    @Override
    protected void initView() {
        registeredEvent();

        //设置EditText长度限制
        etLoginPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (!etLoginPwd.getText().toString().equals(PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.PASSWORD))) {
                    ToastUtil.show("密码不正确");
                } else {
                    Intent intent = new Intent(mContext, ChangeMobileActivity.class);
                    startActivity(intent);
//                    finish();
                }
                break;
        }
    }

    @Subscribe
    public void notifyChangeMobileSuccess(ChangeMobileEvent event) {
        finish();
    }
}
