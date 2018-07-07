package com.sdxxtop.zhidian.widget.decorator;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

/**
 * Created by Administrator on 2018/5/14.
 */

public class ClassDateNameDecorator implements DayViewDecorator {
    private String short_name;
    private Context context;
    private CalendarDay calendarDay;

    public ClassDateNameDecorator(Context context, String short_name, CalendarDay calendarDay) {
        this.context = context;
        this.short_name = short_name;
        this.calendarDay = calendarDay;
    }

    public ClassDateNameDecorator(Context context) {
        this.context = context;
        short_name = "";
    }

    public void setShortName(String shortName, CalendarDay calendarDay) {
        short_name = shortName;
        this.calendarDay = calendarDay;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (calendarDay == null) {
            return false;
        }
        return calendarDay.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new TextSpan(0xFF444444, short_name));
    }
}
