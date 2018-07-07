package com.sdxxtop.zhidian.entity;

/**
 * Created by Administrator on 2018/5/4.
 * 打卡的一个辅助类
 * 是否打卡，打去哪一天的
 */

public class HomeCardBean {
    public long time;
    public String stringTime;
    //打卡的名称
    public String name;

    //打卡名称 不包含休息，只有下班和上班
    public String cardStatusName;


    public boolean canClick;
    public boolean isTakeCarded; //是否已经打卡

    public long currentTime;
}
