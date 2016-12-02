package com.example.mytest.activity.Other.view;

import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.MessageGson;

import java.util.List;

/**
 * Created by gaopeng on 2016/11/10.
 */
public interface MessageView extends BaseView {
    void getMessageList(ApiResponse<List<MessageGson>> apiResponse, boolean isLast);
}
