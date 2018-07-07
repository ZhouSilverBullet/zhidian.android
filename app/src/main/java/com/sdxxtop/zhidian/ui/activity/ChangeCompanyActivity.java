package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ChangeCompanyAdapter;
import com.sdxxtop.zhidian.entity.ShowCompanyBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zhangphil.iosdialog.widget.ActionSheetDialog;

/**
 * 作者：CaiCM
 * 日期：2018/4/18  时间：11:39
 * 邮箱：15010104100@163.com
 * 描述：切换公司界面
 */
public class ChangeCompanyActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.btn_creat)
    Button btnCreat;
    @BindView(R.id.lv_show_company)
    ListView lvShowCompany;

    private ArrayList<ShowCompanyBean.DataEntityX.DataEntity> list = new ArrayList<>();
    private ChangeCompanyAdapter changeCompanyAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_change_company;
    }

    @Override
    protected void initView() {
        changeCompanyAdapter = new ChangeCompanyAdapter(mContext, list);
        lvShowCompany.setAdapter(changeCompanyAdapter);
    }

    @Override
    protected void initData() {
        postShowCompany();
    }

    @Override
    protected void initEvent() {
        lvShowCompany.setOnItemClickListener(this);
    }

    @OnClick({R.id.btn_join, R.id.btn_creat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_join:
                new ActionSheetDialog(mContext)
                        .builder()
                        .setTitle("加入方式")
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("短码加入", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //填写事件
                                        Intent intent = new Intent(mContext, JoinCompFirstActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                break;
            case R.id.btn_creat:
                Intent intent = new Intent(mContext, CreateCompanyActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 用户加入的公司列表，网络请求
     */
    private void postShowCompany() {
        Map<String, String> map = new HashMap<>();
        map.put("ui", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.USER_ID));
        String base64Data = NetUtil.getBase64Data(map);
        showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postShowCompany(base64Data).enqueue(new Callback<ShowCompanyBean>() {
            @Override
            public void onResponse(Call<ShowCompanyBean> call, Response<ShowCompanyBean> response) {
                closeProgressDialog();
                ShowCompanyBean showCompanyBean = response.body();
                if (showCompanyBean.getCode() == 200) {
                    list.clear();
                    list.addAll(showCompanyBean.getData().getData());
                    changeCompanyAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ShowCompanyBean> call, Throwable t) {
                closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.e("log", "onItemClic:" + position);
        ShowCompanyBean.DataEntityX.DataEntity dataEntity = list.get(position);
        ToastUtil.show(dataEntity.getCompany_name());
    }
}
