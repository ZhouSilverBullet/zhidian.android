package com.sdxxtop.zhidian.widget.decorator;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Administrator on 2018/9/1.
 */

public class MyDayFormatter implements DayFormatter {
    private final DateFormat dateFormat;

    /**
     * Format using a default format
     */
    public MyDayFormatter() {
        this.dateFormat = new SimpleDateFormat("d", Locale.getDefault());
    }

    /**
     * Format using a specific {@linkplain DateFormat}
     *
     * @param format the format to use
     */
    public MyDayFormatter(@NonNull DateFormat format) {
        this.dateFormat = format;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public String format(@NonNull CalendarDay day) {
        return dateFormat.format(day.getDate()) + "\n春分";
    }

    public static class DayEntry {

    }
}
