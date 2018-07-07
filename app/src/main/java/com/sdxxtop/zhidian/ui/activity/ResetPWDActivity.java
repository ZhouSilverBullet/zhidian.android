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
 * 日期：2018/3/22  时间：15:06
 * 邮箱：15010104100@163.com
 * 描述：重置密码，输入新密码，再次确认密码，完成。
 */

public class ResetPWDActivity extends BaseActivity {

    @BindView(R.id.et_user_pwd)
    EditText mEtUserPwd;
    @BindView(R.id.et_new_pwd)
    EditText mEtNewPwd;
    @BindView(R.id.btn_finish)
    Button mBtnFinish;

    private String phone;

    @Override
    protected int getActivityView() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initView() {
        //设置EditText长度限制
        mEtUserPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        mEtNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra("phone");
    }

    @OnClick({R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                if (mEtUserPwd.getText().toString().length() < 6) {
                    ToastUtil.show("请输入6-16位密码");
                    return;
                }
                if (mEtUserPwd.getText().toString().length() > 16) {
                    ToastUtil.show("请输入6-16位密码");
                    return;
                }
                if (!mEtNewPwd.getText().toString().equals(mEtUserPwd.getText().toString())) {
                    ToastUtil.show("两次密码不一致");
                    return;
                }
                postModPassword();
                break;
        }
    }


    /**
     * 网络请求，修改密码
     */
    private void postModPassword() {
        Map<String, String> map = new HashMap<>();
        map.put("mb", phone);
        map.put("pd", mEtUserPwd.getText().toString());
        map.put("rpd", mEtNewPwd.getText().toString());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postModPassword(base64Data).enqueue(new Callback<ModPasswordBean>() {
            @Override
            public void onResponse(Call<ModPasswordBean> call, Response<ModPasswordBean> response) {
                closeProgressDialog();
                if (response.body().getCode() == 200) {
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.LOGIN_TIME_TEMP, -1);
                    Intent intent = new Intent(ResetPWDActivity.this, NormalLoginActivity.class);
                    startActivity(intent);
                    showToast("密码修改成功");
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
