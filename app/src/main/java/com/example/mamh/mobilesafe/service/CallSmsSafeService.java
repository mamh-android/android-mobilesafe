package com.example.mamh.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.mamh.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {

    private static final String TAG = "CallSmsSafeService";


    private InnerSmsReceiver innerSmsReceiver;
    private BlackNumberDao dao;


    public CallSmsSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(innerSmsReceiver);
        innerSmsReceiver = null;
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        innerSmsReceiver = new InnerSmsReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);//设置优先级
        registerReceiver(innerSmsReceiver, filter);

        dao = new BlackNumberDao(this);

    }


    private class InnerSmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, " =========  内部广播接收这 ，短息到来了");

            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object o : objs) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);

                String sender = sms.getOriginatingAddress();
                String result = dao.findMode(sender);
                Log.e(TAG, "这里代表 ================" + sender);
                Log.e(TAG, "这里代表 ================" + result);

                if ("2".equals(result) || "3".equals(result)) {
                    //这里代表 短息需要被拦截
                    Log.e(TAG, "这里代表 短息需要被拦截");

                    abortBroadcast();
                }
            }
        }
    }


}
