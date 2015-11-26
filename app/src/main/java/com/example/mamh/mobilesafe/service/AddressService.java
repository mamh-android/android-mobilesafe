package com.example.mamh.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mamh.mobilesafe.db.dao.NumberAddressQueryUtils;

/**
 * Created by mamh on 15-11-25.
 */
public class AddressService extends Service {

    private WindowManager wm;
    private View view;

    private TelephonyManager tm;
    private MyListenerPhone listenerPhone;
    private OutCallReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 服务里面的内部类
    //广播接收者的生命周期和服务一样
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 这就是我们拿到的播出去的电话号码
            String phone = getResultData();
            // 查询数据库
            String address = NumberAddressQueryUtils.queryNumber(phone);
            Toast.makeText(context, address, 1).show();
        }
    }


    private class MyListenerPhone extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // state：状态，incomingNumber：来电号码
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
                    // 查询数据库的操作
                    String address = NumberAddressQueryUtils.queryNumber(incomingNumber);

                    Toast.makeText(getApplicationContext(), address, 1).show();
                    //myToast(address);

                    break;

                case TelephonyManager.CALL_STATE_IDLE://电话的空闲状态：挂电话、来电拒绝
                    //把这个View移除
                    if (view != null) {
                        wm.removeView(view);
                    }
                    break;
                default:
                    break;
            }
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // 监听来电
        listenerPhone = new MyListenerPhone();
        tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

        //用代码去注册广播接收者
        receiver = new OutCallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receiver, filter);

        //实例化窗体
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消监听来电
        tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
        listenerPhone = null;

        //用代码取消注册广播接收者
        unregisterReceiver(receiver);
        receiver = null;

    }


}
