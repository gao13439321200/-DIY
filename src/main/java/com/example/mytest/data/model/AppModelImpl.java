package com.example.mytest.data.model;

import android.util.Log;

import com.example.mytest.data.BaseListen;
import com.example.mytest.data.DataInterface;
import com.example.mytest.data.dagger.BaseModul;
import com.example.mytest.data.dagger.DaggerBaseComponent;
import com.example.mytest.service.DataService;
import com.example.mytest.util.Const;
import com.example.mytest.util.JsonUtil;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gaopeng on 2016/11/4.
 * 网络请求
 */

public class AppModelImpl implements AppModel {
    @Inject
    DataInterface mDataInterface;
    @Inject
    DataService mDataService;
    private BaseListen mBaseListen;
    private static AppModel mAppModel;

    public AppModelImpl(BaseListen baseListen){
        DaggerBaseComponent.builder().baseModul(new BaseModul()).build().inject(this);
        this.mBaseListen = baseListen;
    }

    /**
     * 单例模式
     *
     * @param baseListen 回调
     * @return 单例对象
     */
    public static AppModel getInstance(BaseListen baseListen){
        if (mAppModel == null){
            mAppModel = new AppModelImpl(baseListen);
        }
        return mAppModel;
    }


    /**
     * POST请求
     *
     * @param hashMap 参数
     * @param url     请求的接口
     */
    @Override
    public void retrofit_Post(HashMap<String, Object> hashMap, final String url) {
        Call<String> call =  mDataInterface.getData_String(url, JsonUtil.reverJson(hashMap));
        Log.d(Const.LOGINFO,"接口:"+url + ",参数:" + JsonUtil.reverJson(hashMap));
        if (call != null) {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String infoGson = response.body();
                    Log.d(Const.LOGINFO,"接口:"+url + ",返回值:" + infoGson);
                    if (infoGson != null)
                        mBaseListen.onSuccess(JsonUtil.getJsonResult(infoGson, url , mDataService), url);
                    else
                        mBaseListen.onFailed(null, url);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(Const.LOGINFO,"接口:"+url + ",返回值:" + t);
                    mBaseListen.onFailed(null, url);
                }
            });
        } else {
            Log.d(Const.LOGINFO,"接口:"+url + ",返回值:null");
            mBaseListen.onFailed(null, url);
        }
    }

    @Override
    public void retrofit_Get_download(String url) {
//        Call call = mDataInterface.downloadFile()
    }

}
