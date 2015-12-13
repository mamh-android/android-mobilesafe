package com.example.mamh.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mamh.mobilesafe.db.BlackNumberDBOPenHelper;
import com.example.mamh.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

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

    public List<BlackNumberInfo> find(){
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc", null);
        while (cursor.moveToNext()){
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            BlackNumberInfo info =new BlackNumberInfo(number,mode );
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }
}
