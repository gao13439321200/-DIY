package com.example.mytest.activity.Other;

import android.os.Bundle;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;

public class KeFuActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLeftBtn("返回");
        setTitle("客服");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_other_kefu;
    }
}
