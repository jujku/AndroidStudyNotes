package com.example.networktest;

public class Item {
    private int userId;
    private String name;
    private int id;
    private String title;
    private String body;

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
