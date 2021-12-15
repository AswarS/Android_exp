package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.myapplication.R;
import com.example.myapplication.entity.Shop;
import com.example.myapplication.entity.ShopLoader;

import java.util.ArrayList;
import android.os.Handler;

public class MapFragment extends Fragment {

    MapView mMapView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = view.findViewById(R.id.map_view);

        BaiduMap baiduMap = mMapView.getMap();

        LatLng centerPoint = new LatLng(22.2559, 113.541112);
        MapStatus mMapStatus = new MapStatus.Builder().target(centerPoint).zoom(17).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.logo);
        MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(centerPoint);
        Marker marker = (Marker) baiduMap.addOverlay(markerOptions);

        OverlayOptions textOption = new TextOptions().fontSize(50).text("暨南大学珠海校区").rotate(0).position(centerPoint);
        baiduMap.addOverlay(textOption);

        final ShopLoader shopLoader = new ShopLoader();
        Handler handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                drawShops(shopLoader.getShops());
            }};
        shopLoader.load(handler, "http://file.nidama.net/class/mobile_develop/data/bookstore.json");

        return view;
    }

    void drawShops(ArrayList<Shop> shops){
        if(mMapView == null) return;
        BaiduMap baiduMap = mMapView.getMap();
        for(int i=0; i<shops.size(); i++){
            System.out.println(shops.get(i).getName());
            Shop shop = shops.get(i);
            LatLng centerPoint = new LatLng(shop.getLatitude(), shop.getLongitude());
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.shop);
            MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(centerPoint);
            Marker marker = (Marker) baiduMap.addOverlay(markerOptions);

            OverlayOptions textOption = new TextOptions().fontSize(50).text(shop.getName()).rotate(0).position(centerPoint);
            baiduMap.addOverlay(textOption);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}