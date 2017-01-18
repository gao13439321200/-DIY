package com.example.mytest.dto;

/**
 * Created by gaopeng on 2016/11/18.
 */
public class VideoInfoChildGson {

    /**
     * videopic :
     * course : 02
     * time : 12:49
     * school_name : 浙江省杭州第二中学
     * description :
     * name : 集合与常用逻辑用语
     * cc_id_url : http://192.168.20.66/12/A32ABEED271FB8129C33DC5901307461.mp4
     * teacher : 陈洁
     * vid : 70
     * paixu : 1
     * school_id : 78
     * ccid : A32ABEED271FB8129C33DC5901307461
     * round_num : 2
     */

    private String videopic;
    private String course;
    private String time;
    private String school_name;
    private String description;
    private String name;
    private String cc_id_url;
    private String teacher;
    private String vid;
    private String paixu;
    private String school_id;
    private String ccid;
    private String round_num;

    public VideoInfoChildGson(String cc_id_url , String name){
        this.cc_id_url = cc_id_url;
        this.name = name;
    }


    public String getVideopic() {
        return videopic;
    }

    public void setVideopic(String videopic) {
        this.videopic = videopic;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCc_id_url() {
        return cc_id_url;
    }

    public void setCc_id_url(String cc_id_url) {
        this.cc_id_url = cc_id_url;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getPaixu() {
        return paixu;
    }

    public void setPaixu(String paixu) {
        this.paixu = paixu;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }

    public String getRound_num() {
        return round_num;
    }

    public void setRound_num(String round_num) {
        this.round_num = round_num;
    }
}
