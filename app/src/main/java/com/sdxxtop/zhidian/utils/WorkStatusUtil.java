package com.sdxxtop.zhidian.utils;

/**
 * Created by Administrator on 2018/5/4.
 */

public interface WorkStatusUtil {
    //(1:正常 2:迟到减免 3:早退减免4消迟到早退 5:迟到 6:早退 7:旷工 8:漏打卡)
    int WORK_NORMAL = 1;
    int WORK_LATE_BREAKS = 2;
    int WORK_LEAVE_EARLY_BREAKS = 3;
    int WORK_LATE_ARRIVAL = 4;
    int WORK_LATE = 5;
    int WORK_LEAVE_EARLY = 6;
    int WORK_ABSENTEEISM = 7;
    int WORK_CLOCK_IN_LEAKAGE = 8;

}
