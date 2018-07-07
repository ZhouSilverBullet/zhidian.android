package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SendCodeBean;
import com.sdxxtop.zhidian.entity.VerfiyCodeBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：CaiCM
 * 日期：2018/4/20  时间：9:39
 * 邮箱：15010104100@163.com
 * 描述：忘记密码界面（输入验证码，下一步）
 */
public class SettingForgetPasswordActivity extends BaseActivity{

    @BindView(R.id.et_indentify_code)
    EditText etIndentifyCode;
    @BindView(R.id.btn_get_indentify_code)
    RadioButton btnGetIndentifyCode;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.btn_next)
    Button btnNext;

    private int countDownTime = 60;

    @Override
    protected int getActivityView() {
        return R.layout.activity_setting_forget_password;
    }

    @Override
    protected void initView() {
        btnGetIndentifyCode.setChecked(true);
        btnGetIndentifyCode.setClickable(true);
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        if (!TextUtils.isEmpty(mobile)) {
            StringBuilder sb = new StringBuilder();
            sb.append(mobile).insert(7, " ").insert(3, " ");
            mobile = sb.toString();
            tvMobile.setText(mobile);
        }
    }

    @OnClick({R.id.btn_next, R.id.btn_get_indentify_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_indentify_code:
                if (tvMobile.getText().length() != 11){
                    ToastUtil.show("手机号错误");
                }
                else {
                    getIndentifyCode();
                }
                break;
            case R.id.btn_next:
                if (etIndentifyCode.getText().length() != 4){
                    ToastUtil.show("验证码格式不正确");
                }
                else{
                    postVerfiyCode();
                }
                break;
        }
    }


    /**
     * 网络请求，获取验证码
     */
    private void getIndentifyCode() {
        Map<String, String> map = new HashMap<>();
        map.put("mb", tvMobile.getText().toString().replace(" ", ""));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postPasswordSeendCode(base64Data).enqueue(new Callback<SendCodeBean>() {
            @Override
            public void onResponse(Call<SendCodeBean> call, Response<SendCodeBean> response) {
                closeProgressDialog();
                if (response.body().getCode() == 200) {
                    btnGetIndentifyCode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (countDownTime == 0) {
                                countDownTime = 60;
                                btnGetIndentifyCode.setText("发送验证码");
                                btnGetIndentifyCode.setClickable(true);
                                return;
                            } else {
                                btnGetIndentifyCode.setText(countDownTime + "s");
                                btnGetIndentifyCode.setClickable(false);
                                btnGetIndentifyCode.postDelayed(this, 1000);
                                countDownTime--;
                            }
                        }
                    }, 1000);
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<SendCodeBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    /**
     * 网络请求，校验
     */
    private void postVerfiyCode() {
        final Map<String, String> map = new HashMap<>();
        map.put("mb", tvMobile.getText().toString().replace(" ", ""));
        map.put("ac", etIndentifyCode.getText().toString());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postPasswordVerfiyCode(base64Data).enqueue(new Callback<VerfiyCodeBean>() {
            @Override
            public void onResponse(Call<VerfiyCodeBean> call, Response<VerfiyCodeBean> response) {
                closeProgressDialog();
                if (response.body().getCode() == 200) {
                    Intent intent = new Intent(mContext, SettingResetPasswordActivity.class);
                    intent.putExtra("mobile", tvMobile.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<VerfiyCodeBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
