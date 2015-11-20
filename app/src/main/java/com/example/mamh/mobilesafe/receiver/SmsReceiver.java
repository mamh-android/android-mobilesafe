package com.example.mamh.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Objects;

/**
 * Created by mamh on 15-11-17.
 */
public class SmsReceiver extends BroadcastReceiver{
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objects = (Object[]) intent.getExtras().get("pdus");

        for (Object b:objects){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) b);
            String sender = smsMessage.getOriginatingAddress();
            String msg = smsMessage.getMessageBody();

            if("#*location*#".equals(msg)){
                Log.d(TAG, "gps.....");
                abortBroadcast();
            }else if("#*alarm*#".equals(msg)){
                Log.d(TAG, "alarm.....");
                abortBroadcast();
            }else if("#*wipedata*#".equals(msg)){
                Log.d(TAG, "wipedata.....");
                abortBroadcast();
            }else if("#*lockscreen*#".equals(msg)){
                Log.d(TAG, "lockscreen.....");
                abortBroadcast();
            }
        }

    }
}
