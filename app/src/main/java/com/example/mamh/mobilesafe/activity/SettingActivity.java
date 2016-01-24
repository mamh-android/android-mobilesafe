package com.example.mamh.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mamh.mobilesafe.R;
import com.example.mamh.mobilesafe.service.AddressService;
import com.example.mamh.mobilesafe.service.CallSmsSafeService;
import com.example.mamh.mobilesafe.ui.SettingClikcView;
import com.example.mamh.mobilesafe.ui.SettingItemView;
import com.example.mamh.mobilesafe.utils.ServiceUtils;


public class SettingActivity extends Activity {
    private static final String TAG = "SettingActivity";
    private SettingItemView siv_update;
    private SettingItemView siv_show_address;
    private SettingClikcView scv_change_bg;//设置归属地背景
    private SettingItemView siv_callsms_safe;//短息拦截设置

    private SharedPreferences sp;
    private Intent showAddressIntent;
    private Intent callSmsSafeIntent;

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


        final String[] items = {
                "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"
        };
        final int itemSelected = sp.getInt("which", 0);
        scv_change_bg = (SettingClikcView) findViewById(R.id.scv_changebg);
        scv_change_bg.setDescription(items[itemSelected]);
        scv_change_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " set change background ");
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("归属地提示框风格");
                final int itemSelected1 = sp.getInt("which", 0);

                builder.setSingleChoiceItems(items, itemSelected1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scv_change_bg.setDescription(items[which]);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("which", which);
                        editor.commit();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("cancel", null);
                builder.show();
            }
        });

        siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
        callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
        siv_callsms_safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_callsms_safe.isChecked()) {
                    //is checked
                    siv_callsms_safe.setChecked(false);
                    stopService(callSmsSafeIntent);
                } else {
                    //not checked
                    siv_callsms_safe.setChecked(true);
                    startService(callSmsSafeIntent);
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

        callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
        isServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.example.mamh.mobilesafe.service.CallSmsSafeService");
        siv_callsms_safe.setChecked(isServiceRunning);
    }
}
