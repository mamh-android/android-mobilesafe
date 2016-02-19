package com.example.mamh.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 应用程序信息的业务bean
 * Created by mamh on 16-2-18.
 */
public class AppInfo {

    private Drawable icon;
    private String name;

    /**
     * 应用程序的包名
     */
    private String packName;

    /**
     * 应用程序 安装的位置
     */
    private String installedAddress;

    /**
     * 是否是用户应用还是系统应用
     */
    private boolean isUserApp;

    public AppInfo() {
    }

    public AppInfo(Drawable icon, String name, String packName) {
        this.icon = icon;
        this.name = name;
        this.packName = packName;
    }

    public AppInfo(Drawable icon, String name, String packName, String installedAddress, boolean isUserApp) {
        this.icon = icon;
        this.name = name;
        this.packName = packName;
        this.installedAddress = installedAddress;
        this.isUserApp = isUserApp;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getInstalledAddress() {
        return installedAddress;
    }

    public void setInstalledAddress(String installedAddress) {
        this.installedAddress = installedAddress;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setIsUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", name='" + name + '\'' +
                ", packName='" + packName + '\'' +
                ", installedAddress='" + installedAddress + '\'' +
                ", isUserApp=" + isUserApp +
                '}';
    }
}
