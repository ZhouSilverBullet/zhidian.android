package com.sdxxtop.zhidian.ui.activity;

import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.StatMonthAdapter;
import com.sdxxtop.zhidian.entity.RightPartBean;
import com.sdxxtop.zhidian.entity.StatMonthBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.StatSelectionDateWindow;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ColorTextUtil;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DatePicker;

public class StatMonthActivity extends BaseActivity {

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
    @BindView(R.id.statistical_month_no_data_text)
    TextView noDataText;
    @BindView(R.id.statistical_month_recycler)
    RecyclerView monthRecyclerView;

    public static final String STAT_LEAVE_TYPE = "type";
    public static final String STAT_DAY_TYPE = "day_type";

    public static final int DAY_TYPE = 10; //今日
    public static final int MONTH_TYPE = 20; //月


    @BindView(R.id.statistical_stat_month_top_layout)
    LinearLayout topLayout;
    @BindView(R.id.statistical_month_leave_time_text)
    TextView leaveTimeText;
    @BindView(R.id.statistical_month_leave_time_sort)
    LinearLayout leaveTimeSort;
    @BindView(R.id.statistical_month_leave_munit_text)
    TextView leaveMunitText;
    @BindView(R.id.statistical_month_leave_munit_sort)
    LinearLayout leaveMunitSort;
    @BindView(R.id.statistical_month_sort_layout)
    LinearLayout sortLayout;
    @BindView(R.id.right_recycler)
    RecyclerView rightRecyclerView;
    @BindView(R.id.stat_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.right_drawer_cancel)
    Button drawerCancelBtn;
    @BindView(R.id.right_drawer_confirm)
    Button drawerconfirmBtn;
    @BindView(R.id.statistical_today_text)
    TextView todayText;
    @BindView(R.id.statistical_month_right_sort)
    TextView rightSortText;


    private int type;

