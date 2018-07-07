package com.sdxxtop.zhidian.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ApplyListAdapter;
import com.sdxxtop.zhidian.entity.ApplyListBean;
import com.sdxxtop.zhidian.entity.TestBean;
import com.sdxxtop.zhidian.eventbus.OneKeyEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.RaceSelectionWindow;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 17:14
 * 类的描述：
 */
public class ApplyListFragment extends BaseFragment {

    public static final String APPLY_LIST = "apply_list";
    public static final String APPLY_TYPE = "apply_type";
    public static final int MY_APPLY = 100;
    public static final int MY_APPROVAL = 101;
    private String ids = "";

    @BindView(R.id.lv_recycler_view)
    RecyclerView lvApplied;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.one_key_ll)
    LinearLayout oneKeyLl;
    @BindView(R.id.one_key_select)
    TextView oneKeySelectText;
    HashMap<View, Boolean> map = new HashMap<>();

    private String path;
    private int status;
    private ApplyListAdapter mAdapter;
    private HorAdapter horAdapter;
    private RecyclerView horRecyclerView;
    private int showSelect;

    public static ApplyListFragment newInstance(int status, String path, int showSelect /*1表示隐藏，2表示出现*/) {
        Bundle args = new Bundle();
        args.putString("path", path);
        args.putInt("status", status);
        args.putInt("showSelect", showSelect);
        ApplyListFragment fragment = new ApplyListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_list_view;
    }

    @Override
    protected void initVariables() {
        if (getArguments() != null) {
            path = getArguments().getString("path");
            status = getArguments().getInt("status");
            showSelect = getArguments().getInt("showSelect");
        }
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (showSelect == 2) {
            oneKeyLl.setVisibility(View.VISIBLE);
            map.put(oneKeySelectText, false);
        }
//        <android.support.v7.widget.RecyclerView
//        android:id="@+id/examine_screening_recycler"
//        android:layout_width="match_parent"
//        android:layout_height="35dp"
//        android:visibility="gone" />
        horRecyclerView = new RecyclerView(mContext);
        horRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        horAdapter = new HorAdapter(R.layout.item_apply_list_hor_recycler);
        horRecyclerView.setAdapter(horAdapter);
        horRecyclerView.setVisibility(View.GONE);

        lvApplied.setLayoutManager(new LinearLayoutManager(mContext));
        lvApplied.addItemDecoration(new ItemDivider().setDividerColor(0xFFE5E5E5).setDividerLeftOffset(ViewUtil.dp2px(mContext, 65)));
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (mAdapter != null) {
                    loadData(mAdapter.getData().size());
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData(0);
            }
        });
    }


    @Override
    protected void initData() {
        loadData(0);
    }

    private String searchName;

    //用于缓存
    private List<ApplyListBean.DataBean.ApplyBean> tempData = new ArrayList<>();

    public void searchName(String searchName) {
        this.searchName = searchName;
        if (mAdapter != null) {
//            List<ApplyListBean.DataBean.ApplyBean> applyBeans = searchIntFilter(tempData);
            loadData(0, searchName);
//            mAdapter.replaceData(applyBeans);
        }
    }

    private List<ApplyListBean.DataBean.ApplyBean> searchIntFilter(List<ApplyListBean.DataBean.ApplyBean> apply) {
        if (TextUtils.isEmpty(searchName)) {
            return apply;
        }

        if (apply == null || apply.size() == 0) {
            return apply;
        }

        List<ApplyListBean.DataBean.ApplyBean> tempApplyList = new ArrayList<>();
        for (ApplyListBean.DataBean.ApplyBean applyBean : apply) {
            if (!TextUtils.isEmpty(applyBean.getName()) && applyBean.getName().contains(searchName)) {
                tempApplyList.add(applyBean);
            }
        }

        return tempApplyList;
    }

    private void loadData(final int page, String searchName) {
        Params params = new Params();
        if (!TextUtils.isEmpty(ids)) {
            //从零开始page
            params.put("at", ids);
            params.put("sp", 0);
        } else {
            params.put("sp", page);
        }
        if (!TextUtils.isEmpty(searchName)) {
            params.put("nm", searchName);
        }
        params.put("st", status);
        RequestUtils.createRequest().postApplyList(path, params.getData()).enqueue(
                new RequestCallback<>(new IRequestListener<ApplyListBean>() {
                    @Override
                    public void onSuccess(ApplyListBean applyListBean) {
                        ApplyListBean.DataBean data = applyListBean.getData();
                        if (data != null && data.getApply() != null) {
                            List<ApplyListBean.DataBean.ApplyBean> apply = data.getApply();
                            ApplyListBean.DataBean.UserinfoBean userInfo = data.getUserinfo();
                            if (userInfo != null) {
                                for (ApplyListBean.DataBean.ApplyBean applyBean : apply) {
                                    applyBean.setName(userInfo.getName());
                                    applyBean.setImg(userInfo.getImg());
                                }
                            }
                            //如果全选选中了，我回来就给它全部标记为true选中
                            if (showSelect == 2) {
                                boolean isSelector = map.get(oneKeySelectText);
                                if (isSelector) {
                                    for (ApplyListBean.DataBean.ApplyBean applyBean : apply) {
                                        applyBean.setCheck(true);
                                    }
                                }
                            }

                            if (mAdapter == null) {
                                initAdapter(apply);
                            } else {
                                if (page == 0) {
                                    tempData.clear();
                                    tempData.addAll(apply);
//                                    List<ApplyListBean.DataBean.ApplyBean> applyBeans = searchIntFilter(apply);
                                    mAdapter.replaceData(apply);
                                } else {
                                    tempData.addAll(apply);
//                                    List<ApplyListBean.DataBean.ApplyBean> applyBeans = searchIntFilter(apply);
                                    mAdapter.addData(apply);
                                }
                            }
                        }
                        //只要加载我就检查一次
                        checkAllStatus();
                        finishRefreshLayout(page);
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                        finishRefreshLayout(page);
                    }
                }));
    }


    private void initAdapter(List<ApplyListBean.DataBean.ApplyBean> apply) {
        //先把数据源缓存起来
        tempData.addAll(apply);

//        List<ApplyListBean.DataBean.ApplyBean> applyBeans = searchIntFilter(apply);
        mAdapter = new ApplyListAdapter(R.layout.item_apply_list_fragment_recycler);
        lvApplied.setAdapter(mAdapter);
        mAdapter.setPath(path);
        mAdapter.setStatus(status);
        mAdapter.setIsShow(showSelect == 2);
        mAdapter.addData(apply);
        mAdapter.addHeaderView(horRecyclerView);
        ViewGroup.LayoutParams layoutParams = horRecyclerView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewUtil.dp2px(mContext, 40);
        mAdapter.setCheckAllListener(new ApplyListAdapter.CheckAllListener() {
            @Override
            public void onCheckAll() {
                checkAllStatus();
            }
        });

        oneKeySelectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isCheck = map.get(oneKeySelectText);
                if (isCheck) {
                    List<ApplyListBean.DataBean.ApplyBean> data = mAdapter.getData();
                    for (ApplyListBean.DataBean.ApplyBean datum : data) {
                        datum.setCheck(false);
                    }
                } else {
                    List<ApplyListBean.DataBean.ApplyBean> data = mAdapter.getData();
                    for (ApplyListBean.DataBean.ApplyBean datum : data) {
                        datum.setCheck(true);
                    }
                }
                mAdapter.notifyDataSetChanged();
                setCheck(oneKeySelectText, !isCheck);

            }
        });
    }

    //检查是否全部选中，按钮变化
    private void checkAllStatus() {
        if (!map.containsKey(oneKeySelectText)) {
            return;
        }
        setCheck(oneKeySelectText, map.get(oneKeySelectText));
        List<ApplyListBean.DataBean.ApplyBean> data = mAdapter.getData();
        if (data.size() > 0) {
            for (ApplyListBean.DataBean.ApplyBean datum : data) {
                if (!datum.isCheck()) { //有一个没选中就当全部没选中
                    return;
                }
            }
            setCheck(oneKeySelectText, true);
        }
    }

    //状态变化
    private void setCheck(TextView textView, boolean isCheck) {
        map.put(textView, isCheck);
        textView.setBackgroundResource(isCheck ?
                R.mipmap.select : R.mipmap.unselected);
    }

    private void loadData(final int page) {
        loadData(page, "");
    }

    private void finishRefreshLayout(int page) {
        if (page == 0) {
            refreshLayout.finishRefresh(200);
        } else {
            refreshLayout.finishLoadMore();
        }
    }

    private RaceSelectionWindow raceSelectionWindow;

    public void showWindow() {

        if (testBeanList != null) {
            List<TestBean> data = horAdapter.getData();
            if (data.size() > 0) {
                for (TestBean testBean : testBeanList) {
                    testBean.setSelected(false);
                }
                for (int i = 0; i < testBeanList.size(); i++) {
                    TestBean testBean = testBeanList.get(i);
                    for (TestBean datum : data) {
                        if (datum.getString1().equals(testBean.getString1())) {
                            testBean.setSelected(true);
                        }
                    }
                }
            } else {
                testBeanList.get(0).setSelected(true);
                for (int i = 1; i < testBeanList.size(); i++) {
                    TestBean testBean = testBeanList.get(i);
                    testBean.setSelected(false);
                }
            }
        }

        raceSelectionWindow = new RaceSelectionWindow(mContext, testBeanList);
        raceSelectionWindow.setWidth((int) getResources().getDimension(R.dimen.x564));
        raceSelectionWindow.setHeight(getResources().getDisplayMetrics().heightPixels);
        raceSelectionWindow.setFocusable(true);
        raceSelectionWindow.setTouchable(true);
        raceSelectionWindow.setOutsideTouchable(true);
        raceSelectionWindow.setClippingEnabled(false);
        raceSelectionWindow.setBackgroundDrawable(new ColorDrawable());
        raceSelectionWindow.setAnimationStyle(R.style.AnimationRightFade);
//        raceSelectionWindow.showAsDropDown(titleLL, 310, 20, Gravity.BOTTOM);
        raceSelectionWindow.showAtLocation(oneKeyLl, Gravity.RIGHT, 0, 0);

        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        params.alpha = 0.5f;
        mContext.getWindow().setAttributes(params);

        raceSelectionWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
                params.alpha = 1f;
                mContext.getWindow().setAttributes(params);
            }
        });

        raceSelectionWindow.setRaceClickListener(new RaceSelectionWindow.RaceClickListener() {

            @Override
            public void onRace(List<TestBean> beans) {
                if (beans != null) {
                    testBeanList = beans;
                    notifyValue(beans);
                }
            }
        });

    }

    private void notifyValue(List<TestBean> beans) {
        List<TestBean> arrayList = new ArrayList<>();
        //没次选择回来的时候要变成空
        ids = "";
        boolean isLast = false;
        for (int i = 0; i < beans.size(); i++) {
            TestBean bean = beans.get(i);
            if (bean.isSelected()) {
                arrayList.add(bean);
                if (i == beans.size() - 1) {
                    ids = ids + bean.getLeave_type();
                    isLast = true;
                } else {
                    ids = ids + bean.getLeave_type() + ",";
                }
            }
        }

        if (!isLast && !TextUtils.isEmpty(ids)) {
            ids = ids.substring(0, ids.length() - 1);
        }

        horAdapter.replaceData(arrayList);
        List<TestBean> data = horAdapter.getData();
        if (data.size() > 0) {
            if (data.get(0).getLeave_type() == 0) {
                //全部的情况下
                //重置ids
                ids = "";
                horRecyclerView.setVisibility(View.GONE);
                loadData(0, "");
            } else {
                loadData(0, "");
                horRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            loadData(0, "");
            horRecyclerView.setVisibility(View.GONE);
        }
    }

    private List<TestBean> testBeanList;

//    public void onFragmentKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            if (raceSelectionWindow != null && raceSelectionWindow.isShowing()) {
//                raceSelectionWindow.dismiss();
//            }
//        }
//    }

    public String getIds() {
        return ids;
    }

    public void notifyAdapterForOneKey() {
        loadData(0);
    }

    public String getApplyStrings() {
        String value = "";
        if (mAdapter == null) {
            return value;
        }

        List<ApplyListBean.DataBean.ApplyBean> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            ApplyListBean.DataBean.ApplyBean applyBean = data.get(i);
            if (applyBean.isCheck()) {
                value = value + applyBean.getApply_id() + ",";
            }
        }
        if (value.length() > 1) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    /**
     * 审批点击同意或者驳回刷新页面
     *
     * @param event
     */
    @Subscribe
    public void onRefreshOneKeyEvent(OneKeyEvent event) {
        initData();
    }

    private class HorAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> {

        public HorAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final TestBean item) {
            helper.setText(R.id.item_hor_name, item.getString1());
            helper.getView(R.id.item_hor_delete_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData().remove(item);
                    if (getData().size() == 0) {
                        horRecyclerView.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                    notifyValue(getData());
                }
            });
        }
    }

}
