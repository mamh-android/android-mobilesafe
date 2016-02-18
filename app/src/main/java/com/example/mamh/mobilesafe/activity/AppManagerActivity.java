package com.example.mamh.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.widget.TextView;

import com.example.mamh.mobilesafe.R;

public class AppManagerActivity extends Activity {

    private TextView mAvailRom;
    private TextView mAvailSD;


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

}
