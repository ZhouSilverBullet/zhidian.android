package com.sdxxtop.zhidian.http;

import com.sdxxtop.zhidian.model.ApiService;
import com.sdxxtop.zhidian.model.ConstantValue;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：Retrofit网络请求工具类
 */

public class RequestUtils {
    public static RequestUtils requestUtils;
    public static Retrofit retrofit;

    //工具类的单例模式
    public static RequestUtils getInstance() {
        if (requestUtils == null) {
            synchronized (RequestUtils.class) {
                requestUtils = new RequestUtils();
            }
        }
        return requestUtils;
    }

    public ApiService buildRequest() {
        if (retrofit == null) {
            synchronized (RequestUtils.class) {
                if (retrofit == null) {
                    //retrofit的实例化
                    retrofit = new Retrofit.Builder()
                            .baseUrl(ConstantValue.BASE_URL)
                            .client(OkHttpUtils.newOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();

                }
            }

        }
        return retrofit.create(ApiService.class);
    }

    public static ApiService createRequest() {
        if (retrofit == null) {
            synchronized (RequestUtils.class) {
                if (retrofit == null) {
                    //retrofit的实例化
                    retrofit = new Retrofit.Builder()
                            .baseUrl(ConstantValue.BASE_URL)
                            .client(OkHttpUtils.newOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();

                }
            }

        }
        return retrofit.create(ApiService.class);
    }

    public static ApiService createRequest(long time) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.writeTimeout(time, TimeUnit.MILLISECONDS);
        builder.readTimeout(time, TimeUnit.MILLISECONDS);
        builder.connectTimeout(time, TimeUnit.MILLISECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantValue.BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }
}
