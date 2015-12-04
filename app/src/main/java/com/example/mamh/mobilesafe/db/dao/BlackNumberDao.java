package com.example.mamh.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mamh.mobilesafe.db.BlackNumberDBOPenHelper;

/**
 * Created by mamh on 15-12-2.
 */
public class BlackNumberDao {
    private BlackNumberDBOPenHelper helper;

    public BlackNumberDao(Context context) {
        this.helper = new BlackNumberDBOPenHelper(context);
    }


    public boolean find(String number) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from number where number = ?", new String[]{number});
        boolean result = false;
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        return result;
    }

    public void add(String number, String mode) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        db.insert("blacknumber", null, values);
        db.close();
    }

    public void update(String number, String newmode) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newmode);
        db.update("blacknumber", values, "number= ? ", new String[]{number});
        db.close();
    }

    public void delete(String number) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete("blacknumber", "number= ? ", new String[]{number});
        db.close();
    }
}
