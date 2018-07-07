package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.eventbus.ChangeMobileEvent;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.StringUtil;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：CaiCM
 * 日期：2018/4/20  时间：9:39
 * 邮箱：15010104100@163.com
 * 描述：展示绑定手机号界面界面
 */
public class ModifyMobileActivity extends BaseActivity {

    @BindView(R.id.btn_change_mobile)
    Button btnChangeMobile;
    @BindView(R.id.tv_current_mobile)
    TextView tvCurrentMobile;

    @Override
    protected int getActivityView() {
        return R.layout.activity_modify_mobile;
    }

    @Override
    protected void initView() {
        registeredEvent();

        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");

        tvCurrentMobile.setText(StringUtil.textPhoneValue(mobile));
    }

    @OnClick({R.id.btn_change_mobile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_change_mobile:
                Intent intent = new Intent(mContext, ModifyMobileInputActivity.class);
                startActivity(intent);
//                finish();
                break;
        }
    }

    @Subscribe
    public void notifyChangeMobileSuccess(ChangeMobileEvent event) {
        finish();
    }
}
