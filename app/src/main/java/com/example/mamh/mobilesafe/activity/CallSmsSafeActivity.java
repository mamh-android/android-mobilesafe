package com.example.mamh.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mamh.mobilesafe.R;
import com.example.mamh.mobilesafe.db.dao.BlackNumberDao;
import com.example.mamh.mobilesafe.domain.BlackNumberInfo;

import java.util.List;

public class CallSmsSafeActivity extends Activity {
    private static final String TAG = "CallSmsSafeActivity";
    private ListView lv_callsms_safe;
    private List<BlackNumberInfo> infos;
    private BlackNumberDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);

        lv_callsms_safe = (ListView) findViewById(R.id.lv_call_sms_safe);
        dao = new BlackNumberDao(this);

        infos = dao.find();

        CallSmsAdapter adapter = new CallSmsAdapter();
        lv_callsms_safe.setAdapter(adapter);
    }

    private class CallSmsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return infos.size();
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
            ViewHolder holder;
            //这个回去读取流xml文件，然后去解析，最后才生成我们需要的view，这样很消耗资源的。怎么优化？？？
            View view;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.list_item, null);
                //减少findViewById操作。减少子view的查询次数。
                holder = new ViewHolder();
                holder.tv_black_number = (TextView) view.findViewById(R.id.tv_black_number);
                holder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);

                // 把子view的引用放到父view对象里
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }


            holder.tv_black_number.setText(infos.get(position).getNumber());
            String mode = infos.get(position).getMode();
            if ("1".equals(mode)) {
                holder.tv_mode.setText("电话拦截");
            } else if ("2".equals(mode)) {
                holder.tv_mode.setText("短息拦截");
            } else {
                holder.tv_mode.setText("其他拦截");
            }
            return view;
        }
    }

    //记录view的内存地址
    static private class ViewHolder {
        public TextView tv_black_number;
        public TextView tv_mode;
    }

}
