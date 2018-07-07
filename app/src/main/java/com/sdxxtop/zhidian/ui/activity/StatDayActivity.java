package com.sdxxtop.zhidian.ui.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.StatPartSignBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.StatSelectionDateWindow;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ColorTextUtil;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DatePicker;

public class StatDayActivity extends BaseActivity {

    @BindView(R.id.statistical_title_view)
    SubTitleView titleView;
    @BindView(R.id.statistical_day_title)
    TextView dayTitle;
    @BindView(R.id.statistical_day_selector)
    TextView daySelectorText;
    @BindView(R.id.statistical_up_day)
    TextView upDayText;
    @BindView(R.id.statistical_down_day)
    TextView downDayText;
    @BindView(R.id.statistical_day_text)
    TextView todayText;
    protected int[] colorList = new int[]{0xff049bfc, 0xfffb7344, 0xffa5c0dc,
            0xfff9b29c, 0xfff68e6b, 0xff5a7062, 0xff3b3d40,
            0xff939292, 0xffc9d0d6, 0xff807c7c};
//0xffdddbda
//0xff56b8f9
//0xffebeadb
//0xfff6bfac
//0xfffad4c5
//0xfffad4c5
//0xff80726f
//0xfffcf4f4
//0xffe2e8eb
//0xffeceeef

    public static final String STAT_LEAVE_TYPE = "type";
    public static final String STAT_DAY_TYPE = "day_type";

    public static final int DAY_TYPE = 10; //今日
    public static final int MONTH_TYPE = 20; //月


    private int type;

    @BindView(R.id.statistical_pie_chart)
    PieChart mPieChart;
    @BindView(R.id.statistical_pie_no_data_text)
    TextView noDataText;
    private String textValue;
    private int dayType;
    private StatSelectionDateWindow selectionDateWindow;
    private String chineseSelectorDate;
    private DatePicker datePicker;
    //    private DatePicker datePicker;

    @Override
    protected int getActivityView() {
        return R.layout.activity_stat_day;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            type = getIntent().getIntExtra(StatDayActivity.STAT_LEAVE_TYPE, -1);
            dayType = getIntent().getIntExtra(StatDayActivity.STAT_DAY_TYPE, -1);
        }
    }

    @Override
    protected void initView() {
        super.initView();

        //根据type来进行区分一些操作
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        textValue = getTextValue();
        setPieChart();
        titleView.setTitleValue(ColorTextUtil.getDepartmentValue(type).textValue);

        if (dayType == DAY_TYPE) { //日的
            daySelectorText.setText("选择天");
            upDayText.setText("上一天");
            downDayText.setText("下一天");
            chineseSelectorDate = DateUtil.getChineseSelector0Date(from);
            loadData(type, DateUtil.getSelectorDate(from));
        } else { //月的
            daySelectorText.setText("选择月");
            upDayText.setText("上一月");
            downDayText.setText("下一月");
            chineseSelectorDate = DateUtil.getChineseSelector1Date(from);
            loadData(type, DateUtil.getSelector0Date(from));
        }

        dayTitle.setText(chineseSelectorDate + secondTitle());
    }

    @Override
    protected void initEvent() {
        daySelectorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayType == DAY_TYPE) { //日
                    showSelectDateWindow();
                } else { //月
                    showSelectorDateYear();
                }
            }
        });


        downDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isToday()) {
//                    showToast("没有更早的内容");
                    return;
                }

                if (dayType == DAY_TYPE) { //日
                    upDown(1);
                } else { //月
                    String preMonth = DateUtil.getPreMonth(chineseSelectorDate);
                    chineseSelectorDate = preMonth;
                    String upOrDownMonth = Date2Util.getUpOrDownMonth(chineseSelectorDate);
                    dayTitle.setText(chineseSelectorDate + secondTitle());
                    loadData(type, upOrDownMonth);
                }
            }
        });

        //上一天
        upDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayType == DAY_TYPE) { //日
                    upDown(-1);
                } else { //月
                    String preMonth = DateUtil.getLastMonth(chineseSelectorDate);
                    chineseSelectorDate = preMonth;
                    String upOrDownMonth = Date2Util.getUpOrDownMonth(chineseSelectorDate);
                    dayTitle.setText(chineseSelectorDate + secondTitle());
                    loadData(type, upOrDownMonth);
                }
            }
        });

        todayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                CalendarDay from = CalendarDay.from(instance);
                if (dayType == DAY_TYPE) { //日的
                    chineseSelectorDate = DateUtil.getChineseSelector0Date(from);
                    loadData(type, DateUtil.getSelectorDate(from));
                } else { //月的
                    chineseSelectorDate = DateUtil.getChineseSelector1Date(from);
                    loadData(type, DateUtil.getSelector0Date(from));
                }
                dayTitle.setText(chineseSelectorDate + secondTitle());
            }
        });
    }

    private void todayVisibility() {
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        String dayChineseSelectorDate = "";
        if (dayType == DAY_TYPE) { //日的
            dayChineseSelectorDate = DateUtil.getChineseSelector0Date(from);
        } else { //月的
            dayChineseSelectorDate = DateUtil.getChineseSelector1Date(from);
        }

        if (!dayChineseSelectorDate.equals(chineseSelectorDate)) {
            todayText.setVisibility(View.VISIBLE);
            downDayText.setTextColor(getResources().getColor(R.color.blue));
        } else {
            todayText.setVisibility(View.GONE);
            downDayText.setTextColor(getResources().getColor(R.color.texthintcolor));
        }
    }


    /**
     * 判断是否是今天，然后判断下一天的颜色值和点击
     * 这个把月也判断了
     *
     * @return
     */
    public boolean isToday() {
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        String dayChineseSelectorDate = "";
        if (dayType == DAY_TYPE) { //日的
            dayChineseSelectorDate = DateUtil.getChineseSelector0Date(from);
        } else { //月的
            dayChineseSelectorDate = DateUtil.getChineseSelector1Date(from);
        }

        if (!dayChineseSelectorDate.equals(chineseSelectorDate)) {
            return false;
        } else {
            return true;
        }
    }

    private void showSelectorDateYear() {

        if (datePicker == null) {
            datePicker = new DatePicker(this, DatePicker.YEAR_MONTH);
            Calendar instance = Calendar.getInstance();
            CalendarDay from = CalendarDay.from(instance);
            datePicker.setRangeEnd(from.getYear(), from.getMonth() + 1);
            datePicker.setTextSize(20);
            datePicker.setSelectedItem(from.getYear(), from.getMonth() + 1);
            datePicker.setTitleText("请选择");
            datePicker.setSubmitTextColor(getResources().getColor(R.color.lan));
            datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                @Override
                public void onDatePicked(String year, String month) {
                    chineseSelectorDate = year + "年" + month + "月";
                    dayTitle.setText(chineseSelectorDate + secondTitle());
                    loadData(type, year + "-" + month);
                }
            });
        }

        datePicker.show();

