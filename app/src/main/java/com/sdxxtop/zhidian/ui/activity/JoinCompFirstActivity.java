package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.CheckShortCodeBean;
import com.sdxxtop.zhidian.eventbus.JoinFinishEvent;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;

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
 * 描述：加入公司后输入公司短码，并进行网络校验
 */
public class JoinCompFirstActivity extends BaseActivity {

    @BindView(R.id.et_short_code)
    EditText etShortCode;
    @BindView(R.id.make_sure)
    Button makeSure;

    @Override
    protected int getActivityView() {
        return R.layout.activity_join_comp_first;
    }

    @Override
    protected void initView() {
        registeredEvent();

        //设置EditText长度限制（短码长度）
//        etShortCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
    }

    @OnClick({R.id.make_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.make_sure:
                //跳转到公司、职位界面
                String short_code = etShortCode.getText().toString().trim();
                if (TextUtils.isEmpty(short_code)) {
                    ToastUtil.show("请填写公司短码");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
                map.put("sc", short_code);
                String base64Data = NetUtil.getBase64Data(map);
                showProgressDialog("");
                RequestUtils.getInstance().buildRequest().postCheckShortCode(base64Data).enqueue(new Callback<CheckShortCodeBean>() {
                    @Override
                    public void onResponse(Call<CheckShortCodeBean> call, Response<CheckShortCodeBean> response) {
                        closeProgressDialog();
                        CheckShortCodeBean checkShortCodeBean = response.body();
                        if (checkShortCodeBean.getCode() == 200) {
                            //跳转到公司、职位界面
                            Intent intent = new Intent(JoinCompFirstActivity.this, JoinCompSecondActivity.class);
                            intent.putExtra("compName", checkShortCodeBean.getData().getCompany_name());
                            intent.putExtra("shortCode", checkShortCodeBean.getData().getShort_code());
                            intent.putExtra("company_id", checkShortCodeBean.getData().getCompany_id() + "");

                            startActivity(intent);
                        } else {
                            ToastUtil.show(checkShortCodeBean.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckShortCodeBean> call, Throwable t) {
                        closeProgressDialog();
                        ToastUtil.show("网络错误");
                    }
                });
                break;
        }
    }

    @Subscribe
    public void joinFinishEventMethod(JoinFinishEvent event) {
        finish();
    }
}
