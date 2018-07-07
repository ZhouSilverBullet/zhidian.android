package com.sdxxtop.zhidian.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.HomeRecyclerAdapter;
import com.sdxxtop.zhidian.entity.HomeCardBean;
import com.sdxxtop.zhidian.entity.MainIndexBean;
import com.sdxxtop.zhidian.entity.MainWeekBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.eventbus.FaceEvent;
import com.sdxxtop.zhidian.eventbus.FaceRegEvent;
import com.sdxxtop.zhidian.eventbus.MessageCenterEvent;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.model.ConstantValue;
import com.sdxxtop.zhidian.ui.activity.FaceLivenessExpActivity;
import com.sdxxtop.zhidian.ui.activity.MainActivity;
import com.sdxxtop.zhidian.ui.activity.MessageCenterActivity;
import com.sdxxtop.zhidian.ui.activity.PunchCardActivity;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.DeviceUtil;
import com.sdxxtop.zhidian.utils.FDLocation;
import com.sdxxtop.zhidian.utils.PreferenceUtils;
import com.sdxxtop.zhidian.utils.StringUtil;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.widget.ChangeCardDialog;
import com.sdxxtop.zhidian.widget.decorator.CalendarSelectorDecorator;
import com.sdxxtop.zhidian.widget.decorator.EventDecorator;
import com.sdxxtop.zhidian.widget.decorator.MaxTodayDecorator;
import com.sdxxtop.zhidian.widget.decorator.TodayDecorator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;
import zhangphil.iosdialog.widget.AlertDialog;

/**
 * 作者：CaiCM
 * 日期：2018/3/23  时间：16:40
 * 邮箱：15010104100@163.com
 * 描述：首页打卡界面
 */
public class HomeFragment extends BaseFragment implements Handler.Callback {

    @BindView(R.id.fragment_home_calendar_view)
    MaterialCalendarView calendarView;
    @BindView(R.id.fragment_home_calendar_title)
    TextView calendarTitle;
    @BindView(R.id.fragment_home_calender_today_text)
    TextView todayText;

    @BindView(R.id.home_fragment_company_name)
    TextView homeCompanyNameText;
    @BindView(R.id.fragment_home_dot_layout)
    View homeDotLayout;
    @BindView(R.id.fragment_home_dot_text)
    TextView homeDotText;

    //切换打卡
    @BindView(R.id.home_fragment_icon_layout)
    View changeLayout;
    @BindView(R.id.home_fragment_icon)
    ImageView changeIcon;


    @BindView(R.id.home_fragment_title_layout)
    RelativeLayout homeTileLayout;

    @BindView(R.id.home_fragment_icon_description)
    TextView changeDescription;
    @BindView(R.id.fragment_home_right_text)
    TextView homeRightText;


    @BindView(R.id.home_fragment_recycler)
    RecyclerView mRecyclerView;
    private HomeRecyclerAdapter mAdapter;

    private Handler mHandler;
    private TextView autoTimeText;
    private View takeCardLayout;
    private TextView headerWorkCardStatusText;

