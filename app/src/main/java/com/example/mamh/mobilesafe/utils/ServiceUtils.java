package com.example.mamh.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by mamh on 15-11-26.
 */
public class ServiceUtils {


    //检验服务是否还在运行
    public static boolean isServiceRunning(Context context, String serviceName) {
        //得到服务,管理activity和service
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(1000);//最多接收1000个服务。超出的得不到
        for (ActivityManager.RunningServiceInfo info : infos){
            String className = info.service.getClassName();
            if(className.equals(serviceName)){
                return true;
            }
        }
        return false;
    }
}
