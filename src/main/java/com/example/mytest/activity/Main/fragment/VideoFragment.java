package com.example.mytest.activity.Main.fragment;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.BaseFragment;
import com.example.mytest.activity.Video.MyVideoFragment;
import com.example.mytest.activity.Video.presenter.VideoPresenter;
import com.example.mytest.activity.Video.view.VideoView;
import com.example.mytest.adapter.VideoExpanableAdapter;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.VideoInfoChildGson;
import com.example.mytest.dto.VideoInfoParent;
import com.example.mytest.util.Const;
import com.example.mytest.util.VideoChangeObervable;

import java.io.IOException;
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
    @BindView(R.id.expandable_Listview)
    ExpandableListView mExpandableListView; //可展开得listview
    private List<VideoInfoParent> mInfoParents;
    private VideoExpanableAdapter videoExpanableAdapter;//
    private VideoChangeObervable videoChangeObervable; // 视频切换，被观察者。

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBaseComponent.inject(this);
        mVideoPresenter.getVideoList(subjectID);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        playVideo("http://192.168.20.66:82/data/userdata/vod/resource/2.mp4");
    }

    private void initData() {
        //添加观察者 videoChangeObervable -> 被观察者
        videoChangeObervable = new VideoChangeObervable();
        videoChangeObervable.addObserver(this);

        //初始化
        videoExpanableAdapter = new VideoExpanableAdapter(mInfoParents,getActivity().getApplicationContext(),videoChangeObervable);
        mExpandableListView.setAdapter(videoExpanableAdapter);
//        mExpandableListView.expandGroup(0);
        //接口定义一个回调时调用一组可扩展的列表已被点击。
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                showToast( "ExpandableListView GroupClickListener groupPosition=" + groupPosition);
                return false;
            }
        });
        //用于监听group是否被展开
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0, count = mExpandableListView
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    if (groupPosition != i) {// 关闭其他分组
                        mExpandableListView.collapseGroup(i);
                    }
                }
            }
        });
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
                initData();
                break;
            case Const.FAILED:
                showToast("列表获取失败");
                break;
        }
    }

    //观察者模式回调方法，用于更新用户选择的科目
    @Override
    public void update(Observable observable, Object data) {
        //the simple name of the class represented
        String className = observable.getClass().getSimpleName();

        if(className.equals(VideoChangeObervable.class.getSimpleName())) {
            VideoInfoChildGson videoInfoChildGson = (VideoInfoChildGson) data;
            showToast(videoInfoChildGson.getCcid()+"--"+videoInfoChildGson.getName());
            // 设置title显示当前播放视频名称
            ((BaseActivity)getActivity()).setTitle(videoInfoChildGson.getName());
            try {
                AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("start.mp4");
                fragment.changeVideo(fileDescriptor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            if (!data.toString().equals(subjectID)) {//如果当前科目和选择的科目一样，就不用更新列表
                this.subjectID = data.toString();
                mVideoPresenter.getVideoList(subjectID);
            }
        }

    }
}
