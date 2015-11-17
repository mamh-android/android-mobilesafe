package com.example.mamh.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mamh.mobilesafe.R;

/**
 * Created by mamh on 15-11-1.
 * 自定义的组合控件
 */
public class SettingItemView extends RelativeLayout{
    private CheckBox cb_status;
    private TextView tv_desc;
    private TextView tv_title;

    private String desc_on;
    private String desc_off;

    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        String namespace = "http://schemas.android.com/apk/res-auto";
        String title = attrs.getAttributeValue(namespace, "atitle");
        tv_title.setText(title);
        desc_off=attrs.getAttributeValue(namespace,"desc_off");
        desc_on=attrs.getAttributeValue(namespace,"desc_on");

    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.setting_item_view, this);
        cb_status= (CheckBox) this.findViewById(R.id.cb_status);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);
        tv_title= (TextView) this.findViewById(R.id.tv_title);
        tv_desc.setText(desc_off);
    }

    /**
     * 校验是否选中
     */
    public boolean isChecked(){
        return cb_status.isChecked();
    }
    public void setChecked(boolean checked){
        if(checked){
            setDescription(desc_on);
        }else {
            setDescription(desc_off);
        }
        cb_status.setChecked(checked);
    }

    public void setDescription(String text){
        tv_desc.setText(text);
    }
    public String getDescription(){
        return tv_desc.getText().toString();
    }




















}
