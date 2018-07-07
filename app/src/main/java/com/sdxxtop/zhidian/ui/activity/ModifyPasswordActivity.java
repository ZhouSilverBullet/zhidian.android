package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ModPwdByFormerBean;
import com.sdxxtop.zhidian.eventbus.ChangePasswordEvent;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
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
 * 描述：修改密码界面（输入原密码，输入新密码）
 */
public class ModifyPasswordActivity extends BaseActivity {

    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.tv_forgetpwd)
    TextView tvForgetpwd;
    @BindView(R.id.btn_make_sure)
    Button btnMakeSure;
    private String mobile;

    @Override
    protected int getActivityView() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        //设置EditText长度限制
        etNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etOldPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
    }

    @OnClick({R.id.tv_forgetpwd, R.id.btn_make_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetpwd:
                Intent intent = new Intent(mContext, SettingForgetPasswordActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_make_sure:
                if (etOldPwd.getText().toString().replace(" ", "").length() < 6 || etOldPwd.getText().toString().replace(" ", "").length() > 16) {
                    ToastUtil.show("请输入正确格式的密码");
                    return;
                }
                if (etNewPwd.getText().toString().replace(" ", "").length() < 6 || etNewPwd.getText().toString().replace(" ", "").length() > 16) {
                    ToastUtil.show("请输入正确格式的密码");
                    return;
                }
                postModPwdByFormer();
                break;
        }
    }


    /**
     * 通过原密码修改密码网络请求
     */
    private void postModPwdByFormer() {
        Map<String, String> map = new HashMap<>();
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        map.put("fpd", etOldPwd.getText().toString().replace(" ", ""));
        map.put("npd", etNewPwd.getText().toString().replace(" ", ""));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postModPwdByFormer(base64Data).enqueue(new Callback<ModPwdByFormerBean>() {
            @Override
            public void onResponse(Call<ModPwdByFormerBean> call, Response<ModPwdByFormerBean> response) {
                closeProgressDialog();
                ModPwdByFormerBean modPwdByFormerBean = response.body();
                if (modPwdByFormerBean.getCode() == 200) {
                    ToastUtil.show("密码修改成功");
                    ChangePasswordEvent event = new ChangePasswordEvent();
                    event.password = etNewPwd.getText().toString();
                    EventBus.getDefault().post(event);
                    finish();
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ModPwdByFormerBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }
}
