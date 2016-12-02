package com.example.mytest.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.mytest.R;
import com.example.mytest.activity.BaseActivity;
import com.example.mytest.activity.Login.presenter.LoginPresenter;
import com.example.mytest.activity.Login.view.LoginView;
import com.example.mytest.activity.Main.MainActivity;
import com.example.mytest.util.Const;
import com.example.mytest.util.SPUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaopeng on 2016/11/4.
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    @Inject
    LoginPresenter mLoginPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTitle();
        mBaseComponent.inject(this);//依赖注入
        // TODO: 2016/11/15 高鹏 绑定百度推送（需要绑定百度，暂时不做）
    }

    @OnClick(R.id.btnLogin)
    public void onClick() {
//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        mLoginPresenter.login("gaopeng1", "111111");

//        String username = mUsername.getText().toString().trim();
//        String password = mPassword.getText().toString().trim();
//        if ("".equals(username)) {
//            showToast("用户名不能为空");
//            return;
//        }
//        if ("".equals(password)) {
//            showToast("密码不能为空");
//            return;
//        }
//        mLoginPresenter.login(username, password);

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    /**
     * 登录返回
     *
     * @param task 状态
     */
    @Override
    public void login(String task) {
        switch (task) {
            case Const.SUCCESS:
                showToast("成功");
                SPUtil.put(this, Const.SP_USERNAME, mUsername.getText().toString().trim());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
            case Const.FAILED:
                showToast("失败");
                break;
        }
    }
}

