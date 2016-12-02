package com.example.mytest.util;

import java.util.Observable;

/**
 * Created by gaopeng on 2016/11/21.
 */
public class SubjectUtil extends Observable {

    public void sendMsg(String subjectID){
        setChanged();
        notifyObservers(subjectID);
    }
}
