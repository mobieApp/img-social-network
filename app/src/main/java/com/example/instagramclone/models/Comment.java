package com.example.instagramclone.models;

import java.util.Date;

public class Comment {
    private String content;
    private String listReply;
    private Date timestamp;
    private String userId;
    private String postID;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getListReply() {
        return listReply;
    }

    public void setListReply(String listReply) {
        this.listReply = listReply;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
