package com.example.mytest.activity.Other.view;

import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;

/**
 * Created by gaopeng on 2016/11/15.
 */
public interface SettingView extends BaseView {
    void goOut(ApiResponse apiResponse);

    void setPush(ApiResponse apiResponse);
}
