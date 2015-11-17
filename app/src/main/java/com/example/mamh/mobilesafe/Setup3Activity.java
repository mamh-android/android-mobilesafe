package com.example.mamh.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Setup3Activity extends BaseSetupActivity {
    private Button next;
    private Button previous;
    private Button selectContact;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);


        next = (Button) findViewById(R.id.bt_nextstep3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextActivity();
            }
        });

        previous = (Button) findViewById(R.id.bt_prestep3);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousActivity();
            }
        });

        selectContact = (Button) findViewById(R.id.bt_select_contact);
        selectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setup3Activity.this, SelectContactActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        phone = (EditText) findViewById(R.id.et_setup3_phone);
        String savePhone = sp.getString("safenumber", "");
        phone.setText(savePhone);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String phonestr = data.getStringExtra("phone").replace("-", "").replace(" ", "");
        phone.setText(phonestr);

    }

    public void showPreviousActivity() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_pre_in, R.anim.translate_pre_out);
    }

    public void showNextActivity() {
        //应该保存一下安全号码
        String phonestr = phone.getText().toString();
        if(TextUtils.isEmpty(phonestr)){
            Toast.makeText(this, "安全号码没有设置", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safenumber", phonestr);
        editor.commit();

        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
    }
}
