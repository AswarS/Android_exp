package com.jnu.listview.S;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Databunk {
    private Context context;
    private final String BOOK_FILE_NAME = "Book.txt";
    private ArrayList<Book> bookArrayList= new ArrayList<>();

    public ArrayList<Book> getBookArrayList() {
        return bookArrayList;
    }
    public Databunk(Context context){
        this.context = context;
    }

    public void Save(){
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(context.openFileOutput(BOOK_FILE_NAME, Context.MODE_PRIVATE));
            outputStream.writeObject(bookArrayList);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load(){
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(context.openFileInput(BOOK_FILE_NAME));
            bookArrayList = (ArrayList<Book>)objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
