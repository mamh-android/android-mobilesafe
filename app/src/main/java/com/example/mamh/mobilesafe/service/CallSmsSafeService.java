package com.example.mamh.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.mamh.mobilesafe.db.dao.BlackNumberDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallSmsSafeService extends Service {

    private static final String TAG = "CallSmsSafeService";


    private InnerSmsReceiver innerSmsReceiver;
    private BlackNumberDao dao;

    private TelephonyManager telephonyManager;
    private CallPhoneStateListener callPhoneStateListener;

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

        telephonyManager.listen(callPhoneStateListener, PhoneStateListener.LISTEN_NONE);//不监听
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

        callPhoneStateListener = new CallPhoneStateListener();

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(callPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    private class CallPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String result = dao.findMode(incomingNumber);
                    Log.e(TAG, "电话拦截，这里要挂断电话 ==  " + result + "===" + incomingNumber);
                    if ("1".equals(result) || "3".equals(result)) {
                        //电话拦截，这里要挂断电话
                        Log.e(TAG, "电话拦截，这里要挂断电话 ");


                        //挂断电话
                        endCall();
                    }
                    break;
            }


            //super.onCallStateChanged(state, incomingNumber);
            //当呼叫状态发生变化时候调用
        }

    }

    private void endCall() {

        // serviceManager 类是隐藏，这里不能直接获得，需要用到反射来获取
        // IBinder iBinder = ServiceManager.getService();

        try {
            Class clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

            ITelephony.Stub.asInterface(iBinder).endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
