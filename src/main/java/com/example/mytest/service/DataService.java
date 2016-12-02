package com.example.mytest.service;

import android.util.Log;

import com.example.mytest.dto.ApiResponse;
import com.example.mytest.dto.BookGson;
import com.example.mytest.dto.CeGson;
import com.example.mytest.dto.JiaoCai;
import com.example.mytest.dto.MessageGson;
import com.example.mytest.dto.School;
import com.example.mytest.dto.UpdateInfoGson;
import com.example.mytest.dto.LoginInfoGson;
import com.example.mytest.dto.UserInfoGson;
import com.example.mytest.dto.VideoInfoChildGson;
import com.example.mytest.dto.VideoInfoParent;
import com.example.mytest.util.Const;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by gaopeng on 2016/11/7.
 * 返回数据解析
 */
public class DataService {

    // 登录
    public ApiResponse<LoginInfoGson> login(String info) {
        ApiResponse<LoginInfoGson> apiResponse = new ApiResponse<>(Const.FAILED, "");
        if (info != null) {
            try {
                LoginInfoGson loginInfoGson = new Gson().fromJson(info, LoginInfoGson.class);
                if (!"1".equals(loginInfoGson.success)) {
                    return apiResponse;
                }
                apiResponse.setEvent(Const.SUCCESS);
                apiResponse.setObj(loginInfoGson);
            } catch (Exception e) {
                e.printStackTrace();
                apiResponse.setEvent(Const.FAILED);
            }
        }
        return apiResponse;
    }

    // 系统设置 保存学生信息
    public ApiResponse xitongshezhi_savestudentpersoninfo(String info) {
        ApiResponse apiResponse = new ApiResponse<>(Const.FAILED, "");
        if (info != null) {
            try {
                JSONObject jo = new JSONObject(info);
                apiResponse.setEvent(jo.getString("success"));
            } catch (Exception e) {
                e.printStackTrace();
                apiResponse.setEvent(Const.FAILED);
            }
        }
        return apiResponse;

    }

