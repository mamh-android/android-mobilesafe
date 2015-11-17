package com.example.mamh.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


public class Setup4Activity extends BaseSetupActivity{
    private Button next;
    private Button previous;
    private CheckBox safeEnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        next = (Button) findViewById(R.id.bt_nextstep4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("configed", true);
                editor.commit();
                showNextActivity();
            }
        });
        previous = (Button) findViewById(R.id.bt_prestep4);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousActivity();
            }
        });

        safeEnable = (CheckBox) findViewById(R.id.cb_setup4_safe);
        boolean safeIsEnable = sp.getBoolean("safeenable", false);
        safeEnable.setChecked(safeIsEnable);

    }

    @Override
    public void showNextActivity() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("safeenable", safeEnable.isChecked());
        editor.commit();

        Intent intent = new Intent(Setup4Activity.this, LostFindActivity.class);
        startActivity(intent);
        finish();
    }

    public void showPreviousActivity() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_pre_in, R.anim.translate_pre_out);
    }


}
