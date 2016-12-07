package com.example.mytest.util;


import java.util.Observable;

/**
 * Created by GaoPeng on 2016/12/6.
 */

public class SchoolUtil extends Observable {

    public void sendMessage(String schoolID){
        setChanged();
        notifyObservers(schoolID);
    }

}
