package com.example.mamh.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.mamh.mobilesafe.R;
import com.example.mamh.mobilesafe.service.GPSService;

/**
 * Created by mamh on 15-11-17.
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);


        for (Object b : objects) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) b);
            String sender = smsMessage.getOriginatingAddress();
            String safeNumber = sp.getString("safenumber", "");
            if (sender.contains(safeNumber)) {
                String msg = smsMessage.getMessageBody();
                if ("#*location*#".equals(msg)) {
                    Intent i = new Intent(context, GPSService.class);
                    context.startService(i);
                    String lastlocation = sp.getString("lastlocation", "");
                    if (TextUtils.isEmpty(lastlocation)) {
                        //为空,可能正在获取
                        SmsManager.getDefault().sendTextMessage(sender, null, "geting location", null, null);
                    } else {
                        SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
                    }
                    Log.d(TAG, "gps.....");
                    abortBroadcast();
                } else if ("#*alarm*#".equals(msg)) {
                    Log.d(TAG, "alarm.....");
                    playerMedia(context);
                    abortBroadcast();
                } else if ("#*wipedata*#".equals(msg)) {
                    Log.d(TAG, "wipedata.....");
                    abortBroadcast();
                } else if ("#*lockscreen*#".equals(msg)) {
                    Log.d(TAG, "lockscreen.....");
                    abortBroadcast();
                }
            }
        }

    }


    private void playerMedia(Context context) {
        MediaPlayer mplayer = MediaPlayer.create(context, R.raw.ylzs);
        mplayer.setLooping(false);
        mplayer.setVolume(1.0f, 1.0f);
        mplayer.start();
    }

}
