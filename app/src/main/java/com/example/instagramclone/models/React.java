package com.example.instagramclone.models;

public class React implements Comparable{
    private String userId;
    private Integer point;

    public React(){

    }

    public React(String userId, Integer point) {
        this.userId = userId;
        this.point = point;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    @Override
    public int compareTo(Object o) {
        int comparePoint = ((React)o).getPoint();
        return comparePoint - this.point;
    }
}
