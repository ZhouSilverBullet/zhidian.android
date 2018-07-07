package com.sdxxtop.zhidian.ui.fragment;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.AttendanceAverageBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceAverageFragment extends BaseFragment implements OnChartValueSelectedListener {

    @BindView(R.id.image_left)
    View imageLeft;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.image_right)
    View imageRight;
    @BindView(R.id.attendance_fragment_replace)
    FrameLayout replace;
    @BindView(R.id.average_recycler)
    RecyclerView averageRecycler;
    @BindView(R.id.average_weight_view)
    View weightView;
    @BindView(R.id.average_text)
    TextView averageText;
    private AverAdapter adapter;

    public AttendanceAverageFragment() {
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_attendance_average;
    }

    @Override
    protected void initView() {
        super.initView();

    }

    @Override
    protected void initEvent() {
        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = tvDate.getText().toString();
                handleText(DateUtil.getLastMonth(s));
            }
        });

        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = tvDate.getText().toString();
                handleText(DateUtil.getPreMonth(s));
            }
        });
    }

    private void handleText(String value) {

        if (value != null) {
            String[] yearMonthDay = DateUtil.getYearAndMonth(value);
            if (yearMonthDay != null) {
                Calendar instance = Calendar.getInstance();
                int year = instance.get(Calendar.YEAR);
                int month = 1 + instance.get(Calendar.MONTH);

                if (Integer.parseInt(yearMonthDay[0]) <= year) {
                    if (Integer.parseInt(yearMonthDay[0]) == year) {
                        if (Integer.parseInt(yearMonthDay[1]) > month) {
                            return;
                        }
                    }
                    tvDate.setText(value);
                    loadData(yearMonthDay[0], yearMonthDay[1]);
                }
            }
        }
    }

//    private void handleText(String value) {
//
//        if (value != null) {
//            String[] yearMonthDay = DateUtil.getYearAndMonth(value);
//            tvDate.setText(value);
//            if (yearMonthDay != null) {
////                getChildFragmentManager().beginTransaction().
////                        replace(R.id.attendance_fragment_replace,
////                                AttendanceAverage2Fragment.newInstance(yearMonthDay[0], yearMonthDay[1])).commit();
//                loadData(yearMonthDay[0], yearMonthDay[1]);
//            }
//        }
//
//
//    }

    @Override
    protected void initData() {

        Calendar instance = Calendar.getInstance();
//        getChildFragmentManager()
//                .beginTransaction()
//                .replace(R.id.attendance_fragment_replace
//                        , AttendanceAverage2Fragment.newInstance(instance.get(Calendar.YEAR) + ""
//                                , (1 + instance.get(Calendar.MONTH)) + "")).commit();
        Date time = instance.getTime();
        tvDate.setText(DateUtil.getChineseYearAndMonthDate(time));
        averageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AverAdapter(R.layout.item_aver_recycler);
        averageRecycler.setAdapter(adapter);

        loadData(instance.get(Calendar.YEAR) + "", (1 + instance.get(Calendar.MONTH)) + "");
    }

    private void loadData(String year, String month) {
        Params params = new Params();
        params.put("yr", year);
        params.put("mh", month);
        RequestUtils.createRequest().postUserAverager(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<AttendanceAverageBean>() {
            @Override
            public void onSuccess(AttendanceAverageBean attendanceStatisticalBean) {
                AttendanceAverageBean.DataBean data = attendanceStatisticalBean.getData();
                if (data != null) {
                    handleDayAve(data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void handleDayAve(AttendanceAverageBean.DataBean data) {
        List<AttendanceAverageBean.DataBean.DayAveBean> day_ave = data.getDay_ave();
        if (day_ave != null) {
            adapter.replaceData(day_ave);
        }
        float month_ave = data.getMonth_ave();
        weightView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewUtil.dp2px(mContext, 2), month_ave));
        averageText.setText("平均" + month_ave + "小时");
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private class AverAdapter extends BaseQuickAdapter<AttendanceAverageBean.DataBean.DayAveBean, BaseViewHolder> {
        public AverAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, AttendanceAverageBean.DataBean.DayAveBean item) {
            TextView textBg = helper.getView(R.id.item_aver_text_bg);
            TextView textX = helper.getView(R.id.item_aver_text_x);
            TextView textValue = helper.getView(R.id.item_aver_text_value);
            float work_time = item.getWork_time();
            if (work_time > 24) {
                work_time = 24f;
            }
            textBg.setLayoutParams(new LinearLayout.LayoutParams(0, ViewUtil.dp2px(mContext, 30), work_time));
            int position = helper.getAdapterPosition();
            if (position % 2 == 0) {
                textBg.setBackgroundColor(Color.parseColor("#FF3296FA"));
            } else {
                textBg.setBackgroundColor(Color.parseColor("#553296FA"));
            }
            textX.setText(item.getDay());
            textValue.setText(work_time + "小时");
        }
    }

}