package com.example.simplelistview.S;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import java.util.ArrayList;

public class  Databunk
{
    private  Context context;
    private final String CAT_FILE_NAME="Cat.txt";
    private ArrayList<Cat> catArrayList=new ArrayList<>();

    public Databunk(Context context){
        this.context=context;
    }

    public ArrayList<Cat> getCat(){
        return catArrayList;
    }
    public void save()  {
        ObjectOutputStream outputStream=null;
        try {
            outputStream = new ObjectOutputStream(context.openFileOutput("Cat.txt", Context.MODE_PRIVATE));
            outputStream.writeObject(catArrayList);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        ObjectInputStream inputStream=null;
        try {
            inputStream = new ObjectInputStream(context.openFileInput("Cat.txt"));
            catArrayList = (ArrayList<Cat>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
