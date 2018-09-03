package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.HomeworkListBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.ItemDivider;

import java.util.List;

import butterknife.BindView;

public class HomeworkListActivity extends BaseActivity {

    @BindView(R.id.home_work_list_smart_layout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.home_work_list_recycler)
    RecyclerView mRecyclerView;

    HomeworkListAdapter mAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_homework_list;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ItemDivider());
    }

    @Override
    protected void initEvent() {
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (mAdapter != null) {
                    loadData(mAdapter.getData().size());
                }
            }

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

    private void loadData(final int page) {
        Params params = new Params();
        params.put("sp", page);
        RequestUtils.createRequest().postUserTaskIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<HomeworkListBean>() {
            @Override
            public void onSuccess(HomeworkListBean homeworkListBean) {
                HomeworkListBean.DataBean data = homeworkListBean.getData();
                if (data != null) {
                    handleData(page, data);
                }
                if (mSmartRefreshLayout != null) {
                    mSmartRefreshLayout.finishLoadMore();
                    mSmartRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
//                showToast(errorMsg);
                if (mSmartRefreshLayout != null) {
                    mSmartRefreshLayout.finishLoadMore();
                    mSmartRefreshLayout.finishRefresh();
                }
            }
        }));
    }

    private void handleData(int page, HomeworkListBean.DataBean data) {
        List<HomeworkListBean.DataBean.TaskBean> task = data.getTask();
        if (task != null) {
            if (mAdapter == null) {
                mAdapter = new HomeworkListAdapter(R.layout.item_homework_list_recycler);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.addData(task);
            } else {
                if (page == 0) {
                    mAdapter.replaceData(task);
                } else {
                    mAdapter.addData(task);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == 100) {
            initData();
        }
    }

    public static void startHomeworkListActivity(Context context) {
        Intent intent = new Intent(context, HomeworkListActivity.class);
        context.startActivity(intent);
    }

    private class HomeworkListAdapter extends BaseQuickAdapter<HomeworkListBean.DataBean.TaskBean, BaseViewHolder> {
        public HomeworkListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeworkListBean.DataBean.TaskBean item) {
            TextView titleText = helper.getView(R.id.item_homework_list_title);
            TextView contentText = helper.getView(R.id.item_homework_list_content);
            TextView timeText = helper.getView(R.id.item_homework_list_time);


            titleText.setText(item.getTitle());
            contentText.setText(item.getContent());
            String weixinShowTime = Date2Util.getWeixinShowTime(item.getAdd_time());
            timeText.setText(weixinShowTime);

            final int task_id = item.getTask_id();
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeworkDetailActivity.startHomeworkDetailActivity((BaseActivity)mContext, "" + task_id);
                }
            });
        }
    }

}
