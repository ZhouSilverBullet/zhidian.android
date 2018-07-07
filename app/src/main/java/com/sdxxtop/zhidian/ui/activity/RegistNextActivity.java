package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.RegisterRegBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.DeviceUtil;
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
 * 描述：注册，输入姓名、密码，完成。
 */

public class RegistNextActivity extends BaseActivity {

    @BindView(R.id.btn_finish)
    Button mBtnFinish;
    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.et_user_pwd)
    EditText mEtUserPwd;

    private String phone;
    private String device_name;

    @Override
    protected int getActivityView() {
        return R.layout.activity_regist_next;
    }

    @Override
    protected void initView() {
        //设置EditText长度限制
        mEtUserPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        //获取当前设备名称（手机型号）
        device_name = Build.MODEL;
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra("phone").replace(" ", "");
    }

    @OnClick({R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                if ("".equals(mEtUserName.getText().toString())) {
                    ToastUtil.show("请输入您的真实姓名");
                    return;
                }
                if (mEtUserPwd.getText().toString().length() < 6) {
                    ToastUtil.show("请输入正确格式的密码！");
                    return;
                }
                if (mEtUserPwd.getText().toString().length() > 16) {
                    ToastUtil.show("请输入正确格式的密码！");
                    return;
                }
                postVerfiyCode();
                break;
        }
    }

    /**
     * 网络请求，注册
     */
    private void postVerfiyCode() {
        Map<String, String> map = new HashMap<>();
        map.put("mb", phone);
        map.put("un", mEtUserName.getText().toString().trim());
        map.put("pw", mEtUserPwd.getText().toString());
        map.put("pi", ConstantValue.pi);
        map.put("dn", DeviceUtil.getDeviceNo(mContext));
        map.put("dm", DeviceUtil.getDeviceName());
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postRegisterReg(base64Data).enqueue(new Callback<RegisterRegBean>() {
            @Override
            public void onResponse(Call<RegisterRegBean> call, Response<RegisterRegBean> response) {
                closeProgressDialog();
                if (response.body().getCode() == 200) {
                    ToastUtil.show("注册成功");
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.DEVICE_NAME, device_name);
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.DEVICE_NO, DeviceUtil.getDeviceNo(mContext));
//                    AppSession.getInstance().setUserId(response.body().getData().getUserid() + "");
//                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.USER_ID, response.body().getData().getUserid() + "");
                    Intent intent = new Intent(RegistNextActivity.this, NormalLoginActivity.class);
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.USER_NAME, phone);
                    PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.PASSWORD, mEtUserPwd.getText().toString());
//                    intent.putExtra("un", phone);
//                    intent.putExtra("pw", mEtUserPwd.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<RegisterRegBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
