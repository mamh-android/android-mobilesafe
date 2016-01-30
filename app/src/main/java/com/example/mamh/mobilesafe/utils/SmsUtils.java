package com.example.mamh.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;

/**
 * 短息相关的工具类
 * Created by mamh on 16-1-30.
 */
public class SmsUtils {

    public interface BackupSmsCallBack {
        /**
         * 开始备份的 时候 设置的最大值
         *
         * @param max
         */
        void beforeBackup(int max);

        void onBackup(int process);
    }


    /**
     * 备份短息
     *
     * @param context
     * @param callBack
     */
    public static void backupSms(Context context, BackupSmsCallBack callBack) throws Exception {
        ContentResolver resolver = context.getContentResolver();


        File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        //把用户的短息 一条一条的读取 ，然后按照一定的格式写到文件里面

        XmlSerializer serializer = Xml.newSerializer();
        //初始化生成器
        serializer.setOutput(fileOutputStream, "utf-8");
        serializer.startDocument("utf-8", true);//生成一个开头
        serializer.startTag(null, "smss");
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[]{"body", "address", "type", "date"}, null, null, null);

        callBack.beforeBackup(cursor.getCount());

        //添加一个属性 最大短信条数 max
        serializer.attribute(null, "max", cursor.getCount() + "");

        int process = 0;
        while (cursor.moveToNext()) {
            serializer.startTag(null, "sms");


            serializer.startTag(null, "body");
            String body = cursor.getString(0);
            serializer.text(body);
            serializer.endTag(null, "body");

            serializer.startTag(null, "address");
            String address = cursor.getString(1);
            serializer.text(address);
            serializer.endTag(null, "address");

            serializer.startTag(null, "type");
            String type = cursor.getString(2);
            serializer.text(type);
            serializer.endTag(null, "type");

            serializer.startTag(null, "date");
            String date = cursor.getString(3);
            serializer.text(date);
            serializer.endTag(null, "date");

            serializer.endTag(null, "sms");

            process++;
            callBack.onBackup(process);
        }
        cursor.close();
        serializer.endTag(null, "smss");
        serializer.endDocument();
        fileOutputStream.close();
    }

    /**
     * @param context
     */
    public static void restoreSms(Context context) {
        Log.e("xxxxx", " smsRestore ");
        //读取xml文件
        Xml.newPullParser();

        //把读取的短息 body， date，type，address

        //把短息插入到系统短信应用中
        Uri uri = Uri.parse("content://sms/");
        ContentValues values = new ContentValues();
        values.put("body", "bodyxxxxxxxxxxxxxxxxxxxxxxxxx");
        values.put("date", "1454138684879");
        values.put("type", "1");
        values.put("address", "1231");
        context.getContentResolver().insert(uri, values);

    }
}
