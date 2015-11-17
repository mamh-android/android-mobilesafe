package com.example.mamh.mobilesafe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class LostFindActivity extends Activity {

    private static final String TAG = "LostFindActivity";

    private SharedPreferences sp;

    private TextView reEnterSetup;

    private ImageView lockImageView;
    private TextView lockTextView;
    private TextView safeNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断一下是否设置过
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = sp.getBoolean("configed", false);
        if (!configed) {
            //跳转
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }

        //就在手机防盗的页面
        setContentView(R.layout.activity_lost_find);
        reEnterSetup = (TextView) findViewById(R.id.tv_reenter_setup);
        reEnterSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        safeNumber = (TextView) findViewById(R.id.tv_safe_number);
        String safeNum = sp.getString("safenumber", "");
        if (!TextUtils.isEmpty(safeNum)) {
            safeNumber.setText(safeNumber.getText() + safeNum);
        }


        lockImageView = (ImageView) findViewById(R.id.iv_lock);
        lockTextView = (TextView) findViewById(R.id.tv_lock);

        Boolean safeIsEnable = sp.getBoolean("safeenable", false);
        if (safeIsEnable) {
            lockTextView.setText("手机防盗是否开启：" + "开启");
            lockImageView.setImageResource((R.drawable.lock));
        } else {
            lockTextView.setText("手机防盗是否开启：" + "关闭");
            lockImageView.setImageResource((R.drawable.unlock));
        }
    }


}
