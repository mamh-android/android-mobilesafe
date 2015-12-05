package com.example.mamh.mobilesafe.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mamh on 15-12-2.
 */
public class BlackNumberDBOPenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "blacknumber.db";

    public BlackNumberDBOPenHelper(Context context ) {
        super(context, DB_NAME, null, 1);
    }

    public BlackNumberDBOPenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table blacknumber(" +
                "_id integer primary key autoincrement," +
                "number varchar(20)," +
                "mode varchar(2)" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
