package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SendCodeBean;
import com.sdxxtop.zhidian.entity.VerfiyCodeBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PhoneTextWatcher;
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
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：忘记密码界面
 */
public class ForgetPWDActivity extends BaseActivity implements TextWatcher {


    @BindView(R.id.et_phonenum)
    EditText mEtPhonenum;
    @BindView(R.id.et_indentify_code)
    EditText mEtIndentifyCode;
    @BindView(R.id.btn_get_indentify_code)
    RadioButton mBtnGetIndentifyCode;
    @BindView(R.id.btn_next)
    Button mBtnNext;

    private int countDownTime = 60;

    @Override
    protected int getActivityView() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView() {
        mBtnGetIndentifyCode.setChecked(false);
        mBtnGetIndentifyCode.setClickable(false);
        //设置手机号格式
        mEtPhonenum.addTextChangedListener(new PhoneTextWatcher(mEtPhonenum));
        //设置EditText长度限制
        mEtPhonenum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        mEtIndentifyCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
    }

    @Override
    protected void initEvent() {
        mEtPhonenum.addTextChangedListener(this);
    }

    @OnClick({R.id.btn_get_indentify_code, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_indentify_code:
                getIndentifyCode();
                break;
            case R.id.btn_next:
                if (mEtPhonenum.getText().toString().replace(" ", "").length() < 11) {
                    ToastUtil.show("请输入正确的手机号码！");
                    return;
                }
                if (mEtIndentifyCode.getText().toString().length() != 4) {
                    ToastUtil.show("请输入正确验证码！");
                    return;
                }
                postVerfiyCode();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mBtnGetIndentifyCode.setClickable(charSequence.toString().replace(" ", "").length() < 11 ? false : true);
        mBtnGetIndentifyCode.setChecked(charSequence.toString().replace(" ", "").length() < 11 ? false : true);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 网络请求，获取验证码
     */
    private void getIndentifyCode() {
        Map<String, String> map = new HashMap<>();
        map.put("mb", mEtPhonenum.getText().toString().replace(" ", ""));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postPasswordSeendCode(base64Data).enqueue(new Callback<SendCodeBean>() {
            @Override
            public void onResponse(Call<SendCodeBean> call, Response<SendCodeBean> response) {
                closeProgressDialog();
                if (response.body().getCode() == 200) {
                    mBtnGetIndentifyCode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (countDownTime == 0) {
                                countDownTime = 60;
                                mBtnGetIndentifyCode.setText("发送验证码");
                                mBtnGetIndentifyCode.setClickable(true);
                                return;
                            } else {
                                mBtnGetIndentifyCode.setText(countDownTime + "s");
                                mBtnGetIndentifyCode.setClickable(false);
                                mBtnGetIndentifyCode.postDelayed(this, 1000);
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
        Map<String, String> map = new HashMap<>();
        map.put("mb", mEtPhonenum.getText().toString().replace(" ", ""));
        map.put("ac", mEtIndentifyCode.getText().toString());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postPasswordVerfiyCode(base64Data).enqueue(new Callback<VerfiyCodeBean>() {
            @Override
            public void onResponse(Call<VerfiyCodeBean> call, Response<VerfiyCodeBean> response) {
                closeProgressDialog();
                if (response.body().getCode() == 200) {
                    Intent intent = new Intent(ForgetPWDActivity.this, ResetPWDActivity.class);
                    intent.putExtra("phone", mEtPhonenum.getText().toString().replace(" ", ""));
                    startActivity(intent);
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
}
