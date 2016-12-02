package com.example.mytest.activity.Video.view;

import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.VideoInfoParent;

import java.util.List;

/**
 * Created by gaopeng on 2016/11/18.
 */
public interface VideoView extends BaseView {
    void getVideoList(ApiResponse<List<VideoInfoParent>> apiResponse);

}
