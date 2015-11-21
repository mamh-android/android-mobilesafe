package com.example.mamh.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

/**
 * Created by mamh on 15-11-21.
 */
public class GPSService extends Service {
    private static final String TAG = "GPSService";
    private LocationManager lm;
    private GPSLocationListener gps;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() in GPSservice");
        initLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(gps);
        gps = null;
        Log.d(TAG, "onDestroy() in GPSservice");
    }

    private void initLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> provider = lm.getAllProviders();
        Log.d(TAG, provider.toString());

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String providername = lm.getBestProvider(criteria, true);

        gps = new GPSLocationListener();
        lm.requestLocationUpdates(providername, 0, 0, gps);


    }

    private class GPSLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //位置发生变化回调这个方法
            double longitude = location.getLongitude();//经度
            double latitude = location.getLatitude();//纬度
            float accuracy = location.getAccuracy();//精确度
            Log.d(TAG, " " + longitude + "=" + latitude + "=" + accuracy);
            //发短息给安全号码,把最后一次保存
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("lastlocation", "j:" + longitude + "\nw:" + latitude + "\n" + accuracy);
            editor.commit();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //状态发生改变时回调,开启，关闭就是状态改变

        }

        @Override
        public void onProviderEnabled(String provider) {
            //某一个位置提供者可以使用回调这个方法
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
