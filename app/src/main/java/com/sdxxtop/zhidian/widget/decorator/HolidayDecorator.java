package com.sdxxtop.zhidian.widget.decorator;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
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

public class HolidayDecorator implements DayViewDecorator {
    private Context context;
    private HashSet<CalendarDay> dates;

    public HolidayDecorator(Context context, List<String> holiday) {
        this.context = context;
        Collection<CalendarDay> dates = new ArrayList<>();
        for (String s : holiday) {
            dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(s, "yyyy-MM-dd"))));
        }
        this.dates = new HashSet<>(dates);
    }

    public HolidayDecorator(Context context) {
        this.dates = new HashSet<>();
    }

    public void setHoidayData(List<String> holiday) {
        this.context = context;
        Collection<CalendarDay> dates = new ArrayList<>();
        for (String s : holiday) {
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
        if (dates.contains(day)) {
            LogUtils.e("HolidyDecorator", "shouldDecorate = " + day.toString());
        }
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        LogUtils.e("HolidyDecorator", "position = ");
        view.addSpan(new TextSpan(0xFF444444, "休"));
    }
}
