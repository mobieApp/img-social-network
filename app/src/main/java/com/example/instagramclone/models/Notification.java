package com.example.instagramclone.models;

import java.util.Date;

public class Notification {
    private String userId;
    private String text;
    private String postId;
    private boolean isPost;
    private Date timestamp;

    public Notification(String userId, String text, String postId, boolean ispost, Date timestamp) {
        this.userId = userId;
        this.text = text;
        this.postId = postId;
        this.isPost = ispost;
        this.timestamp = timestamp;
    }

    public Notification(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isIspost() {
        return isPost;
    }

    public void setIspost(boolean ispost) {
        this.isPost = ispost;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
