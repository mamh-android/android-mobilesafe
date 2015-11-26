package com.example.mamh.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mamh.mobilesafe.service.AddressService;
import com.example.mamh.mobilesafe.ui.SettingItemView;
import com.example.mamh.mobilesafe.utils.ServiceUtils;


public class SettingActivity extends Activity {
    private static final String TAG = "SettingActivity";
    private SettingItemView siv_update;
    private SettingItemView siv_show_address;
    private SharedPreferences sp;
    private Intent showAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        siv_update= (SettingItemView) findViewById(R.id.siv_update);

        boolean update = sp.getBoolean("update", false);
        if (update){
            siv_update.setChecked(true);
        }else {
            siv_update.setChecked(false);
        }

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                if(siv_update.isChecked()){
                    //is checked
                    siv_update.setChecked(false);
                    editor.putBoolean("update",false);
                }else{
                    //not checked
                    siv_update.setChecked(true);
                    editor.putBoolean("update",true);
                }
                editor.commit();

            }
        });

        siv_show_address= (SettingItemView) findViewById(R.id.siv_show_address);
        showAddress = new Intent(this, AddressService.class);
        boolean show_address = sp.getBoolean("show_address", false);
        boolean isServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.example.mamh.mobilesafe.service.AddressService");
        if (show_address){
            siv_show_address.setChecked(true);
        }else {
            siv_show_address.setChecked(false);
        }
        siv_show_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                if(siv_show_address.isChecked()){
                    //is checked
                    siv_show_address.setChecked(false);
                    stopService(showAddress);
                    editor.putBoolean("show_address",false);
                }else{
                    //not checked
                    siv_show_address.setChecked(true);
                    startService(showAddress);
                    Log.d(TAG, "                    startService(showAddress);\n");
                    editor.putBoolean("show_address",true);
                }
                editor.commit();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        showAddress = new Intent(this, AddressService.class);
        boolean show_address = sp.getBoolean("show_address", false);
        if (show_address){
            siv_show_address.setChecked(true);
        }else {
            siv_show_address.setChecked(false);
        }
    }
}
