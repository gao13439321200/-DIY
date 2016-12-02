package com.example.mytest.activity.Other.presenter;

import android.content.Context;

import com.example.mytest.activity.Other.ActivateActivity;
import com.example.mytest.activity.Other.MessageActivity;
import com.example.mytest.activity.Other.SettingActivity;
import com.example.mytest.activity.Other.StudentInfoActivity;
import com.example.mytest.activity.Other.view.ActivateView;
import com.example.mytest.activity.Other.view.MessageView;
import com.example.mytest.activity.Other.view.SettingView;
import com.example.mytest.activity.Other.view.StudentInfoView;
import com.example.mytest.data.BasePresenter;
import com.example.mytest.data.BaseView;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.MessageGson;
import com.example.mytest.util.Const;
import com.example.mytest.util.MyUrl;
import com.example.mytest.util.SPUtil;
import com.example.mytest.util.ScreenUtil;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by gaopeng on 2016/11/9.
 */
public class OtherPresenter extends BasePresenter {
    private StudentInfoView mStudentInfoView;
    private MessageView mMessageView;
    private ActivateView mActivateView;
    private SettingView mSettingView;
    private int sum = 0;

    @Inject
    public OtherPresenter(BaseView baseView, Context context) {
        super(context);
        if (baseView.getClass().equals(StudentInfoActivity.class))
            this.mStudentInfoView = (StudentInfoView) baseView;
        if (baseView.getClass().equals(MessageActivity.class))
            this.mMessageView = (MessageView) baseView;
        if (baseView.getClass().equals(ActivateActivity.class))
            this.mActivateView = (ActivateView) baseView;
        if (baseView.getClass().equals(SettingActivity.class))
            this.mSettingView = (SettingView) baseView;
        if (sum == 0) {//设置消息信息的条数
            //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
            int screenHeight = (int) (ScreenUtil.getScreenHeight(context) / ScreenUtil.getScreenDensity(context));//屏幕高度(dp)
            sum = screenHeight / 40;
        }
    }

    /**
     * 保存个人信息
     *
     * @param stuBirthday 生日
     * @param cityName    城市
     * @param stuName     真实姓名
     * @param stuSex      性别
     * @param stuNick     昵称
     * @param schoolname  学校
     */
    public void saveInfo(String stuBirthday, String cityName, String stuName, String stuSex, String stuNick, String schoolname) {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("xueduan", xueduan);
        getDataMap.put("student_birthday", stuBirthday);
        getDataMap.put("province_name", cityName);
        getDataMap.put("student_name", stuName);
        getDataMap.put("student_sex", stuSex);
        getDataMap.put("nick", stuNick);
        getDataMap.put("schoolname", schoolname);
        mAppModel.retrofit_Post(getDataMap, MyUrl.XITONGSHEZHI_SAVESTUDENTPERSONINFO);
    }

    /**
     * 获取消息列表
     *
     * @param page 页数
     */
    public void getMessageList(int page) {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("type", "");
        getDataMap.put("read", "");
        getDataMap.put("user_type", "1");
        getDataMap.put("count", sum);
        getDataMap.put("page", page + "");
        mAppModel.retrofit_Post(getDataMap, MyUrl.MESSAGES_GETMESSAGEDATA);
    }

    /**
     * 将消息标记为已读
     *
     * @param id 消息id
     */
    public void readMessage(String id) {
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("id", id);
        mAppModel.retrofit_Post(getDataMap, MyUrl.MESSAGES_CHANGEMESSAGESTATUS);
    }


    /**
     * 清空消息
     */
    public void clearMessage(){
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("userType", "1");
        mAppModel.retrofit_Post(getDataMap, MyUrl.MESSAGES_DELMSG);
    }
    /**
     * 激活课程
     */
    public void activateCourse(String number){
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("code", number);
        mAppModel.retrofit_Post(getDataMap, MyUrl.ACTIVATECOURSE);
    }

    /**
     * 注销
     */
    public void goOut(){
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("userid", SPUtil.get(mContext,Const.SP_BAIDUID,""));
        mAppModel.retrofit_Post(getDataMap, MyUrl.XITONG_USERLOGOUT_APP);
    }
    /**
     * 设置推送开关
     */
    public void setPush(String pushon){
        getDataMap = new HashMap<>();
        getDataMap.put("userID", userID);
        getDataMap.put("userid", SPUtil.get(mContext,Const.SP_BAIDUID,""));
        getDataMap.put("pushon", pushon);
        getDataMap.put("soundon", "0");
        mAppModel.retrofit_Post(getDataMap, MyUrl.XITONG_PUSHON);
    }

    @Override
    public void onSuccess(ApiResponse apiResponse, String url) {
        super.onSuccess(apiResponse, url);
        switch (url) {
            case MyUrl.XITONGSHEZHI_SAVESTUDENTPERSONINFO://保存个人信息
                mStudentInfoView.saveInfo(apiResponse.getEvent());
                break;
            case MyUrl.MESSAGES_GETMESSAGEDATA://获取消息列表
                boolean isLast = false;//判断是否是最后一页
                if (apiResponse != null) {
                    List<MessageGson> list = (List<MessageGson>) apiResponse.getObjList();
                    if (list.size() < sum) {
                        isLast = true;
                    }
                }
                mMessageView.getMessageList(apiResponse,isLast);
                break;
            case MyUrl.ACTIVATECOURSE://激活课程
                mActivateView.activateCourse(apiResponse);
                break;
            case MyUrl.XITONG_USERLOGOUT_APP://注销
                mSettingView.goOut(apiResponse);
                break;
            case MyUrl.XITONG_PUSHON://推送开关
                mSettingView.setPush(apiResponse);
                break;
        }
    }
}
