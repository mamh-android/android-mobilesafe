package com.example.mamh.mobilesafe.engine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.mamh.mobilesafe.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务方法，提供手机里面的所有的应用
 * Created by mamh on 16-2-18.
 */
public class AppInfoProvider {

    public static List<AppInfo> getAppInfo(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();


        PackageManager pm = context.getPackageManager();

        //0表示不关心这个标识， Additional option flags. Use any combination of
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);

        //packageInfo相当于应用程序清单文件，manifest节点里面的内容。
        for (PackageInfo packageInfo : packInfos) {
            String packName = packageInfo.packageName;
            Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
            String name = packageInfo.applicationInfo.loadLabel(pm).toString();
            AppInfo appInfo = new AppInfo(icon, name, packName);
            appInfos.add(appInfo);
        }

        return appInfos;
    }

}
