package com.example.instagramclone.models;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchRecent implements Comparable<SearchRecent>{
    String userid;
    Date timestamp;
    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public SearchRecent(String userid) {
        this.userid = userid;
        this.timestamp = new Date();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public SearchRecent() {
        this.userid = "";
        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String GetStringTimestamp(){return dateFormat.format(getTimestamp()); }

    @Override
    public int compareTo(SearchRecent o) {
        return -1*getTimestamp().compareTo(o.getTimestamp());
    }
}
