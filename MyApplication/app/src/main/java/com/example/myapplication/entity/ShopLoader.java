package com.example.myapplication.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.os.Handler;

public class ShopLoader {
    private ArrayList<Shop> shops = new ArrayList<>();

    public ArrayList<Shop> getShops(){
        return shops;
    }

    public String download(String urlString){
        try{
            //设置连接属性
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setUseCaches(false);

            //连接成功后，获得输入流，开始获取数据
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            InputStreamReader input = new InputStreamReader(inputStream);
            BufferedReader buffer = new BufferedReader(input);
            if(conn.getResponseCode()==200){    //200意味着成功返回网站地址
                String inputLine;
                StringBuffer resultDate = new StringBuffer();
                //读取返回的全部数据，将所有数据连在一起，作为结果返回
                while((inputLine = buffer.readLine()) != null){
                    resultDate.append(inputLine);
                }
                String text = resultDate.toString();
                Log.v("out————————————————>", text);
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void parseJson(String text){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(text);
            JSONArray jsonArray = jsonObject.getJSONArray("shops");
            int length = jsonArray.length();
            for(int i=0; i<length; i++){
                JSONObject shopJson = jsonArray.getJSONObject(i);
                Shop shop = new Shop();
                shop.setName(shopJson.getString("name"));
                shop.setMemo(shopJson.getString("memo"));
                shop.setLatitude(shopJson.getDouble("latitude"));
                shop.setLongitude(shopJson.getDouble("longitude"));
                shops.add(shop);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void load(final Handler handler, final String url){
        new Thread(() -> {
            String content = download(url);
            parseJson(content);
            handler.sendEmptyMessage(1);
        }).start();
    }
}
