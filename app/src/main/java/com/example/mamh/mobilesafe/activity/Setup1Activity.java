package com.example.mamh.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mamh.mobilesafe.R;


public class Setup1Activity extends BaseSetupActivity {
    private Button previous;
    private Button next;


    private String TAG = "Setup1Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);


        previous = (Button) findViewById(R.id.bt_prestep1);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousActivity();
            }
        });
        next = (Button) findViewById(R.id.bt_nextstep1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextActivity();
            }
        });
    }

    public void showNextActivity() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
    }

    @Override
    public void showPreviousActivity() {
        Intent intent = new Intent(Setup1Activity.this, LostFindActivity.class);
        startActivity(intent);
        finish();
    }


}
