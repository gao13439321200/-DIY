package com.example.mytest.data;

import android.content.Context;
import android.widget.Toast;

import com.example.mytest.data.model.AppModel;
import com.example.mytest.data.model.AppModelImpl;
import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.UpdateInfoGson;
import com.example.mytest.util.Const;
import com.example.mytest.util.MyUrl;
import com.example.mytest.util.SPUtil;
import com.example.mytest.util.UpdateUtil;

import java.util.HashMap;

/**
 * Created by gaopeng on 2016/11/8.
 * 请求和逻辑处理父类
 */
public class BasePresenter implements BaseListen {
    public AppModel mAppModel;
    public Context mContext;
    public String userID;
    public String xueduan;
    public UpdateView mUpdateVIew;
    public HashMap<String,Object> getDataMap;//所有子类中请求用的map

    public BasePresenter(Context context) {
//        this.mAppModel = AppModelImpl.getInstance(this);
        this.mAppModel = new AppModelImpl(this);
        this.mContext = context;
        if (userID == null || "".equals(userID)){
            userID = (String) SPUtil.get(context, Const.SP_USERID,"");
        }
        if (xueduan == null || "".equals(xueduan)){
            xueduan = (String) SPUtil.get(context, Const.SP_XUEDUAN,"");
        }
    }

    /**
     * 系统更新
     */
    public void updataApp(UpdateView updateVIew){
        mUpdateVIew = updateVIew;
        getDataMap = new HashMap<>();
        getDataMap.put("version", UpdateUtil.getVerName(mContext));
        getDataMap.put("type", "4");
        mAppModel.retrofit_Post(getDataMap, MyUrl.XITONG_UPDATEAPP);
    }



    @Override
    public void onSuccess(ApiResponse apiResponse, String url) {
        switch (url){
            case MyUrl.XITONG_UPDATEAPP:
                switch (apiResponse.getEvent()){
                    case Const.SUCCESS:
                        String version = UpdateUtil.getVerCode(mContext) + "";
                        String version_new =((UpdateInfoGson)apiResponse.getObj()).getVersion_no();
                        if (version.equals(version_new)) {
                            mUpdateVIew.updateApp(apiResponse,false);
                        }else{
                            mUpdateVIew.updateApp(apiResponse,true);
                        }
                        break;
                    case Const.FAILED:
                        mUpdateVIew.updateApp(apiResponse,true);
                        break;
                }
                break;
        }
    }

    @Override
    public void onFailed(ApiResponse apiResponse, String url) {
        Toast.makeText(mContext, "信息获取失败", Toast.LENGTH_SHORT).show();
    }
}
