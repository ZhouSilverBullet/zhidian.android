package com.sdxxtop.zhidian.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.StatPeopleBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.popupwindow.StatSelectionDateWindow;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.GridDividerItemDecoration;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 人员出勤
 */
public class StatPeopleActivity extends BaseActivity {

    private StatPeoAdapter mAdapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_stat_people;
    }

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
    @BindView(R.id.stat_people_total)
    TextView peopleTotal;
    @BindView(R.id.stat_people_recycler)
    RecyclerView recyclerView;


    private String textValue;
    private StatSelectionDateWindow selectionDateWindow;
    private String chineseSelectorDate;

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.addItemDecoration(
                new GridDividerItemDecoration(ViewUtil.dp2px(mContext, 1)
                        , getResources().getColor(R.color.recycler_text_item_line_color)));

        mAdapter = new StatPeoAdapter(R.layout.item_stat_people_recycler);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        //根据type来进行区分一些操作
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        textValue = "人员出勤";

        daySelectorText.setText("选择天");
        upDayText.setText("上一天");
        downDayText.setText("下一天");
        chineseSelectorDate = DateUtil.getChineseSelector0Date(from);
        loadData(DateUtil.getSelectorDate(from));

        dayTitle.setText(chineseSelectorDate + textValue);
    }

    @Override
    protected void initEvent() {
        daySelectorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDateWindow();
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
                upDown(1);
            }
        });

        upDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDown(-1);
            }
        });

        todayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                CalendarDay from = CalendarDay.from(instance);
                chineseSelectorDate = DateUtil.getChineseSelector0Date(from);
                loadData(DateUtil.getSelectorDate(from));
                dayTitle.setText(chineseSelectorDate + textValue);
            }
        });
    }

    private void todayVisibility() {
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        String dayChineseSelectorDate = "";
        dayChineseSelectorDate = DateUtil.getChineseSelector0Date(from);
        if (!dayChineseSelectorDate.equals(chineseSelectorDate)) {
            todayText.setVisibility(View.VISIBLE);
            downDayText.setTextColor(getResources().getColor(R.color.blue));
        } else {
            downDayText.setTextColor(getResources().getColor(R.color.texthintcolor));
            todayText.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否是今天，然后判断下一天的颜色值和点击
     *
     * @return
     */
    public boolean isToday() {
        Calendar instance = Calendar.getInstance();
        CalendarDay from = CalendarDay.from(instance);
        String dayChineseSelectorDate = "";
        dayChineseSelectorDate = DateUtil.getChineseSelector0Date(from);
        if (!dayChineseSelectorDate.equals(chineseSelectorDate)) {
            return false;
        } else {
            return true;
        }
    }

    private void upDown(int day) {
        String[] upOrDownDay = Date2Util.getUpOrDownDay(chineseSelectorDate, day);
        if (upOrDownDay != null) {
            String showDate = upOrDownDay[0];
            String loadDate = upOrDownDay[1];
            chineseSelectorDate = showDate;
            dayTitle.setText(showDate + textValue);
            loadData(loadDate);
        }
    }


    public void loadData(String ddAndMd) {

        todayVisibility();

        Params params = new Params();
        params.put("tt", 1); //1:今日 2:月份
        params.put("dd", ddAndMd); //日参数
        RequestUtils.createRequest().postStatPeople(params.getData())
                .enqueue(new RequestCallback<>(new IRequestListener<StatPeopleBean>() {
                    @Override
                    public void onSuccess(StatPeopleBean contactPartBean) {
                        StatPeopleBean.DataBean data = contactPartBean.getData();
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


    private void handleData(StatPeopleBean.DataBean data) {
        int total_num = data.getTotal_num();
        int true_num = data.getTrue_num();

        peopleTotal.setText(true_num + "/" + total_num);

        /**
         * "not_num" : 66, //未到
         "late_num" : 0,//迟到
         "early_num" : 0,//早退
         "leave_num": 0,//请假
         "bus_num": 0,//出差
         "out_num" : 0,//外勤
         "overtime_num" : 0,//申请加班
         "absent_num" : 0,//旷工
         */
        List<PeopleBean> stringList = new ArrayList<>();
        stringList.add(new PeopleBean(data.getNot_num(), "未到"));
        stringList.add(new PeopleBean(data.getLate_num(), "迟到"));
        stringList.add(new PeopleBean(data.getEarly_num(), "早退"));
        stringList.add(new PeopleBean(data.getLeave_num(), "请假"));
        stringList.add(new PeopleBean(data.getBus_num(), "出差"));
        stringList.add(new PeopleBean(data.getOut_num(), "外勤"));
        stringList.add(new PeopleBean(data.getOvertime_num(), "申请加班"));
        stringList.add(new PeopleBean(data.getAbsent_num(), "旷工"));
        stringList.add(new PeopleBean(0, ""));

        mAdapter.replaceData(stringList);
    }

    class PeopleBean {
        public int num;
        public String value;

        public PeopleBean(int num, String value) {
            this.num = num;
            this.value = value;
        }
    }


    private void showSelectDateWindow() {
        if (selectionDateWindow == null) {
            selectionDateWindow = new StatSelectionDateWindow(this, false, true);
            int weight = getResources().getDisplayMetrics().widthPixels;
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
                    dayTitle.setText(getTitleDate(date) + "人员出勤");
                    loadData(date);
                }
            });
        }

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        selectionDateWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);
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

    class StatPeoAdapter extends BaseQuickAdapter<PeopleBean, BaseViewHolder> {

        public StatPeoAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, PeopleBean item) {
            TextView peopleText = helper.getView(R.id.item_stat_people_text);
            TextView peopleDec = helper.getView(R.id.item_stat_people_dec);

            if (TextUtils.isEmpty(item.value)) {
                peopleText.setVisibility(View.GONE);
                peopleDec.setVisibility(View.GONE);
            } else {
                peopleText.setVisibility(View.VISIBLE);
                peopleDec.setVisibility(View.VISIBLE);
                peopleText.setText(item.num + "");
                peopleDec.setText(item.value + "");
            }
        }
    }
}
