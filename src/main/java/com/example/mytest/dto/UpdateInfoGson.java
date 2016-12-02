package com.example.mytest.dto;

/**
 * Created by gaopeng on 2016/11/14.
 */
public class UpdateInfoGson {

    /**
     * "state":1,(请求状态 1成功 0失败)
     * "message":"获取最新版本信息成功",（提示信息）
     * "version_no":"1.0.0",（最新版本号）
     * "version_name":"提分王家长版",（最新版本名称）
     * "isforce":0,（是否强制更新 1强制 0不强制）
     * “haslatest”:0,(是否有最新版 1有0无 亦可根据版本号自己判断)
     * "file_name":"TiFenWang.ipa",（最新版本文件名）
     * "comment":"1、美化界面； 2、修改首页更新； 3、修改设置页布局。 " （最新版本更新说明）
     * "downlaod_page":"http://down.huixueyuan.com/publish_cs/android.html",（下载页面，仅限安卓和ios）
     * "file_path":"http://down.huixueyuan.com/publish_cs/android/TiFenWang.apk"（文件路径，仅限安卓）
     */
    private int state;
    private String message;
    private String version_no;
    private String version_name;
    private int isforce;
    private int haslatest;
    private String file_name;
    private String comment;
    private String downlaod_page;
    private String file_path;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion_no() {
        return version_no;
    }

    public void setVersion_no(String version_no) {
        this.version_no = version_no;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public int getIsforce() {
        return isforce;
    }

    public void setIsforce(int isforce) {
        this.isforce = isforce;
    }

    public int getHaslatest() {
        return haslatest;
    }

    public void setHaslatest(int haslatest) {
        this.haslatest = haslatest;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDownlaod_page() {
        return downlaod_page;
    }

    public void setDownlaod_page(String downlaod_page) {
        this.downlaod_page = downlaod_page;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
