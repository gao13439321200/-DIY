package com.example.mytest.activity.Login.presenter;

import android.content.Context;
import android.provider.Settings;

import com.example.mytest.activity.Login.view.LoginView;
import com.example.mytest.data.BasePresenter;
import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.LoginInfoGson;
import com.example.mytest.util.Const;
import com.example.mytest.util.MyUrl;
import com.example.mytest.util.SPUtil;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by gaopeng on 2016/11/4.
 * 登录页面请求数据和逻辑处理
 */
public class LoginPresenter extends BasePresenter {

    private LoginView mLoginView;
    private Context mContext;

    @Inject
    public LoginPresenter(BaseView baseView, Context context) {
        super(context);
        this.mLoginView = (LoginView) baseView;
        this.mContext = context;
    }

    /**
     * 登录
     *
     * @param uname 用户名
     * @param pwd   密码
     */
    public void login(String uname, String pwd) {
        String deviceID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        getDataMap = new HashMap<>();
        getDataMap.put("username", uname);
        getDataMap.put("password", pwd);
        getDataMap.put("usertype", Const.USERTYPE);//pad端传1,手机端传2，
        getDataMap.put("userId", "");   // userId && channelId 需要绑定百度成功后才能得到数据，第一次登陆传 空字符串
        getDataMap.put("channelId", "");
        getDataMap.put("terminal", "a"); // a 代表安卓
        getDataMap.put("model", deviceID); //获取手机型号
        getDataMap.put("release", android.os.Build.VERSION.SDK_INT + ","
                + android.os.Build.VERSION.RELEASE);//RELEASE获取版本号
        mAppModel.retrofit_Post(getDataMap, MyUrl.LOGIN);
    }

    @Override
    public void onSuccess(ApiResponse apiResponse, String url) {
        super.onSuccess(apiResponse, url);
        switch (url){
            case MyUrl.LOGIN://登录
                switch (apiResponse.getEvent()){
                    case Const.SUCCESS:
                        SPUtil.put(mContext,Const.SP_USERID,((LoginInfoGson)apiResponse.getObj()).getUser_id());
                        SPUtil.put(mContext,Const.SP_XUEDUAN,((LoginInfoGson)apiResponse.getObj()).getXueduan());
                        SPUtil.put(mContext,Const.SP_NICK,((LoginInfoGson)apiResponse.getObj()).getNick());
                        SPUtil.put(mContext,Const.SP_REALNAME,((LoginInfoGson)apiResponse.getObj()).getStudent_name());
                        SPUtil.put(mContext,Const.SP_BIRTH,((LoginInfoGson)apiResponse.getObj()).getStudent_birthday());
                        SPUtil.put(mContext,Const.SP_CITY,((LoginInfoGson)apiResponse.getObj()).getCity());
                        SPUtil.put(mContext,Const.SP_SCHOOL,((LoginInfoGson)apiResponse.getObj()).getStudent_school());
                        Const.XUEDUAN = ((LoginInfoGson)apiResponse.getObj()).getXueduan();
                        mLoginView.login(Const.SUCCESS);
                        break;
                    case Const.FAILED:
                        mLoginView.login(Const.FAILED);
                        break;
                }
                break;
        }


    }
}
