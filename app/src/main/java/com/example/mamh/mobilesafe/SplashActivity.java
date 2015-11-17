package com.example.mamh.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mamh.mobilesafe.utils.Utils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends Activity {
    private static final int ENTER_HOME = 0;
    private static final int SHOW_UPDATE_DIALOG = 1;
    private static final int URL_ERROR = 2;
    private static final int NETWORK_ERROR = 3;
    private static final int JSON_ERROR = 4;

    private String TAG = "SplashActivity";

    private TextView tv_splash_version;
    private TextView tv_download_percent;

    private String description;
    private String apkurl;

    private CheckUpdateHandler checkUpdateHandler = new CheckUpdateHandler();
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp=getSharedPreferences("config", MODE_PRIVATE);
        //设置版本名称
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version_id);
        tv_splash_version.setText("version: " + getVersionName());

        tv_download_percent= (TextView) findViewById(R.id.tv_splash_download_percent_id);

        boolean update = sp.getBoolean("update",false);
        //检查升级，连网一般在子线程里做
        if(update){
            checkUpdate();
        }else {
            checkUpdateHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHome();
                }
            }, 2000);
        }

        //做一个渐变的效果
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(500);//500毫秒
        RelativeLayout rl_layout= (RelativeLayout) findViewById(R.id.rl_root_spalash_id);
        rl_layout.startAnimation(animation);

    }

    /**
     * 检查是否需要升级，在子线程里做
     * 连网，需要权限
     */
    private void checkUpdate() {
        CheckUpdateThread checkUpdateThread = new CheckUpdateThread();
        checkUpdateThread.start();
    }


    /**
     * 得到版本名称
     *
     * @return
     */
    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 记得导入android.os.Handler包
     * 但是，有时会碰到在非主UI线程更新视图的需要。这个时候我们有两种处理的方式。
     * 一种是Handler一种是Activity中的 runOnUiThread（Runnable ）方法。
     * 对于第一中方法，是采用传递消息的方式，调用Handler中方法来处理消息更新视图。
     * 这种方式对于不是很频繁的调用是可取的。
     * 如果更新的较快，则消息处理会一直排队处理，这样显示会相对滞后。
     * 这个时候就可以考虑使用第二中方式，将需要执行的代码放到Runnable的run方法中，
     * 然后调用runOnUiThread（）这个方法将Runnable的对象传入即可。
     */
    class CheckUpdateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_UPDATE_DIALOG:
                    //显示升级对话框
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URL_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "URL  ERROR", Toast.LENGTH_LONG).show();
                    break;
                case NETWORK_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "NETWORK ERROR", Toast.LENGTH_SHORT).show();
                    break;
                case JSON_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "JSON  ERROR", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *  显示升级的对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setCancelable(false);//必须点对话框才可以 ,点返回，其他地方没反应,强制升级
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                enterHome();
                dialogInterface.dismiss();
            }
        });


        builder.setTitle(getResources().getString(R.string.update_alert_dialog_title));
        builder.setMessage(description);
        String button_text = getResources().getString(R.string.update_positive_button_text);
        builder.setPositiveButton(button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载apk，并替换
                String sdState = Environment.getExternalStorageState();
                if(sdState.equals(Environment.MEDIA_MOUNTED)){
                    //sd卡存在,面向组件编程
                    String savepath = Environment.getExternalStorageDirectory().getAbsoluteFile()+"/mobilesafe.apk";
                    FinalHttp finalHttp = new FinalHttp();
                    finalHttp.download(apkurl, savepath,new DownloadAjaxCallBack());
                }else{
                    Toast.makeText(getApplicationContext(),"no sdcard", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        button_text = getResources().getString(R.string.update_negative_button_text);
        builder.setNegativeButton(button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //对话框消失，进入主页面
                dialogInterface.dismiss();
                enterHome();
            }
        });
    }

    /**
     * 进入主页面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //关闭当前splash的页面
        finish();
    }

    class  DownloadAjaxCallBack extends AjaxCallBack<File>{
        @Override
        public void onLoading(long count, long current) {
            super.onLoading(count, current);
            String download_percent_text = getResources().getString(R.string.splash_download_percent_text);
            long percent = current * 100 / count;
            tv_download_percent.setVisibility(View.VISIBLE);
            tv_download_percent.setText(download_percent_text+ percent + "%");
        }

        @Override
        public void onSuccess(File file) {
            super.onSuccess(file);
            installApk(file);
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
            Toast.makeText(getApplicationContext(), "Download Fail", Toast.LENGTH_SHORT).show();
        }
        private void installApk(File file) {
            Intent intent=new Intent();
            intent.setAction("android.intent.action.VIEEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }


    class CheckUpdateThread extends Thread {
        public void run() {
            long startTime = System.currentTimeMillis();

            //封装一个messge
            //不要去new，Message msg = new Message();
            Message msg = Message.obtain();

            //URl http://192.168.0.110/test/updateinfo.html
            //把url作为一个value值
            try {

                //URL url = new URL("http://192.168.0.110/test/updateinfo.html");
                URL url = new URL(getString(R.string.serverurl));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");//注意是大写GET
                conn.setConnectTimeout(4000);
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream is = conn.getInputStream();
                    //把流转换为字符串
                    String result = Utils.readFromStream(is);
                    Log.d(TAG, "ok: " + result);
                    //json解析
                    JSONObject obj = null;
                    obj = new JSONObject(result);
                    String version = obj.getString("version");
                    description = obj.getString("description");
                    apkurl = obj.getString("apkurl");

                    if (getVersionName().equals(version)) {
                        msg.what = ENTER_HOME;
                    } else {
                        msg.what = SHOW_UPDATE_DIALOG;
                    }
                }
            } catch (MalformedURLException e) {
                msg.what = URL_ERROR;
                e.printStackTrace();
            } catch (IOException e) {
                msg.what = NETWORK_ERROR;
                e.printStackTrace();
            } catch (JSONException e) {
                msg.what = JSON_ERROR;

                e.printStackTrace();
            } finally {
                long endTime = System.currentTimeMillis();
                long deltaTime = endTime-startTime;
                if (deltaTime < 2000){
                    try {
                        Thread.sleep(2000-deltaTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                checkUpdateHandler.sendMessage(msg);
            }
        }
    }
}
