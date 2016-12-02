package com.example.mytest.data.model;

import java.util.HashMap;

/**
 * Created by gaopeng on 2016/11/7.
 * 网络请求
 */
public interface AppModel {
    /**
     * POST请求
     *
     * @param hashMap 参数
     * @param url  请求的接口
     */
    void retrofit_Post(HashMap<String,Object> hashMap, String url);


    /**
     * 更新下载
     *
     * @param url 下载地址
     */
    void retrofit_Get_download(String url);

}
