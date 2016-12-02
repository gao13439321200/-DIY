package com.example.mytest.activity.Main.fragment;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mytest.R;
import com.example.mytest.activity.BaseFragment;
import com.example.mytest.activity.Video.MyVideoFragment;
import com.example.mytest.activity.Video.presenter.VideoPresenter;
import com.example.mytest.activity.Video.view.VideoView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.VideoInfoChildGson;
import com.example.mytest.dto.VideoInfoParent;
import com.example.mytest.util.Const;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaopeng on 2016/11/10.
 * 视频类
 */
public class VideoFragment extends BaseFragment implements VideoView, Observer {

    @Inject
    VideoPresenter mVideoPresenter;
    @BindView(R.id.video_rg_parent)
    RadioGroup mVideoRgParent;
    @BindView(R.id.video_rg_child)
    RadioGroup mVideoRgChild;
    //    @BindView(R.id.video_player)
//    android.widget.VideoView mVideoPlayer;
    private List<VideoInfoParent> mInfoParents;
    private List<VideoInfoChildGson> mInfoChildGsons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBaseComponent.inject(this);
        mVideoPresenter.getVideoList(subjectID);
        ButterKnife.bind(this, mView);
        initData();
        return mView;
    }

    private void initData() {
        mInfoParents = new ArrayList<>();
        mInfoChildGsons = new ArrayList<>();
        mVideoRgParent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = (RadioButton) getActivity().findViewById(i);
                mInfoChildGsons.clear();
                mInfoChildGsons.addAll(mInfoParents.get(Integer.parseInt(button.getTag().toString())).getList());
                setChildGroup();
            }
        });
        mVideoRgChild.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = (RadioButton) getActivity().findViewById(i);
                VideoInfoChildGson video = mInfoChildGsons.get(Integer.parseInt(button.getTag().toString()));
                getActivity().setTitle(video.getName());
                try {
                    AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("start.mp4");
                    fragment.changeVideo(fileDescriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        playVideo("http://192.168.20.66:82/data/userdata/vod/resource/2.mp4");

    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_video;
    }

    MyVideoFragment fragment;
    /**
     * 加载视频
     *
     * @param url 视频地址
     */
    private void playVideo(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isAllScreen", false);
        fragment = new MyVideoFragment();
        fragment.setArguments(bundle);
        changeFragment(R.id.video_main, fragment);
    }


    /**
     * 获取视频列表
     */
    @Override
    public void getVideoList(ApiResponse<List<VideoInfoParent>> apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                mInfoParents = apiResponse.getObjList();
                setParentGroup();
                mInfoChildGsons.clear();
                mInfoChildGsons.addAll(mInfoParents.get(0).getList());
                setChildGroup();
                break;
            case Const.FAILED:
                showToast("列表获取失败");
                break;
        }
    }

    /**
     * 设置父菜单
     */
    private void setParentGroup() {
        mVideoRgParent.removeAllViews();
        for (int i = 0; i < mInfoParents.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(mInfoParents.get(i).getName());
            radioButton.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTag(i);
            radioButton.setBackgroundResource(R.drawable.base_subjectrb);
            mVideoRgParent.addView(radioButton, LinearLayout.LayoutParams.MATCH_PARENT, 150);
        }
    }

    /**
     * 设置子菜单
     */
    private void setChildGroup() {
        mVideoRgChild.removeAllViews();
        for (int i = 0; i < mInfoChildGsons.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(mInfoChildGsons.get(i).getTeacher() + " " + mInfoChildGsons.get(i).getName());
            radioButton.setBackgroundResource(R.drawable.base_subjectrb);
            radioButton.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTag(i);
            mVideoRgChild.addView(radioButton, LinearLayout.LayoutParams.MATCH_PARENT, 150);
        }
    }


    //观察者模式回调方法，用于更新用户选择的科目
    @Override
    public void update(Observable observable, Object data) {
        if (!data.toString().equals(subjectID)) {//如果当前科目和选择的科目一样，就不用更新列表
            this.subjectID = data.toString();
            mVideoPresenter.getVideoList(subjectID);
        }
    }
}
