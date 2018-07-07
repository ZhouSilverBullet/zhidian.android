package com.sdxxtop.zhidian.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.AttendanceAverageBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceAverage2Fragment extends BaseFragment implements OnChartValueSelectedListener {

    @BindView(R.id.chart1)
    HorizontalBarChart mChart;
    private ArrayList<String> label;
    private AttendanceAverageBean.DataBean dataAvg;
    private String year;
    private String month;

    public AttendanceAverage2Fragment() {
    }

    public static AttendanceAverage2Fragment newInstance(String year, String month) {

        Bundle args = new Bundle();
        args.putString("year", year);
        args.putString("month", month);
        AttendanceAverage2Fragment fragment = new AttendanceAverage2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_attendance2_average;
    }


    @Override
    protected void initView() {
        //设置相关属性
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(31);
        mChart.setScaleEnabled(false);
        mChart.setHorizontalScrollBarEnabled(true);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        //x轴
        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawLabels(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(0f);

        //y轴
        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setEnabled(false);
        yl.setAxisMinimum(0f);

        //y轴
        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);
        yr.setEnabled(false);

        //设置数据
        mChart.setFitBars(true);
        mChart.animateY(2500);

        Legend l = mChart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setFormSize(8f);
//        l.setXEntrySpace(4f);
//        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            year = getArguments().getString("year");
            month = getArguments().getString("month");
        }

        loadData(year + "", month + "");
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

    private void handleDayAve(AttendanceAverageBean.DataBean dataAvg) {
        this.dataAvg = dataAvg;
        final List<AttendanceAverageBean.DataBean.DayAveBean> day_ave = dataAvg.getDay_ave();
        final ArrayList<String> xVals = new ArrayList<>();//X轴数据
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        float barWidth = 5f;
        float spaceForBar = 5f;
        label = new ArrayList<>();

        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < day_ave.size(); i++) {
            AttendanceAverageBean.DataBean.DayAveBean dayAveBean = day_ave.get(i);
            String day = dayAveBean.getDay();
            float work_time = dayAveBean.getWork_time();
            Integer integer = Integer.valueOf(day);
            label.add(work_time + "小时");
            xVals.add(integer + "");
            BarEntry barEntry = new BarEntry(i * spaceForBar, dayAveBean.getWork_time());
            yVals1.add(barEntry);
            if (i % 2 == 0) {
                integers.add(0xFF3296FA);
            } else {
                integers.add(0x553296FA);
            }
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1.setColors(integers);
            set1.setValueFormatter(new CustomFormatter());
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

            LimitLine yLimitLine = new LimitLine(dataAvg.getMonth_ave(), "平均" + dataAvg.getMonth_ave() + "小时");
            yLimitLine.setLineColor(Color.GREEN);
            yLimitLine.setTextColor(0xFF3296FA);
            yLimitLine.enableDashedLine(5f, 2f, 0f);
            // 获得左侧侧坐标轴
            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            leftAxis.addLimitLine(yLimitLine);

        } else {
            set1.setColors(integers);
            set1.setValueFormatter(new CustomFormatter());
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            mChart.setData(data);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setLabelCount(xVals.size());
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
//                    for (int i = 0; i < day_ave.size(); i++) {
//                        AttendanceAverageBean.DataBean.DayAveBean dayAveBean = day_ave.get(i);
//                        String day = dayAveBean.getDay();
//                        float work_time = dayAveBean.getWork_time();
//                        float v = Float.parseFloat(day);
//                        if (v == value) {
//                            return xVals.get(i);
//                        }
//                    }
                    return xVals.get((int) value % xVals.size());
                }
            });
            xAxis.setAxisLineWidth(barWidth);
            // 设置x轴的LimitLine
            LimitLine yLimitLine = new LimitLine(dataAvg.getMonth_ave(), "平均" + dataAvg.getMonth_ave() + "小时");
            yLimitLine.setLineColor(Color.GREEN);
            yLimitLine.setTextColor(Color.GREEN);
            yLimitLine.setTextSize(14f);
            yLimitLine.enableDashedLine(15f, 2f, 10f);
            // 获得左侧侧坐标轴
            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            leftAxis.addLimitLine(yLimitLine);
        }

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    private class CustomFormatter implements IValueFormatter {

        public CustomFormatter() {
        }

        // data
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return value + "小时";
        }
    }

    private class XCustomFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return null;
        }
    }

}