package com.example.mytest.data;

import com.example.mytest.dto.ApiResponse;

/**
 * Created by gaopeng on 2016/6/23.
 * 返回监听父类
 */
public interface BaseListen<T> {
    void onSuccess(ApiResponse<T> apiResponse, String url);
    void onFailed(ApiResponse<T> apiResponse, String url);
}
