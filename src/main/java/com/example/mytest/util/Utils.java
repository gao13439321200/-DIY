package com.example.mytest.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mytest.activity.Login.LoginActivity;
import com.example.mytest.activity.Main.MainActivity;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String ACTIVITY_SERVICE = "";
    public static MainActivity mainActivity;
    public static LoginActivity loginActivity;
    //public static LoadingActivity loadingActivity;

    public static final String TAG = "LoadingActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.ifdoo.activity.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.ifdoo.activity.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";

    public static String appid = "";
    public static String userId = "";
    public static String channelId = "";
    public static String requestId = "";

  /*  //判断应用程序是否在后台运行，如果在后台则不显示toast
    *//**     *判断当前应用程序处于前台还是后台     */
    public static boolean showToast=true;
    /**
     * 防止点击事件响应过慢。用户多次点击。
     * */
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 竞技任务报告查看排名弹窗防止点击过快
     *
     * @return
     */
    public synchronized static boolean popWindowIsFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static String getTopActivity(Activity context)
    {
        String str="";
        ActivityManager manager = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE) ;

        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;

        if(runningTaskInfos != null){
            str = (runningTaskInfos.get(0).topActivity).toString() ;
        }
        else{
            return "" ;
        }
        return str ;
    }

    /**

     * 检测某ActivityUpdate是否在当前Task的栈顶

     */
    static String cmdName ="";
    public static boolean isTopActivy(Activity context){
        cmdName = context.getComponentName().getClassName();
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;
        if(null != runningTaskInfos){

            cmpNameTemp=(runningTaskInfos.get(0).topActivity).getClassName();

            Log.e("cmpname","cmpname: "+cmpNameTemp);

        }
        Log.e("cmpname", "cmdName: " + cmdName);
        if(null == cmpNameTemp)return false;
        return cmpNameTemp.equals(cmdName);

    }

    //暂停后台播放音乐
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static boolean muteAudioFocus(Context context, boolean bMute) {
        if (context == null) {
            Log.d("ANDROID_LAB", "context is null.");
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            // 2.1以下的版本不支持下面的API：requestAudioFocus和abandonAudioFocus
            Log.d("ANDROID_LAB", "Android 2.1 and below can not stop music");
            return false;
        }
        boolean bool = false;
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (bMute) {//发送一个请求来获取音频的焦点
            int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED; //成功
        } else {//放弃音频焦点
            int result = am.abandonAudioFocus(null);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        Log.d("ANDROID_LAB", "pauseMusic bMute=" + bMute + " result=" + bool);
        return bool;//很有用
    }
    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            loginActivity = (LoginActivity) context;
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasBind(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.putString("appid", appid);
        editor.putString("userId", userId);
        editor.putString("channelId", channelId);
        editor.putString("requestId", requestId);
        editor.commit();
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreferenceIpAddress", ex.toString());
        }
        return null;
    }

    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /** * 按名字清除本应用数据库 * * @param context * @param dbName */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    //判断设备是否联网
    //是否联网网络
    public static boolean hasInternet(final Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info!=null && info.isConnected());
        } catch (Exception e) {
            return false;
        }
    }
    /**头像画布*/

    public  Bitmap createCircleImage(Bitmap resource, int mins)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        int width = resource.getWidth();
        int height = resource.getHeight();
        int min = width < height ? width : height;

        float s = mins/min;
        Matrix matrix = new Matrix();
        matrix.postScale(s, s);
        Bitmap source = Bitmap.createScaledBitmap(resource, mins, mins, true);

        Bitmap target = Bitmap.createBitmap(mins, mins , Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(mins / 2 , mins / 2 , mins / 2 , paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);

        return target;
    }

    // 判断是否是手机号码
    public static boolean isPhone(String number){
        String pattern = "^1(3[0-9]|4[5,7]|5[012356789]|7[0678]|8[0-9])\\d{8}$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(number);
        return  m.matches();
    }


    // 判断是否是qq号码
    public static boolean isQq(String qq){
        String pattern = "^\\d{5,11}$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(qq);
        return  m.matches();
    }


    //判断是否有空格
    public static boolean hasBlank(String tmpString) {
        if (tmpString.startsWith(" ") || tmpString.endsWith(" "))
            return true;
        String s[] = tmpString.split("[ ]");
        if (s.length == 1)
            return false;
        else if (s.length >= 2)
            return true;

        return false;
    }

    // 判断是否包含汉字
    public static boolean isHaveChina(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ss = str.charAt(i);
            boolean s = String.valueOf(ss).matches("[\u4e00-\u9fa5]");
            if (s) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasArray1 = false;
    /**
     * 写入应用缓存文件
     *
     * @param context
     * @param filename
     * @param info
     * @param hasArray 是否含有array
     * @throws Exception
     */
    public static void writeInfo(Context context, String filename, HashMap<String, Object> info,boolean hasArray) throws Exception {
        hasArray1 = hasArray;
        File file = getAppCacheDir(context, filename);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(hasArray ? info.toString() : info);
        oos.flush();
        oos.close();
    }

    /**
     * 读取应用缓存文件
     *
     * @param context
     * @param filename
     * @return
     * @throws Exception
     */
    public static HashMap<String, Object> readInfo(Context context, String filename) throws Exception {
        File file = getAppCacheDir(context, filename);
        HashMap<String, Object> imInfo = new HashMap<>();
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        if (!hasArray1) {
            imInfo = (HashMap<String, Object>) ois.readObject();
        }else {
            String info = (String) ois.readObject();
            Log.d(TAG, "存储的数据：" + info);
            JSONObject obj = new JSONObject(info);
            imInfo.put("userID", obj.getString("userID"));
            imInfo.put("list", obj.getJSONArray("list"));
            imInfo.put("did", obj.has("did")?obj.getString("did"):"");
            if (obj.has("imgArray"))
                imInfo.put("imgArray", obj.getJSONArray("imgArray"));
        }
        ois.close();
        Logger.d("sendResultMap= "+ imInfo.toString());
        return imInfo;

    }

    /**
     * 获取应用缓存路径
     *
     * @param context
     * @param fileName
     * @return
     */
    public static File getAppCacheDir(Context context, String fileName) {
        String cachePath = context.getFilesDir().getPath();

        Log.i("file", cachePath + File.separator + fileName);
        return new File(cachePath + File.separator + fileName);
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {

        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }


    /**
     * webview 设置图片的显示居中 (通过 设置html style 设置图片默认大小为webview大小)
     * @param html 图片地址
     * @return
     */
    public static String getStringBaseUrl(String html){
        // 设置页面居中显示，内容大小自适应webview大小
        StringBuffer sb = new StringBuffer();
        sb.append("<html>")
                .append("<head>")
                .append("<meta http-equiv='Content-Type' content='text/html'; charset='UTF-8'>")
                .append("<style type='text/css'>")
                .append(".response-img {max-width: 100%;}")
                .append("#box {width: 100%;height: 100%;display: table;text-align: center;background: #fff;}")
                .append("#box span {display: table-cell;vertical-align: middle;}")
                .append("</style>")
                .append("<title>")
                .append("</title>")
                .append("</head>")
                .append("<body style='text-align: center;' onClick='window.myInterfaceName.showToast(\"finish Activity\")'>")
                .append("<div id='box'>")
                .append("<span>")
                .append("<img src='" + html + "' class='response-img' style='width: 100%'/>")
                .append("</span>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return sb.toString();
    }


}
