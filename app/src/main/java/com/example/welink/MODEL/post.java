package com.example.welink.MODEL;

public class post {
    String date;
    String description;
    String image;
    String time;
    String uid;
    String username;

    public post() {
    }

    public post(String date, String description, String image, String time, String uid, String username) {
        this.date = date;
        this.description = description;
        this.image = image;
        this.time = time;
        this.uid = uid;
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
