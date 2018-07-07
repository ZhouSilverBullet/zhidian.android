package com.sdxxtop.zhidian.popupwindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.othershe.calendarview.utils.CalendarUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.ApplyMonthBean;
import com.sdxxtop.zhidian.entity.MainIndexBean;
import com.sdxxtop.zhidian.entity.SelectionDateBean;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.utils.Date2Util;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.FullyLinearLayoutManager;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.ToastUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.decorator.CalendarSelectorDecorator;
import com.sdxxtop.zhidian.widget.decorator.MaxTodayDecorator;
import com.sdxxtop.zhidian.widget.decorator.TodayDecorator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 作者：${刘香龙} on 2018/5/2 0002 18:16
 * 类的描述：
 */
public class SelectionDateWindow extends PopupWindow {

    private Calendar calendar;
    private LayoutInflater inflater;
    private Context mContext;

    private MaterialCalendarView materialCalendarView;
    private TextView tvTitle;
    private LinearLayout llBottom;

    private boolean isShowBottom;
    int[] cDate;
    public String stringDate;
    private String currentDate;
    private RecyclerView dateRecycler;
    private int at;
    private TextView dateTitle;
    private CalendarAdapter adapter;
    private EventDecorator eventDecorator;

    private void init() {
        inflater = LayoutInflater.from(mContext);
        if (calendar == null) {
            cDate = CalendarUtil.getCurrentDate();
        } else {
            cDate = CalendarUtil.getCurrentDate(calendar);
        }

        initView();
        initData();

        abnormalDateLoad();
    }

