package com.example.mytest.activity.Main.presenter;

import android.content.Context;

import com.example.mytest.activity.Main.MainActivity;
import com.example.mytest.activity.Main.view.MainView;
import com.example.mytest.data.BasePresenter;
import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.UserInfoGson;
import com.example.mytest.util.Const;
import com.example.mytest.util.MyUrl;
import com.example.mytest.util.SPUtil;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by gaopeng on 2016/11/16.
 */
public class MainPresenter extends BasePresenter {
    private MainView mMainView;

    @Inject
    public MainPresenter(BaseView baseView, Context context) {
        super(context);
        if (baseView.getClass().equals(MainActivity.class))
            mMainView = (MainView) baseView;
    }

    /**
     * 获取教材
     */
    public void getJiaoCai() {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        mAppModel.retrofit_Post(getDataMap, MyUrl.GETJIAOCAI);
    }

    /**
     * 获取用户权限
     */
    public void getUserPower() {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        mAppModel.retrofit_Post(getDataMap, MyUrl.GETUSERPOWER);
    }

    /**
     * 获取用户权限
     */
    public void getUserInfo() {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        mAppModel.retrofit_Post(getDataMap, MyUrl.GETUSERINFO);
    }

    /**
     * 保存教材版本
     */
    public void saveJiaoCai(String banben) {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("banben", banben);
        mAppModel.retrofit_Post(getDataMap, MyUrl.SAVEJIAOCAI);
    }

    /**
     * 获取学校列表
     */
    public void getSchoolList() {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        mAppModel.retrofit_Post(getDataMap, MyUrl.GETSCHOOLLIST);
    }

    @Override
    public void onSuccess(ApiResponse apiResponse, String url) {
        super.onSuccess(apiResponse, url);
        switch (url) {
            case MyUrl.GETJIAOCAI://获取教材
                mMainView.getJiaoCai(apiResponse);
                break;
            case MyUrl.GETUSERPOWER://获取用户权限和教材信息
                mMainView.getUserPower(apiResponse);
                break;
            case MyUrl.SAVEJIAOCAI://保存教材
                mMainView.saveJiaoCai(apiResponse);
                break;
            case MyUrl.GETUSERINFO://保存教材
                switch (apiResponse.getEvent()) {
                    case Const.SUCCESS:
                        SPUtil.put(mContext, Const.SP_NICK, ((UserInfoGson) apiResponse.getObj()).getNick());
                        SPUtil.put(mContext, Const.SP_PIC, ((UserInfoGson) apiResponse.getObj()).getPic());
                        break;
                }
                mMainView.getUserInfo(apiResponse);
                break;
            case MyUrl.GETSCHOOLLIST:
                mMainView.getSchoolList(apiResponse);
                break;
        }
    }
}
