package com.example.simplelistview.dataprocessor;

import java.io.Serializable;

public class Cat implements Serializable {
    private int imageResourceId;
    private String name;

    public Cat(String name, int imageResourceId) {
        this.name=name;
        this.imageResourceId=imageResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String cat_name) {
        this.name=cat_name;
    }
}
