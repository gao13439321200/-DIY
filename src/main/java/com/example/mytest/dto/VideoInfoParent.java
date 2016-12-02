package com.example.mytest.dto;

import java.util.List;

/**
 * Created by gaopeng on 2016/11/21.
 */
public class VideoInfoParent {
    private String name;
    private List<VideoInfoChildGson> mList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VideoInfoChildGson> getList() {
        return mList;
    }

    public void setList(List<VideoInfoChildGson> list) {
        mList = list;
    }
}
