package com.example.mamh.mobilesafe;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mamh.mobilesafe.db.dao.NumberAddressQueryUtils;

import java.util.ArrayList;
import java.util.List;

public class NumberAddressQueryActivity extends Activity {
    private static final String TAG = "NumberAddressQueryActivity";
    private EditText phone;
    private Button query;
    private TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address_query);

        phone = (EditText) findViewById(R.id.et_phone);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    String address = NumberAddressQueryUtils.queryNumber(s.toString());
                    result.setText(address);
                }
            }
        });

        query = (Button) findViewById(R.id.bt_query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(NumberAddressQueryActivity.this, "号码为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String address = NumberAddressQueryUtils.queryNumber(phoneNumber);
                    result.setText(address);
                }


            }
        });
        result = (TextView) findViewById(R.id.tv_result);

    }

}
