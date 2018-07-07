package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.widget.SubTitleView;

import butterknife.BindView;
import butterknife.OnClick;
import zhangphil.iosdialog.widget.ActionSheetDialog;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：15:06
 * 邮箱：15010104100@163.com
 * 描述：登陆成功，左面加入公司，右面创建公司
 */
public class LoginSuccessActivity extends BaseActivity {

    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.login_success_title_view)
    SubTitleView titleView;
    @BindView(R.id.btn_creat)
    Button btnCreat;
    private int splashValue;

    @Override
    protected int getActivityView() {
        return R.layout.activity_login_success;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            splashValue = getIntent().getIntExtra("splash", -1);
        }
    }

    @Override
    protected void initEvent() {
        titleView.getLeftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFinish();
            }
        });
    }

    private void toFinish() {
        if (splashValue != -1) { //splash直接过来，返回必须要回到登录界面，这样才对
            Intent intent = new Intent(mContext, NormalLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        toFinish();
    }

    @OnClick({R.id.btn_join, R.id.btn_creat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_join:
                new ActionSheetDialog(LoginSuccessActivity.this)
                        .builder()
                        .setTitle("加入方式")
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("短码加入", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //填写事件
                                        Intent intent = new Intent(LoginSuccessActivity.this, JoinCompFirstActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                break;
            case R.id.btn_creat:
                Intent intent = new Intent(LoginSuccessActivity.this, CreateCompanyActivity.class);
                startActivity(intent);
                break;
        }
    }
}
