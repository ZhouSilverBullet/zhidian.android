package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.WorkListBean;
import com.sdxxtop.zhidian.eventbus.MessageCenterEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.widget.SubTitleView;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class MineWorkListActivity extends BaseActivity {

    @BindView(R.id.mine_work_list_title_view)
    SubTitleView mTitleView;
    @BindView(R.id.mine_work_list_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.mine_work_list_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private WorkListAdapter mAdapter;
    private boolean isFirst = false;

    @Override
    protected int getActivityView() {
        return R.layout.activity_mine_work_list;
    }

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void initData() {
        super.initData();
        loadData(0);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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

    private void loadData(final int page) {
        Params params = new Params();
        params.put("sp", page);
        RequestUtils.createRequest().postMsgReport(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<WorkListBean>() {
            @Override
            public void onSuccess(WorkListBean workListBean) {
                WorkListBean.DataBean data = workListBean.getData();
                if (data != null) {
                    if (!isFirst) { //通知主页刷新
                        isFirst = true;
                        EventBus.getDefault().post(new MessageCenterEvent());
                    }
                    List<WorkListBean.DataBean.ReportBean> report = data.getReport();
                    if (report != null) {
                        if (mAdapter == null) {
                            mAdapter = new WorkListAdapter(R.layout.item_mine_work_list_recycler);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.addData(report);
                        } else {
                            if (page == 0) {
                                mAdapter.replaceData(report);
                            } else {
                                mAdapter.addData(report);
                            }
                        }
                    }
                }

                if (mRefreshLayout != null) {
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
                if (mRefreshLayout != null) {
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.finishLoadMore();
                }
            }
        }));
    }

    class WorkListAdapter extends BaseQuickAdapter<WorkListBean.DataBean.ReportBean, BaseViewHolder> {
        public WorkListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final WorkListBean.DataBean.ReportBean item) {

            TextView titleText = helper.getView(R.id.item_mine_work_list_recycler_title);
            final View redView = helper.getView(R.id.item_mine_work_list_recycler_red_view);
            TextView summaryText = helper.getView(R.id.item_mine_work_list_recycler_summary);
            TextView planText = helper.getView(R.id.item_mine_work_list_recycler_plan);
            TextView problemText = helper.getView(R.id.item_mine_work_list_recycler_problem);
            TextView timeText = helper.getView(R.id.item_mine_work_list_recycler_time);

            final int reportType = item.getReport_type();
            switch (reportType) {
                case 1:
                    titleText.setText(item.getName() + "的日报");
                    summaryText.setText("1.今日工作总结： " + item.getSummary());
                    planText.setText("2.明日工作计划： " + item.getPlan());
                    break;
                case 2:
                    titleText.setText(item.getName() + "的周报");
                    summaryText.setText("1.本周工作总结： " + item.getSummary());
                    planText.setText("2.下周工作计划： " + item.getPlan());
                    break;
                default:
                    titleText.setText(item.getName() + "的月报");
                    summaryText.setText("1.本月工作总结： " + item.getSummary());
                    planText.setText("2.下月工作计划： " + item.getPlan());
                    break;
            }

            String problem = item.getProblem();
            if (TextUtils.isEmpty(problem)) {
                problemText.setVisibility(View.GONE);
            } else {
                problemText.setVisibility(View.VISIBLE);
                problemText.setText("3.工作中的问题： " + problem);
            }

            titleText.requestLayout();
            timeText.setText(getShowTime(item.getAdd_time()));

            if (item.getIs_read() == 2) {
                redView.setVisibility(View.VISIBLE);
            } else {
                redView.setVisibility(View.GONE);
            }

            final int reportId = item.getReport_id();
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setIs_read(1);
                    redView.setVisibility(View.GONE);
                    MineWorkDetailActivity.startWorkDetailActivity(mContext, reportType, reportId);
                }
            });
        }
    }

    public static String getShowTime(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(date);
            Calendar nowDay = Calendar.getInstance();

            int month = currentDate.get(Calendar.MONTH) + 1;
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            int nowMonth = nowDay.get(Calendar.MONTH) + 1;
            int nowsDay = nowDay.get(Calendar.DAY_OF_MONTH);

            String format = sdf.format(date);
            if (month != nowMonth) {
                return format;
            }

            if (day != nowsDay) {
                if (nowsDay - day == 1) {
                    return "昨天";
                }
                return format;
            }

            int hour = currentDate.get(Calendar.HOUR_OF_DAY);
            int nowHour = nowDay.get(Calendar.HOUR_OF_DAY);
            int minute = currentDate.get(Calendar.MINUTE);
            int nowMinute = nowDay.get(Calendar.MINUTE);
            if (nowHour == hour && nowMinute - minute <= 1) {
                return "刚刚";
            }

            String hourAnd = time.split(" ")[1];
            String substring = hourAnd.substring(0, hourAnd.lastIndexOf(":"));
            return substring;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void startMineWorkListActivity(Context context) {
        Intent intent = new Intent(context, MineWorkListActivity.class);
        context.startActivity(intent);
    }
}
