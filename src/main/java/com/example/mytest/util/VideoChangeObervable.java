package com.example.mytest.util;

import com.example.mytest.dto.VideoInfoChildGson;

import java.util.Observable;

/**
 * Created by lijun on 2016/12/07.
 */
public class VideoChangeObervable extends Observable {

    public void sendMsg(VideoInfoChildGson videoInfoChildGson){
        setChanged();
        notifyObservers(videoInfoChildGson);
    }

}
