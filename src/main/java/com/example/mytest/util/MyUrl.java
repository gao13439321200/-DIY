package com.example.mytest.util;

/**
 * Created by gaopeng on 2016/11/7.
 * 接口声明类
 */
public class MyUrl {
    //网络访问 固定的前缀
    /**
     * 正式环境
     */
//   public static final String HTML = "http://api.huixueyuan.cn/ifdood_dev01/v2";//正式接口
//   public static final String HTML_SEND_ANWSER = "http://api.huixueyuan.cn/ifdood_dev01";
//   public static final String HTML_PACKAGE_DETAIL = "http://sos.huixueyun.com/jiaoxue/package/ppp_package_detail/";//课程包正式环境
//   public static String HTML_ZNJF = "http://sos.huixueyun.com/api/diag";//智能教辅正式

    /**
     * 本地测试环境
     */
//    public static final String HTML = "http://192.168.12.227/ifdood_dev01/v2";//测试接口
//    public static final String HTML_SEND_ANWSER = "http://192.168.12.227/ifdood_dev01";
//    public static final String HTML_PACKAGE_DETAIL="http://192.168.12.176/jiaoxue/package/ppp_package_detail/";//课程包测试环境
//    public static String HTML_ZNJF = "http://192.168.12.176/api/diag";//智能教辅测试环境

    /**
     * mp4测试环境
     */
    public static final String HTML = "http://192.168.20.104/ifdood_dev01/v2";//测试接口
    public static final String HTML_SEND_ANWSER = "http://192.168.20.104/ifdood_dev01";
    public static final String HTML_PACKAGE_DETAIL="http://192.168.20.104/jiaoxue/package/ppp_package_detail/";//课程包测试环境
    public static String HTML_ZNJF = "http://192.168.20.104/api/diag";//智能教辅测试环境

    //登录
    public static final String LOGIN = "xitong/userLogin_app.php";
    //设置保存个人信息
    public static final String XITONGSHEZHI_SAVESTUDENTPERSONINFO = "xitongshezhi/saveStudentPersonInfo.php";
    //获取消息列表
    public static final String MESSAGES_GETMESSAGEDATA = "messages/getMessageData.php";
    //标记消息为已读
    public static final String MESSAGES_CHANGEMESSAGESTATUS = "messages/changeMessageStatus.php";
    //清空消息
    public static final String MESSAGES_DELMSG = "messages/delMsg.php";
    //系统更新
    public static final String XITONG_UPDATEAPP = "xitong/checkUpdate.php";
    //激活课程
    public static final String ACTIVATECOURSE = "user/codeSavePower.php";
    //注销登录
    public static final String XITONG_USERLOGOUT_APP = "xitong/userlogout_app.php";
    //设置推送开关
    public static final String XITONG_PUSHON = "xitong/pushon_app.php";
    //获取教材
    public static final String GETJIAOCAI = "comm/getBooks_app.php";
    //获取个人教材
    public static final String GETUSERPOWER = "comm/getUserInfo_0415.php";
    //保存教材版本
    public static final String SAVEJIAOCAI = "user/saveBanben.php";
    //获取个人信息
    public static final String GETUSERINFO = "gerenxinxi/getgerenxinxi.php";
    //获取视频列表
    public static final String GETVIDEOLIST = "mingshi/getStudentVideo.php";
    //获取学校列表
    public static final String GETSCHOOLLIST = "zhuangyuantiku/getArea.php";
    //试卷列表
    public static final String GETEXAMLIST = "zhuangyuantiku/getzhuangyuantiku.php";

}
