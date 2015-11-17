package com.example.mamh.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mamh on 15-11-4.
 */
public class MD5Utils {
    public static String md5Password(String password){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            for (byte b :result){
                int number = b & 0xff;
                String string = Integer.toHexString(number);
                if(string.length() == 1){
                    stringBuilder.append("0");
                }
                stringBuilder.append(string);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        return stringBuilder.toString();
    }
}
