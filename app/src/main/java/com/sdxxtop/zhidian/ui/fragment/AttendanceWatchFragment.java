package com.sdxxtop.zhidian.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.HomeRecyclerAdapter;
import com.sdxxtop.zhidian.entity.AttendanceWatchBean;
import com.sdxxtop.zhidian.entity.MainIndexBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.PunchCardActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.decorator.CalendarSelectorDecorator;
import com.sdxxtop.zhidian.widget.decorator.ClassDateDecorator;
import com.sdxxtop.zhidian.widget.decorator.ClassDateNameDecorator;
import com.sdxxtop.zhidian.widget.decorator.HolidayDecorator;
import com.sdxxtop.zhidian.widget.decorator.MaxTodayDecorator;
import com.sdxxtop.zhidian.widget.decorator.OutsetEventDecorator;
import com.sdxxtop.zhidian.widget.decorator.RuleDecorator;
import com.sdxxtop.zhidian.widget.decorator.TextGrayDecorator;
import com.sdxxtop.zhidian.widget.decorator.TodayTextDecorator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 考勤视图
 */
public class AttendanceWatchFragment extends BaseFragment {

    @BindView(R.id.attendance_calendar_view)
    MaterialCalendarView calendarView;
    @BindView(R.id.home_fragment_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_date)
    TextView tvTitle;
    private RuleDecorator ruleDecorator;
    private ClassDateDecorator classDateDecorator;
    private HolidayDecorator holidayDecorator;
    private HomeRecyclerAdapter mAdapter;
    private List<ClassDateNameDecorator> decoratorList;
    //用于存当前选择项
    private CalendarDay currentCalendarDay;
    private OutsetEventDecorator outsetEventDecorator;

    public AttendanceWatchFragment() {
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_attendance_watch;
    }

    @Override
    protected void initView() {
        //隐藏标题，这里需要自定义
        calendarView.setTopbarVisible(false);

        calendarView.state().edit().setMaximumDate(Date2Util.getLastDayOfMonth())
                .commit();
        //选中当前日期
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (Date2Util.compareToday2(date)) {
                    if (currentCalendarDay != null) {
                        calendarView.setSelectedDate(currentCalendarDay);
                    }
                    return;
                }
                currentCalendarDay = calendarView.getSelectedDate();
                loadDayData(DateUtil.getSelectorDate(date));
            }
        });


        tvTitle.setText(DateUtil.getChineseYearAndMonthDate(calendarView.getSelectedDate()));

        ruleDecorator = new RuleDecorator(mContext);
        holidayDecorator = new HolidayDecorator(mContext);
        outsetEventDecorator = new OutsetEventDecorator(Color.RED);
        decoratorList = new ArrayList<>();
        calendarView.addDecorators(new CalendarSelectorDecorator(mContext, R.drawable.calendar_selector2),
                new TodayTextDecorator(mContext),
                new TextGrayDecorator(mContext),
                ruleDecorator,
                holidayDecorator,
                outsetEventDecorator,
                new MaxTodayDecorator(mContext)
        );
        for (int i = 0; i < 31; i++) {
            decoratorList.add(new ClassDateNameDecorator(mContext));
        }
        calendarView.addDecorators(decoratorList);
