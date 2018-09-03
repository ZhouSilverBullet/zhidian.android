package com.sdxxtop.zhidian.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/18.
 */

public class ParentSelectBean implements Serializable {
    //班级
    public static final int TYPE_GRADE = 20;
    //学生
    public static final int TYPE_STUDENT = 21;

    public int type;
    public String value;
    //用于bottomSelectorView 的一个展示名字
    public String name;
    public int studentId;
    public int gradeId;
    public String color;
}