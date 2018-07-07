package com.sdxxtop.zhidian.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.sdxxtop.zhidian.entity.ReportIndexBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.MineWorkActivity;
import com.sdxxtop.zhidian.ui.activity.MineWorkDetailActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineWorkFragment extends BaseFragment {

    @BindView(R.id.mine_work_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.mine_work_smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    private int type;
    private WorkAdapter mAdapter;

    public static MineWorkFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        MineWorkFragment fragment = new MineWorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_mine_work;
    }

    @Override
    protected void initVariables() {
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new ItemDivider().setLastLineNotDraw(true)
                .setDividerLeftOffset(ViewUtil.dp2px(mContext, 70))
                .setDividerRightOffset(ViewUtil.dp2px(mContext, 10))
        );

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
        super.initData();
        //初始化加载
        loadData(0);
    }

    private void loadData(final int page) {
        Params params = new Params();
        params.put("tp", type);
        params.put("sp", page);
        RequestUtils.createRequest().postReportIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<ReportIndexBean>() {
            @Override
            public void onSuccess(ReportIndexBean reportIndexBean) {
                ReportIndexBean.DataBean data = reportIndexBean.getData();
                if (data != null) {
                    List<ReportIndexBean.DataBean.ReportBean> report = data.getReport();
                    if (report != null) {
                        if (mAdapter == null) {
                            mAdapter = new WorkAdapter(R.layout.item_mine_work_recycler);
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

                if (mSmartRefreshLayout != null) {
                    mSmartRefreshLayout.finishLoadMore();
                    mSmartRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);

                if (mSmartRefreshLayout != null) {
                    mSmartRefreshLayout.finishLoadMore();
                    mSmartRefreshLayout.finishRefresh();
                }
            }
        }));
    }


    class WorkAdapter extends BaseQuickAdapter<ReportIndexBean.DataBean.ReportBean, BaseViewHolder> {
        public WorkAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, ReportIndexBean.DataBean.ReportBean item) {
            CircleImageView imgView = helper.getView(R.id.item_mine_work_recycler_img);
            TextView smallText = helper.getView(R.id.item_mine_work_recycler_small_text);
            TextView contentText = helper.getView(R.id.item_mine_work_recycler_content);
            TextView typeTimeText = helper.getView(R.id.item_mine_work_recycler_type_time);
            TextView rightText = helper.getView(R.id.item_mine_work_recycler_right);
            View redView = helper.getView(R.id.item_mine_work_recycler_red_view);

            int reportType = item.getReport_type();

            if (item.getIs_read() == 2) {
                redView.setVisibility(View.VISIBLE);
            } else {
                redView.setVisibility(View.GONE);
            }

            if (type == 1) {
                switch (reportType) {
                    case 1: //日报
                        ViewUtil.setColorItemView("#3296FA", "日", smallText, imgView);
                        break;
                    case 2: //周报
                        ViewUtil.setColorItemView("#FCBA28", "周", smallText, imgView);
                        break;
                    default: //月报
                        ViewUtil.setColorItemView("#FC716C", "月", smallText, imgView);
                        break;
                }
            } else {
                ViewUtil.setColorItemView(item.getImg(), item.getName(), smallText, imgView);
            }

            Calendar calendar = Calendar.getInstance();
            switch (reportType) {
                case 1: //日报
                    String reportDate = item.getReport_date();
                    Date reportTimeDate = new Date(DateUtil.convertTimeToLong(reportDate, "yyyy-MM-dd"));
                    calendar.setTime(reportTimeDate);
                    String day = getDay(calendar);
                    String month = getMonth(calendar);

                    typeTimeText.setText("日报");
                    if (type == 1) {
                        contentText.setText(month + "." + day);
                    } else {
                        contentText.setText(item.getName() + month + "." + day);
                    }
                    break;
                case 2: //周报
                    String startDate = item.getStart_date();
                    String endDate = item.getEnd_date();
                    Date startTimeDate = new Date(DateUtil.convertTimeToLong(startDate, "yyyy-MM-dd"));
                    Date endTimeDate = new Date(DateUtil.convertTimeToLong(endDate, "yyyy-MM-dd"));
                    calendar.setTime(startTimeDate);
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(endTimeDate);

                    String showDate = showDate(calendar, endCalendar);

                    typeTimeText.setText("周报");
                    if (type == 1) {
                        contentText.setText(showDate);
                    } else {
                        contentText.setText(item.getName() + showDate);
                    }
                    break;
                default: //月报
                    String reportMonth = item.getReport_month();
                    Date reportMonthTimeDate = new Date(DateUtil.convertTimeToLong(reportMonth, "yyyy-MM"));
                    calendar.setTime(reportMonthTimeDate);
                    int monthMoth = calendar.get(Calendar.MONTH) + 1;
                    String stringMonth;
                    if (monthMoth < 10) {
                        stringMonth = "0" + monthMoth;
                    } else {
                        stringMonth = "" + monthMoth;
                    }

                    typeTimeText.setText("月报");
                    if (type == 1) {
                        contentText.setText(stringMonth + "月");
                    } else {
                        contentText.setText(item.getName() + stringMonth + "月");
                    }
                    break;
            }

            //weight导致textview不进行重新绘制bug
            contentText.requestLayout();
            rightText.setText(getShowTime(item.getAdd_time()));

            final int reportId = item.getReport_id();
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int workType;
                    switch (type) {
                        case 2:
                            workType = MineWorkActivity.TYPE_READ;
                            break;
                        case 3:
                            workType = MineWorkActivity.TYPE_COPY;
                            break;
                        default:
                            workType = MineWorkActivity.TYPE_SEND;
                            break;
                    }
                    MineWorkDetailActivity.startWorkDetailActivity(mContext, workType, reportId);
                }
            });
        }
    }

    private String showDate(Calendar startCalendar, Calendar endCalendar) {
        String startDay = getDay(startCalendar);
        String startMonth = getMonth(startCalendar);
        String endDay = getDay(endCalendar);
        String endMonth = getMonth(endCalendar);

        return startMonth + "." + startDay + "-" + endMonth + "." + endDay;
    }

    private String getDay(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            return "0" + day;
        } else {
            return "" + day;
        }
    }

    private String getMonth(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            return "0" + month;
        } else {
            return "" + month;
        }
    }

    public static String getShowTime(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(date);
            Calendar nowDay = Calendar.getInstance();

            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH) + 1; // 0-based!
            String stringMonth;
            if (month < 10) {
                stringMonth = "0" + month;
            } else {
                stringMonth = "" + month;
            }

            int day = currentDate.get(Calendar.DAY_OF_MONTH);
            String stringDay;
            if (day < 10) {
                stringDay = "0" + day;
            } else {
                stringDay = "" + day;
            }

            int nowYear = nowDay.get(Calendar.YEAR);
            int nowMonth = nowDay.get(Calendar.MONTH) + 1; // 0-based!
            int nowsDay = nowDay.get(Calendar.DAY_OF_MONTH);

            if (year != nowYear) {
                return year + "." + stringMonth + "." + stringDay;
            }

            if (month != nowMonth) {
                return stringMonth + "." + stringDay;
            }

            if (day != nowsDay) {
                if (nowsDay - day == 1) {
                    return "昨天";
                }

                return stringMonth + "." + stringDay;
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
}

