package com.example.mamh.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.mamh.mobilesafe.db.dao.NumberAddressQueryUtils;

/**
 * Created by mamh on 15-11-25.
 */
public class AddressService extends Service {
    private static final String TAG = "AddressService";
    //读取手机状态，需要权限
    //<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    private TelephonyManager tm;
    private MyPhoneStateListener myPhoneStateListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化电话服务
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d(TAG, " on create()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消监听来电
        tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        myPhoneStateListener = null;
        Log.d(TAG, " on destroy()");
    }


    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //第一个参数是状态， 第二个参数是打入的电话
            super.onCallStateChanged(state, incomingNumber);
            Log.d(TAG, " on call state changed");

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = NumberAddressQueryUtils.queryNumber(incomingNumber);
                    Toast.makeText(AddressService.this, "归属地： " + address, Toast.LENGTH_LONG);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                default:
                    break;
            }


        }
    }
}
