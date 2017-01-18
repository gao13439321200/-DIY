package com.example.mytest.activity.Main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.BaseFragment;
import com.example.mytest.activity.Main.MainActivity;
import com.example.mytest.activity.Video.VideoBaseFragment;
import com.example.mytest.activity.Video.presenter.VideoPresenter;
import com.example.mytest.activity.Video.view.VideoView;
import com.example.mytest.adapter.VideoExpanableAdapter;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.VideoInfoChildGson;
import com.example.mytest.dto.VideoInfoParent;
import com.example.mytest.util.Const;
import com.example.mytest.util.ScreenSwitchUtils;
import com.example.mytest.util.VideoChangeObervable;

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
    @BindView(R.id.expandable_Listview)
    ExpandableListView mExpandableListView; //可展开得listview
    private List<VideoInfoParent> mInfoParents = new ArrayList<>();
    private VideoExpanableAdapter videoExpanableAdapter;//
    private VideoChangeObervable videoChangeObervable; // 视频切换，被观察者。
    private VideoBaseFragment mVideoBaseFragment;
    private Bundle saveBundle;
    private ScreenSwitchUtils mScreenSwitchUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d("VideoFragment", "    videoFragment走了OnCreateView");
        mBaseComponent.inject(this);
        ButterKnife.bind(this, mView);
        saveBundle = savedInstanceState;
        if (getActivity().getResources().getConfiguration().orientation == 1){//竖屏
            mVideoPresenter.getVideoList(subjectID);
        } else {//横屏
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
            mVideoBaseFragment = setVideoFragment(savedInstanceState.getString("path"), true, savedInstanceState.getInt("position"));//加载视频
            ((MainActivity) getActivity()).hideBottom();
            ((MainActivity) getActivity()).hideTitle();
        }
//        if (savedInstanceState != null && savedInstanceState.getBoolean("isAllScreen")) {//全屏
//            Log.d("VideoFragment", "    videoFragment全屏");
//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
//            mVideoBaseFragment = setVideoFragment(savedInstanceState.getString("path"), true, savedInstanceState.getInt("position"));//加载视频
//            ((MainActivity) getActivity()).hideBottom();
//            ((MainActivity) getActivity()).hideTitle();
//        } else {
//            mVideoPresenter.getVideoList(subjectID);
//        }
//        mScreenSwitchUtils = ScreenSwitchUtils.init(getActivity(),this);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        playVideo("http://192.168.20.66:82/data/userdata/vod/resource/2.mp4");
    }

    private void initData() {
        //添加观察者 videoChangeObervable -> 被观察者
        videoChangeObervable = new VideoChangeObervable();
        videoChangeObervable.addObserver(this);

        //初始化
        videoExpanableAdapter = new VideoExpanableAdapter(mInfoParents, getActivity().getApplicationContext(), videoChangeObervable);
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

    /**
     * 关闭展开得group
     *
     * @param currentPosition
     */
    private void closeALl(int currentPosition) {
        int groupCount = mInfoParents.size();
        for (int i = 0; i < groupCount; i++) {
            if (i != currentPosition) {
                mExpandableListView.collapseGroup(i);
            }
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_video;
    }

    /**
     * 加载视频
     *
     * @param url 视频地址
     */
    private void playVideo(String url) {
        mVideoBaseFragment = setVideoFragment(url, false, 0);
    }


    @Override
    public void onDestroy() {
        Log.d("VideoFragment", "    VideoFragment已经销毁");
        super.onDestroy();
    }

    /**
     * 获取视频列表
     */
    @Override
    public void getVideoList(ApiResponse<List<VideoInfoParent>> apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                mInfoParents = apiResponse.getObjList();
//                initData();
                if (mInfoParents != null && mInfoParents.size() > 0) {
//                    videoExpanableAdapter.notifyDataSetChanged();
//                    mExpandableListView.notifyAll();
                    Log.d("VideoFragment", "    VideoFragment走了刷新");
                    initData();
                    if (saveBundle != null && saveBundle.getString("path") != null && !"".equals(saveBundle.getString("path"))) {
                        mVideoBaseFragment = setVideoFragment(saveBundle.getString("path"), false, saveBundle.getInt("position"));
                        saveBundle = null;
                        Log.d("VideoFragment", "    VideoFragment重置了");
                    } else {
                        Log.d("VideoFragment", "    VideoFragment没重置");
                        mVideoBaseFragment = setVideoFragment(mInfoParents.get(0).getList().get(0).getCc_id_url(), false, 0);
                    }
                } else {
                    showToast("本章暂无视频");
                }
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

        if (className.equals(VideoChangeObervable.class.getSimpleName())) {
            VideoInfoChildGson videoInfoChildGson = (VideoInfoChildGson) data;
//            showToast(videoInfoChildGson.getCcid() + "--" + videoInfoChildGson.getName());
            // 设置title显示当前播放视频名称
            ((BaseActivity) getActivity()).setTitle(videoInfoChildGson.getName());
            mVideoBaseFragment = setVideoFragment(videoInfoChildGson.getCc_id_url(), false, 0);
        } else {
            if (!data.toString().equals(subjectID)) {//如果当前科目和选择的科目一样，就不用更新列表
                this.subjectID = data.toString();
                mVideoPresenter.getVideoList(subjectID);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("VideoFragment", "    VideoFragment保存的路径：" + mVideoBaseFragment.getPath());
        Log.d("VideoFragment", "    VideoFragment保存的全屏：" + mVideoBaseFragment.getAllScreen());
        Log.d("VideoFragment", "    VideoFragment保存的进度：" + mVideoBaseFragment.getCurrentPosition());
        outState.putBoolean("isAllScreen", mVideoBaseFragment.getAllScreen());
        outState.putString("path", mVideoBaseFragment.getPath());
        outState.putInt("position", mVideoBaseFragment.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        mScreenSwitchUtils.start(getActivity());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        mScreenSwitchUtils.stop();
//    }

//    }
}