    public void setSelectedDate(Calendar calendar) {
        if (materialCalendarView != null) {
            materialCalendarView.setSelectedDate(calendar);
            cDate = CalendarUtil.getCurrentDate(calendar);
            currentDate = cDate[0] + "-" + cDate[1] + "-" + cDate[2];
            stringDate = DateUtil.getSelectorDate(materialCalendarView.getSelectedDate());
            tvTitle.setText(cDate[0] + "年" + cDate[1] + "月" + cDate[2] + "日");
            dateTitle.setText(stringDate);
            initData();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public SelectionDateWindow(Context context, boolean isShowBottom, int at, Calendar calendar) {
        this.mContext = context;
        this.isShowBottom = isShowBottom;
        this.at = at;
        this.calendar = calendar;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void initView() {
        View view = inflater.inflate(R.layout.item_selection_date_window, null);
        this.setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable());

        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.material_calendar);
        if (at != 8) {
            materialCalendarView.state().edit().setMaximumDate(Date2Util.getLastDayOfMonth())
                    .commit();
        } else {
            materialCalendarView.state().edit()
                    .commit();
        }
        materialCalendarView.setTopbarVisible(false);
        tvTitle = (TextView) view.findViewById(R.id.tv_date);
        llBottom = (LinearLayout) view.findViewById(R.id.ll_bottom);

        if (isShowBottom) {
            llBottom.setVisibility(View.VISIBLE);
        } else {
            llBottom.setVisibility(View.GONE);
        }

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        materialCalendarView.setSelectedDate(calendar);
        materialCalendarView.setCurrentDate(calendar);

        tvTitle.setText(DateUtil.getChineseYearAndMonthDate(materialCalendarView.getSelectedDate()));

        currentDate = cDate[0] + "-" + cDate[1] + "-" + cDate[2];

        stringDate = DateUtil.getSelectorDate(materialCalendarView.getSelectedDate());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                stringDate = DateUtil.getSelectorDate(date);
//                dateTitle.setText(stringDate);
                initData();
            }
        });

        //确定
        view.findViewById(R.id.btn_determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && adapter != null) {  //把选择的数据传回去
                    List<MainIndexBean.DataBean.SignLogBean> data = adapter.getData();
                    List<MainIndexBean.DataBean.SignLogBean> list = new ArrayList<>();
                    for (MainIndexBean.DataBean.SignLogBean datum : data) {
                        if (datum.isSelector()) {
                            list.add(datum);
                        }
                    }
                    listener.onSelector(TextUtils.isEmpty(stringDate) ? currentDate : stringDate, list);
                }
                dismiss();
            }
        });


        view.findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        view.findViewById(R.id.image_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialCalendarView.goToPrevious();
            }
        });

        view.findViewById(R.id.image_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialCalendarView.goToNext();
            }
        });

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                tvTitle.setText(DateUtil.getChineseYearAndMonthDate(date));
                abnormalDateLoad();
            }
        });

        eventDecorator = new EventDecorator(Color.RED);
        if (at != 8) {
            materialCalendarView.addDecorators(new CalendarSelectorDecorator(mContext),
                    new TodayDecorator(mContext),
                    eventDecorator, new MaxTodayDecorator(mContext));
        } else {
            materialCalendarView.addDecorators(new CalendarSelectorDecorator(mContext),
                    new TodayDecorator(mContext), eventDecorator);
        }

        dateTitle = (TextView) view.findViewById(R.id.item_selector_date_title);

        dateRecycler = (RecyclerView) view.findViewById(R.id.item_selector_date_recycler);
        dateRecycler.setLayoutManager(new FullyLinearLayoutManager(mContext));
        adapter = new CalendarAdapter(R.layout.item_selector_date_recycler);
        dateRecycler.setAdapter(adapter);
        dateRecycler.addItemDecoration(new ItemDivider());
        dateRecycler.setFocusable(false);
        TextView emptyView = new TextView(mContext);
        if (at == 2) {
            emptyView.setText("该天没有漏打卡记录");
        } else {
            emptyView.setText("该天没有迟到早退记录");
        }
        emptyView.setGravity(Gravity.CENTER);
        adapter.setEmptyView(emptyView);
        ViewGroup.LayoutParams layoutParams = emptyView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewUtil.dp2px(mContext, 120);
    }

    private void initData() {
        if (at == 8) { //调班不请求接口
            return;
        }
        Params params = new Params();
        params.put("at", at);
        params.put("sd", TextUtils.isEmpty(stringDate) ? currentDate : stringDate);
        RequestUtils.createRequest().postApplyMainDay(params.getData()).enqueue(
                new RequestCallback<>(new IRequestListener<SelectionDateBean>() {
                    @Override
                    public void onSuccess(SelectionDateBean selectionDateBean) {
                        if (materialCalendarView == null) {
                            return;
                        }
                        SelectionDateBean.DataBean data = selectionDateBean.getData();
                        if (data != null && data.getSign_log() != null) {
                            String class_name = data.getClass_name();
                            String showDate = DateUtil.getChineseMonthAndDay(stringDate);
                            if (TextUtils.isEmpty(class_name)) {
                                dateTitle.setText(showDate);
                            } else {
                                dateTitle.setText(showDate + "   " + class_name);
                            }
                            recyclerData(data.getSign_log());
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }
                }));

    }

    private void abnormalDateLoad() {
        if (at == 8) { //调班不请求接口
            return;
        }
        if (materialCalendarView == null) {
            return;
        }
        CalendarDay currentDate = materialCalendarView.getCurrentDate();
        String[] yearList = DateUtil.getStartEndYear(currentDate);

        if (yearList == null) {
            return;
        }

        Params params = new Params();
        params.put("at", at);
        params.put("sd", yearList[0]);
        params.put("ed", yearList[1]);
        RequestUtils.createRequest().postApplyMonth(params.getData()).enqueue(
                new RequestCallback<>(new IRequestListener<ApplyMonthBean>() {
                    @Override
                    public void onSuccess(ApplyMonthBean MainIndexBean) {
                        if (materialCalendarView == null) {
                            return;
                        }

                        ApplyMonthBean.DataBean data = MainIndexBean.getData();
                        if (data != null && data.getAbnormal() != null) {
                            eventDecorator.setDates(data.getAbnormal());
                            materialCalendarView.invalidateDecorators();
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }
                }));
    }

    private void recyclerData(List<MainIndexBean.DataBean.SignLogBean> logBeanList) {
        List<MainIndexBean.DataBean.SignLogBean> tempList = new ArrayList<>();
        for (int i = 0; i < logBeanList.size(); i++) {
            MainIndexBean.DataBean.SignLogBean signLogBean = logBeanList.get(i);
            signLogBean.setSelector(true);
            int status = signLogBean.getStatus();
            if (at == 2) { //筛选出漏打卡的
                if (status == 8) {
                    tempList.add(signLogBean);
                }
            } else if (at == 3) { //筛选出迟到早退
                if (status == 5 || status == 6) {
                    tempList.add(signLogBean);
                }
            }
        }

        adapter.replaceData(tempList);
    }


    private class CalendarAdapter extends BaseQuickAdapter<MainIndexBean.DataBean.SignLogBean, BaseViewHolder> {

        public CalendarAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final MainIndexBean.DataBean.SignLogBean item) {
            TextView typeText = helper.getView(R.id.item_selector_date_type_text);
            TextView cardText = helper.getView(R.id.item_selector_date_type_card);
            final TextView selectorText = helper.getView(R.id.item_selector_date_type_selector);
            if (at == 2) {
                String sign_time = item.getSign_time();
                if (!TextUtils.isEmpty(sign_time)) {
//                    Date date = new Date(DateUtil.convertTimeToLong(sign_time, "yyyy-MM-dd HH:mm:ss"));
//                    Calendar instance = Calendar.getInstance();
//                    instance.setTime(date);
//                    int hour = instance.get(Calendar.HOUR_OF_DAY);
//                    int minute = instance.get(Calendar.MINUTE);
//                    String hourString = "";
//                    String minuteString = "";
//                    if (hour < 10) {
//                        hourString = "0" + hour;
//                    } else {
//                        hourString = "" + hour;
//                    }
//                    if (minute < 10) {
//                        minuteString = "0" + minute;
//                    } else {
//                        minuteString = "" + minute;
//                    }

                    typeText.setText(item.getSign_name() + "打卡：" + sign_time);
                }
                cardText.setText("漏打卡");
            } else if (at == 3) {
                int status = item.getStatus();
                if (status == 5) {
                    cardText.setText("迟到：" + item.getMinute() + "分钟");
                } else if (status == 6) {
                    cardText.setText("早退：" + item.getMinute() + "分钟");
                }

                typeText.setText(item.getSign_name() + "打卡：" + item.getSys_date());
            }
            selectorText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isSelector()) {
                        item.setSelector(false);
                        selectorText.setBackgroundResource(R.drawable.unselected);
                    } else {
                        item.setSelector(true);
                        selectorText.setBackgroundResource(R.mipmap.select);
                    }
                }
            });

        }
    }

    private SelectorDateListener listener;

    public void setSelectorDateListener(SelectorDateListener listener) {
        this.listener = listener;
    }

    public interface SelectorDateListener {
        void onSelector(String date, List<MainIndexBean.DataBean.SignLogBean> selectorList);
    }


    // 设置小红点的Decorator
    class EventDecorator implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color) {
            this.color = color;
            this.dates = new HashSet<>();
        }

        public void setDates(List<String> abnormal) {
            //后台给予的日期需要解析一下
            Collection<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < abnormal.size(); i++) {
                String s = abnormal.get(i);
                dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(s, "yyyy-MM-dd"))));
            }
            if (this.dates == null) {
                this.dates = new HashSet<>(dates);
            } else {
                this.dates.addAll(dates);
            }
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(8, color) {
                @Override
                public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNum) {
                    int oldColor = paint.getColor();
                    int color = Color.RED;
                    if (color != 0) {
                        paint.setColor(color);
                    }
                    canvas.drawCircle((left + right) / 2, bottom + baseline - 5 , 5, paint);
                    paint.setColor(oldColor);
                }
            });
        }
    }
}
