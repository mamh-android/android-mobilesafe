package com.example.mamh.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mamh.mobilesafe.R;
import com.example.mamh.mobilesafe.domain.AppInfo;
import com.example.mamh.mobilesafe.engine.AppInfoProvider;

import java.util.List;

public class AppManagerActivity extends Activity {

    private TextView mAvailRom;
    private TextView mAvailSD;

    private ListView lv_app_manger;
    private LinearLayout ll_loading;


    private List<AppInfo> appInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        mAvailRom = (TextView) findViewById(R.id.tv_avail_rom);
        mAvailSD = (TextView) findViewById(R.id.tv_avail_sd);

        long sdsize = getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
        long romsize = getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
        mAvailRom.setText("可用空间:" + Formatter.formatFileSize(this, romsize));
        mAvailSD.setText("sd卡可用空间:" + Formatter.formatFileSize(this, sdsize));

        lv_app_manger = (ListView) findViewById(R.id.lv_app_manger);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

        //这里，如果应用多了，这里会是一个耗时的操作，要写在子线程里面
        //appInfos = AppInfoProvider.getAppInfo(this);

        ll_loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfos = AppInfoProvider.getAppInfo(AppManagerActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv_app_manger.setAdapter(new AppManagerAdapter());
                        ll_loading.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }


    /**
     * 获取某个目录可用空间
     *
     * @param path
     * @return
     */
    private long getAvailSpace(String path) {
        StatFs statFs = new StatFs(path);
        statFs.getBlockCountLong();//获取分区的个数
        long size = statFs.getBlockSizeLong();//获取每个分区的大小
        long count = statFs.getAvailableBlocksLong();

        return size * count;
    }


    private class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(getApplicationContext());
            tv.setText(appInfos.get(position).toString());
            return tv;
        }
    }


}