    //获取消息列表
    public ApiResponse<List<MessageGson>> getmessagedata(String info) {
        ApiResponse<List<MessageGson>> apiResponse = new ApiResponse<>(Const.FAILED, "");
        if (info != null) {
            try {
                JSONObject jo = new JSONObject(info);
                String success = jo.getString("success");
                String message = jo.getString("message");
                if (success.equals("1")) {
                    JSONArray jsonArray = jo.has("data") ? jo.getJSONArray("data") : new JSONArray();
                    List<MessageGson> list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MessageGson messageGson = new Gson().fromJson(jsonArray.opt(i).toString(), MessageGson.class);
                        list.add(messageGson);
                    }
                    apiResponse.setEvent(Const.SUCCESS);
                    apiResponse.setMsg(message);
                    apiResponse.setObjList(list);
                }
            } catch (Exception e) {
                e.printStackTrace();
                apiResponse.setEvent(Const.FAILED);
            }
        }
        return apiResponse;

    }

    //系统更新
    public ApiResponse<UpdateInfoGson> updateApp(String info) {
        ApiResponse<UpdateInfoGson> apiResponse = new ApiResponse<>(Const.FAILED, "");
        try {
            JSONObject object = new JSONObject(info);
            apiResponse.setEvent(object.has("state") ? object.getString("state") : Const.FAILED);
            apiResponse.setMsg(object.has("message") ? object.getString("message") : "信息获取失败");
            apiResponse.setObj(new Gson().fromJson(info, UpdateInfoGson.class));
        } catch (JSONException e) {
            e.printStackTrace();
            apiResponse.setEvent(Const.FAILED);
        }

        return apiResponse;
    }

    //激活课程
    public ApiResponse activateCourse(String info) {
        ApiResponse apiResponse = new ApiResponse(Const.FAILED, "");
        try {
            JSONObject object = new JSONObject(info);
            apiResponse.setEvent(object.has("state") ? object.getString("state") : "0");
        } catch (JSONException e) {
            e.printStackTrace();
            apiResponse.setEvent(Const.FAILED);
        }
        return apiResponse;
    }

    //注销登录
    public ApiResponse goOut(String info) {
        ApiResponse apiResponse = new ApiResponse(Const.FAILED, "");
        try {
            JSONObject object = new JSONObject(info);
            if (object.has("success")) {
                if ("1".equals(object.getString("success")) || "2".equals(object.getString("success")))
                    apiResponse.setEvent(Const.SUCCESS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            apiResponse.setEvent(Const.FAILED);
        }
        return apiResponse;
    }

    //设置推送
    public ApiResponse setPush(String info) {
        ApiResponse apiResponse = new ApiResponse(Const.FAILED, "");
        try {
            JSONObject object = new JSONObject(info);
            apiResponse.setEvent(object.has("success") ? object.getString("success") : Const.FAILED);
        } catch (JSONException e) {
            e.printStackTrace();
            apiResponse.setEvent(Const.FAILED);
        }
        return apiResponse;
    }

    //获取教材
    public ApiResponse<Map<String, List<BookGson>>> getJiaoCai(String info) {
        ApiResponse apiResponse = new ApiResponse(Const.FAILED, "");
        Map<String, List<BookGson>> map = new HashMap<>();
        try {
            JSONObject object = new JSONObject(info);
            apiResponse.setEvent(object.has("state") ? object.getString("state") : Const.FAILED);
            JSONObject object1 = object.getJSONObject("list");
            JSONObject obj = object1.getJSONObject(Const.XUEDUAN);
            Iterator<?> key1 = obj.keys();
            while (key1.hasNext()) {
                String id = (String) key1.next();
                if (id.equals("09")) {
                    continue;
                }
                List<BookGson> list = new ArrayList<>();
                JSONObject object2 = obj.getJSONObject(id);
                Iterator<?> key2 = object2.keys();
                while (key2.hasNext()) {
                    String id1 = (String) key2.next();
                    BookGson bookGson = new BookGson();
                    JSONArray jsonArray1 = object2.getJSONArray(id1);
                    List<CeGson> list1 = new ArrayList<>();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        list1.add(new Gson().fromJson(jsonArray1.opt(i).toString(), CeGson.class));
                    }
                    bookGson.setGsonList(list1);
                    String name = list1.get(0).getBook_name();
                    Log.d("DataService", "name:" + name);
                    bookGson.setName(name.substring(0, name.indexOf(" ")));
                    bookGson.setId(id1);
                    list.add(bookGson);
                }
                map.put(id, list);
            }
            apiResponse.setObjList(map);
        } catch (JSONException e) {
            e.printStackTrace();
            apiResponse.setEvent(Const.FAILED);
        }
        return apiResponse;
    }

    //获取用户权限和教材信息
    public ApiResponse<Map<String, JiaoCai>> getUserPower(String info) {
        ApiResponse<Map<String, JiaoCai>> apiResponse = new ApiResponse<>(Const.FAILED, "");
        Map<String, JiaoCai> map = new HashMap<>();
        try {
            JSONObject object = new JSONObject(info);
            apiResponse.setEvent("1".equals(object.getString("state")) ? Const.SUCCESS : Const.FAILED);
            JSONObject objList = object.getJSONObject("list");
            Iterator<?> key = objList.keys();
            while (key.hasNext()) {
                JiaoCai jiaoCai = new JiaoCai();
                String id = (String) key.next();
                JSONObject objItem = objList.getJSONObject(id);
                jiaoCai.setBookID(objItem.getString("jiaocai_id"));
                jiaoCai.setCeID(objItem.getString("book_id"));
                map.put(id, jiaoCai);
            }
            apiResponse.setObjList(map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return apiResponse;
    }

    //保存教材
    public ApiResponse saveJiaoCai(String info) {
        ApiResponse apiResponse = new ApiResponse(Const.FAILED, "");
        try {
            JSONObject jsonObject = new JSONObject(info);
            apiResponse.setEvent(jsonObject.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return apiResponse;
    }

    //获取个人信息
    public ApiResponse<UserInfoGson> getUserInfo(String info) {
        ApiResponse<UserInfoGson> apiResponse = new ApiResponse<>(Const.FAILED, "");
        if (info != null && !"".equals(info)) {
            try {
                apiResponse.setEvent(Const.SUCCESS);
                apiResponse.setObj(new Gson().fromJson(info, UserInfoGson.class));
            } catch (Exception e) {
                apiResponse.setEvent(Const.FAILED);
            }
        }
        return apiResponse;
    }

    //获取视频列表
    public ApiResponse<List<VideoInfoParent>> getVideoList(String info) {
        ApiResponse<List<VideoInfoParent>> apiResponse = new ApiResponse<>(Const.FAILED, "");
        List<VideoInfoParent> listParent = new ArrayList<>();
        if (info != null && !"".equals(info)) {
            try {
                JSONObject jsonObject = new JSONObject(info);
                apiResponse.setEvent(jsonObject.getString("state"));
                JSONArray videoList = jsonObject.getJSONArray("data");
                for (int i = 0; i < videoList.length(); i++) {
                    VideoInfoParent videoInfoParent = new VideoInfoParent();
                    JSONObject item = (JSONObject) videoList.opt(i);
                    List<VideoInfoChildGson> list = new ArrayList<>();
                    JSONArray itemList = item.getJSONArray("sub_video");
                    for (int j = 0; j < itemList.length(); j++) {
                        list.add(new Gson().fromJson(itemList.opt(j).toString(), VideoInfoChildGson.class));
                    }
                    videoInfoParent.setName(item.getString("name"));
                    videoInfoParent.setList(list);
                    listParent.add(videoInfoParent);
                }
                apiResponse.setObjList(listParent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return apiResponse;
    }

    //获取学校列表
    public ApiResponse<List<School>> getSchoolList(String info) {
        ApiResponse<List<School>> apiResponse = new ApiResponse<>(Const.FAILED, "");
        List<School> listSchool = new ArrayList<>();
        if (info != null) {
            try {
                JSONObject object = new JSONObject(info);
                Iterator iterator = object.keys();
                while (iterator.hasNext()){
                    School school = new School();
                    String id = iterator.next().toString();
                    school.setId(id);
                    school.setName(object.getString(id));
                    listSchool.add(school);
                }
                apiResponse.setEvent(Const.SUCCESS);
                apiResponse.setObjList(listSchool);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return apiResponse;
    }


}