//        calendarView.addDecorators(new TodayDecorator(mContext), new MaxTodayDecorator(mContext));

        initRecycler();

        currentCalendarDay = calendarView.getSelectedDate();
        loadDayData(DateUtil.getSelectorDate(calendarView.getSelectedDate()));
    }

    @Override
    protected void initEvent() {
        mRootView.findViewById(R.id.image_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.goToPrevious();
            }
        });

        mRootView.findViewById(R.id.image_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.goToNext();
            }
        });

        mRootView.findViewById(R.id.iv_today_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                CalendarDay from = CalendarDay.from(instance);
                String dayChineseSelectorDate = "";
                dayChineseSelectorDate = DateUtil.getChineseYearAndMonthDate(from);
                calendarView.setSelectedDate(Calendar.getInstance().getTime());
                tvTitle.setText(dayChineseSelectorDate);
                calendarView.setCurrentDate(instance);
                loadData(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH));
                loadDayData(DateUtil.getSelectorDate(from));
            }
        });

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                tvTitle.setText(DateUtil.getChineseYearAndMonthDate(date));
                loadData(date.getYear(), date.getMonth());
            }
        });

        mAdapter.setFieldImageListener(new HomeRecyclerAdapter.FieldImageListener() {
            @Override
            public void onDeleteClick() {
                initData();
            }

            @Override
            public void onAddPhotoClick() {
                initData();
            }
        });
    }

    private void todayVisibility() {
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        String dayChineseSelectorDate = "";
        dayChineseSelectorDate = DateUtil.getChineseYearAndMonthDate(from);

        if (!dayChineseSelectorDate.equals(tvTitle.getText().toString())) {
            mRootView.findViewById(R.id.iv_today_image).setVisibility(View.VISIBLE);
//            downDayText.setTextColor(getResources().getColor(R.color.blue));
        } else {
            mRootView.findViewById(R.id.iv_today_image).setVisibility(View.GONE);
//            downDayText.setTextColor(getResources().getColor(R.color.texthintcolor));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mAdapter != null) {
            mAdapter.onResult(requestCode, resultCode, data);
        }
    }

    private void loadData(final int year, final int month) {
        todayVisibility();

        Params params = new Params();
        params.put("yr", year);
        params.put("mh", (month + 1));
        RequestUtils.createRequest().postUserIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<AttendanceWatchBean>() {
            @Override
            public void onSuccess(AttendanceWatchBean attendanceWatchBean) {
                AttendanceWatchBean.DataBean data = attendanceWatchBean.getData();
                if (data != null) {
                    handleDataBean(data, year, month);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }

    private void handleDataBean(AttendanceWatchBean.DataBean data, int year, int month) {
        AttendanceWatchBean.DataBean.RuleBean rule = data.getRule();
        if (rule == null) {
            return;
        }
        String workWeekDay = rule.getWork_week_day();
        List<String> weekendInMonth = null;
        if (!TextUtils.isEmpty(workWeekDay)) {
            String[] split = workWeekDay.split(",");
            weekendInMonth = Date2Util.getWeekendInMonth(split, year, (month + 1));
        }
        List<AttendanceWatchBean.DataBean.AndroidClassDateBean> classDate = data.getAndroid_class_date();
        List<String> holiday = data.getHoliday();
        List<String> abnormal = data.getAbnormal();

        outsetEventDecorator.setDates(abnormal);
        ruleDecorator.setDates(rule, classDate, holiday, weekendInMonth);

        if (classDate != null) {
            for (AttendanceWatchBean.DataBean.AndroidClassDateBean classDateBean : classDate) {
                String class_date = classDateBean.getClass_date();
                for (int i = 0; i < holiday.size(); i++) {
                    String hoDay = holiday.get(i);
                    if (!TextUtils.isEmpty(class_date) && class_date.equals(hoDay)) {
                        holiday.remove(hoDay);
                        break;
                    }
                }
            }

            for (int i = 0; i < classDate.size(); i++) {
                AttendanceWatchBean.DataBean.AndroidClassDateBean classDateBean = classDate.get(i);

                String class_date = classDateBean.getClass_date();

                if (weekendInMonth != null) {
                    for (String s : weekendInMonth) {
                        if (!TextUtils.isEmpty(s) && s.equals(class_date)) {
                            //如果class date这个数据里面有 假设星期六和星期天 有加班的要剔除，最后变成休息
                            weekendInMonth.remove(s);
                            break;
                        }
                    }
                }

                CalendarDay calendarDay = new CalendarDay(new Date(DateUtil.convertTimeToLong(class_date, "yyyy-MM-dd")));
                String short_name = classDateBean.getShort_name();
                decoratorList.get(i).setShortName(short_name, calendarDay);
            }
        }

        if (weekendInMonth != null) {
            holiday.addAll(weekendInMonth);
        }

        holidayDecorator.setHoidayData(holiday);
        calendarView.addDecorators(decoratorList);
    }

    private void removeDecorator() {
        if (ruleDecorator != null) {
            calendarView.removeDecorator(ruleDecorator);
        }

        if (classDateDecorator != null) {
            calendarView.removeDecorator(classDateDecorator);
        }

        if (holidayDecorator != null) {
            calendarView.removeDecorator(holidayDecorator);
        }
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);
        mAdapter = new HomeRecyclerAdapter(R.layout.item_fragment_home_recycler, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setIsNotHomeData(true);
    }


    @Override
    protected void initData() {
        CalendarDay selectedDate = calendarView.getSelectedDate();
        loadData(selectedDate.getYear(), selectedDate.getMonth());
    }

    /**
     * 指定日期打卡
     */
    private void loadDayData(String signDate) {

        Params params = new Params();
        params.put("sd", signDate);
        RequestUtils.createRequest().postMainDay(params.getData())
                .enqueue(new RequestCallback<>(getActivity(), new IRequestListener<MainIndexBean>() {
                    @Override
                    public void onSuccess(MainIndexBean mainIndexBean) {
                        MainIndexBean.DataBean data = mainIndexBean.getData();
                        if (data != null) {
                            handleMainIndexData(data, false);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }
                }));
    }

    private void handleMainIndexData(MainIndexBean.DataBean data, boolean b) {
        int signTime = data.getSign_time();
        String sign_date = data.getSign_date();
        String sign_name = data.getSign_name();

        List<MainIndexBean.DataBean.SignLogBean> signLogList = data.getSign_log();

        if (!TextUtils.isEmpty(sign_date) && !TextUtils.isEmpty(sign_name) && signLogList != null) {
            Collections.sort(signLogList);
            //进行缓存的一个最后是一个真的数据
            List<MainIndexBean.DataBean.SignLogBean> tempSignLogList = new ArrayList<>();
            List<MainIndexBean.DataBean.SignLogBean> removeLogList = new ArrayList<>(signLogList);

            View view = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_home_recycler_header, null);
            //上班下班 打开名称
            String[] signNameString = sign_name.split(",");

            //上班下班 打卡时间
            String[] signDateString = sign_date.split(",");

            //上下班打卡时间的长度是否相等 一般后台给的都相等
            if (signNameString.length == signDateString.length) {

                List<String> signNameList = Arrays.asList(signNameString);
                List<String> tempTimeList = new ArrayList<>();
                for (int i = 0; i < signNameString.length; i++) {
                    String signDate = signDateString[i];

                    //获取打卡时间 用于打卡使用
//                long aLong = DateUtil.convertTimeToLong(signDate, "yyyy-MM-dd HH:mm:ss");
//                HomeCardBean homeCardBean = new HomeCardBean();
//                homeCardBean.time = aLong;
//                homeCardBean.name = signNameString[i];
//                homeCardBean.cardStatusName = signNameString[i];
//                timeList.add(homeCardBean);
                    String[] signDateTime = signDate.split(" ");
                    if (signDateTime.length == 2) {
                        tempTimeList.add(signDateTime[1]);
                    }
                }

                //确定角标
                List<Integer> indexList = new ArrayList<>();
                for (int i = 0; i < tempTimeList.size(); i++) {
                    for (int j = 0; j < signLogList.size(); j++) {
                        MainIndexBean.DataBean.SignLogBean signLogBean = signLogList.get(j);
                        String sys_date = signLogBean.getSys_date();
                        if (!TextUtils.isEmpty(sys_date) && tempTimeList.contains(sys_date) && !signLogBean.isCompare()) {
                            int indexOf = tempTimeList.indexOf(sys_date);
                            if (!indexList.contains(indexOf)) {
                                indexList.add(indexOf);
                            }
                        }
                    }
                }


                for (int i = 0; i < tempTimeList.size(); i++) {
                    if (indexList.contains(i) && removeLogList.size() > 0) {
                        MainIndexBean.DataBean.SignLogBean signLogBean = removeLogList.get(0);
//                    String timeSys = tempTimeList.get(i);
//                    long dateLong = DateUtil.convertTimeToLong(timeSys, "HH:mm:ss");
//                    String sysDate = signLogBean.getSys_date();
//                    long logLong = DateUtil.convertTimeToLong(sysDate, "HH:mm:ss");
//                    if (Math.abs(dateLong - logLong) <= signTime) {  //小于这个时间段就可以打卡
//                        timeList.get(i).canClick = true;
//                    }
                        tempSignLogList.add(signLogBean);
                        removeLogList.remove(0);
                    } else {
//                    timeList.get(i).canClick = false;
                        MainIndexBean.DataBean.SignLogBean logBean = new MainIndexBean.DataBean.SignLogBean();
                        logBean.setSys_date(tempTimeList.get(i));
                        logBean.setSign_name(signNameList.get(i));
                        logBean.setLeakCard(true);
                        tempSignLogList.add(logBean);
                    }
                }
            }
            handleHeaderView(view, data);
            mAdapter.removeAllHeaderView();
            mAdapter.addHeaderView(view);
            int is_rest = data.getIs_rest();
//            if (is_rest != 0) {
//                for (HomeCardBean homeCardBean : timeList) {
//                    homeCardBean.name = "休息";
//                }
//            }
            String selectorDate = DateUtil.getSelectorDate(calendarView.getSelectedDate());
            mAdapter.setSignDateString(signDateString);
            mAdapter.setIsNeed(data.getIs_need());
            mAdapter.addData(tempSignLogList, data.getApply(), is_rest, selectorDate);
        }
    }

    private void handleHeaderView(View view, final MainIndexBean.DataBean data) {
        View autoTimeText = view.findViewById(R.id.item_home_recycler_top_layout);
        autoTimeText.setVisibility(View.GONE);

        // 补卡
        View fillCard = view.findViewById(R.id.item_home_recycler_header_fill_card);
        int is_leak = data.getIs_leak();
        if (is_leak == 2) {
            fillCard.setVisibility(View.GONE);
        } else if (is_leak == 1) {
            fillCard.setVisibility(View.VISIBLE);
            // 补卡
            fillCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<MainIndexBean.DataBean.SignLogBean> sign_log = data.getSign_log();
                    List<MainIndexBean.DataBean.SignLogBean> signLogList = new ArrayList<>();
                    for (MainIndexBean.DataBean.SignLogBean signLogBean : sign_log) {
                        int status = signLogBean.getStatus();
                        if (status == 8) {
                            signLogList.add(signLogBean);
                        }
                    }
                    Intent intent = new Intent(mContext, PunchCardActivity.class);
                    intent.putExtra("sign_log", (ArrayList) signLogList);
                    String selectorDate = DateUtil.getSelectorDate(calendarView.getSelectedDate());
                    intent.putExtra("selector_date", selectorDate);
                    intent.putExtra("at", 2);
                    startActivity(intent);
                }
            });
        }
    }
}