//        if (datePicker == null) {
//            datePicker = new DatePicker(StatDayActivity.this, DatePicker.YEAR_MONTH);
//            Calendar instance = Calendar.getInstance();
//            CalendarDay from = CalendarDay.from(instance);
//            datePicker.setRangeEnd(from.getYear(), 12);
//            datePicker.setWheelModeEnable(false);
//            datePicker.setTextSize(20);
//            datePicker.setSelectedItem(from.getYear(), from.getMonth() + 1);
//            datePicker.setTitleText("请选择");
//            datePicker.setLabel("年", "月", "日");
//            datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
//                @Override
//                public void onDatePicked(String year, String month) {
//                    chineseSelectorDate = year + "年" + month + "月";
//                    dayTitle.setText(chineseSelectorDate + secondTitle());
//                    loadData(type, year + "-" + month);
//                }
//            });
//        }
//
//        datePicker.show();

    }

    private void upDown(int day) {
        String[] upOrDownDay = Date2Util.getUpOrDownDay(chineseSelectorDate, day);
        if (upOrDownDay != null) {
            String showDate = upOrDownDay[0];
            String loadDate = upOrDownDay[1];
            chineseSelectorDate = showDate;
            dayTitle.setText(showDate + secondTitle());
            loadData(type, loadDate);
        }
    }


    private void setPieChart() {

        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(40.f, 0.f, 40.f, 0.f);
        mPieChart.setNoDataText("暂无数据");

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText("");

        mPieChart.setDrawHoleEnabled(false);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setDrawCenterText(false);
        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

//        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true); //是否自动换行
        l.setDrawInside(false);
        l.setXEntrySpace(5f);
        l.setYEntrySpace(2f);
        l.setYOffset(0f);

        // 输入标签样式
        mPieChart.setEntryLabelColor(0xff333333);
        mPieChart.setEntryLabelTextSize(12f);
    }

    @Override
    protected void initData() {
        super.initData();

    }

    public void loadData(int type, String ddAndMd) {

        todayVisibility();

        Params params = new Params();
        if (dayType == DAY_TYPE) {
            params.put("tt", 1); //1:今日 2:月份
            params.put("dd", ddAndMd); //日参数
        } else {
            params.put("tt", 2); //1:今日 2:月份
            params.put("md", ddAndMd); //月参数
        }
        params.put("si", type);
        if (type == 4 || type == 8 || type == 10) {  // 4 8 10请求这个接口
            RequestUtils.createRequest().postStatPartApply(params.getData())
                    .enqueue(new RequestCallback<>(new IRequestListener<StatPartSignBean>() {
                        @Override
                        public void onSuccess(StatPartSignBean contactPartBean) {
                            StatPartSignBean.DataBean data = contactPartBean.getData();
                            if (data != null) {
                                handleData(data);
                            }
                        }

                        @Override
                        public void onFailure(int code, String errorMsg) {
                            showToast(errorMsg);
                        }
                    }));
        } else {
            RequestUtils.createRequest().postStatPartSign(params.getData())
                    .enqueue(new RequestCallback<>(new IRequestListener<StatPartSignBean>() {
                        @Override
                        public void onSuccess(StatPartSignBean contactPartBean) {
                            StatPartSignBean.DataBean data = contactPartBean.getData();
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

    }


    private void handleData(StatPartSignBean.DataBean data) {
        List<StatPartSignBean.DataBean.StatBean> stat = data.getStat();
        if (stat != null) {
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            for (StatPartSignBean.DataBean.StatBean statBean : stat) {
                float num = statBean.getNum();
                entries.add(new PieEntry(num, statBean.getPart_name()));
            }
            setData(entries);
        }
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f); //设置圆饼图的距离
        dataSet.setSelectionShift(5f);//设置饼状Item被选中时变化的距离

        dataSet.setColors(colorList);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(0.5f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new CustomFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setDrawValues(true);
        mPieChart.setData(data);
        mPieChart.setDrawEntryLabels(false);
        mPieChart.highlightValues(null);

        //刷新
        mPieChart.invalidate();
        mPieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        mPieChart.setNoDataText("");

        if (entries == null || entries.size() == 0) {
            noDataText.setVisibility(View.VISIBLE);
            noDataText.setText(getNoDataValue());
        } else {
            noDataText.setVisibility(View.GONE);
        }
    }

    private class CustomFormatter implements IValueFormatter {

        public CustomFormatter() {
        }

        // data
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            PieEntry pieEntry = (PieEntry) entry;
            String label = pieEntry.getLabel();
            float y = pieEntry.getY();
            if (type == 7) { //平均工时||月加班图
                return label + y + "小时";
            }

            if (type == 8) {
                return label + y + "天";
            }

            if (type == 2 || type == 4 || type == 6) {
                return label + (int) y + "人";
            }

            return label + (int) y + "次";
        }

    }

    private float getValue(float value) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
        //   b.setScale(2,  BigDecimal.ROUND_HALF_UP)
    }

    private void showSelectDateWindow() {
        if (selectionDateWindow == null) {
            selectionDateWindow = new StatSelectionDateWindow(this, false, true);
            int weight = getResources().getDisplayMetrics().widthPixels;
//        int height = ViewUtil.dp2px(mContext, 420);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            selectionDateWindow.setWidth(weight);
            selectionDateWindow.setHeight(height);
            selectionDateWindow.setFocusable(true);
            selectionDateWindow.setTouchable(true);
            selectionDateWindow.setOutsideTouchable(true);
            selectionDateWindow.setAnimationStyle(R.style.AnimationRightBottom);

            selectionDateWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().setAttributes(lp);
                }
            });

            selectionDateWindow.setSelectorDateListener(new StatSelectionDateWindow.SelectorDateListener() {
                @Override
                public void onSelector(String date, CalendarDay calendarDay) {
                    dayTitle.setText(getTitleDate(date) + secondTitle());
                    loadData(type, date);
                }
            });
        }

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        selectionDateWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);
    }

    private String getTextValue() {
        if (TextUtils.isEmpty(textValue)) {
            textValue = ColorTextUtil.getDepartmentValue(type).textValue;
            if (type < 4) {
                textValue = textValue + "图";
                textValue = textValue.substring(1, textValue.length());
            }
        }
        if (type >= 4) {
            textValue = "公司" + textValue;
        }
        return textValue;
    }

    //无数据的时候使用
    private String getNoDataValue() {
        String textValue = "";
        switch (type) {
            case 1:
                textValue = "今日无迟到数据";
                break;
            case 2:
                textValue = "今日无旷工数据";
                break;
            case 3:
                textValue = "今日无漏打卡数据";
                break;
            case 4:
                textValue = "今日无外勤数据";
                break;
            case 5:
                textValue = "本月无迟到数据";
                break;
            case 6:
                textValue = "本月无旷工数据";
                break;
            case 7:
                textValue = "本月无平均工时数据";
                break;
            case 8:
                textValue = "本月无加班数据";
                break;
            case 9:
                textValue = "本月无漏打卡数据";
                break;
            case 10:
                textValue = "本月无外勤数据";
                break;
        }

        return textValue;
    }


    /**
     * 返回格式 2018年4月28日
     *
     * @param value 2018-4-28
     * @return
     */
    private String getTitleDate(String value) {
        Date date = new Date(DateUtil.convertTimeToLong(value, "yyyy-MM-dd"));
        CalendarDay from = CalendarDay.from(date);
        chineseSelectorDate = DateUtil.getChineseSelector0Date(from);
        return chineseSelectorDate;
    }

    private String secondTitle() {
        String value = "";
        switch (type) {
            case 1:
                value = "迟到图";
                break;
            case 2:
                value = "旷工图";
                break;
            case 3:
                value = "漏打卡图";
                break;
            case 4:
                value = "外勤图";
                break;
            case 5: //月迟到榜
                value = "公司迟到图";
                break;
            case 6: //月旷工
                value = "公司旷工图";
                break;
            case 7: //月工时榜
                value = "公司平均工时图";
                break;
            case 8: //月加班榜
                value = "公司加班图";
                break;
            case 9: //月漏打卡
                value = "公司漏打卡图";
                break;
            case 10: //月外勤
                value = "公司外勤统计图";
                break;
        }
        return value;
    }
}
