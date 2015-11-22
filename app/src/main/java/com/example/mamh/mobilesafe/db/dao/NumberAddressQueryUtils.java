package com.example.mamh.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * -
 * Created by mamh on 15-11-22.
 */
public class NumberAddressQueryUtils {

    private static final String TAG = "NumberAddressQueryUtils";

    /**
     * 传一个电话号码进去输出归属地
     *
     * @param number
     * @return
     */
    public static String queryNumber(String number) {
        String path = "/data/data/com.example.mamh.mobilesafe/address.db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        String sql = "";
        String[] selectionArgs = null;
        String address = "";

        if (number.matches("^1[345678]\\d{9}$")) {
            sql = "select location from data2 where id = (select outkey from data1 where id=?)";
            selectionArgs = new String[]{
                    number.substring(0, 7)
            };
            Cursor cursor = database.rawQuery(sql, selectionArgs);
            while ((cursor.moveToNext())) {
                String location = cursor.getString(0);
                address = location;
            }
            cursor.close();
        } else {
            switch (number.length()) {
                case 3:
                    address = "匪警号码";
                    break;
                case 4:
                    address = "模拟器";
                    break;
                case 5:
                    address = "客服号码";
                    break;
                case 6:
                    address = "六位号码";
                    break;
                case 7:
                case 8:
                    address = "local number";
                    break;
                case 9:
                    break;
                case 10:
                    break;
                default:
                    if (number.length() > 10 && number.startsWith("0")) {
                        sql = "select location from data2 where area  = ?";
                        selectionArgs = new String[]{
                                number.substring(1, 3)
                        };
                    }
                    if (number.length() > 11 && number.startsWith("0")) {
                        sql = "select location from data2 where area  = ?";
                        selectionArgs = new String[]{
                                number.substring(1, 4)
                        };
                    }

                    Cursor cursor = database.rawQuery(sql, selectionArgs);
                    Log.d(TAG, "cursor = " + cursor);
                    while ((cursor.moveToNext())) {
                        String location = cursor.getString(0);
                        address = location.substring(0, location.length() - 2);
                    }
                    cursor.close();

                    break;
            }
        }


        return address;
    }
}
