package com.example.instagramclone.models;

public class Account {
    private String name;
    private String email;
    private String password;
    private String story;
    private String website;
    private String sex;

    public Account(){
        name = "";
        email = "";
        password = "";
        story = "";
        website = "";
        sex = "";
    }

    public Account(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
