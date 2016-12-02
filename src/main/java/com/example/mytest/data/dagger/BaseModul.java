package com.example.mytest.data.dagger;

import android.content.Context;

import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.BaseFragment;
import com.example.mytest.data.BaseView;
import com.example.mytest.data.DataInterface;
import com.example.mytest.service.DataService;
import com.example.mytest.util.MyUrl;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by gaopeng on 2016/11/4.
 * 依赖注入Modul
 */
@Module
public class BaseModul {
    private BaseView mBaseView;
    private Context mContext;

    public BaseModul(BaseView baseView, BaseActivity activity) {
        this.mBaseView = baseView;
        this.mContext = activity;
    }

    public BaseModul(BaseView baseView, BaseFragment baseFragment) {
        this.mBaseView = baseView;
        this.mContext = baseFragment.getActivity();
    }

    public BaseModul() {

    }

    @Provides
    @Singleton
    public Retrofit getRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(MyUrl.HTML + File.separator)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    @Provides
    @Singleton
    protected DataInterface getDataInterface(Retrofit retrofit) {
        return retrofit.create(DataInterface.class);
    }

    @Provides
    @Singleton
    protected DataService getDataService() {
        return new DataService();
    }

    @Provides
    @Singleton
    protected BaseView getBaseView() {
        return mBaseView;
    }

    @Provides
    @Singleton
    protected Context getContext() {
        return mContext;
    }

}
