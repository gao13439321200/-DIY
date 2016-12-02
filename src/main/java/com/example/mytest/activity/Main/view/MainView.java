package com.example.mytest.activity.Main.view;

import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.BookGson;
import com.example.mytest.dto.JiaoCai;
import com.example.mytest.dto.School;
import com.example.mytest.dto.UserInfoGson;

import java.util.List;
import java.util.Map;

/**
 * Created by gaopeng on 2016/11/16.
 */
public interface MainView extends BaseView {
    void getJiaoCai(ApiResponse<Map<String, List<BookGson>>> apiResponse);

    void getUserPower(ApiResponse<Map<String, JiaoCai>> apiResponse);

    void saveJiaoCai(ApiResponse apiResponse);

    void getUserInfo(ApiResponse<UserInfoGson> apiResponse);

    void getSchoolList(ApiResponse<List<School>> apiResponse);
}
