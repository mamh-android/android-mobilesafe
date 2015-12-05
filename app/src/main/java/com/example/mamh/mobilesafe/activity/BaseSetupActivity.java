package com.example.mamh.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by mamh on 15-11-15.
 */
public abstract class BaseSetupActivity extends Activity {

    private String TAG = "BaseSetupActivity";

    private GestureDetector detector;
    protected SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new GestureDetector(this, new MySimpleOnGestureListener());
        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //使用手势识别器
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    public abstract void showNextActivity();

    public abstract void showPreviousActivity();


    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //屏蔽斜滑动
            if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
                Log.d(TAG, "斜着滑动了");
                return true;
            }

            if (Math.abs(velocityX) < 200) {
                Log.d(TAG, "滑动他太慢");
                return true;
            }

            if ((e2.getRawX() - e1.getRawX()) > 200) {
                //显示上一个页面，从左往右
                showPreviousActivity();
                return true;
                //返回true不做其他事情了
            }
            if ((e1.getRawX() - e2.getRawX()) > 200) {
                //显示下一个页面，从右往左
                showNextActivity();
                return true;
            }
            Log.d(TAG, " == " + Math.abs(e1.getRawX() - e2.getRawX()));
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

}
