package com.example.mamh.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mamh.mobilesafe.ui.SettingItemView;

import org.w3c.dom.Text;

import static android.widget.Toast.LENGTH_LONG;


public class Setup2Activity extends BaseSetupActivity{
    private static final String TAG = "Setup2Activity";
    private Button next;

    private Button previous;
    private SettingItemView bindSim;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        
        
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        
        next = (Button) findViewById(R.id.bt_nextstep2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showNextActivity();
            }
        });
        previous = (Button) findViewById(R.id.bt_prestep2);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousActivity();
            }
        });

        String sim = sp.getString("sim", null);
        bindSim = (SettingItemView) findViewById(R.id.siv_bind_sim);
        if(TextUtils.isEmpty(sim)){
            //没有绑定
            bindSim.setChecked(false);
        }else{
            //已经绑定
            bindSim.setChecked(true);
        }
        bindSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                //保存sim序列号
                String simSerialNumber = telephonyManager.getSimSerialNumber()+"aaa";
                Log.d(TAG, "sim: " +simSerialNumber);
                if(bindSim.isChecked()){
                    bindSim.setChecked(false);
                    editor.putString("sim", null);
                }else{
                    bindSim.setChecked(true);
                    editor.putString("sim", simSerialNumber);
                }
                editor.commit();
            }
        });
    }

    public void showPreviousActivity() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_pre_in, R.anim.translate_pre_out);
    }

    public void showNextActivity() {
        String sim = sp.getString("sim", "");
        Log.d(TAG, "sim " + sim);
        if(TextUtils.isEmpty(sim)){
            Toast.makeText(Setup2Activity.this, "sim卡没有绑定，不能下一步了", Toast.LENGTH_LONG).show();
            return;
        }


        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
    }


}
