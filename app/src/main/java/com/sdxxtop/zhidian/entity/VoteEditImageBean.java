package com.sdxxtop.zhidian.entity;

/**
 * Created by Administrator on 2018/5/19.
 */

public class VoteEditImageBean {
    public static final int TYPE_TEXT = 1; //文字
    public static final int TYPE_IMAGE = 2; //图文

    public String editValue;
    public String imagePath;
    public int type;

    public VoteEditImageBean(String editValue, String imagePath, int type) {
        this.editValue = editValue;
        this.imagePath = imagePath;
        this.type = type;
    }
}
