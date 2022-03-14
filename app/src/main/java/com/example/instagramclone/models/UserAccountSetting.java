package com.example.instagramclone.models;

import java.util.ArrayList;

public class UserAccountSetting {
    private String username;
    private String name;
    private String story;
    private String website;
    private Integer posts;
    private ArrayList<User> follower;
    private ArrayList<User> following;
    private String avatar;

    public UserAccountSetting(){
        username = "";
        name = "";
        story = "";
        website = "";
        posts = 0;
        follower = new ArrayList<>();
        following = new ArrayList<>();
        avatar = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getNumberPosts() {
        return posts;
    }

    public void setNumberPosts(Integer posts) {
        this.posts = posts;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getNumberFollower(){
        return follower.size();
    }

    public Integer getNumberFollowing(){
        return following.size();
    }
}
