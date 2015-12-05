package com.example.mamh.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.mamh.mobilesafe.R;

public class AtoolsActivity extends Activity {

    private static final String TAG = "AtoolsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        TextView query = (TextView) findViewById(R.id.tv_query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNumberAddressQueryActivity();
            }
        });
    }

    private void enterNumberAddressQueryActivity() {
        Intent intent = new Intent(this, NumberAddressQueryActivity.class);
        startActivity(intent);
        finish();
    }

}
