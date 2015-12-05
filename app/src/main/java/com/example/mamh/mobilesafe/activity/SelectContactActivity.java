package com.example.mamh.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mamh.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectContactActivity extends Activity {
    private static final String TAG = "SelectContactActivity";
    private ListView contactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        contactListView = (ListView) findViewById(R.id.list_select_contact);
        final List<Map<String, String>> data = getContactInfo();

        String[] strings = {"name", "phone"};
        int[] ints = {R.id.name, R.id.phone};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.contact_item, strings, ints);
        contactListView.setAdapter(simpleAdapter);
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = data.get(position).get("phone");
                Intent data = new Intent();
                data.putExtra("phone", phone);
                setResult(0, data);
                finish();

            }
        });
    }

    public List<Map<String, String>> getContactInfo() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //内容解析器
        ContentResolver contentResolver = getContentResolver();


        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = contentResolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(0);
            if (contactId != null) {
                Map<String, String> map = new HashMap<String, String>();
                Cursor dataCursor = contentResolver.query(
                        datauri,
                        new String[]{"data1", "mimetype"},
                        "contact_id=?",
                        new String[]{contactId},
                        null
                );
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    Log.d(TAG, data1 + "  " + mimetype);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        map.put("name", data1);
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        map.put("phone", data1);
                    }
                }
                dataCursor.close();
                list.add(map);
            }
        }
        cursor.close();


        return list;
    }
}
