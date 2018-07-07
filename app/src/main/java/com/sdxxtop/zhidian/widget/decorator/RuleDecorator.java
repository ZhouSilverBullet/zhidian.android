package com.sdxxtop.zhidian.widget.decorator;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.sdxxtop.zhidian.entity.AttendanceWatchBean;
import com.sdxxtop.zhidian.utils.DateUtil;
import com.sdxxtop.zhidian.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class RuleDecorator implements DayViewDecorator {
    private String short_name;
    private HashSet<CalendarDay> dates;

    ///
    public RuleDecorator(Context context, AttendanceWatchBean.DataBean.RuleBean rule,
                         List<AttendanceWatchBean.DataBean.AndroidClassDateBean> classDateBeanList,
                         List<String> holiday, List<String> weekendInMonth) {

        Collection<CalendarDay> dates = new ArrayList<>();
        for (AttendanceWatchBean.DataBean.AndroidClassDateBean classDateBean : classDateBeanList) {
            String class_date = classDateBean.getClass_date();
            dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(class_date, "yyyy-MM-dd"))));
        }

        for (String s : holiday) {
            dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(s, "yyyy-MM-dd"))));
        }

        if (weekendInMonth != null) {
            for (String s : weekendInMonth) {
                dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(s, "yyyy-MM-dd"))));
            }
        }

        short_name = rule.getShort_name();
        this.dates = new HashSet<>(dates);
    }
    ///

    public RuleDecorator(Context context) {
        this.dates = new HashSet<>();
    }

    public void setDates(AttendanceWatchBean.DataBean.RuleBean rule,
                         List<AttendanceWatchBean.DataBean.AndroidClassDateBean> classDateBeanList,
                         List<String> holiday, List<String> weekendInMonth) {

        Collection<CalendarDay> dates = new ArrayList<>();
        for (AttendanceWatchBean.DataBean.AndroidClassDateBean classDateBean : classDateBeanList) {
            String class_date = classDateBean.getClass_date();
            dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(class_date, "yyyy-MM-dd"))));
        }

        for (String s : holiday) {
            dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(s, "yyyy-MM-dd"))));
        }

        if (weekendInMonth != null) {
            for (String s : weekendInMonth) {
                dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(s, "yyyy-MM-dd"))));
            }
        }

        short_name = rule.getShort_name();
        if (this.dates == null) {
            this.dates = new HashSet<>(dates);
        } else {
            this.dates.addAll(dates);
        }
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (!dates.contains(day)) {
            LogUtils.e("RuleDecorator", "shouldDecorate = " + day.toString());
        }
        return !dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new TextSpan(0xFF444444, short_name));
    }
}
