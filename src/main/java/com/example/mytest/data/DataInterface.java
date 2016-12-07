package com.example.mytest.data;


import com.example.mytest.activity.MyTest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by gaopeng on 2016/11/4.
 * 网络请求
 */
public interface DataInterface {

    //POST请求
    @FormUrlEncoded
    @POST
    Call<String> getData_String(@Url String url , @Field("json") String s);

    //更新下载
    @GET
    Call downloadFile(@Url String url);


}
