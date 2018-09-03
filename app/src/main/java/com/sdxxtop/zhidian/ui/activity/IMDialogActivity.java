package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.alipush.AnalyticsHome;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.timchat.model.FriendshipInfo;
import com.tencent.qcloud.timchat.model.GroupInfo;
import com.tencent.qcloud.timchat.model.UserInfo;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IMDialogActivity extends AppCompatActivity {

    @BindView(R.id.dialog_activity_txt_title)
    TextView mTitle;
    @BindView(R.id.dialog_activity_txt_msg)
    TextView mMsg;
    @BindView(R.id.dialog_activity_btn_neg)
    Button mBtnNeg;
    @BindView(R.id.dialog_activity_btn_pos)
    Button mBtnPos;
    @BindView(R.id.lLayout_bg)
    LinearLayout mBgLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imdialog);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    protected void initView() {
        setFinishOnTouchOutside(false);

        mTitle.setText("下线通知");
        mMsg.setText("您的账号已于另一台手机上登录。");
        mBtnNeg.setText("退出");
        mBtnPos.setText("重新登录");
    }

    protected void initEvent() {
        mBtnNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mBtnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });
    }

    private void toLogin() {
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(IMDialogActivity.this, getString(com.tencent.qcloud.timchat.R.string.login_error), Toast.LENGTH_SHORT).show();
                logout();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(IMDialogActivity.this, getString(com.tencent.qcloud.timchat.R.string.login_succ), Toast.LENGTH_SHORT).show();
                String deviceMan = android.os.Build.MANUFACTURER;
                //注册小米和华为推送
//                    if (deviceMan.equals("Xiaomi") && shouldMiInit()) {
//                        MiPushClient.registerPush(getApplicationContext(), "2882303761517480335", "5411748055335");
//                    } else if (deviceMan.equals("HUAWEI")) {
//                        PushManager.requestToken(getApplicationContext());
//                    }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void logout() {
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();

        AnalyticsHome.unbindAccount();
        Intent intentNormalLogin = new Intent(this, NormalLoginActivity.class);
        intentNormalLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentNormalLogin);
    }
}
