package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/4/6  时间：13:14
 * 邮箱：15010104100@163.com
 * 描述：公司规模选择器数据
 */

public class CompanySizeData {
    private int id;
    private String name;

    public CompanySizeData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name ;
    }
}
