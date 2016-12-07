package com.example.mytest.activity.Exam.presenter;

import android.content.Context;

import com.example.mytest.activity.Exam.view.ExamView;
import com.example.mytest.activity.Main.fragment.ExamFragment;
import com.example.mytest.data.BasePresenter;
import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.util.MyUrl;
import com.example.mytest.util.ScreenUtil;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by GaoPeng on 2016/12/5.
 */

public class ExamPresenter extends BasePresenter {

    private ExamView mExamView;
    private int sum;

    @Inject
    ExamPresenter(BaseView baseView, Context context) {
        super(context);
        if (baseView.getClass().equals(ExamFragment.class))
            mExamView = (ExamView) baseView;
        if (sum == 0) {//设置消息信息的条数
            //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
            int screenHeight = (int) (ScreenUtil.getScreenHeight(context) / ScreenUtil.getScreenDensity(context));//屏幕高度(dp)
            sum = screenHeight / 60;
        }
    }

    /**
     * 获取试卷
     */
    public void getShiJuan(String page, String chosen_subject, String schoolId) {
        getDataMap = new HashMap<>();
        getDataMap.put("page", page);
        getDataMap.put("course", chosen_subject);
        getDataMap.put("school_id", schoolId);
        getDataMap.put("userID", userID);
        getDataMap.put("pad", "apad");
        getDataMap.put("num", sum);
        mAppModel.retrofit_Post(getDataMap, MyUrl.GETEXAMLIST);
    }

    @Override
    public void onSuccess(ApiResponse apiResponse, String url) {
        super.onSuccess(apiResponse, url);
        switch (url) {
            case MyUrl.GETEXAMLIST:
                mExamView.getShiJuan(apiResponse);
                break;
        }
    }
}
