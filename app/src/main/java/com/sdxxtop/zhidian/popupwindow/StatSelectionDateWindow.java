package com.sdxxtop.zhidian.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.othershe.calendarview.utils.CalendarUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.widget.decorator.MaxTodayDecorator;
import com.sdxxtop.zhidian.widget.decorator.TodayDecorator;

import java.util.Calendar;
import java.util.Date;

/**
 * 作者：${刘香龙} on 2018/5/2 0002 18:16
 * 类的描述：
 */
public class StatSelectionDateWindow extends PopupWindow {

    private boolean isStat;
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
    private TextView dateTitle;
    private View dateBottomLayout;

    /**
     *
     * @param context
     * @param isShowBottom
     * @param isStat 统计的时候头部只显示 xxxx年xx日
     */
    public StatSelectionDateWindow(Context context, boolean isShowBottom, boolean isStat) {
        this.mContext = context;
        this.isShowBottom = isShowBottom;
        this.isStat = isStat;

        init();

    }

    private void init() {
        inflater = LayoutInflater.from(mContext);

        cDate = CalendarUtil.getCurrentDate();

        initView();

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void initView() {
        View view = inflater.inflate(R.layout.item_selection_date_window, null);
        this.setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.material_calendar);
        Calendar instance = Calendar.getInstance();

        materialCalendarView.state().edit().setMaximumDate(getLastDayOfMonth())
                .commit();
        materialCalendarView.setTopbarVisible(false);
        tvTitle = (TextView) view.findViewById(R.id.tv_date);
        llBottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        dateBottomLayout = view.findViewById(R.id.date_bottom_layout);

        if (isShowBottom) {
            llBottom.setVisibility(View.VISIBLE);
        } else {
            llBottom.setVisibility(View.GONE);
            dateBottomLayout.setVisibility(View.GONE);
        }

        materialCalendarView.setSelectedDate(instance);

        if (isStat) {
            tvTitle.setText(DateUtil.getChineseYearAndMonthDate(materialCalendarView.getSelectedDate()));
        } else {
            tvTitle.setText(cDate[0] + "年" + cDate[1] + "月" + cDate[2] + "日");
        }

        currentDate = cDate[0] + "-" + cDate[1] + "-" + cDate[2];

        stringDate = DateUtil.getSelectorDate(materialCalendarView.getSelectedDate());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (compareToday2(date)) {
//                    ToastUtil.show("没有更早的内容");
                    materialCalendarView.setDateSelected(date, false);
                    return;
                }
//                if (isStat) {
//                    tvTitle.setText(DateUtil.getChineseYearAndMonthDate(date));
//                } else {
//                    tvTitle.setText(DateUtil.getChineseSelectorDate(date));
//                }
                stringDate = DateUtil.getSelectorDate(date);
                dateTitle.setText(stringDate);

                if (listener != null) {
                    listener.onSelector(stringDate, date);
                }
            }
        });

        //确定
        view.findViewById(R.id.btn_determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });


        materialCalendarView.addDecorators(new TodayDecorator(mContext), new MaxTodayDecorator(mContext));

        dateTitle = (TextView) view.findViewById(R.id.item_selector_date_title);

    }

    public void setSelectorDate(CalendarDay date) {
        if (materialCalendarView != null) {
            materialCalendarView.setSelectedDate(date);
        }
    }


    private SelectorDateListener listener;

    public void setSelectorDateListener(SelectorDateListener listener) {
        this.listener = listener;
    }

    public interface SelectorDateListener {
        void onSelector(String date, CalendarDay selectorCalendarDay);
    }

    public boolean compareToday2(CalendarDay date) {
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


    /**
     * 获取本月的最后一天
     * @return
     */
    public Date getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDayOfMonth = calendar.getTime();
        return lastDayOfMonth;
    }
}
