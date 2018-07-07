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
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckSafeActivity extends BaseActivity {

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
        return R.layout.activity_check_safe;
    }

    @Override
    protected void initView() {
        btnGetIndentifyCode.setChecked(true);
        btnGetIndentifyCode.setClickable(true);
//        Intent intent = getIntent();
//        String mobile = intent.getStringExtra("mobile");
        String mobile = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_NAME);
        if (!TextUtils.isEmpty(mobile)) {
            StringBuilder sb = new StringBuilder();
            sb.append(mobile).insert(7, " ").insert(3, " ");
            mobile = sb.toString();
            tvMobile.setText(mobile);
        }
    }

    @OnClick({R.id.btn_get_indentify_code, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_indentify_code:
                if (tvMobile.getText().length() != 13) {
                    ToastUtil.show("手机号错误");
                } else {
                    getIndentifyCode();
                }
                break;
            case R.id.btn_next:
                if (etIndentifyCode.getText().length() != 4) {
                    ToastUtil.show("验证码格式不正确");
                } else {
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
//                    Intent intent = new Intent(mContext, SettingResetPasswordActivity.class);
//                    intent.putExtra("mobile", tvMobile.getText().toString());
//                    startActivity(intent);
                    Intent intent = new Intent(mContext, NoticeReciveRangeActivity.class);
                    intent.putExtra("isPartSelect", NoticeReciveRangeActivity.PART_NOT_SELECTOR);
                    intent.putExtra("singleSelect", NoticeReciveRangeActivity.SINGLE_SELECTOR);
                    intent.putExtra(NoticeReciveRangeActivity.NOTICE_TITLE, "选择");
                    intent.putExtra(NoticeReciveRangeActivity.NOTICE_TYPE, NoticeReciveRangeActivity.NOTICE_TYPE_CHECK_SAFE);
                    startActivityForResult(intent, 100);

//                    finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            finish();
        }
    }
}
