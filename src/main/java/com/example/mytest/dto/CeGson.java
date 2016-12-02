package com.example.mytest.dto;

/**
 * Created by gaopeng on 2016/11/16.
 */
public class CeGson {


    /**
     * id : 597
     * grade_flag : 1
     * book_name : 人教版 数学 三年级 上册
     * jiaocai_type : 1
     * order : 0
     * course : 02
     */

    private String id;
    private String grade_flag;
    private String book_name;
    private String jiaocai_type;
    private String order;
    private String course;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade_flag() {
        return grade_flag;
    }

    public void setGrade_flag(String grade_flag) {
        this.grade_flag = grade_flag;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getJiaocai_type() {
        return jiaocai_type;
    }

    public void setJiaocai_type(String jiaocai_type) {
        this.jiaocai_type = jiaocai_type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
