package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.alipush.AnalyticsHome;
import com.sdxxtop.zhidian.entity.UcenterRemindBean;
import com.sdxxtop.zhidian.entity.UcenterSetBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.tencent.imsdk.TIMManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zhangphil.iosdialog.widget.AlertDialog;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：11:39
 * 邮箱：15010104100@163.com
 * 描述：我的设置界面
 */
public class MySettingActivity extends BaseActivity {

    @BindView(R.id.tv_into_company)
    TextView tvIntoCompany;
    @BindView(R.id.btn_log_off)
    Button btnLogOff;
    @BindView(R.id.rl_into_company)
    RelativeLayout rlIntoCompany;
    @BindView(R.id.rl_into_company_info)
    RelativeLayout rlIntoCompanyInfo;
    @BindView(R.id.rl_into_device_info)
    RelativeLayout rlIntoDeviceInfo;
    @BindView(R.id.rl_into_invite_staff)
    RelativeLayout rlIntoInviteStaff;
    @BindView(R.id.rl_into_out_power)
    RelativeLayout rlIntoOutPower;
    @BindView(R.id.rl_into_account_pwd)
    RelativeLayout rlIntoAccountPwd;
    @BindView(R.id.rl_into_message_notice)
    RelativeLayout rlIntoMessageNotice;
    @BindView(R.id.rl_into_remind)
    RelativeLayout rlIntoRemind;
    @BindView(R.id.rl_into_about_us)
    RelativeLayout rlIntoAboutUs;
    @BindView(R.id.tv_into_remind)
    TextView tvIntoRemind;
    private String mobile;
    private int is_manager;
    private int is_device;
    private int is_invite;
    private int is_out;
    private OptionPicker picker;

    @Override
    protected int getActivityView() {
        return R.layout.activity_my_setting;
    }

