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

public class ClassDateDecorator implements DayViewDecorator {
    private List<AttendanceWatchBean.DataBean.AndroidClassDateBean> classDateBeanList;
    private String short_name;
    private Context context;
    private int position = -1;
    private HashSet<CalendarDay> dates;

    public ClassDateDecorator(Context context, List<AttendanceWatchBean.DataBean.AndroidClassDateBean> classDateBeanList, List<String> holiday) {
        this.context = context;
        Collection<CalendarDay> dates = new ArrayList<>();
        for (AttendanceWatchBean.DataBean.AndroidClassDateBean classDateBean : classDateBeanList) {
            String class_date = classDateBean.getClass_date();
            boolean isSame = false; //循环是否相同
            for (int i = 0; i < holiday.size(); i++) {
                String s = holiday.get(i);
                if (s.equals(class_date)) {
                    isSame = true;
                }
            }
            if (!isSame) {
                dates.add(new CalendarDay(new Date(DateUtil.convertTimeToLong(class_date, "yyyy-MM-dd"))));
            }
        }
        if (classDateBeanList.size() > 0) {
            short_name = classDateBeanList.get(0).getShort_name();
        }
        this.classDateBeanList = classDateBeanList;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        ++position;
        LogUtils.e("ClassDateDecorator", "position = " + position + "  " + classDateBeanList.get(position).getShort_name());
        view.addSpan(new TextSpan(0xFF444444, classDateBeanList.get(position).getShort_name()));
    }
}
