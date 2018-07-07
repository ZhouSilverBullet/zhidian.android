package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ModPasswordBean;
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

/**
 * 作者：CaiCM
 * 日期：2018/4/20  时间：9:39
 * 邮箱：15010104100@163.com
 * 描述：设置新密码界面（输入新密码，确认新密码）
 */
public class SettingResetPasswordActivity extends BaseActivity {

    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_sure_pwd)
    EditText etSurePwd;
    @BindView(R.id.btn_make_sure)
    Button btnMakeSure;
    private String mobile;

    @Override
    protected int getActivityView() {
        return R.layout.activity_setting_reset_password;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        //设置EditText长度限制
        etNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etSurePwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
    }

    @OnClick({R.id.btn_make_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_make_sure:
                if (!etNewPwd.getText().toString().equals(etSurePwd.getText().toString())){
                    ToastUtil.show("两次密码不一致");
                    return;
                }
                if (etNewPwd.getText().toString().length() < 6 || etNewPwd.getText().toString().length() > 16){
                    ToastUtil.show("密码长度6~16位");
                    return;
                }
                postModPassword();
                break;
        }
    }

    /**
     * 网络请求，设置新密码
     */
    private void postModPassword() {
        Map<String, String> map = new HashMap<>();
        map.put("mb", mobile);
        map.put("pd", etNewPwd.getText().toString());
        map.put("rpd", etSurePwd.getText().toString());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postModPassword(base64Data).enqueue(new Callback<ModPasswordBean>() {
            @Override
            public void onResponse(Call<ModPasswordBean> call, Response<ModPasswordBean> response) {
                closeProgressDialog();
                if (response.body().getCode() == 200) {
                    ToastUtil.show("新密码设置成功");
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TIME_TEMP, -1);
                    finish();
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ModPasswordBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
