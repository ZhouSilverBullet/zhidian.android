package com.sdxxtop.zhidian.ui.fragment;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.AttendanceDetailBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.LeaveUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceDetailFragment extends BaseFragment {

    @BindView(R.id.image_left)
    View imageLeft;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.image_right)
    View imageRight;
    @BindView(R.id.attendance_detail_recycler)
    RecyclerView recyclerView;
    private DetailAdapter mAdapter;


    public AttendanceDetailFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_attendance_detail;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new ItemDivider());
        mAdapter = new DetailAdapter(R.layout.item_attendance_detail_recycler);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        Calendar instance = Calendar.getInstance();
        loadData(instance.get(Calendar.YEAR) + "", (1 + instance.get(Calendar.MONTH)) + "");

        Date time = instance.getTime();
        tvDate.setText(DateUtil.getChineseYearAndMonthDate(time));
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

    private void loadData(String year, String month) {
        Params params = new Params();
        params.put("yr", year);
        params.put("mh", month);
        RequestUtils.createRequest().postUserDetail(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<AttendanceDetailBean>() {
            @Override
            public void onSuccess(AttendanceDetailBean attendanceDetailBean) {
                AttendanceDetailBean.DataBean data = attendanceDetailBean.getData();
                if (data != null) {
                    handleData(data);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }
        }));
    }

    private void handleData(AttendanceDetailBean.DataBean data) {
        mAdapter.replaceData(data.getSign_log());
    }

    class DetailAdapter extends BaseQuickAdapter<AttendanceDetailBean.DataBean.SignLogBean, BaseViewHolder> {
        public DetailAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, AttendanceDetailBean.DataBean.SignLogBean item) {
            TextView detailDate = helper.getView(R.id.item_attendance_detail_date);
            RecyclerView detailRecycler = helper.getView(R.id.item_attendance_detail_recycler);
            TextView detailDec = helper.getView(R.id.item_attendance_detail_dec);

            detailRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
            String sign_log = item.getSign_log();
            if (!TextUtils.isEmpty(sign_log)) {
                String[] split = sign_log.split(",");
                List<String> stringList = Arrays.asList(split);
                detailRecycler.setAdapter(new GridRecAdapter(R.layout.item_attendance_detail_grid_recycler, stringList));
            } else {
                List<String> stringList = new ArrayList<>();
                detailRecycler.setAdapter(new GridRecAdapter(R.layout.item_attendance_detail_grid_recycler, stringList));
            }

            String week = item.getWeek();
            int key = Integer.parseInt(week);
            String weekName = LeaveUtil.getWeekName(key);
            detailDate.setText(item.getDate() + "\t" + weekName);
            detailDec.setText(item.getDesc());
        }
    }

    private class GridRecAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public GridRecAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            TextView view = helper.getView(R.id.item_attendance_detail_text);
            view.setText(item);
        }
    }


}
