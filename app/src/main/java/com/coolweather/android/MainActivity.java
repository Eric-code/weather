package com.coolweather.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        LocationManager locationManager = (LocationManager) getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager
        String provider = LocationManager.GPS_PROVIDER;// 指定LocationManager的定位方法
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);// 调用getLastKnownLocation()方法获取当前的位置信息
            getLocationInfo(location);// 获得经度和纬度的方法
            // return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void getLocationInfo(Location location) {
        String latLongInfo;
        if (location != null) {
            double lat = location.getLatitude();//获取纬度
            double lng = location.getLongitude();//获取经度
            latLongInfo = "纬度为: " + lat + "\n经度为: " + lng;
        } else {
            latLongInfo = "没有发现到当前位置";
        }
    }

}