package com.sdxxtop.zhidian.adapter;

/**
 * Created by Administrator on 2018/8/2.
 */

public interface ISelector {
    void checkAll(boolean isAdd);

    boolean iteratorAllValue();

    public interface CheckListener {
        void onCheck();
    }
}