    private String textValue;
    private int dayType;
    private StatSelectionDateWindow selectionDateWindow;
    private String chineseSelectorDate;
    private DatePicker datePicker;
    private StatMonthAdapter mAdapter;
    private SelectionAdapter selectionAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_stat_month;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            type = getIntent().getIntExtra(StatMonthActivity.STAT_LEAVE_TYPE, -1);
            dayType = getIntent().getIntExtra(StatMonthActivity.STAT_DAY_TYPE, -1);
        }
    }

    @Override
    protected void initView() {
        super.initView();

        handleTitle();

        setRecyclerView();

//        rightRecyclerView.setAdapter();

        //根据type来进行区分一些操作
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        textValue = getTextValue();
        titleView.setTitleValue(ColorTextUtil.getUsermentValue(type).textValue);

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
        loadRightData();
    }

    /**
     * 用于根据type来判断头部 迟到次数 迟到时间
     * 隐藏和显示
     */
    private void handleTitle() {
        switch (type) {
            case 1:
                //leaveTimeSort
                leaveTimeSort.setVisibility(View.GONE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveMunitText.setText("迟到时间");
                noDataText.setText("今日无迟到数据");
                break;
            case 2:
                leaveTimeSort.setVisibility(View.GONE);
                leaveMunitSort.setVisibility(View.GONE);
                sortLayout.setVisibility(View.GONE);
                noDataText.setText("今日无旷工数据");
                break;
            case 3:
                leaveTimeSort.setVisibility(View.GONE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveMunitText.setText("漏打卡");
                noDataText.setText("今日无漏打卡数据");
                break;
            case 4: //今日打卡记录 换adapter 考虑后不换了，麻烦
                leaveTimeSort.setVisibility(View.GONE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveMunitText.setText("打卡记录");
                rightSortText.setVisibility(View.GONE);
                noDataText.setText("今日无打卡记录");
                break;

            case 5: //月迟到榜
                leaveTimeSort.setVisibility(View.VISIBLE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveTimeText.setText("迟到次数");
                leaveMunitText.setText("迟到分钟");
                noDataText.setText("本月无迟到数据");
                break;
            case 6: //月旷工
                leaveTimeSort.setVisibility(View.GONE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveMunitText.setText("旷工次数");
                noDataText.setText("本月无旷工数据");
                break;
            case 7: //月工时榜
                leaveTimeSort.setVisibility(View.VISIBLE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveTimeText.setText("平均工时");
                leaveMunitText.setText("总工时");
                noDataText.setText("本月无工时数据");
                break;
            case 8: //月加班榜
                leaveTimeSort.setVisibility(View.VISIBLE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveTimeText.setText("平均加班");
                leaveMunitText.setText("总加班");
                noDataText.setText("本月无加班数据");
                break;
            case 9: //月漏打卡
                leaveTimeSort.setVisibility(View.GONE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveMunitText.setText("漏打卡次数");
                noDataText.setText("本月无漏打卡数据");
                break;
            case 10: //月外勤
                leaveTimeSort.setVisibility(View.GONE);
                leaveMunitSort.setVisibility(View.VISIBLE);
                leaveMunitText.setText("外勤次数");
                noDataText.setText("本月无外勤数据");
                break;
        }
    }

    private void setRecyclerView() {
        monthRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        monthRecyclerView.addItemDecoration(new ItemDivider());
        mAdapter = new StatMonthAdapter(R.layout.item_stat_month_recycler);
        monthRecyclerView.setAdapter(mAdapter);
        mAdapter.setType(type);
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

        //上一天
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

        leaveTimeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ave排序就要把排序状态全部改成false ,为ave排序
                List<StatMonthBean.DataBean.StatBean> data = mAdapter.getData();
                List<StatMonthBean.DataBean.StatBean> statBeanList = new ArrayList<>();

                if (mAdapter.getNumSort() == 1) {
                    for (StatMonthBean.DataBean.StatBean datum : data) {
                        datum.setAveSort(false);
                        datum.setSquareSort(false);
                        statBeanList.add(datum);
                    }
                    Collections.sort(statBeanList, new Comparator<StatMonthBean.DataBean.StatBean>() {
                        @Override
                        public int compare(StatMonthBean.DataBean.StatBean o1, StatMonthBean.DataBean.StatBean o2) {
                            return o2.parse(o2.getAve()).compareTo(o1.parse(o1.getAve()));
                        }
                    });
                    mAdapter.replaceData(statBeanList);
                    mAdapter.setNumSort(0); //相反的处理
                } else { //默认第一次点击由打到小
                    for (StatMonthBean.DataBean.StatBean datum : data) {
                        datum.setAveSort(false);
                        datum.setSquareSort(true);
                        statBeanList.add(datum);
                    }
                    Collections.sort(statBeanList, new Comparator<StatMonthBean.DataBean.StatBean>() {
                        @Override
                        public int compare(StatMonthBean.DataBean.StatBean o1, StatMonthBean.DataBean.StatBean o2) {
                            return o1.parse(o1.getAve()).compareTo(o2.parse(o2.getAve()));
                        }
                    });
                    mAdapter.replaceData(statBeanList);
                    mAdapter.setNumSort(1);//相反的处理
                }
            }
        });

        //
        leaveMunitSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //num排序就要把排序状态全部改成false ,为num排序
                List<StatMonthBean.DataBean.StatBean> data = mAdapter.getData();
                List<StatMonthBean.DataBean.StatBean> statBeanList = new ArrayList<>();

                if (mAdapter.getNumSort() == 1) {
                    for (StatMonthBean.DataBean.StatBean datum : data) {
                        datum.setAveSort(false);//不是ave排序
                        datum.setSquareSort(false); //反排序
                        statBeanList.add(datum);
                    }
                    Collections.sort(statBeanList, new Comparator<StatMonthBean.DataBean.StatBean>() {
                        @Override
                        public int compare(StatMonthBean.DataBean.StatBean o1, StatMonthBean.DataBean.StatBean o2) {
                            return o2.parse(o2.getNum()).compareTo(o1.parse(o1.getNum()));
                        }
                    });
                    mAdapter.replaceData(statBeanList);
                    mAdapter.setNumSort(0); //相反的处理
                } else { //默认第一次点击由打到小
                    for (StatMonthBean.DataBean.StatBean datum : data) {
                        datum.setAveSort(false); //不是ave排序
                        datum.setSquareSort(true);  //正排序
                        statBeanList.add(datum);
                    }
                    Collections.sort(statBeanList, new Comparator<StatMonthBean.DataBean.StatBean>() {
                        @Override
                        public int compare(StatMonthBean.DataBean.StatBean o1, StatMonthBean.DataBean.StatBean o2) {
                            return o1.parse(o1.getNum()).compareTo(o2.parse(o2.getNum()));
                        }
                    });
                    mAdapter.replaceData(statBeanList);
                    mAdapter.setNumSort(1);//相反的处理
                }
            }
        });


        titleView.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        drawerCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        drawerconfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(type, ddAndMd);
                drawerLayout.closeDrawer(Gravity.RIGHT);
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
            datePicker = new DatePicker(this, cn.qqtheme.framework.picker.DatePicker.YEAR_MONTH);
            Calendar instance = Calendar.getInstance();
            CalendarDay from = CalendarDay.from(instance);
            datePicker.setRangeEnd(from.getYear(), from.getMonth() + 1);
            datePicker.setTextSize(20);
            datePicker.setSelectedItem(from.getYear(), from.getMonth() + 1);
            datePicker.setTitleText("请选择");
            datePicker.setSubmitTextColor(getResources().getColor(R.color.lan));
            datePicker.setOnDatePickListener(new cn.qqtheme.framework.picker.DatePicker.OnYearMonthPickListener() {
                @Override
                public void onDatePicked(String year, String month) {
                    chineseSelectorDate = year + "年" + month + "月";
                    dayTitle.setText(chineseSelectorDate + secondTitle());
                    loadData(type, year + "-" + month);
                }
            });
        }

        datePicker.show();

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


    private String ddAndMd;

    public void loadData(int type, String ddAndMd) {

        todayVisibility();

        this.ddAndMd = ddAndMd;
        Params params = new Params();

        if (selectionAdapter != null) {
            List<RightPartBean.DataBean.PartBean> data = selectionAdapter.getData();
            String value = "";
            for (RightPartBean.DataBean.PartBean datum : data) {
                if (datum.isCheck()) {
                    value = value + datum.getPart_id() + ",";
                }
            }
            if (value.length() > 0) {
                value = value.substring(0, value.length() - 1);
                params.put("pi", value);
            }
        }

        if (dayType == DAY_TYPE) {
            params.put("tt", 1); //1:今日 2:月份
            params.put("dd", ddAndMd); //日参数
        } else {
            params.put("tt", 2); //1:今日 2:月份
            params.put("md", ddAndMd); //月参数
        }
        params.put("si", type);
        if (type == 8 || type == 10) {  // 4 8 10请求这个接口
            RequestUtils.createRequest().postStatUserApply(params.getData())
                    .enqueue(new RequestCallback<>(new IRequestListener<StatMonthBean>() {
                        @Override
                        public void onSuccess(StatMonthBean contactPartBean) {
                            StatMonthBean.DataBean data = contactPartBean.getData();
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
            RequestUtils.createRequest().postStatUserSign(params.getData())
                    .enqueue(new RequestCallback<>(new IRequestListener<StatMonthBean>() {
                        @Override
                        public void onSuccess(StatMonthBean contactPartBean) {
                            StatMonthBean.DataBean data = contactPartBean.getData();
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


    private void handleData(StatMonthBean.DataBean data) {
        if (data.getStat().size() == 0) {
            noDataText.setVisibility(View.VISIBLE);
        } else {
            noDataText.setVisibility(View.GONE);
        }
        mAdapter.replaceData(data.getStat());
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
            textValue = ColorTextUtil.getUsermentValue(type).textValue;
            if (type == 3) {
                textValue = textValue + "榜";
            }
            textValue = textValue.substring(1, textValue.length());
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

    private void loadRightData() {
        Params params = new Params();
        RequestUtils.createRequest().postStatGetPart(params.getData()).enqueue(new RequestCallback<RightPartBean>(new IRequestListener<RightPartBean>() {
            @Override
            public void onSuccess(RightPartBean rightPartBean) {
                RightPartBean.DataBean data = rightPartBean.getData();
                if (data != null) {
                    List<RightPartBean.DataBean.PartBean> part = data.getPart();
                    if (part != null && part.size() > 0) {
                        selectionAdapter = new SelectionAdapter(R.layout.item_stat_selector_recycler, part);
                        rightRecyclerView.addItemDecoration(new ItemDivider());
                        rightRecyclerView.setAdapter(selectionAdapter);
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }
        }));
    }


    class SelectionAdapter extends BaseQuickAdapter<RightPartBean.DataBean.PartBean, BaseViewHolder> {

        public SelectionAdapter(int layoutResId, @Nullable List<RightPartBean.DataBean.PartBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final RightPartBean.DataBean.PartBean item) {
            final TextView textSelectorText = helper.getView(R.id.item_stat_text_selector);
            TextView textName = helper.getView(R.id.item_stat_text_name);
            setChecked(textSelectorText, item.isCheck());
            textName.setText(StringUtil.stringNotNull(item.getPart_name()));
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean check = item.isCheck();
                    item.setCheck(!check);
                    setChecked(textSelectorText, !check);
                }
            });

        }

        private void setChecked(TextView textView, boolean isCheck) {
            if (isCheck) {
                textView.setBackgroundResource(R.drawable.selected);
            } else {
                textView.setBackgroundResource(R.drawable.unselected);
            }
        }
    }

    private String secondTitle() {
        String value = "";
        switch (type) {
            case 1:
                value = "迟到榜";
                break;
            case 2:
                value = "旷工";
                break;
            case 3:
                value = "漏打卡";
                break;
            case 4: //今日打卡记录 换adapter 考虑后不换了，麻烦
                value = "打卡记录";
                break;
            case 5: //月迟到榜
                value = "迟到榜";
                break;
            case 6: //月旷工
                value = "旷工榜";
                break;
            case 7: //月工时榜
                value = "工时榜";
                break;
            case 8: //月加班榜
                value = "加班榜";
                break;
            case 9: //月漏打卡
                value = "漏打卡榜";
                break;
            case 10: //月外勤
                value = "外勤榜";
                break;
        }
        return value;
    }
}
