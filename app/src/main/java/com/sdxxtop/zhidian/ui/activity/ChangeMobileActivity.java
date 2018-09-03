package com.sdxxtop.zhidian.ui.activity;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.UcenterModMobileBean;
import com.sdxxtop.zhidian.entity.UcenterSendCodeBean;
import com.sdxxtop.zhidian.eventbus.ChangeMobileEvent;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PhoneTextWatcher;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

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
 * 描述：更换手机账号界面（输入新手机号，输入验证码）
 */
public class ChangeMobileActivity extends BaseActivity {

    @BindView(R.id.et_new_mobile)
    EditText etNewMobile;
    @BindView(R.id.et_indentify_code)
    EditText etIndentifyCode;
    @BindView(R.id.btn_get_indentify_code)
    RadioButton btnGetIndentifyCode;
    @BindView(R.id.btn_make_sure)
    Button btnMakeSure;

    private int countDownTime = 60;
    @Override
    protected int getActivityView() {
        return R.layout.activity_change_mobile;
    }

    @Override
    protected void initView() {
        btnGetIndentifyCode.setChecked(false);
        btnGetIndentifyCode.setClickable(false);
        //设置EditText长度限制
        etNewMobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        etIndentifyCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
    }

    @Override
    protected void initEvent() {
        etNewMobile.addTextChangedListener(new PhoneTextWatcher(etNewMobile) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                btnGetIndentifyCode.setClickable(s.toString().replace(" ", "").length() < 11 ? false : true);
                btnGetIndentifyCode.setChecked(s.toString().replace(" ", "").length() < 11 ? false : true);
            }
        });
    }

    @OnClick({R.id.btn_make_sure, R.id.btn_get_indentify_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_indentify_code:
                postUcenterSendCode();
                break;
            case R.id.btn_make_sure:
                String phoneValue = etNewMobile.getText().toString().trim();
                if (TextUtils.isEmpty(phoneValue)){
                    ToastUtil.show("请输入新手机号");
                    return;
                }
                if (phoneValue.replace(" ", "").length() < 11) {
                    ToastUtil.show("请输入新手机号");
                    return;
                }
                if (etIndentifyCode.getText().toString().length() != 4) {
                    ToastUtil.show("请输入正确验证码！");
                    return;
                }
                postUcenterModMobile();
                break;
        }
    }

    /**
     * 修改手机号发送验证码网络请求
     */
    private void postUcenterSendCode() {
        Map<String, String> map = new HashMap<>();
        map.put("mb", etNewMobile.getText().toString().replace(" ", ""));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterSendCode(base64Data).enqueue(new Callback<UcenterSendCodeBean>() {
            @Override
            public void onResponse(Call<UcenterSendCodeBean> call, Response<UcenterSendCodeBean> response) {
                closeProgressDialog();
                UcenterSendCodeBean ucenterSendCodeBean = response.body();
                if (ucenterSendCodeBean.getCode() == 200) {
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
            public void onFailure(Call<UcenterSendCodeBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    /**
     * 修改手机号网络请求
     */
    private void postUcenterModMobile() {
        Map<String, String> map = new HashMap<>();
        final String phone = etNewMobile.getText().toString().replace(" ", "");
        map.put("mb", phone);
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("ac", etIndentifyCode.getText().toString().replace(" ", ""));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postUcenterModMobile(base64Data).enqueue(new Callback<UcenterModMobileBean>() {
            @Override
            public void onResponse(Call<UcenterModMobileBean> call, Response<UcenterModMobileBean> response) {
                closeProgressDialog();
                UcenterModMobileBean ucenterModMobileBean = response.body();
                if (ucenterModMobileBean.getCode() == 200) {
                    ToastUtil.show("手机账号更换成功");
                    ChangeMobileEvent event = new ChangeMobileEvent();
                    event.phone = phone;
                    EventBus.getDefault().post(event);
                    finish();
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<UcenterModMobileBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
