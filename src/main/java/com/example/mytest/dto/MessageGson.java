package com.example.mytest.dto;

/**
 * Created by gaopeng on 2016/11/10.
 */
public class MessageGson {
    /**
     * id : 3542173
     * title : 教材版本已修改
     * created : 2016-11-10 14:14:55
     * status : 0
     * type : 1
     * contenturl : 教材版本已修改
     */

    private String id;//消息id
    private String title;//标题
    private String created;//创建时间
    private String status;//是否已读 0未读，1已读
    private String type;
    private String contenturl;//消息内容

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContenturl() {
        return contenturl;
    }

    public void setContenturl(String contenturl) {
        this.contenturl = contenturl;
    }
}
