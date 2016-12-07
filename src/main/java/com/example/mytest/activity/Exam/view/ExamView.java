package com.example.mytest.activity.Exam.view;

import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.ShiJuanGson;

import java.util.List;

/**
 * Created by GaoPeng on 2016/12/5.
 */

public interface ExamView extends BaseView {
    void getShiJuan(ApiResponse<List<ShiJuanGson>> apiResponse);
}
