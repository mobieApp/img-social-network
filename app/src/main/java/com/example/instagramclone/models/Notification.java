package com.example.instagramclone.models;

import java.util.ArrayList;
import java.util.Date;

public class Notification implements Comparable<Notification>{
    private String FromUserId;
    private String ToUserId;
    private String message;
    private String postId;
    private boolean isPost;
    private Date timestamp;
    private String imgPost;
    public Notification(String FromUserId,String toUserId ,String msg, String postId, boolean isPost, Date timestamp, String imgPost) {
        this.FromUserId = FromUserId;
        this.ToUserId = toUserId;
        this.message = msg;
        this.postId = postId;
        this.isPost = isPost;
        this.timestamp = timestamp;
        this.imgPost = imgPost;
    }

    public Notification(){

    }

    public String getFromUserId() {
        return FromUserId;
    }

    public void setFromUserId(String fromUserId) {
        FromUserId = fromUserId;
    }

    public String getToUserId() {
        return ToUserId;
    }

    public void setToUserId(String toUserId) {
        ToUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
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

    public String getImgPost() {
        return imgPost;
    }

    public void setImgPost(String imgPost) {
        this.imgPost = imgPost;
    }

    @Override
    public int compareTo(Notification o) {
        return -1*getTimestamp().compareTo(o.getTimestamp());
    }
}
