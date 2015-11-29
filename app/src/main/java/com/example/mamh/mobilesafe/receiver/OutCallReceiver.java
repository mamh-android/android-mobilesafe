package com.example.mamh.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.mamh.mobilesafe.db.dao.NumberAddressQueryUtils;

/**
 * Created by mamh on 15-11-29.
 */
public class OutCallReceiver extends BroadcastReceiver {

    private static final String TAG = "OutCallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, " on recevie");
        String outPhone = getResultData();//拨出去的电话号码
        String address = NumberAddressQueryUtils.queryNumber(outPhone);
        Toast.makeText(context, "归属地： " + address, Toast.LENGTH_LONG).show();
    }
}
