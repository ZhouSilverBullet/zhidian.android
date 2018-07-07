package com.sdxxtop.zhidian.http;

/**
 * Created by Administrator on 2018/5/7.
 */

public interface IRequestListener2<T> {
    void onSuccess(T t);
    void onFailure(int code, String errorMsg);
}
