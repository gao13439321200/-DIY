package com.example.mytest.activity.Other;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.Other.presenter.OtherPresenter;
import com.example.mytest.activity.Other.view.ActivateView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.util.Const;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ActivateActivity extends BaseActivity implements ActivateView {

    @BindView(R.id.other_activate_num)
    EditText mOtherActivateNum;
    @Inject
    OtherPresenter mOtherPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("激活课程");
        setLeftBtn("返回");
        setRightBtn("激活");
        mBaseComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_other_activate;
    }

    @OnClick(R.id.base_right)
    public void activateClick(View view){
        String num = mOtherActivateNum.getText().toString().trim();
        if (num.equals("")){
            showToast("拜托写点东西再激活吧。。");
            return;
        }
        if (!num.matches("[1-9]\\d*")) {
            showToast("看清楚！激活码只能输入数字！");
            return;
        }
        mOtherPresenter.activateCourse(num);
    }

    /**
     * 激活课程
     */
    @Override
    public void activateCourse(ApiResponse apiResponse) {
        switch (apiResponse.getEvent()){
            case Const.SUCCESS:
                showToast("成功啦！成功啦！快去看看吧~");
                mOtherActivateNum.setText("");
                break;
            case Const.FAILED:
                showToast("失败了，看看激活码是不是写错了，再试试");
                break;
        }
    }
}
