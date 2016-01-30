package com.example.mamh.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mamh.mobilesafe.R;
import com.example.mamh.mobilesafe.utils.SmsUtils;

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

        //短息的备份
        TextView smsBackup = (TextView) findViewById(R.id.tv_sms_backup);
        smsBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsBackup();
            }
        });

        //短息的还原
        TextView smsRestore = (TextView) findViewById(R.id.tv_sms_restore);
        smsRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, " smsRestore ");
                smsRestore();
            }
        });
    }

    //短息的还原
    private void smsRestore() {
        Log.e(TAG, " smsRestore ");
        SmsUtils.restoreSms(this);
    }

    private void smsBackup() {
        try {
            SmsUtils.backupSms(this, new SmsUtils.BackupSmsCallBack() {
                @Override
                public void beforeBackup(int max) {
                    //添加了回调，使用接口
                    //在备份短息之前执行的一个方法
                }

                @Override
                public void onBackup(int process) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enterNumberAddressQueryActivity() {
        Intent intent = new Intent(this, NumberAddressQueryActivity.class);
        startActivity(intent);
        finish();
    }

}