    private String address; //地址
    private int isNeed;
    private CalendarDay currentDate;
    private EventDecorator eventDecorator;
    private View headerView;
    private int is_rest;

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        //TODO 很多地方需要适配
        statusBar(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android6.0以上系统
//            StatusBarUtil.setDarkStatusIcon(getActivity().getWindow(), false);
//        } else {
//            StatusBarUtil.compat(getActivity());
//        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        topViewPadding(homeTileLayout);

        initCalendar();
        initRecycler();
        int param = PreferenceUtils.getInstance(getActivity()).getIntParam(ConstantValue.TAKE_CARD_STATE, ChangeCardDialog.GPS);
        takeCardStatus(param);
        changeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeCardDialog cardDialog = new ChangeCardDialog(v.getContext());
                cardDialog.show();
                cardDialog.setDialogClickListener(new ChangeCardDialog.DialogClickListener() {
                    @Override
                    public void onDialogClick(int position) {
                        takeCardStatus(position);
                    }
                });
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) { //再次出现的时候调用，第一次创建fragment的时候并不走该方法
            todayText.setVisibility(View.GONE);
            calendarView.setSelectedDate(Calendar.getInstance().getTime());
            calendarView.setCurrentDate(currentDate);
            initData();
            statusBar(false);
        }
    }

    private void takeCardStatus(int position) {
        switch (position) {
            case ChangeCardDialog.GPS:
                PreferenceUtils.getInstance(getActivity()).saveParam(ConstantValue.TAKE_CARD_STATE, ChangeCardDialog.GPS);
                changeIcon.setImageResource(R.drawable.home_page_gps);
                changeDescription.setText("GPS打卡");
                break;
            case ChangeCardDialog.WIFI:
                PreferenceUtils.getInstance(getActivity()).saveParam(ConstantValue.TAKE_CARD_STATE, ChangeCardDialog.WIFI);
                changeIcon.setImageResource(R.drawable.home_page_wifi);
                changeDescription.setText("WiFi打卡");
                break;
            case ChangeCardDialog.FACE:
                PreferenceUtils.getInstance(getActivity()).saveParam(ConstantValue.TAKE_CARD_STATE, ChangeCardDialog.FACE);
                changeIcon.setImageResource(R.drawable.home_page_face);
                changeDescription.setText("人脸识别");
                break;
            case ChangeCardDialog.FIELD:
                PreferenceUtils.getInstance(getActivity()).saveParam(ConstantValue.TAKE_CARD_STATE, ChangeCardDialog.FIELD);
                changeIcon.setImageResource(R.drawable.home_page_my_field);
                changeDescription.setText("外勤");
                break;
        }
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setFocusable(false);
        mAdapter = new HomeRecyclerAdapter(R.layout.item_fragment_home_recycler, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setFieldImageListener(new HomeRecyclerAdapter.FieldImageListener() {
            @Override
            public void onDeleteClick() {
                if (calendarView != null) {
                    CalendarDay selectedDate = calendarView.getSelectedDate();
                    loadDayData((DateUtil.getSelectorDate(selectedDate)));
                } else {
                    initData();
                }
            }

            @Override
            public void onAddPhotoClick() {
                if (calendarView != null) {
                    CalendarDay selectedDate = calendarView.getSelectedDate();
                    loadDayData((DateUtil.getSelectorDate(selectedDate)));
                } else {
                    initData();
                }
            }
        });
        todayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayText.setVisibility(View.GONE);
                calendarView.setCurrentDate(currentDate);
                calendarView.setSelectedDate(Calendar.getInstance().getTime());
                calendarTitle.setText(DateUtil.getTitleDate(CalendarDay.from(Calendar.getInstance().getTime())));
                initData();
            }
        });

        homeDotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageCenterActivity.class);
                startActivity(intent);
            }
        });

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Date currentDate = calendarView.getCurrentDate().getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DATE, 7);
                Date time = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String startDate = sdf.format(currentDate);
                String endDate = sdf.format(time);
                abnormalRequest(startDate, endDate);

                //这周的第一天和currentDay的第一天是否相同，相同就回到了本周
                Date mondayOfThisWeek = Date2Util.getMondayOfThisWeek();
                String firstWeek = sdf.format(mondayOfThisWeek);
                if (firstWeek.equals(startDate)) {
                    todayText.setVisibility(View.GONE);
                    homeRightText.setBackgroundResource(R.drawable.calendar_text_right_gray);
                } else {
                    todayText.setVisibility(View.VISIBLE);
                    homeRightText.setBackgroundResource(R.drawable.calendar_text_right);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mAdapter != null) {
            mAdapter.onResult(requestCode, resultCode, data);
        }
    }

    /**
     * 初始化日历
     */
    private void initCalendar() {
        //获取当前日期
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                //超过今天的日期不能进行选择
//                .setMaximumDate(CalendarDay.from(year, date.getMonth(), date.getDay() - 1))
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setMaximumDate(Date2Util.getSundayOfThisWeek())
                .commit();

        //隐藏标题，这里需要自定义
        calendarView.setTopbarVisible(false);
        //选中当前日期
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                if (compareToday(date)) {
//                    todayText.setVisibility(View.GONE);
//                } else {
//                    todayText.setVisibility(View.VISIBLE);
//                }

                if (compareToday2(date)) {
                    ToastUtil.show("后面日期无法被选择");
                    calendarView.setDateSelected(date, false);
                    return;
                }

                calendarTitle.setText(DateUtil.getTitleDate(date));
                loadDayData(DateUtil.getSelectorDate(date));
            }
        });

        CalendarDay selectedDate = calendarView.getSelectedDate();
        calendarTitle.setText(DateUtil.getTitleDate(selectedDate));
        calendarView.addDecorators(new CalendarSelectorDecorator(mContext), new TodayDecorator(mContext), new MaxTodayDecorator(mContext));

        currentDate = calendarView.getCurrentDate();
    }

    /**
     * 后面日期不进行选择
     *
     * @param date
     * @return
     */
    private boolean compareToday2(CalendarDay date) {
        int year = Calendar.getInstance().get(Calendar.YEAR); //年
        int month = Calendar.getInstance().get(Calendar.MONTH); //月
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH); //日
        int year1 = date.getYear();
        int month1 = date.getMonth();
        int day1 = date.getDay();
        if (year1 > year) {
            return true;
        } else if (year1 == year && month1 > month) {
            return true;
        } else if (year1 == year && month1 == month && day1 > day) {
            return true;
        }

        return false;
    }

    private boolean compareToday(CalendarDay date) {
//        int year = Calendar.getInstance().get(Calendar.YEAR); //年
//        int month = Calendar.getInstance().get(Calendar.MONTH); //月
//        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH); //日
//        int year1 = date.getYear();
//        int month1 = date.getMonth();
//        int day1 = date.getDay();
        String stringDate1 = DateUtil.getSelectorDate(date);
        String stringDate2 = DateUtil.getSelectorDate(currentDate);
        long date1 = DateUtil.convertTimeToLong(stringDate1, "yyyy-MM-dd");
        long date2 = DateUtil.convertTimeToLong(stringDate2, "yyyy-MM-dd");
        long total = date1 - date2;
        if (total >= 0 && total <= 7 * 24 * 60 * 60 * 1000) {
            return true;
        }

//        if (year == year1 && month == month1 && day == day1) {
//            return true;
//        }
        return false;
    }

    @OnClick(R.id.fragment_home_left_text)
    void onPreviousClicked() {
        calendarView.goToPrevious();
    }

    @OnClick(R.id.fragment_home_right_text)
    void onNextClicked() {
        calendarView.goToNext();
    }

    @Override
    protected void initData() {
        //默认为不能打卡
        canTakeCard = 2;
        Params params = new Params();
        RequestUtils.createRequest().postMainIndex(params.getData())
                .enqueue(new RequestCallback<>(getActivity(), new IRequestListener<MainIndexBean>() {
                    @Override
                    public void onSuccess(MainIndexBean mainIndexBean) {
                        MainIndexBean.DataBean data = mainIndexBean.getData();
                        if (data != null) {
                            handleMainIndexData(data, true);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }
                }));
    }

    /**
     * 指定日期打卡
     */
    private void loadDayData(String signDate) {
        canTakeCard = 2;
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


    // 用来装打开时间
    List<HomeCardBean> timeList = new ArrayList<>();
    //允许打卡的事件间隔 默认用个60分钟
    int signTime = 60 * 60 * 1000;

    //上班类型 1:上班下班 2:全为上班打卡
    int workType = 1;

    // 1 可以打卡 2不在打卡范围 3 表示已经打卡了
    int canTakeCard = -1;

    //选择上下班的当前位置的名字
    String positionName;
    //打卡的时间(2018-04-18 09:00:00)
    private String takeCardCurrentTime;

    //日志的长度
    private int index = -1;

    private int locationNotifyIndex = 0;

    private void handleMainIndexData(MainIndexBean.DataBean data, boolean isFirst) {
        if (calendarView == null) {
            return;
        }

        CalendarDay selectedDate = calendarView.getSelectedDate();
        if (selectedDate == null) {
            return;
        }

        List<String> abnormal = data.getAbnormal();
        if (abnormal != null) {
            if (eventDecorator != null) {
                calendarView.removeDecorator(eventDecorator);
            }
            //等请求回来时候小红点设置
            eventDecorator = new EventDecorator(Color.RED, abnormal);
            calendarView.addDecorator(eventDecorator);
        }

        if (timeList != null && isToday()) {
            timeList.clear();
        }

        int is_need = data.getIs_need();
        data.setIsFirst(isFirst);
        if (isFirst) {
            String company_name = data.getCompany_name();
            homeCompanyNameText.setText(StringUtil.stringNotNull(company_name));

            int msg_num = data.getMsg_num();
            if (msg_num == 0) {
                homeDotText.setVisibility(View.INVISIBLE);
            } else {
                homeDotText.setVisibility(View.VISIBLE);
                if (msg_num > 99) {
                    homeDotText.setText("99+");
                } else {
                    homeDotText.setText(msg_num + "");
                }
            }

            if (isToday()) {
                int sign_time = data.getSign_time();
                //转化成
                signTime = sign_time * 60 * 1000;
                workType = data.getWork_type();
            }
        }

        isNeed = is_need;


        String sign_date = data.getSign_date();
        String sign_name = data.getSign_name();

        List<MainIndexBean.DataBean.SignLogBean> signLogList = data.getSign_log();
        if (!TextUtils.isEmpty(sign_date) && !TextUtils.isEmpty(sign_name) && signLogList != null) {
            //进行缓存的一个最后是一个真的数据
            List<MainIndexBean.DataBean.SignLogBean> tempSignLogList = new ArrayList<>();
            List<MainIndexBean.DataBean.SignLogBean> removeLogList = new ArrayList<>(signLogList);

            if (headerView == null) {
                headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_fragment_home_recycler_header, null);
            }
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

                    if (isToday()) {
                        //获取打卡时间 用于打卡使用
                        long aLong = DateUtil.convertTimeToLong(signDate, "yyyy-MM-dd HH:mm:ss");
                        HomeCardBean homeCardBean = new HomeCardBean();
                        homeCardBean.time = aLong;
                        homeCardBean.stringTime = signDate;
                        homeCardBean.name = signNameString[i];
                        homeCardBean.cardStatusName = signNameString[i];
                        timeList.add(homeCardBean);
                    }

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
                Integer integer = -1;
                if (indexList.size() > 0) {
                    integer = Collections.max(indexList);
                }

                if (isToday()) {
                    locationNotifyIndex = integer;
                }

//                if (isToday()) {
                index = integer;
//                }

                for (int i = 0; i < tempTimeList.size(); i++) {

                    if (indexList.contains(i) && removeLogList.size() > 0) {
                        MainIndexBean.DataBean.SignLogBean signLogBean = removeLogList.get(0);

                        if (isToday()) {
                            HomeCardBean homeCardBean = timeList.get(i);
                            homeCardBean.isTakeCarded = true;
                            //log里面肯定是不能打卡的
                            homeCardBean.canClick = false;
                        }

//                        setTimeHomeCard(homeCardBean, sysDate, timeSys);
                        tempSignLogList.add(signLogBean);
                        removeLogList.remove(0);
                    } else {

                        if (i <= integer) {
                            if (isToday()) {
                                timeList.get(i).canClick = false;
                                timeList.get(i).isTakeCarded = false;
                            }
                        } else {
                            //默认都没打过卡，所以为true
                            if (isToday()) {
                                timeList.get(i).canClick = true;
                                timeList.get(i).isTakeCarded = false;
                            }
                        }
                        MainIndexBean.DataBean.SignLogBean logBean = new MainIndexBean.DataBean.SignLogBean();
                        String timeSys = tempTimeList.get(i);
                        logBean.setSys_date(timeSys);
                        logBean.setSign_name(signNameList.get(i));
                        logBean.setLeakCard(true);
                        tempSignLogList.add(logBean);

                    }
                }
            }
            this.data = data;
            // 补卡

            headerPuncard(headerView);
            if (isToday()) {
                handleHeaderView(headerView);
                mAdapter.removeAllHeaderView();
                mAdapter.addHeaderView(headerView);
            }

            is_rest = data.getIs_rest();
            MainIndexBean.DataBean.ApplyBean applyBean = data.getApply();
            if (is_rest != 0) {
                if (isToday()) {
                    for (HomeCardBean homeCardBean : timeList) {
                        homeCardBean.name = "休息";
                    }
                }
            }

            if (isNeed == 2) {
                if (isToday()) {
                    for (HomeCardBean homeCardBean : timeList) {
                        homeCardBean.name = "无需考勤";
                    }
                }
            }
//            else {
//                if (isToday()) {
//                    //有请假的也要筛选出来请假的
//                    for (int i = 0; i < timeList.size(); i++) {
//                        HomeCardBean homeCardBean = timeList.get(i);
//                        if (applyBean != null) {
//                            String start_time = applyBean.getStart_time();
//                            String end_time = applyBean.getEnd_time();
//                            int apply_type = applyBean.getApply_type();
//                            long startLongTime = DateUtil.convertTimeToLong(start_time, "yyyy-MM-dd HH:mm:ss");
//                            long endLongTime = DateUtil.convertTimeToLong(end_time, "yyyy-MM-dd HH:mm:ss");
//                            long sysDateLongTime = DateUtil.convertTimeToLong(signDateString[i], "yyyy-MM-dd HH:mm:ss");
//                            //在这个时间之间就先是 申请类型(1:请假 4:出差 6:集体请假 7:集体出差)
//                            if (sysDateLongTime >= startLongTime && sysDateLongTime <= endLongTime) {
//                                if (apply_type == 1 || apply_type == 6) {
//                                    homeCardBean.name = "请假";
//                                } else if (apply_type == 4 || apply_type == 7) {
//                                    homeCardBean.name = "出差";
//                                }
//                            }
//                        }
//                    }
//                }
//            }

            if (selectedDate != null) {
                String selectorDate = DateUtil.getSelectorDate(selectedDate);
                mAdapter.setIsNeed(isNeed);
                mAdapter.setSignDateString(signDateString);
                mAdapter.addData(tempSignLogList, applyBean, is_rest, selectorDate);
            }
        }
    }

    private void headerPuncard(View view) {
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
                    if (HomeFragment.this.data == null) {
                        return;
                    }
                    //补漏打卡
                    List<MainIndexBean.DataBean.SignLogBean> sign_log = HomeFragment.this.data.getSign_log();
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

    private void setTimeHomeCard(HomeCardBean homeCardBean, String sysDate, String timeSys) {
        long dateLong = DateUtil.convertTimeToLong(timeSys, "HH:mm:ss");
        long logLong = DateUtil.convertTimeToLong(sysDate, "HH:mm:ss");
        if (Math.abs(dateLong - logLong) <= signTime) {  //小于这个时间段就可以打卡
            homeCardBean.canClick = true;
        } else {
            homeCardBean.canClick = false;
        }
    }

    private MainIndexBean.DataBean data;

    private void handleHeaderView(View view) {

        autoTimeText = (TextView) view.findViewById(R.id.item_home_recycler_header_auto_time);
        headerWorkCardStatusText = (TextView) view.findViewById(R.id.item_home_recycler_header_work);

        if (data == null) {
            return;
        }
        //打卡
        takeCardLayout = view.findViewById(R.id.item_home_recycler_header_layout);
        takeCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isToday()) {
                    //不是今天打卡什么提示也不给
                    return;
                }
                //下班打卡提醒
                int workNumber = getWorkNumber(data);  // workType 上班类型 1:上班下班 2:全为上班打卡
                //全是上班不提醒打卡
                if (workNumber == 2 && isNeed != 2 && is_rest == 0 && workType != 2) {
                    long currentTime = DateUtil.convertTimeToLong(takeCardCurrentTime, "yyyy-MM-dd HH:mm:ss") - System.currentTimeMillis();
                    if (currentTime > 0) {
                        int currentMinute = (int) (currentTime / (1000 * 60.0) + 1);
                        new AlertDialog(mContext).builder()
                                .setTitle("提示")
                                .setMsg("离下班还有" + currentMinute + "分钟，确定打卡吗？")
                                .setNegativeButton("", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .setPositiveButton("", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        takeCardControl(data);
                                    }
                                })
                                .show();
                        return;
                    }
                }

                //打卡逻辑
                takeCardControl(data);
            }
        });


        autoTimeText.setText(DateUtil.show24Date());
        if (mHandler == null) {
            mHandler = new Handler(this);
        }
        mHandler.sendEmptyMessageDelayed(100, 1000);

