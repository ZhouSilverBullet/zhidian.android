package com.sdxxtop.zhidian.entity;

/**
 * 作者：CaiCM
 * 日期：2018/5/5  时间：19:47
 * 邮箱：15010104100@163.com
 * 描述：我的Fragment权限展示实体
 */

public class PowerBean {
    private String name;
    private int id;
    private int img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "PowerBean{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
