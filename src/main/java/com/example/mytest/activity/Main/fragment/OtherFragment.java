package com.example.mytest.activity.Main.fragment;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytest.R;
import com.example.mytest.activity.BaseFragment;
import com.example.mytest.activity.Other.ActivateActivity;
import com.example.mytest.activity.Other.KeFuActivity;
import com.example.mytest.activity.Other.MessageActivity;
import com.example.mytest.activity.Other.SettingActivity;
import com.example.mytest.activity.Other.StudentInfoActivity;
import com.example.mytest.activity.Other.UpdateActivity;
import com.example.mytest.util.Const;
import com.example.mytest.util.ImageUtil;
import com.example.mytest.util.SPUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by gaopeng on 2016/11/8.
 * 其他
 */
public class OtherFragment extends BaseFragment {

    @BindView(R.id.other_Picture)
    ImageView mOtherPicture;
    @BindView(R.id.other_tv_nick)
    TextView mOtherTvNick;
    @BindView(R.id.other_tv_uname)
    TextView mOtherTvUname;
    private ImageLoader loader = ImageLoader.getInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mOtherTvNick.setText((String) SPUtil.get(getActivity(), Const.SP_NICK, ""));
        mOtherTvUname.setText("用户名：" + SPUtil.get(getActivity(), Const.SP_USERNAME, ""));
        Log.d(TAG, "图片地址：" + SPUtil.get(getActivity(), Const.SP_PIC, ""));
        loadImage();
        return mView;
    }

    public void loadImage() {
//        String url = (String) SPUtil.get(getActivity(), Const.SP_PIC, "");
        String url = "http://api.huixueyuan.cn//html//SOURCEP//students//1449804525.jpg";
        ImageUtil imageUtil = new ImageUtil(getActivity());
        imageUtil.loadImage(loader, url, mOtherPicture);
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_other;
    }

    /**
     * 个人信息
     */
    @OnTouch(R.id.other_rl_info)
    public boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        startActivity(new Intent(getActivity(), StudentInfoActivity.class));
        return false;
    }

    /**
     * 点击事件（消息，客服，激活课程，更新，设置）
     */
    @OnClick({R.id.other_rl_message, R.id.other_rl_kefu, R.id.other_rl_course, R.id.other_rl_updata, R.id.other_rl_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.other_rl_message://消息
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.other_rl_kefu://客服
                startActivity(new Intent(getActivity(), KeFuActivity.class));
                break;
            case R.id.other_rl_course://激活课程
                startActivity(new Intent(getActivity(), ActivateActivity.class));
                break;
            case R.id.other_rl_updata://更新
                startActivity(new Intent(getActivity(), UpdateActivity.class));
                break;
            case R.id.other_rl_setting://设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        loader.clearMemoryCache();//清除缓存
        super.onDestroyView();
    }
}
