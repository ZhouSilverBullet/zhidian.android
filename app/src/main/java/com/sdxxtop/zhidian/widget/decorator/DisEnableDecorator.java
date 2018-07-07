package com.sdxxtop.zhidian.widget.decorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/5.
 */

public class DisEnableDecorator implements DayViewDecorator {


    private CalendarDay calendarDay;
    private final int day1;
    private final int month;
    private final int year;

    public DisEnableDecorator() {
        calendarDay = CalendarDay.from(Calendar.getInstance().getTime());
        day1 = calendarDay.getDay();
        month = calendarDay.getMonth();
        year = calendarDay.getYear();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

//        if (day.getYear() > year) {
//            return true;
//        }
//
//        if (day.getYear() == year && day.getMonth() > month) {
//            return true;
//        }
//
//        if (day.getYear() == year && day.getMonth() == month && day.getDay() > day1) {
//            return true;
//        }

        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(false);
    }
}
