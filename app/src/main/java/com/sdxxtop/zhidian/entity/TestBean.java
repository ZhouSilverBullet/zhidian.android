package com.sdxxtop.zhidian.entity;

/**
 * 作者：${刘香龙} on 2018/4/28 0028 12:21
 * 类的描述：
 */
public class TestBean {
    String string1;
    boolean isSelected;
    int leave_type;

    public TestBean(String string1, boolean isSelected, int type) {
        this.string1 = string1;
        this.isSelected = isSelected;
        leave_type = type;
    }

    public void setLeave_type(int leave_type) {
        this.leave_type = leave_type;
    }

    public int getLeave_type() {
        return leave_type;
    }

    public TestBean(String string1, String string2, String string3, String string4, String string5, String string6) {
        this.string1 = string1;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }
}
