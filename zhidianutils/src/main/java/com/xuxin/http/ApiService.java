package com.xuxin.http;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by Administrator on 2018/8/6.
 */

public interface ApiService {

    @Multipart
    @POST("tim/modifyGroupImg")
    Call<BaseModel> postTimModifyGroupImg(@PartMap HashMap<String, RequestBody> map);
}
