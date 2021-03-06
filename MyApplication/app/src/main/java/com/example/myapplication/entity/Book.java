package com.example.myapplication.entity;

import java.io.Serializable;

public class Book implements Serializable {
    private int resourceId;
    private String title;

    public Book(String title, int resourceId) {
        this.resourceId = resourceId;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getTitle() {
        return title;
    }
}