//        if (locationPushStart) { //已经开启了，就不在执行开启服务操作，等打卡的时候再进行设置
//            return;
//        }

        if (data.isFirst()) {
            int remindMin = data.getRemind_min();
            if (remindMin != 0 && is_rest == 0 && isNeed != 2) { //为0时就是不提醒
                String sign_date = data.getSign_date();
                if (!TextUtils.isEmpty(sign_date)) {
                    String[] signDate = sign_date.split(",");
                    List<String> signDateList =  Arrays.asList(signDate);
                    List<MainIndexBean.DataBean.SignLogBean> sign_log = data.getSign_log();
                    String[] split = signDate[0].split(" ");
                    long logLongDate = 0;
                    if (sign_log.size() > 0) {
                        MainIndexBean.DataBean.SignLogBean signLogBean = sign_log.get(sign_log.size() - 1);
                        String sysDate = signLogBean.getSys_date();
                        sysDate = split[0] + sysDate;
                        logLongDate = DateUtil.convertTimeToLong(sysDate, "yyyy-MM-dd HH:mm:ss");
                    }

                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).startLocationService(signDateList, workType == 2, remindMin * 60 * 1000, logLongDate);
//                        LocationService.startLocationService(getActivity(), signDateList, workType == 2, remindMin * 60 * 1000, logLongDate);
                    }
//                    LocationPushHelper.getInstance().setLocation(signDateList, workType == 2, remindMin * 60 * 1000);
                }
            }
        }
    }

    private void takeCardControl(final MainIndexBean.DataBean data) {
        if (canTakeCard == 1) {
            int state = PreferenceUtils.getInstance(mContext).getIntParam(ConstantValue.TAKE_CARD_STATE, ChangeCardDialog.GPS);
            if (state == ChangeCardDialog.FIELD) { //如果是外勤打卡，等gps可以定位，能够得到地址再访问打卡接口
                String spAddress = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_ADDRESS);
                if (TextUtils.isEmpty(spAddress)) {  //一开始手机没给予定位权限，进行定位请求，防止用户开启手机定位权限，必须再次定位
                    FDLocation.getInstance().setLocationAddressListener(new FDLocation.LocationAddressListener() {
                        @Override
                        public void onAddress(String address) {
                            if (!TextUtils.isEmpty(address)) {
                                HomeFragment.this.address = address;
                                tackCard(data);
                            } else {
                                ToastUtil.show("请给予app定位权限");
                            }
                            //要清除回调，不然可能一直有回调的情况
                            FDLocation.getInstance().removeAddressListener();
                        }
                    });
                } else {
                    address = spAddress;
                    tackCard(data);
                }
            } else if (state == ChangeCardDialog.FACE) { //人脸识别暂时未开放
//                        ToastUtil.show("人脸打开暂未开放");
                String[] stringParam = {Manifest.permission.CAMERA};
                if (EasyPermissions.hasPermissions(mContext, stringParam)) {
                    if (data.getIs_reg_face() == 1) { //1：已注册 2：未注册
                        Intent intent = new Intent(mContext, FaceLivenessExpActivity.class);
                        intent.putExtra("face", 1); //登陆
                        startActivity(intent);
                    } else {
                        new AlertDialog(mContext)
                                .builder()
                                .setMsg("打卡需先录入人脸信息")
                                .setPositiveButton("录入", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mContext, FaceLivenessExpActivity.class);
                                        intent.putExtra("face", 2);  //注册
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();

                    }
                } else {
                    EasyPermissions.requestPermissions(mContext, "请允许知点开启相机权限", BaseActivity.REQUEST_PERMISSION_CAMRE, stringParam);
                }

            } else {  //不是外勤打卡的情况
                tackCard(data);
            }
        } else if (canTakeCard == 2) {
            ToastUtil.show("不在打卡时间范围内");
        } else {
            ToastUtil.show("不在打卡时间范围内");
        }
    }

    //获取第几次打卡，然后获取打卡 long 时间
    private int getWorkNumber(final MainIndexBean.DataBean data) {
        String sign_date = data.getSign_date();
        String[] signDateList = sign_date.split(",");
        if (isNeed == 2 || is_rest != 0) { //无需考勤 和 休息的情况下可以随便打卡，按照顺序打卡
            int i = index + 1;
            if (i > signDateList.length - 1) {
                i = signDateList.length - 1;
            }
            takeCardCurrentTime = signDateList[i];
            if ((i + 1) % 2 == 1) {   //是奇数
                return 1;
            } else {
                return 2;
            }
        } else {
            for (int i = 0; i < timeList.size(); i++) {
                HomeCardBean homeCardBean = timeList.get(i);
                long currentTime = Math.abs(System.currentTimeMillis() - homeCardBean.time);
                homeCardBean.currentTime = currentTime;
                boolean inner = false;
                HomeCardBean homeCardBean2 = null;
                if (i + 1 < timeList.size()) {
                    homeCardBean2 = timeList.get(i + 1);
//                    if (!homeCardBean.canClick && homeCardBean2.canClick) { //可以打卡之间
//                        if (Math.abs(System.currentTimeMillis() - homeCardBean2.time) < signTime) {
//                            inner = true;
//                        }
//                    }

                    //第一个可以打卡， 但是第二个的时间轴也已经过去了，那就只能打下一张卡
                    if (homeCardBean.canClick && System.currentTimeMillis() - homeCardBean2.time < 0) {
                        inner = true;
                        homeCardBean2 = homeCardBean;
                    }
                } else { //最后一个
                    homeCardBean2 = homeCardBean;
                    inner = true;
                }

                if (inner && Math.abs(System.currentTimeMillis() - homeCardBean2.time) < signTime) {
                    if (homeCardBean2.canClick) {
                        int a = timeList.indexOf(homeCardBean2) + 1;
                        if (a % 2 == 1) {   //是奇数
                            return 1;
                        } else {
                            return 2;
                        }
                    }
                }
            }

//            long sysLong = System.currentTimeMillis();
//            for (int i = size; i < signDateList.length; i++) {
//                String time = signDateList[i];
//                HomeCardBean homeCardBean = timeList.get(i);
//                boolean inner = false;
//                if (i + 1 < timeList.size()) {
//                    HomeCardBean homeCardBean2 = timeList.get(i + 1);
//                    if (sysLong <= homeCardBean2.time && homeCardBean.time >= sysLong) {
//                        inner = true;
//                    }
//                } else { //最后一个
//                    inner = true;
//                }
//
//                long signDateLong = DateUtil.convertTimeToLong(time, "yyyy-MM-dd HH:mm:ss");
//                if (Math.abs(sysLong - signDateLong) <= signTime && inner) {
//                    startTakeCardCurrentTime = signDateLong;
//                    takeCardCurrentTime = time;
//                    int a = i + 1;
//                    if (a % 2 == 1) {   //是奇数
//                        return 1;
//                    } else {
//                        return 2;
//                    }
//                }
//            }
        }
        return 0;
    }

    //打卡

    private void tackCard(final MainIndexBean.DataBean data) {

        //是哪次打卡
        int is_work = getWorkNumber(data);
//        int is_work = 1;
        Params params = new Params();
        params.putDeviceNo();
        //是否休息日is_rest
        params.put("ir", data.getIs_rest() + "");
        params.put("in", isNeed + "");
        params.put("iw", is_work + "");
        params.put("sgn", positionName);
        int state = PreferenceUtils.getInstance(mContext).getIntParam(ConstantValue.TAKE_CARD_STATE, ChangeCardDialog.GPS);
        switch (state) {
            case ChangeCardDialog.GPS:
            case ChangeCardDialog.FACE:
                String stringParam = PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_JING_DU);
                String locationString = stringParam
                        + "," + PreferenceUtils.getInstance(mContext).getStringParam(ConstantValue.MAP_WEI_DU);
                if ("0".equals(stringParam) || "".equals(stringParam)) {
                    params.put("slt", "");
                } else {
                    params.put("slt", locationString);
                }

                break;
            case ChangeCardDialog.WIFI:
                params.put("bi", DeviceUtil.getWifiId(mContext));
                break;
//            case ChangeCardDialog.FACE:
//
////                map.put("img", );
//                break;
            case ChangeCardDialog.FIELD:
                params.put("ad", address);
                break;
        }
        params.put("st", state + "");
        params.put("sd", takeCardCurrentTime);

        RequestUtils.createRequest().postMainSign(params.getData()).enqueue(
                new RequestCallback<>(getActivity(), new IRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        locationPushStart = false; //打卡完成后重新开启

                        String msg = baseModel.msg;
                        ToastUtil.show("打卡成功");

                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).clearAlarm();
                        }
                        //打完卡之后立马刷新界面
                        initData();
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        locationPushStart = false; //打卡完成后重新开启
                        ToastUtil.show(errorMsg);
                    }
                }));

    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 100) {
            if (getActivity() != null && !getActivity().isFinishing() && autoTimeText != null) {
                autoTimeText.setText(DateUtil.show24Date());
                if (timeList != null && takeCardLayout != null && headerWorkCardStatusText != null) {
                    if (isNeed == 2 || is_rest != 0) { //无需考勤 和 休息的情况下可以随便打卡，按照顺序打卡
                        if (index + 1 <= timeList.size() - 1) {
                            HomeCardBean homeCardBean = timeList.get(index + 1);
                            if (isNeed == 2) {
                                headerWorkCardStatusText.setText("无需考勤");
                            } else { //休息
                                headerWorkCardStatusText.setText(homeCardBean.name);
                            }
                            if (homeCardBean.canClick) {
                                canTakeCard = 1;
                                positionName = homeCardBean.cardStatusName;
                                if (isToday()) {  //是今天颜色才就变灰
                                    takeCardLayout.setBackgroundResource(R.drawable.punch_card);
                                } else {
                                    takeCardLayout.setBackgroundResource(R.drawable.punch_card_forbidden);
                                }
                            } else { //这个情况下已经打完卡了
                                canTakeCard = 2;
                                takeCardLayout.setBackgroundResource(R.drawable.punch_card_forbidden);
                            }
                        } else {
                            canTakeCard = 3;

                            //无需考勤判断设置
                            if (isNeed == 2) {
                                headerWorkCardStatusText.setText("无需考勤");
                            } else { //休息
                                headerWorkCardStatusText.setText("休息");
                            }

                            takeCardLayout.setBackgroundResource(R.drawable.punch_card_forbidden);
                        }
                    } else { //不是无需考勤
                        boolean isSkip = false;
                        for (int i = 0; i < timeList.size(); i++) {
                            HomeCardBean homeCardBean = timeList.get(i);
                            long currentTime = Math.abs(System.currentTimeMillis() - homeCardBean.time);
                            homeCardBean.currentTime = currentTime;
                            boolean inner = false;
                            HomeCardBean homeCardBean2 = null;
                            if (i + 1 < timeList.size()) {
                                homeCardBean2 = timeList.get(i + 1);
//                                boolean currentTime2 = Math.abs(System.currentTimeMillis() - homeCardBean2.time) < signTime;
//                                if (!homeCardBean.canClick && homeCardBean2.canClick) { //可以打卡之间
//                                    if (currentTime2) {
//                                        inner = true;
//                                    }
//                                }

                                //第一个可以打卡， 但是第二个的时间轴也已经过去了，那就只能打下一张卡
                                if (homeCardBean.canClick && System.currentTimeMillis() - homeCardBean2.time < 0) {
                                    inner = true;
                                    homeCardBean2 = homeCardBean;
                                }

                            } else { //最后一个
                                homeCardBean2 = homeCardBean;
                                inner = true;
                            }

                            if (inner && Math.abs(System.currentTimeMillis() - homeCardBean2.time) < signTime) {
                                if (homeCardBean2.canClick) {
                                    canTakeCard = 1;
                                    positionName = homeCardBean2.cardStatusName;
                                    takeCardCurrentTime = homeCardBean2.stringTime;

                                    //无需考勤判断设置
                                    if (isNeed == 2) {
                                        headerWorkCardStatusText.setText("无需考勤");
                                    } else {
                                        headerWorkCardStatusText.setText(homeCardBean2.name);
                                    }

                                    if (isToday()) {  //是今天颜色才就变灰
                                        takeCardLayout.setBackgroundResource(R.drawable.punch_card);
                                    } else {
                                        takeCardLayout.setBackgroundResource(R.drawable.punch_card_forbidden);
                                    }
                                    isSkip = true;
                                    break;  //只要有一个正常的就结束循环
                                } else {
                                    //打完卡之后继续判断
                                    canTakeCard = 3;

                                    //无需考勤判断设置
                                    if (isNeed == 2) {
                                        headerWorkCardStatusText.setText("无需考勤");
                                    } else {
                                        headerWorkCardStatusText.setText(homeCardBean2.name);
                                    }

                                    takeCardLayout.setBackgroundResource(R.drawable.punch_card_forbidden);
                                }
                            } else {
                                canTakeCard = 2;

                                //无需考勤判断设置
                                if (isNeed == 2) {
                                    headerWorkCardStatusText.setText("无需考勤");
                                } else {
                                    headerWorkCardStatusText.setText(homeCardBean2.name);
                                }

                                takeCardLayout.setBackgroundResource(R.drawable.punch_card_forbidden);

                                //不需要打卡 请假 出差，这三个是没有日志
//                            if (isNeed != 2 && !"请假".equals(homeCardBean.name) && !"休息".equals(homeCardBean.name) && !"出差".equals(homeCardBean.name)) {
//                                break;
//                            }
//                            if (!homeCardBean.isTakeCarded) { //如果没打卡就轮到这个时间点打卡了，就直接出循环
//
//                            }
                            }
                        }

                        if (!isSkip) { //不是有打卡的情况下跳出来的
                            //是否有时间大于现在，且当前没有可以打卡的
                            boolean isHasTimeMaxNow = false;
                            int hasIndex = 0;
                            for (int i = 0; i < timeList.size(); i++) {
                                HomeCardBean homeCardBean = timeList.get(i);
                                if (homeCardBean.time - System.currentTimeMillis() > 0) {
                                    isHasTimeMaxNow = true;
                                    hasIndex = i;
                                    //跳出这个循环，使用最近的一次大于现在的打卡时间
                                    break;
                                }
                            }

                            HomeCardBean homeCardBean;
                            if (isHasTimeMaxNow) {
                                homeCardBean = timeList.get(hasIndex);
                            } else {
                                //取最后的一个值
                                homeCardBean = timeList.get(timeList.size() - 1);
                            }

                            canTakeCard = 2;
                            //无需考勤判断设置
                            if (isNeed == 2) {
                                headerWorkCardStatusText.setText("无需考勤");
                            } else {
                                headerWorkCardStatusText.setText(homeCardBean.name);
                            }
                            takeCardLayout.setBackgroundResource(R.drawable.punch_card_forbidden);
                        }
                    }
                }
                mHandler.sendEmptyMessageDelayed(100, 1000);
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeMessages(100);
            mHandler = null;
        }
    }

    @Subscribe
    public void faceEvent(FaceEvent event) {
        if (event.code == 100) { //打卡
            tackCard(data);
        }
    }

    @Subscribe
    public void faceRegEvent(FaceRegEvent event) {
        initData();
    }

    @Subscribe
    public void changeCompanyEvent(ChangeCompanyEvent event) {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).clearAlarm();
        }
        initData();
    }

    /**
     * 判断是不是今天
     *
     * @return
     */
    public boolean isToday() {
        if (calendarView == null) {
            return false;
        }

        CalendarDay selectedDate = calendarView.getSelectedDate();
        if (selectedDate == null) {
            return false;
        }
        int year = selectedDate.getYear();
        int month = selectedDate.getMonth();
        int day = selectedDate.getDay();


        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        if (from.getYear() == year && from.getMonth() == month && from.getDay() == day) {
            return true;
        } else {
            return false;
        }
    }

    @Subscribe
    public void messageCenterEventMethod(MessageCenterEvent event) {
        initData();
    }

    boolean locationPushStart = false; //

    private void abnormalRequest(String startDate, String endDate) {
        Params params = new Params();
        params.put("sd", startDate);
        params.put("ed", endDate);
        RequestUtils.createRequest().postMainWeek(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<MainWeekBean>() {
            @Override
            public void onSuccess(MainWeekBean mainWeekBean) {
                MainWeekBean.DataBean data = mainWeekBean.getData();
                if (data != null) {
                    if (calendarView != null) {
                        List<String> abnormal = data.getAbnormal();
                        if (abnormal != null && abnormal.size() > 0) {
                            if (eventDecorator != null) {
                                calendarView.removeDecorator(eventDecorator);
                            }
                            eventDecorator = new EventDecorator(Color.RED, abnormal);
                            calendarView.addDecorator(eventDecorator);
                        }
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                showToast(errorMsg);
            }
        }));
    }
}
