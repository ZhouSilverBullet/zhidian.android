package com.sdxxtop.zhidian.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowContactSearchAdapter;
import com.sdxxtop.zhidian.entity.ContactSearchBean;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.NetUtil;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;
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
 * 日期：2018/3/23  时间：16:39
 * 邮箱：15010104100@163.com
 * 描述：通讯录搜索界面
 */
public class ContactSearchActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.rl_serch_background)
    RelativeLayout rlSerchBackground;
    @BindView(R.id.lv_show_search)
    ListView lvShowSearch;
    private List<ContactSearchBean.DataEntity.UserinfoEntity> listSerch = new ArrayList<>();
    private ShowContactSearchAdapter showContactSearchAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_contact_search;
    }

    @Override
    protected void initView() {
        showContactSearchAdapter = new ShowContactSearchAdapter(mContext, listSerch);
        lvShowSearch.setAdapter(showContactSearchAdapter);
    }

    @Override
    protected void initEvent() {
//        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
        lvShowSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ContactDetailActivity.class);
                intent.putExtra("userId", listSerch.get(position).getUserid() + "");
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    /**
     * 监听
     */
//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            Log.e("LOG", etSearch.getText().toString());
//            if (etSearch.getText().toString().equals("")) {
//                ToastUtil.show("请输入搜索内容");
//            } else {
//                postContactSearch(etSearch.getText().toString());
//            }
//            return true;
//        }
//        return false;
//    }


    /**
     * 搜索联系人网络请求
     */
    public void postContactSearch(String searchContent) {
        Map<String, String> map = new HashMap<>();
        //PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID)
        map.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        map.put("sh", searchContent);
        String base64Data = NetUtil.getBase64Data(map);
        //showProgressDialog("");
        RequestUtils.getInstance().buildRequest().postContactSearch(base64Data).enqueue(new Callback<ContactSearchBean>() {
            @Override
            public void onResponse(Call<ContactSearchBean> call, Response<ContactSearchBean> response) {
                LogUtils.e("log", "serch");
                //closeProgressDialog();
                ContactSearchBean searchBean = response.body();
                if (searchBean.getCode() == 200) {
                    listSerch.clear();
                    listSerch.addAll(searchBean.getData().getUserinfo());
                    showContactSearchAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(searchBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ContactSearchBean> call, Throwable t) {
                //closeProgressDialog();
                ToastUtil.show("网络错误");
            }
        });
    }


    /**
     * beforeTextChanged    输入前确认执行该方法
     * onTextChanged    输入过程中执行该方法
     * afterTextChanged 输入结束执行该方法
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        LogUtils.e("log", "发起联系人搜索");
        if (etSearch.getText().toString().equals("")) {
                listSerch.clear();
                showContactSearchAdapter.notifyDataSetChanged();
            } else {
                postContactSearch(etSearch.getText().toString());
            }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
