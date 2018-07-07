package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：创建公司成功界面
 */
public class CreateCompanySuccActivity extends BaseActivity {

    @BindView(R.id.rl_succ)
    RelativeLayout rlSucc;
    @BindView(R.id.tv_line)
    TextView tvLine;
    @BindView(R.id.iv_face)
    ImageView ivFace;
    @BindView(R.id.rl_face)
    RelativeLayout rlFace;
    @BindView(R.id.iv_course)
    ImageView ivCourse;
    @BindView(R.id.rl_course)
    RelativeLayout rlCourse;
    @BindView(R.id.iv_punch)
    ImageView ivPunch;
    @BindView(R.id.rl_punch)
    RelativeLayout rlPunch;
    @BindView(R.id.iv_manage)
    ImageView ivManage;
    @BindView(R.id.rl_manage)
    RelativeLayout rlManage;
    @BindView(R.id.iv_mobile)
    ImageView ivMobile;
    @BindView(R.id.rl_mobil)
    RelativeLayout rlMobil;
    @BindView(R.id.iv_leg)
    ImageView ivLeg;
    @BindView(R.id.rl_leg)
    RelativeLayout rlLeg;
    @BindView(R.id.btn_invite)
    Button btnInvite;
    @BindView(R.id.btn_main)
    Button btnMain;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    @Override
    protected int getActivityView() {
        return R.layout.activity_create_company_succ;
    }

    @OnClick({R.id.btn_invite, R.id.btn_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_invite:
                Intent intent = new Intent(mContext, InviteStaffActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_main:
                Intent intentMain = new Intent(mContext, MainActivity.class);
                intentMain.putExtra(MainActivity.MAIN_SKIP,  1);
                startActivity(intentMain);
                finish();
                break;
        }
    }
}
