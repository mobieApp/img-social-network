package com.example.instagramclone.models;

import java.util.ArrayList;

public class User {
    private String password;
    private String email;
    private String phone;
    private String gender;
    private String dob;
    private String username;
    private String name;
    private String story;
    private String website;
    private Integer posts;
    private ArrayList<String> follower;
    private ArrayList<String> following;
    private String avatar;
    private ArrayList<SearchRecent> recent;
    private ArrayList<React> react;
    private String userid;


    public ArrayList<SearchRecent> getRecent() {
        return recent;
    }

    public void setRecent(ArrayList<SearchRecent> recent) {
        this.recent = recent;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public User(){
//        userid = "";
        password = "";
        email = "";
        phone = "";
        gender = "";
        dob = "";
        username = "";
        name = "";
        story = "";
        website = "";
        posts = 0;
        follower = new ArrayList<>();
        following = new ArrayList<>();
        react = new ArrayList<>();
        recent = new ArrayList<SearchRecent>();
        avatar = "";
    }

    public User(String username, String name, String avatar) {
        this.username = username;
        this.name = name;
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        dob = dob;
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

    public void setName(String fullName) {
        this.name = fullName;
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

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<String> follower) {
        this.follower = follower;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public Integer NumberFollower(){
        return follower.size();
    }

    public Integer NumberFollowing(){
        return following.size();
    }

    public ArrayList<React> getReact() {
        return react;
    }

    public void setReact(ArrayList<React> react) {
        this.react = react;
    }
}
