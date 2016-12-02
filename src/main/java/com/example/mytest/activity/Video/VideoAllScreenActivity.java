package com.example.mytest.activity.Video;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;

/**
 * Created by gaopeng on 2016/11/24.
 * 视频全屏类
 */
public class VideoAllScreenActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        hideTitle();
        initData();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_video_all_screen;
    }

    private void initData() {
        Bundle bundle = new Bundle();
        bundle.putString("url", getIntent().getStringExtra("url"));
        bundle.putInt("time", getIntent().getIntExtra("time", 0));
        bundle.putBoolean("isAllScreen", true);
        MyVideoFragment fragment = new MyVideoFragment();
        fragment.setArguments(bundle);
        changeFragment(R.id.test, fragment);
    }

    @Override
    protected void onDestroy() {
        Log.d("MyVideoFragment", "onDestroy");
        super.onDestroy();
    }
}
