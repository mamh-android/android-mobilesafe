package com.example.mamh.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mamh.mobilesafe.R;

/**
 * Created by mamh on 15-11-1.
 * 自定义的组合控件
 */
public class SettingClikcView extends RelativeLayout {
    private ImageView imageView;
    private TextView tv_desc;
    private TextView tv_title;


    public SettingClikcView(Context context) {
        super(context);
        initView(context);
    }

    public SettingClikcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        String namespace = "http://schemas.android.com/apk/res-auto";
        String title = attrs.getAttributeValue(namespace, "atitle");
        tv_title.setText(title);
    }

    public SettingClikcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.setting_click_item, this);
        imageView = (ImageView) this.findViewById(R.id.cb_status);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
    }


    public void setDescription(String text) {
        tv_desc.setText(text);
    }

    public String getDescription() {
        return tv_desc.getText().toString();
    }

    public void setTitle(String title) {
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

}
