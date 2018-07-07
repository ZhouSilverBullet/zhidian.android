package com.sdxxtop.zhidian.widget.decorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.sdxxtop.zhidian.utils.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 * 设置小红点的Decorator
 */

public class AttendanceEventDecorator implements DayViewDecorator {
    private int color;
    private HashSet<CalendarDay> dates;

    public AttendanceEventDecorator(int color, List<String> abnormal) {
        this.color = color;
        //后台给予的日期需要解析一下
        Collection<CalendarDay> dates = new ArrayList<>();
        for (int i = 0; i < abnormal.size(); i++) {
            String s = abnormal.get(i);
            dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(s, "yyyy-MM-dd"))));
        }

        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(8, color));
    }
}
