package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowOutPowerDrawerLayoutAdapter;
import com.sdxxtop.zhidian.entity.JoinByCodeBean;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;
import com.sdxxtop.zhidian.eventbus.JoinFinishEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
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
 * 描述：公司短码校验成功后，展示公司名称，并输入职位，提交。
 * 成功后进入对应公司的首页
 */
public class JoinCompSecondActivity extends BaseActivity {

    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.et_job)
    EditText etJob;
    @BindView(R.id.btn_join)
    Button btnJoin;

    @BindView(R.id.tv_part)
    TextView tvPart;
    @BindView(R.id.join_second_list_view)
    ListView listView;
    @BindView(R.id.join_second_draw_layout)
    DrawerLayout drawerLayout;

    private String shortCode;
    private String compName;

    private ShowOutPowerDrawerLayoutAdapter adapter;
    private String company_id;

    @Override
    protected int getActivityView() {
        return R.layout.activity_join_comp_second;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        shortCode = intent.getStringExtra("shortCode");
        compName = intent.getStringExtra("compName");
        company_id = intent.getStringExtra("company_id");

        tvCompany.setText(compName);

        adapter = new ShowOutPowerDrawerLayoutAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UcenterOutIndexBean.DataEntity.PartEntity partEntity = adapter.getData().get(position);
                tvPart.setText(partEntity.getNo_prefix());
                drawerLayout.closeDrawer(Gravity.RIGHT);
                JoinCompSecondActivity.this.partId = partEntity.getPart_id();
            }
        });
    }

    @Override
    protected void initData() {
        Params params = new Params();
        params.put("ci", company_id);
        RequestUtils.createRequest().postOutGetPart(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<UcenterOutIndexBean>() {
            @Override
            public void onSuccess(UcenterOutIndexBean ucenterOutIndexBean) {
                UcenterOutIndexBean.DataEntity data = ucenterOutIndexBean.getData();
                if (data != null) {
                    List<UcenterOutIndexBean.DataEntity.PartEntity> part = data.getPart();
                    if (part != null) {
                        adapter.replaceData(part);
                        if (part.size() > 0) {
                            String no_prefix = part.get(0).getNo_prefix();
                            int partId = part.get(0).getPart_id();
                            tvPart.setText(no_prefix);
                            JoinCompSecondActivity.this.partId = partId;
                        }
                    }
                }
            }


            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private int partId;

    @OnClick({R.id.btn_join, R.id.rl_part})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_join:
//                if (partId == 0) {
//                    showToast("部门不能为空");
//                    return;
//                }
//
//                String value = etJob.getText().toString();
//                if (TextUtils.isEmpty(value)) {
//                    showToast("职位不能为空");
//                    return;
//                }

                Map<String, String> map = new HashMap<>();
                map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
                map.put("sc", shortCode);
                map.put("pt", etJob.getText().toString());
                map.put("ci", StringUtil.stringNotNull(company_id));
                map.put("pi", partId + "");
                String base64Data = NetUtil.getBase64Data(map);
                showProgressDialog("");
                RequestUtils.getInstance().buildRequest().postJoinByCode(base64Data).enqueue(new Callback<JoinByCodeBean>() {
                    @Override
                    public void onResponse(Call<JoinByCodeBean> call, Response<JoinByCodeBean> response) {
                        closeProgressDialog();
                        JoinByCodeBean joinByCodeBean = response.body();
                        if (joinByCodeBean.getCode() == 200) {
                            AppSession.getInstance().setCompanyId(joinByCodeBean.getData().getCompany_id() + "");
//                            PreferenceUtils.getInstance(mContext).saveParam(ConstantValue.COMPANY_ID, joinByCodeBean.getData().getCompany_id() + "");
                            //跳转到首页
                            Intent intent = new Intent(JoinCompSecondActivity.this, MainActivity.class);
                            intent.putExtra(MainActivity.MAIN_SKIP, 1);
                            startActivity(intent);
                            EventBus.getDefault().post(new JoinFinishEvent());
                            finish();
                        } else {
                            ToastUtil.show(joinByCodeBean.getMsg().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<JoinByCodeBean> call, Throwable t) {
                        closeProgressDialog();
                        ToastUtil.show("网络错误");
                    }
                });
                break;
            case R.id.rl_part:
                drawerLayout.openDrawer(Gravity.RIGHT);
                break;
        }
    }
}
