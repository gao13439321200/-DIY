package com.example.mytest.activity.Other;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.Other.presenter.OtherPresenter;

import javax.inject.Inject;

import butterknife.BindView;

public class MessageInfoActivity extends BaseActivity {

    @BindView(R.id.other_message_info_tv)
    TextView mOtherMessageInfoTv;
    @Inject
    OtherPresenter mOtherPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("content");
        mOtherMessageInfoTv.setText(title);
        setTitle("消息详情");
        setLeftBtn("返回");
        mBaseComponent.inject(this);
        mOtherPresenter.readMessage(id);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_other_message_info;
    }
}
