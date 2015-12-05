package com.example.mamh.mobilesafe.domain;

/**
 * 黑名单号码的业务bean
 * Created by mamh on 15-12-5.
 */
public class BlackNumberInfo {
    private String number;
    private String mode;


    public BlackNumberInfo(String number, String mode) {
        this.number = number;
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
