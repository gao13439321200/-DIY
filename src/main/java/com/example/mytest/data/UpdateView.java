package com.example.mytest.data;

import com.example.mytest.dto.ApiResponse;

/**
 * Created by gaopeng on 2016/11/14.
 */
public interface UpdateView extends BaseView {
    void updateApp(ApiResponse apiResponse, boolean isUpdate);
}
