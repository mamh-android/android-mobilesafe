package com.example.mamh.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mamh on 15-11-15.
 */
public class BootCompleteReceiver extends BroadcastReceiver{
    private static final String TAG  = "BootCompleteReceiver";

    private TelephonyManager telephonyManager;

    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "on receive");
        //读取之前保存的sim卡信息
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);

        String saveSim = sp.getString("sim", "")+"afu";
        String realSim = telephonyManager.getSimSerialNumber();

        //比较是否一样
        if(saveSim.equals(realSim)){

        }else{
            //sim变更了
            Log.d(TAG, "sim卡变更了");
            Toast.makeText(context, "sim卡变更了",Toast.LENGTH_LONG).show();
        }

        //读取当前sim卡信息

    }
}
