package com.example.mytest.activity.Video.presenter;

import android.content.Context;

import com.example.mytest.activity.Main.fragment.VideoFragment;
import com.example.mytest.activity.Video.view.VideoView;
import com.example.mytest.data.BasePresenter;
import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.util.MyUrl;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by gaopeng on 2016/11/18.
 */
public class VideoPresenter extends BasePresenter {
    private VideoView mVideoView;
    private String subjectID;

    @Inject
    public VideoPresenter(BaseView view, Context context) {
        super(context);
        if (view.getClass().equals(VideoFragment.class)) {
            mVideoView = (VideoView) view;
        }
    }

    /**
     * 获取视频列表
     */
    public void getVideoList(String subjectID) {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("courseID", subjectID);
        mAppModel.retrofit_Post(getDataMap, MyUrl.GETVIDEOLIST);
    }

    @Override
    public void onSuccess(ApiResponse apiResponse, String url) {
        super.onSuccess(apiResponse, url);
        switch (url){
            case MyUrl.GETVIDEOLIST:
                mVideoView.getVideoList(apiResponse);
                break;
        }
    }
}
