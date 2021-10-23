package com.example.simplelistview.dataprocessor;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBank {
    private ArrayList<Cat> arrayListCats=new ArrayList<Cat>();
    private Context context;
    private final String CAT_FILE_NAME="cats.txt";
    public DataBank(Context context)
    {
        this.context=context;
    }
    public ArrayList<Cat> getCats() {
        return arrayListCats;
    }


    public void Save()
    {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(context.openFileOutput(CAT_FILE_NAME,Context.MODE_PRIVATE));
            oos.writeObject(arrayListCats);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load()
    {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(context.openFileInput(CAT_FILE_NAME));
            arrayListCats = (ArrayList<Cat>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