    @Override
    protected void initView() {
        if (PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.REMIND_MIN).equals("")) {
            tvIntoRemind.setText("5分钟前");
        } else {
            tvIntoRemind.setText(PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.REMIND_MIN));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        postUcenterSet();
    }

    @OnClick({R.id.rl_into_company, R.id.rl_into_company_info, R.id.rl_into_device_info, R.id.rl_into_invite_staff, R.id.rl_into_out_power, R.id.rl_into_account_pwd, R.id.rl_into_message_notice, R.id.rl_into_remind, R.id.rl_into_about_us, R.id.btn_log_off})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_into_company:
                Intent intentChangeCompany = new Intent(mContext, ChangeCompanyActivity.class);
                startActivity(intentChangeCompany);
                break;
            case R.id.rl_into_company_info:
                Intent intentCompanyInfo = new Intent(mContext, CompanyInfoActivity.class);
                startActivity(intentCompanyInfo);
                break;
            case R.id.rl_into_device_info:
                Intent intentDeviceInfo = new Intent(mContext, DeviceInfoActivity.class);
                startActivity(intentDeviceInfo);
                break;
            case R.id.rl_into_invite_staff:
                Intent intentInviteStaff = new Intent(mContext, InviteStaffActivity.class);
                startActivity(intentInviteStaff);
                break;
            case R.id.rl_into_out_power:
                Intent intentOutPower = new Intent(mContext, OutPowerActivity.class);
                startActivity(intentOutPower);
                break;
            case R.id.rl_into_account_pwd:
                Intent intentAccountPwd = new Intent(mContext, AccountPwdActivity.class);
                intentAccountPwd.putExtra("mobile", mobile);
                startActivity(intentAccountPwd);
                break;
            case R.id.rl_into_message_notice:
                Intent intentMessageNotice = new Intent(mContext, MessageNoticeActivity.class);
                startActivity(intentMessageNotice);
                break;
            case R.id.rl_into_remind:
                onOptionPicker();
                break;
            case R.id.rl_into_about_us:
                Intent intentAboutUs = new Intent(mContext, AboutUsActivity.class);
                startActivity(intentAboutUs);
                break;
            case R.id.btn_log_off:
                new AlertDialog(mContext).builder().setTitle("提示")
                        .setMsg("确定退出本账号吗？")
                        .setPositiveButton("退出", new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(View view) {
                                AnalyticsHome.unbindAccount();
                                Intent intentNormalLogin = new Intent(mContext, NormalLoginActivity.class);
                                startActivity(intentNormalLogin);
                                PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TIME_TEMP, -1);
                                finishAffinity();//可以关闭当前activity所属的activity栈中所有的activity

                                //腾讯im退出
                                TIMManager.getInstance().logout(null);
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
                break;
        }
    }


    private String[] optionString = new String[]{
            "不提醒", "5分钟前", "10分钟前", "20分钟前"
    };
    private String[] optionTime = new String[]{
            "0", "5", "10", "20"
    };

    /**
     * 允许上下班时间差选择器
     */
    public void onOptionPicker() {
        picker = new OptionPicker(mContext, optionString);
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setCycleDisable(true);
        picker.setTextSize(20);
        picker.setCanceledOnTouchOutside(true);
        picker.setTextColor(Color.rgb(50, 150, 250));
        picker.setDividerColor(Color.rgb(153, 153, 153));
        picker.setCancelTextColor(Color.rgb(153, 153, 153));
        picker.setSubmitTextColor(Color.rgb(50, 150, 250));
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tvIntoRemind.setText(item);
                postUcenterRemind();
            }
        });

        picker.setSelectedIndex(getSelectedIndex());
        picker.show();
    }

    private int getSelectedIndex() {
        int index;
        switch (tvIntoRemind.getText().toString()) {
            case "5分钟前":
                index = 1;
                break;
            case "10分钟前":
                index = 2;
                break;
            case "20分钟前":
                index = 3;
                break;
            default:
                index = 0;
                break;
        }
        return index;
    }


    /**
     * 修改打卡提醒网络请求
     */
    private void postUcenterRemind() {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("rm", optionTime[getSelectedIndex()]);
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterRemind(base64Data).enqueue(new Callback<UcenterRemindBean>() {
            @Override
            public void onResponse(Call<UcenterRemindBean> call, Response<UcenterRemindBean> response) {
                closeProgressDialog();
                UcenterRemindBean ucenterRemindBean = response.body();
                if (ucenterRemindBean.getCode() == 200) {
                    ToastUtil.show("修改成功");
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.REMIND_MIN, tvIntoRemind.getText().toString());
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<UcenterRemindBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    /**
     * 我的设置主页网络请求
     */
    private void postUcenterSet() {
        Map<String, String> map = new HashMap<>();
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterSet(base64Data).enqueue(new Callback<UcenterSetBean>() {
            @Override
            public void onResponse(Call<UcenterSetBean> call, Response<UcenterSetBean> response) {
                closeProgressDialog();
                UcenterSetBean ucenterSetBean = response.body();
                if (ucenterSetBean.getCode() == 200) {
                    mobile = ucenterSetBean.getData().getMobile() + "";
                    if (ucenterSetBean.getData().getRemind_min() == 0) {
                        tvIntoRemind.setText("不提醒");
                    } else {
                        tvIntoRemind.setText(ucenterSetBean.getData().getRemind_min() + "分钟前");
                    }
                    tvIntoCompany.setText(ucenterSetBean.getData().getCompany_name());
                    is_manager = ucenterSetBean.getData().getIs_manager();
                    is_device = ucenterSetBean.getData().getIs_device();
                    is_invite = ucenterSetBean.getData().getIs_invite();
                    is_out = ucenterSetBean.getData().getIs_out();

                    if (is_manager == 1 || is_device == 1) {
                        rlIntoDeviceInfo.setVisibility(View.VISIBLE);
                    } else {
                        rlIntoDeviceInfo.setVisibility(View.GONE);
                    }

                    if (is_manager == 1 || is_invite == 1) {
                        rlIntoInviteStaff.setVisibility(View.VISIBLE);
                    } else {
                        rlIntoInviteStaff.setVisibility(View.GONE);
                    }

                    if (is_manager == 1 || is_out == 1) {
                        rlIntoOutPower.setVisibility(View.VISIBLE);
                    } else {
                        rlIntoOutPower.setVisibility(View.GONE);
                    }
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<UcenterSetBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
