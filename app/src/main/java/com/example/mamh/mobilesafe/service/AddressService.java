package com.example.mamh.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mamh.mobilesafe.R;
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
    private OutCallReceiver outCallReceiver;

    //窗体管理者
    private WindowManager windowManager;

    private TextView myToastTextView;

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

        outCallReceiver = new OutCallReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        //用代码注册广播接收者,有注册就有取消。。。。
        registerReceiver(outCallReceiver, intentFilter);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消监听来电
        tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        myPhoneStateListener = null;
        Log.d(TAG, " on destroy()");

        //取消广播监听者
        unregisterReceiver(outCallReceiver);
        outCallReceiver = null;

    }

    private void myToast(String address) {
        myToastTextView = new TextView(this);
        myToastTextView.setText(address);

        myToastTextView.setTextSize(22);
        myToastTextView.setTextColor(Color.RED);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        windowManager.addView(myToastTextView, params);//利用wm来添加view
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
                    Toast.makeText(AddressService.this, "归属地： " + address, Toast.LENGTH_LONG).show();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //电话空闲的时候把view移除
                    //这就是挂电话。来电拒绝。都会回调这个方法。
                    if (myToastTextView != null) {
                        if (myToastTextView.getParent() != null) {
                            windowManager.removeView(myToastTextView);
                        }
                    }
                    break;
                default:
                    break;
            }


        }
    }

    private class OutCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, " on recevie");
            String outPhone = getResultData();//拨出去的电话号码
            String address = NumberAddressQueryUtils.queryNumber(outPhone);
            //Toast.makeText(context, "归属地： " + address, Toast.LENGTH_LONG).show();
            myToast(address);
        }
    }

}
