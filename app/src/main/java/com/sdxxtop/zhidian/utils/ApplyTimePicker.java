package com.sdxxtop.zhidian.utils;

import android.app.Activity;

import com.sdxxtop.zhidian.R;

import java.util.Calendar;

import cn.addapp.pickers.picker.DateTimePicker;

/**
 * Created by Administrator on 2018/6/1.
 */

public class ApplyTimePicker {

    public static void timePicker(Activity context, Calendar calendar, DateTimePicker.OnDateTimePickListener listener) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        DateTimePicker picker = new DateTimePicker(context, DateTimePicker.HOUR_24);
        picker.setActionButtonTop(true);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2025, 11, 11);
        picker.setTimeRangeStart(0, 0);
        picker.setTimeRangeEnd(23, 59);
        picker.setTopBackgroundColor(0xFFEEEEEE);
        int h = (int) context.getResources().getDimension(R.dimen.y601);
        picker.setHeight(h);
        picker.setTopHeight(52);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(18);
        picker.setCancelTextColor(0xFF999999);
        picker.setCancelTextSize(18);
        picker.setSubmitTextColor(0xFF3296FA);
        picker.setSubmitTextSize(18);

        picker.setTextSize(20);
        picker.setBackgroundColor(0xFFffffff);

        picker.setLabel("年", "月", "日", "时", "分");
        if (listener != null) {
            picker.setOnDateTimePickListener(listener);
        }
        picker.setSelectedItem(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.show();
    }

}
