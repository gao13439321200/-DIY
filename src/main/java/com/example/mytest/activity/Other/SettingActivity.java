package com.example.mytest.activity.Other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.Login.LoginActivity;
import com.example.mytest.activity.Other.presenter.OtherPresenter;
import com.example.mytest.activity.Other.view.SettingView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.util.Const;
import com.example.mytest.view.SlideSwitchView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements SettingView {

    @BindView(R.id.other_setting_switchview)
    SlideSwitchView mOtherSettingSwitchview;
    @Inject
    OtherPresenter mOtherPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("系统设置");
        setLeftBtn("返回");
        setRightBtn("注销");
        mBaseComponent.inject(this);
        mOtherSettingSwitchview.setOnChangeListener(new SlideSwitchView.OnSwitchChangedListener() {
            @Override
            public void onSwitchChange(SlideSwitchView switchView, boolean isChecked) {
                mOtherPresenter.setPush(isChecked ? "1" : "0");
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_other_setting;
    }

    //注销
    @OnClick(R.id.base_right)
    public void outClick(View view) {
        mOtherPresenter.goOut();
    }

    /**
     * 注销
     */
    @Override
    public void goOut(ApiResponse apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                showToast("再见啦~");
                startActivity(new Intent(SettingActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
            case Const.FAILED:
                showToast("注销失败了~不让你走~");
                break;
        }
    }

    /**
     * @param apiResponse
     */
    @Override
    public void setPush(ApiResponse apiResponse) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                showToast("设置成功啦~");
                break;
            case Const.FAILED:
                showToast("设置失败，再试试吧");
                break;
        }
    }
}
