package com.example.instagramclone.models;

import java.util.ArrayList;

public class User {
    private String password;
    private String email;
    private String phone;
    private String gender;
    private String Dob;
    private String username;
    private String name;
    private String story;
    private String website;
    private Integer posts;
    private ArrayList<String> follower;
    private ArrayList<String> following;
    private String avatar;

    public User(){
        password = "";
        email = "";
        phone = "";
        gender = "";
        Dob = "";
        username = "";
        name = "";
        story = "";
        website = "";
        posts = 0;
        follower = new ArrayList<>();
        following = new ArrayList<>();
        avatar = "";
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
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
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
}
