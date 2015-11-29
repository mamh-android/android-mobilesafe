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
    private Intent showAddressIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        siv_update = (SettingItemView) findViewById(R.id.siv_update);

        boolean update = sp.getBoolean("update", false);
        if (update) {
            siv_update.setChecked(true);
        } else {
            siv_update.setChecked(false);
        }

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                if (siv_update.isChecked()) {
                    //is checked
                    siv_update.setChecked(false);
                    editor.putBoolean("update", false);
                } else {
                    //not checked
                    siv_update.setChecked(true);
                    editor.putBoolean("update", true);
                }
                editor.commit();

            }
        });

        siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
        showAddressIntent = new Intent(this, AddressService.class);
        boolean isServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.example.mamh.mobilesafe.service.AddressService");
        Log.d(TAG, " is running = " + isServiceRunning);
        siv_show_address.setChecked(isServiceRunning);
        siv_show_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_show_address.isChecked()) {
                    //is checked
                    siv_show_address.setChecked(false);
                    stopService(showAddressIntent);
                    Log.d(TAG, "                    stopService(showAddressIntent);\n");
                } else {
                    //not checked
                    siv_show_address.setChecked(true);
                    startService(showAddressIntent);
                    Log.d(TAG, "                    startService(showAddressIntent);\n");
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        showAddressIntent = new Intent(this, AddressService.class);
        boolean isServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.example.mamh.mobilesafe.service.AddressService");
        siv_show_address.setChecked(isServiceRunning);
    }
}
