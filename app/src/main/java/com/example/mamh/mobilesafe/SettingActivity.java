package com.example.mamh.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.mamh.mobilesafe.ui.SettingItemView;


public class SettingActivity extends Activity {
    private SettingItemView siv_update;
    private SharedPreferences sp;

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




    }


}
