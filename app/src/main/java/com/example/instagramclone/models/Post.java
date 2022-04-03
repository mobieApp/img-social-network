package com.example.instagramclone.models;

import com.example.instagramclone.Utils.UserAuthentication;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Post {
    // variables for storing data
    // of our recycler view item
    private String id;
    private String media_url;
    private String userId;
    private String caption;
    private Date timestamp;

    public ArrayList<String> getReports() {
        return reports;
    }

    public void setReports(ArrayList<String> reports) {
        this.reports = reports;
    }

    public ArrayList<String> getIsHide() {
        return isHide;
    }

    public void setIsHide(ArrayList<String> isHide) {
        this.isHide = isHide;
    }

    private ArrayList<String> reports;
    private ArrayList<String> isHide;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private ArrayList<String> listLike;

    public ArrayList<String> getListLike() {
        return listLike;
    }

    public void setListLike(ArrayList<String> listLikes) {
        this.listLike = listLikes;
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

    public Post(){
        this.listLike = new ArrayList<>();
    }

    public Post(String id, String media_url, String userId, String caption, Date timestamp, ArrayList<String> listLike) {
        this.id = id;
        this.media_url = media_url;
        this.userId = userId;
        this.caption = caption;
        this.timestamp = timestamp;
        this.listLike = listLike;
        this.reports = new ArrayList<String>();
        this.isHide = new ArrayList<String>();
    }
}
