package com.sdxxtop.zhidian.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.sdxxtop.zhidian.AppSession;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ISelector;
import com.sdxxtop.zhidian.adapter.ParentListAdapter;
import com.sdxxtop.zhidian.entity.ParentListBean;
import com.sdxxtop.zhidian.entity.ParentSelectBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.BottomSelectorView;
import com.sdxxtop.zhidian.widget.ContactNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ParentListActivity extends BaseActivity {

    public static final String SELECT_TYPE = "select_type"; //选择的类型
    public static final String SELECT_STATUS = "select_status"; //选择的状态

    public static final String SELECT_SET_DATA = "select_set_data"; //选择的类型后的数据

    public static final int TYPE_MULTI_SELECT = 10; //多选
    public static final int TYPE_SINGLE_SELECT = 11; //单选
    public static final int STATUS_SELECT_CLASS = 50; //只进行选择班级

    public static final int CUSTOMER_RESULT_OK = 99; //成功选择

    @BindView(R.id.parent_list_navigation_view)
    ContactNavigationView mNavigationView;
    @BindView(R.id.parent_list_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.parent_list_bottom_view)
    BottomSelectorView bottomSelectorView;
    @BindView(R.id.parent_list_all_background)
    RelativeLayout allRelativeLayout;
    @BindView(R.id.parent_list_cb_check_all)
    CheckBox checkBox;
    private ParentListAdapter mAdapter;
    private int selectType;
    private int selectStatus;
    private String skipClassName;

    @Override
    protected int getActivityView() {
        return R.layout.activity_parent_list;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            selectType = getIntent().getIntExtra(SELECT_TYPE, -1);
            selectStatus = getIntent().getIntExtra(SELECT_STATUS, -1);
            skipClassName = getIntent().getStringExtra("class_name");
            if (selectType == TYPE_MULTI_SELECT || selectType == TYPE_SINGLE_SELECT) {
                bottomSelectorView.setVisibility(View.VISIBLE);
            } else {
                bottomSelectorView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initView() {
        mAdapter = new ParentListAdapter(R.layout.item_parent_list_recycler);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.view_empty, null);
//        mAdapter.setEmptyView(view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new ItemDivider().setDividerLeftOffset(ViewUtil.dp2px(mContext, 66)));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setBottomSelectorView(bottomSelectorView);
        mAdapter.setSelectType(selectType);
        mAdapter.setSelectStatus(selectStatus);
    }

    @Override
    protected void initEvent() {
        mAdapter.setSelectorClickListener(new ParentListAdapter.SelectorClickListener() {
            @Override
            public void onSelectorClick(int position) {
                if (mAdapter != null) {
                    ParentListBean.DataBeanX.DataBean dataBean = mAdapter.getData().get(position);
                    if (dataBean.isPart()) {
                        loadData(mNavigationView.getCount() + 1, dataBean.getType_id() + "");
                    } else {
//                        ParentDetailActivity.startParentDetailActivity(mContext);
                        ContactDetailActivity.startContactDetailActivityParent(mContext,
                                dataBean.getUserid() + "", dataBean.getStudent_id() + "");
                    }
                }
            }
        });

        mNavigationView.setNavigationItemClickListener(new ContactNavigationView.NavigationItemClickListener() {
            @Override
            public void click(ParentListBean.DataBeanX.NavBean bean) {
                loadData(bean.getType(), bean.getType_id() + "");
            }
        });

        mAdapter.setCheckListener(new ISelector.CheckListener() {
            @Override
            public void onCheck() {
                boolean isCheck = mAdapter.iteratorAllValue();
                checkBox.setChecked(isCheck);
            }
        });

        allRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = checkBox.isChecked();
                boolean toCheck = !checked;

                mAdapter.checkAll(toCheck);

                checkBox.setChecked(toCheck);
            }
        });

        bottomSelectorView.getSubmitBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        ArrayList<ParentSelectBean> cacheSelectSet = mAdapter.getCacheSelectSet();
        if (cacheSelectSet.size() == 0) {
            if (!TextUtils.isEmpty(skipClassName)) {
                if (skipClassName.equals("studentAttendance")) {
                    showToast("选择不能为空");
                } else if(skipClassName.equals("courseUpdate")) {
                    showToast("请选择班级");
                }
            } else {
                showToast("选择不能为空");
            }
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(SELECT_SET_DATA, cacheSelectSet);
        setResult(ParentListActivity.CUSTOMER_RESULT_OK, intent);
        finish();
    }

    @Override
    protected void initData() {
        super.initData();
        loadData(1, AppSession.getInstance().getCompanyId());
    }

    /**
     * @param type   层级关系
     * @param typeId 查询类型对应的id比
     */
    private void loadData(final int type, final String typeId) {
        Params params = new Params();
        params.put("ti", typeId);
        params.put("tp", type);
        showProgressDialog("");
        RequestUtils.createRequest().postContactParent(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ParentListBean>() {
            @Override
            public void onSuccess(ParentListBean parentListBean) {
                ParentListBean.DataBeanX dataX = parentListBean.getData();
                if (dataX != null) {
                    boolean parent = dataX.isParent();
                    List<ParentListBean.DataBeanX.NavBean> navBeanList = dataX.getNav();
                    List<ParentListBean.DataBeanX.DataBean> data = dataX.getData();
                    if (navBeanList != null) {
                        mNavigationView.replaceData(navBeanList);
                    }
                    if (data != null) {
                        mAdapter.setIsParent(parent);
                        mAdapter.setNavigationViewList(mNavigationView.getAdapterData());
                        mAdapter.replaceData(data);
                    }
                }

                if (mNavigationView.getCount() >= 3) {
                    if (selectType == TYPE_MULTI_SELECT || selectType == TYPE_SINGLE_SELECT) {
                        if (selectType == TYPE_MULTI_SELECT) {
                            allRelativeLayout.setVisibility(View.VISIBLE);
                        } else {
                            allRelativeLayout.setVisibility(View.GONE);
                        }
                        if (selectType == TYPE_MULTI_SELECT) {
                            boolean isCheck = mAdapter.iteratorAllValue();
                            checkBox.setChecked(isCheck);
                        }
                    }
                } else {
                    allRelativeLayout.setVisibility(View.GONE);
                }

                closeProgressDialog();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    public static void startParentListActivity(Context context) {
        Intent intent = new Intent(context, ParentListActivity.class);
        context.startActivity(intent);
    }

    public static void startParentListActivity(Activity context, int select_type, int requestCode) {
        Intent intent = new Intent(context, ParentListActivity.class);
        intent.putExtra(SELECT_TYPE, select_type);
        context.startActivityForResult(intent, requestCode);
    }

    //这个是CourseUpdateActivity启动过来的
    public static void startParentListActivity(Activity context, int select_type, int select_status, String name, int requestCode) {
        Intent intent = new Intent(context, ParentListActivity.class);
        intent.putExtra(SELECT_TYPE, select_type);
        intent.putExtra(SELECT_STATUS, select_status);
        intent.putExtra("class_name", name);
        context.startActivityForResult(intent, requestCode);
    }
}
