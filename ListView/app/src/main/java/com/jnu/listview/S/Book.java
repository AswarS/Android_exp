package com.jnu.listview.S;

public class Book{
    public String getName() {
        return name;
    }

    public int getImage_id() {
        return image_id;
    }

    private String name;
    private int image_id;

    public Book(String name, int image_id){
        this.name = name;
        this.image_id = image_id;
    }

    public void setName(String book_name){
        this.name = book_name;
    }
}
