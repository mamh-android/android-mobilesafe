package com.example.mamh.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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

    private View myToastView;
    private WindowManager.LayoutParams params;
    private SharedPreferences sp;
    long[] mHits = new long[2];

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
        if (myToastView != null) {
            if (myToastView.getParent() != null) {
                windowManager.removeView(myToastView);
            }
        }
    }

    private void myToast(String address) {
        //"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"
        int[] ids = {
                R.drawable.call_locate_white, R.drawable.call_locate_orange,
                R.drawable.call_locate_blue, R.drawable.call_locate_gray,
                R.drawable.call_locate_green};
        myToastView = View.inflate(this, R.layout.address_show, null);
        final TextView myToastTextView = (TextView) myToastView.findViewById(R.id.tv_address);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        int which = sp.getInt("which", 0);
        myToastTextView.setBackgroundResource(ids[which]);
        myToastTextView.setText(address);

        myToastTextView.setTextSize(22);
        myToastTextView.setTextColor(Color.RED);

        params = new WindowManager.LayoutParams();

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.x = sp.getInt("lastx", 0);
        params.y = sp.getInt("lasty", 0);
        //params.type = WindowManager.LayoutParams.TYPE_TOAST;//土司类型，不能响应触摸事件
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;//添加权限

        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        myToastView.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch ((event.getAction())) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX();
                        float newY = event.getRawY();
                        float dx = newX - startX;
                        float dy = newY - startY;
                        //如何更新view在屏幕上的位置
                        params.x += dx;
                        params.y += dy;
                        //考虑边界问题
                        if (params.x < 0) params.x = 0;
                        if (params.y < 0) params.y = 0;
                        if (params.x > windowManager.getDefaultDisplay().getWidth() - myToastView.getWidth()) {
                            params.x = windowManager.getDefaultDisplay().getWidth() - myToastView.getWidth();
                        }
                        if (params.y > windowManager.getDefaultDisplay().getHeight() - myToastView.getHeight()) {
                            params.y = windowManager.getDefaultDisplay().getHeight() - myToastView.getHeight();
                        }
                        windowManager.updateViewLayout(myToastView, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lastx", params.x);
                        editor.putInt("lastx", params.x);
                        editor.commit();
                        break;
                }
                return false;// 表示事件没有处理晚
                //return true;//这里返回true，click事件就不会响应了

            }
        });

        myToastView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    Log.e(TAG, " double click");
                    if (myToastView != null) {
                        if (myToastView.getParent() != null) {
                            windowManager.removeView(myToastView);
                        }
                    }
                }

            }
        });


        windowManager.addView(myToastView, params);//利用wm来添加view
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
                    myToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //电话空闲的时候把view移除
                    //这就是挂电话。来电拒绝。都会回调这个方法。
                    if (myToastView != null) {
                        if (myToastView.getParent() != null) {
                            windowManager.removeView(myToastView);
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
