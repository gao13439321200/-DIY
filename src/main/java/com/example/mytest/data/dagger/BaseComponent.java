package com.example.mytest.data.dagger;

import com.example.mytest.activity.Login.LoginActivity;
import com.example.mytest.activity.Main.MainActivity;
import com.example.mytest.activity.Main.fragment.VideoFragment;
import com.example.mytest.activity.Other.ActivateActivity;
import com.example.mytest.activity.Other.MessageActivity;
import com.example.mytest.activity.Other.MessageInfoActivity;
import com.example.mytest.activity.Other.SettingActivity;
import com.example.mytest.activity.Other.StudentInfoActivity;
import com.example.mytest.activity.Other.UpdateActivity;
import com.example.mytest.data.model.AppModelImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gaopeng on 2016/11/4.
 * 依赖注入Component
 */
@Singleton
@Component(modules = BaseModul.class)
public interface BaseComponent {
    void inject(AppModelImpl appModel);

    void inject(LoginActivity loginActivity);

    void inject(StudentInfoActivity studentInfoActivity);

    void inject(MessageActivity messageActivity);

    void inject(MessageInfoActivity messageInfoActivity);

    void inject(UpdateActivity updateActivity);

    void inject(ActivateActivity activateActivity);

    void inject(SettingActivity settingActivity);

    void inject(MainActivity mainActivity);

    void inject(VideoFragment videoFragment);

}
