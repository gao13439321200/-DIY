package com.example.mytest.activity.Main.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytest.R;
import com.example.mytest.activity.BaseFragment;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaopeng on 2016/11/8.
 */
public class ExamFragment extends BaseFragment implements Observer {
    @BindView(R.id.exam_recycler)
    SwipeMenuRecyclerView mExamRecycler;
    @BindView(R.id.exam_swipe_layout)
    SwipeRefreshLayout mExamSwipeLayout;
    private String schoolID = "0";//默认是“全部”
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_exam;
    }

    @Override
    public void update(Observable o, Object arg) {
        String a = "1";
    }

}
