package com.sdxxtop.zhidian.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ContactRecyclerAdapter;
import com.sdxxtop.zhidian.entity.ContactIndexBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.LogUtils;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.contact_search_recycler)
    RecyclerView mRecyclerView;
    private ContactRecyclerAdapter mAdapter;
    private boolean toConversation;
    private String userId;
    //    private List<ContactSearchBean.DataEntity.UserinfoEntity> listSerch = new ArrayList<>();
//    private ShowContactSearchAdapter showContactSearchAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_contact_search;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            toConversation = getIntent().getBooleanExtra("conversation", false);
        }
    }

    @Override
    protected void initView() {
        userId = AppSession.getInstance().getUserId();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ItemDivider().setDividerLeftOffset(ViewUtil.dp2px(mContext, 66)));
        mAdapter = new ContactRecyclerAdapter(R.layout.item_contact_recycler);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setToConversation(toConversation);
    }

    @Override
    protected void initEvent() {
//        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
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
        Params params = new Params();
        params.put("ci", PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.COMPANY_ID));
        params.put("sh", searchContent);
        RequestUtils.createRequest().postContactSearch(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ContactIndexBean>() {
            @Override
            public void onSuccess(ContactIndexBean contactIndexBean) {
                ContactIndexBean.DataEntity data = contactIndexBean.getData();
                if (data != null) {
                    List<ContactIndexBean.DataEntity.ColloectUserEntity> userinfo = data.getUserinfo();
                    if (userinfo != null) {
                        List<ContactIndexBean.DataEntity.ColloectUserEntity> tempList = new ArrayList<>();
                        for (ContactIndexBean.DataEntity.ColloectUserEntity userinfoBean : userinfo) {
                            if ((userinfoBean.getUserid() + "").equals(userId)) {
                                tempList.add(userinfoBean);
                            }
                        }
                        if (tempList.size() > 0) {
                            userinfo.removeAll(tempList);
                        }
                        mAdapter.replaceData(userinfo);
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
//        RequestUtils.getInstance().buildRequest().postContactSearch(base64Data).enqueue(new Callback<ContactSearchBean>() {
//            @Override
//            public void onResponse(Call<ContactSearchBean> call, Response<ContactSearchBean> response) {
//                LogUtils.e("log", "serch");
//                //closeProgressDialog();
//                ContactSearchBean searchBean = response.body();
//                if (searchBean.getCode() == 200) {
//                    listSerch.clear();
//                    listSerch.addAll(searchBean.getData().getUserinfo());
//                    showContactSearchAdapter.notifyDataSetChanged();
//                } else {
//                    ToastUtil.show(searchBean.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ContactSearchBean> call, Throwable t) {
//                //closeProgressDialog();
//                ToastUtil.show("网络错误");
//            }
//        });
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
        String searchContentValue = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(searchContentValue)) {
            mAdapter.replaceData(new ArrayList<ContactIndexBean.DataEntity.ColloectUserEntity>());
        } else {
            postContactSearch(searchContentValue);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
