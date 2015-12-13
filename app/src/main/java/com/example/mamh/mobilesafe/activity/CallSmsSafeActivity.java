package com.example.mamh.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mamh.mobilesafe.R;
import com.example.mamh.mobilesafe.db.dao.BlackNumberDao;
import com.example.mamh.mobilesafe.domain.BlackNumberInfo;

import java.util.List;

public class CallSmsSafeActivity extends Activity {
    private static final String TAG = "CallSmsSafeActivity";
    private ListView lv_callsms_safe;
    private List<BlackNumberInfo> infos;
    private BlackNumberDao dao;
    private Button add;
    private Context mContext;

    private EditText et_blackNumber;
    private CheckBox cb_phone;
    private CheckBox cb_sms;
    private Button bt_ok;
    private Button bt_cancel;

    private CallSmsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_call_sms_safe);

        lv_callsms_safe = (ListView) findViewById(R.id.lv_call_sms_safe);
        dao = new BlackNumberDao(this);

        infos = dao.find();

        adapter = new CallSmsAdapter();
        lv_callsms_safe.setAdapter(adapter);

        add = (Button) findViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog dialog = builder.create();
                View contentView = View.inflate(mContext, R.layout.dialog_add_blacknumber, null);
                et_blackNumber = (EditText) contentView.findViewById(R.id.et_phone);
                cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
                cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
                bt_ok = (Button) contentView.findViewById(R.id.bt_ok);
                bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
                dialog.setView(contentView, 0, 0, 0, 0);
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String blackNumber = et_blackNumber.getText().toString();
                        if (TextUtils.isEmpty(blackNumber)) {
                            Toast.makeText(mContext, "不能为空", Toast.LENGTH_LONG).show();
                        }
                        String mode = "";
                        if (cb_phone.isChecked() && cb_sms.isChecked()) {
                            //all
                            mode = "3";
                        } else if (cb_phone.isChecked()) {
                            //
                            mode = "1";
                        } else if (cb_sms.isChecked()) {
                            mode = "2";
                        } else {
                            Toast.makeText(mContext, "请选择拦截模式", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dao.add(blackNumber, mode);
                        //更新listview的内容
                        //在list里添加到第一个位置上
                        infos.add(0, new BlackNumberInfo(blackNumber, mode));//新添加的显示在上面
                        adapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
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
