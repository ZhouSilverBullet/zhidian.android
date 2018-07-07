package com.sdxxtop.zhidian.utils;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Administrator on 2018/6/23.
 */

public class ATSpan extends ClickableSpan {

    private int userId;
    private int parentId;

    public ATSpan(int userId, int parentId) {
        this.userId = userId;
        this.parentId = parentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public void onClick(View widget) {
//        Toast.makeText(widget.getContext(), "toast", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#FF5DB5F4"));
    }
}
