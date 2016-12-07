package com.example.mytest.util;

import android.util.Log;

import com.example.mytest.dto.ApiResponse;
import com.example.mytest.service.DataService;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gaopeng on 2016/11/7.
 * Json数据处理
 */
public class JsonUtil {

    /**
     * 将map参数转换成String格式
     *
     * @param map 参数map
     * @return    参数String
     */
    public static String reverJson(Map map) {
        JSONObject jsonObject;
        jsonObject = new JSONObject();
        try {
            if (map.isEmpty()) {
                map = new HashMap();
            }
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String name = iter.next().toString();
                if (name.equals("ids") && !map.get(name).equals("")) {
                    JSONObject value = (JSONObject) map.get(name);
                    jsonObject.put(name, value);
                } else {
                    if (name.equals("img") && !map.get(name).equals("")) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();

//                        Bitmap bitmap = ImageUtil.createBitmap((String) map.get(name));// 根据原图路径获取bitmep 对象
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                        String photo = "";
                        byte[] buffer = bos.toByteArray();

                        try {
                            bos.close();
                            // 将图片的字节流数据加密成base64字符输出
                            photo = android.util.Base64.encodeToString(buffer, 0, buffer.length,
                                    android.util.Base64.NO_WRAP);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("2222", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>json photo == " + photo);
                        jsonObject.put(name, photo);
                    } else {
                        jsonObject.put(name, map.get(name));
                    }
                }
            }
            //这里针对比较特殊的传值方式  需要添加一个md5值加密
            jsonObject.put("mk", MD5Util.md5(Const.MD5String));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return jsonObject.toString();
    }

    /**
     * 解析返回值
     *
     * @param result 请求结果
     * @param url    接口
     * @return       处理后的返回值
     */
    public static ApiResponse getJsonResult(String result, String url , DataService dataService) {
        ApiResponse obj = null;
        switch (url) {
            case MyUrl.LOGIN://登录
                obj = dataService.login(result);
                break;
            case MyUrl.XITONGSHEZHI_SAVESTUDENTPERSONINFO://保存个人信息
                obj = dataService.xitongshezhi_savestudentpersoninfo(result);
                break;
            case MyUrl.MESSAGES_GETMESSAGEDATA://获取消息列表
                obj = dataService.getmessagedata(result);
                break;
            case MyUrl.XITONG_UPDATEAPP://系统更新
                obj = dataService.updateApp(result);
                break;
            case MyUrl.ACTIVATECOURSE://激活课程
                obj = dataService.activateCourse(result);
                break;
            case MyUrl.XITONG_USERLOGOUT_APP://注销
                obj = dataService.goOut(result);
                break;
            case MyUrl.XITONG_PUSHON://推送开关
                obj = dataService.setPush(result);
                break;
            case MyUrl.GETJIAOCAI://获取教材列表
                obj = dataService.getJiaoCai(result);
                break;
            case MyUrl.GETUSERPOWER://获取个人教材
                obj = dataService.getUserPower(result);
                break;
            case MyUrl.SAVEJIAOCAI://保存教材
                obj = dataService.saveJiaoCai(result);
                break;
            case MyUrl.GETUSERINFO://获取个人信息
                obj = dataService.getUserInfo(result);
                break;
            case MyUrl.GETVIDEOLIST://获取视频列表
                obj = dataService.getVideoList(result);
                break;
            case MyUrl.GETSCHOOLLIST://获取学校列表
                obj = dataService.getSchoolList(result);
                break;
            case MyUrl.GETEXAMLIST://获取试卷
                obj = dataService.getShiJuanList(result);
                break;
        }
        return obj;
    }

}
