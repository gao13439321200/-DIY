package com.example.mytest.activity.Other;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.Other.presenter.OtherPresenter;
import com.example.mytest.data.UpdateView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.UpdateInfoGson;
import com.example.mytest.util.Const;
import com.example.mytest.util.UpdateUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class UpdateActivity extends BaseActivity implements UpdateView {
    @Inject
    OtherPresenter mOtherPresenter;
    @BindView(R.id.other_update_tv_code)
    TextView mOtherUpdateTvCode;
    @BindView(R.id.other_update_tv_new)
    TextView mOtherUpdateTvNew;
    @BindView(R.id.other_update_tv_msg)
    TextView mOtherUpdateTvMsg;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("版本检测");
        setLeftBtn("返回");
        mBaseComponent.inject(this);
        mOtherPresenter.updataApp(this);

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_other_update;
    }

    @Override
    public void updateApp(ApiResponse apiResponse, boolean isUpdate) {
        switch (apiResponse.getEvent()) {
            case Const.SUCCESS:
                if (isUpdate){
                    UpdateInfoGson userInfoGson = (UpdateInfoGson) apiResponse.getObj();
                    url = userInfoGson.getFile_path();
                    setRightBtn("更新");
                    mOtherUpdateTvCode.setText("当前版本："+ UpdateUtil.getVerCode(this));
                    mOtherUpdateTvNew.setText("最新版本："+userInfoGson.getVersion_no());
                    mOtherUpdateTvMsg.setText("更新内容："+userInfoGson.getComment());
                }else{
                    mOtherUpdateTvCode.setText("当前版本："+ UpdateUtil.getVerCode(this));
                    mOtherUpdateTvNew.setText("最新版本："+UpdateUtil.getVerCode(this));
                    mOtherUpdateTvMsg.setText("已是最新版本啦！不用更新啦~");
                }
                break;
            case Const.FAILED:
                showToast("获取版本失败");
                mOtherUpdateTvCode.setText("当前版本："+ UpdateUtil.getVerCode(this));
                mOtherUpdateTvNew.setText("最新版本：--");
                mOtherUpdateTvMsg.setText("更新内容：--");
                break;
        }
    }

    //更新
    @OnClick(R.id.base_right)
    public void updateClick(View view) {
        UpdateUtil.downloadFile(url,this);
    }


}
