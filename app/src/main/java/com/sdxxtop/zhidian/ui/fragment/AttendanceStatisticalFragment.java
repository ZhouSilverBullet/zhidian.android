package com.sdxxtop.zhidian.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.AttendanceStatisticalBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ItemDivider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceStatisticalFragment extends BaseFragment {

    @BindView(R.id.image_left)
    View imageLeft;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.image_right)
    View imageRight;
    @BindView(R.id.attendance_statistical_recycler)
    RecyclerView recyclerView;
    private StatisticalAdapter mAdapter;

    public AttendanceStatisticalFragment() {
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new ItemDivider().setDividerColor(0xFFE5E5E5));
        mAdapter = new StatisticalAdapter(R.layout.item_statistical_custom_view);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        Calendar instance = Calendar.getInstance();
        loadDate(instance.get(Calendar.YEAR) + "", (1 + instance.get(Calendar.MONTH)) + "");

        Date time = instance.getTime();
        tvDate.setText(DateUtil.getChineseYearAndMonthDate(time));
    }

    private void loadDate(String year, String month) {
        Params params = new Params();
        params.put("yr", year);
        params.put("mh", month);
        RequestUtils.createRequest().postUserStat(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<AttendanceStatisticalBean>() {
                    @Override
                    public void onSuccess(AttendanceStatisticalBean attendanceStatisticalBean) {
                        AttendanceStatisticalBean.DataBean data = attendanceStatisticalBean.getData();
                        if (data != null) {
                            handleData(data);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        showToast(errorMsg);
                    }
                }));
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
                    loadDate(yearMonthDay[0], yearMonthDay[1]);
                }
            }
        }
    }

    private void handleData(AttendanceStatisticalBean.DataBean data) {
        List<String> stringList = new ArrayList<>();
        String work_day = data.getWork_day();
        String bus_day = data.getBus_day();
        String overtime_day = data.getOvertime();
        String leave_day = data.getLeave_day();
        String late_num = data.getLate_num();
        String early_num = data.getEarly_num();
        String leak_num = data.getLeak_num();
        String absent_day = data.getAbsent_day();
        String out_num = data.getOut_num();
        stringList.add(work_day);
        stringList.add(bus_day);
        stringList.add(overtime_day);
        stringList.add(leave_day);
        stringList.add(late_num);
        stringList.add(early_num);
        stringList.add(leak_num);
        stringList.add(absent_day);
        stringList.add(out_num);

        mAdapter.replaceData(stringList);
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_attendance_statistical;
    }

    class StatisticalAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public StatisticalAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            TextView nameText = helper.getView(R.id.item_statistical_day_name);
            TextView countText = helper.getView(R.id.item_statistical_day_count);
            int position = helper.getAdapterPosition();
            switch (position) {
                case 0:
                    nameText.setText("工作天数");
                    break;
                case 1:
                    nameText.setText("出差天数");
                    break;
                case 2:
                    nameText.setText("加班天数");
                    break;
                case 3:
                    nameText.setText("请假天数");
                    break;
                case 4:
                    nameText.setText("迟到次数");
                    break;
                case 5:
                    nameText.setText("早退次数");
                    break;
                case 6:
                    nameText.setText("漏打卡次数");
                    break;
                case 7:
                    nameText.setText("旷工次数");
                    break;
                case 8:
                    nameText.setText("外勤次数");
                    break;
            }


            if (TextUtils.isEmpty(item)) {
                if (position < 4) {
                    countText.setText("");
                } else {
                    countText.setText("");
                }
            } else {
                countText.setText(item);
            }
        }
    }
}
