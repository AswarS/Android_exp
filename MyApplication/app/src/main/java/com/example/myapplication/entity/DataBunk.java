package com.example.myapplication.entity;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBunk {
    private final Context context;
    private final String BOOK_FILE_NAME="Book.txt";
    private ArrayList<Book> bookArrayList =new ArrayList<>();

    public DataBunk(Context context){
        this.context=context;
    }

    public ArrayList<Book> getBook(){
        return bookArrayList;
    }
    public void save()  {
        ObjectOutputStream outputStream=null;
        try {
            outputStream = new ObjectOutputStream(context.openFileOutput(BOOK_FILE_NAME, Context.MODE_PRIVATE));
            outputStream.writeObject(bookArrayList);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        ObjectInputStream inputStream=null;
        try {
            inputStream = new ObjectInputStream(context.openFileInput(BOOK_FILE_NAME));
            bookArrayList = (ArrayList<Book>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
