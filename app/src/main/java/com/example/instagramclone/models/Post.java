package com.example.instagramclone.models;

import com.example.instagramclone.Utils.UserAuthentication;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Comparator;
import java.util.Date;

public class Post {
    // variables for storing data
    // of our recycler view item
    private String media_url;
    private String userId;
    private String caption;
    private Date timestamp;
    private int likesCount;

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Post(String media_url, String userId, String caption, Date timestamp, int likesCount) {
        this.media_url = media_url;
        this.userId = userId;
        this.caption = caption;
        this.timestamp = timestamp;
        this.likesCount = likesCount;
    }

    public Post(){

    }
}
