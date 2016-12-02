package com.example.mytest.dto;

import java.util.List;

/**
 * Created by gaopeng on 2016/11/16.
 */
public class BookGson {

    private String name;
    private String id;
    private List<CeGson> mGsonList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CeGson> getGsonList() {
        return mGsonList;
    }

    public void setGsonList(List<CeGson> gsonList) {
        mGsonList = gsonList;
    }
}
