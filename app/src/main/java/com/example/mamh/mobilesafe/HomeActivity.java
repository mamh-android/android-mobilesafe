package com.example.mamh.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mamh.mobilesafe.utils.MD5Utils;

/**
 * Created by mamh on 15-10-31.
 */
public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";
    private GridView list_home;

    private HomeListAdapter homeListAdapter;

    private EditText et_setup_pwd;
    private EditText et_setup_confirm;
    private Button bt_ok;
    private Button bt_cancel;
    private AlertDialog dialog;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        list_home = (GridView) findViewById(R.id.list_home_id);
        TypedArray ar = this.getResources().obtainTypedArray(R.array.images_ids);
        String[] names = this.getResources().getStringArray(R.array.names);

        int len = ar.length();
        int[] ids = new int[len];
        for (int i = 0; i < len; i++)
            ids[i] = ar.getResourceId(i, 0);
        ar.recycle();
        homeListAdapter = new HomeListAdapter(names, ids);
        list_home.setAdapter(homeListAdapter);
        list_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //进入手机防盗
                        showLostFindDialog();
                        break;
                    case 7:
                        enterAtoolsActivity();
                        break;
                    case 8:
                        enterSettingActivity();
                        break;
                }
            }
        });
    }

    private void enterAtoolsActivity() {
        Intent intent = new Intent(HomeActivity.this, AtoolsActivity.class);
        startActivity(intent);
    }

    private void enterSettingActivity() {
        Log.d(TAG, "进入手机设置页面");
        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void enterLostFindActivity() {
        Log.d(TAG, "进入手机防盗页面");
        Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
        startActivity(intent);
    }

    private void showLostFindDialog() {
        //判断是否设置过密码
        if (isSetupPassword()) {
            //已经设置密码了,弹出输入密码对话框
            showEnterDialog();
        } else {
            //没有设置密码
            showSetupPwdDialog();
        }
    }


    private void showSetupPwdDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        //自定义一个布局文件
        View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
        et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
        et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "cancel button onclick");
                dialog.dismiss();
            }
        });
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "ok button onclick");
                String password = et_setup_pwd.getText().toString().trim();
                String password_confirm = et_setup_confirm.getText().toString().trim();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)) {
                    Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(password_confirm)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", MD5Utils.md5Password(password));
                    editor.commit();
                    dialog.dismiss();
                    enterLostFindActivity();
                } else {
                    Toast.makeText(HomeActivity.this, "两次密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showEnterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        //自定义一个布局文件
        View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
        et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "cancel button onclick");
                dialog.dismiss();
            }
        });
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "ok button onclick");
                String password = et_setup_pwd.getText().toString().trim();
                String saved_password = sp.getString("password", null);
                password = MD5Utils.md5Password(password);
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(saved_password)) {
                    dialog.dismiss();
                    enterLostFindActivity();
                } else {
                    Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "saved password: " + saved_password);
                    et_setup_pwd.setText("");
                    return;
                }
            }
        });
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private boolean isSetupPassword() {
        String password = sp.getString("password", null);
        if (password != null && !TextUtils.isEmpty(password)) {
            return true;
        }
        return false;
    }


    private class HomeListAdapter extends BaseAdapter {
        private String[] names;
        private int[] ids;

        public HomeListAdapter(String[] names, int[] ids) {
            this.names = names;
            this.ids = ids;
        }


        @Override
        public int getCount() {
            return this.names.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_item);
            TextView tv = (TextView) view.findViewById(R.id.tv_item);
            tv.setText(names[position]);
            iv.setImageResource(ids[position]);
            return view;
        }
    }
}
