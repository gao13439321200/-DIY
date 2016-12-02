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

}
