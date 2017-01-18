package com.example.mytest.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mytest.R;
import com.example.mytest.activity.Video.VideoBaseFragment;
import com.example.mytest.data.BaseView;
import com.example.mytest.data.dagger.BaseComponent;
import com.example.mytest.data.dagger.BaseModul;
import com.example.mytest.data.dagger.DaggerBaseComponent;

import butterknife.ButterKnife;

/**
 * Created by gaopeng on 2016/11/9.
 * fragment父类
 */
public abstract class BaseFragment extends Fragment implements BaseView {
    protected View mView;
    protected final String TAG = "MYLOG";
    public BaseComponent mBaseComponent;
    protected String subjectID = "02";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutID(), null, false);
        ButterKnife.bind(this, mView);
        mBaseComponent = DaggerBaseComponent.builder().baseModul(new BaseModul(this, this)).build();
        return mView;
    }

    /**
     * 获取布局id
     *
     * @return 布局id
     */
    public abstract int getLayoutID();

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 加载碎片
     *
     * @param fragment 具体的fragment
     */
    protected void changeFragment(int layoutID, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutID, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 加载视频的fragment
     *
     * @param path            视频的mp4地址
     * @param isAllScreen     是否全屏
     * @param currentPosition 播放的位置
     */
    public VideoBaseFragment setVideoFragment(String path, boolean isAllScreen, int currentPosition) {
        VideoBaseFragment videoFragment = new VideoBaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", path);
        bundle.putBoolean("isAllScreen", isAllScreen);
        bundle.putInt("currentPosition", currentPosition);
        videoFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.video_Screen, videoFragment);// 2017/1/9 高鹏 所有加载视频的父布局的id统一用video_Screen
        fragmentTransaction.commit();
        return videoFragment;
    }
}
