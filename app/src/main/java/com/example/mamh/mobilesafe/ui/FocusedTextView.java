package com.example.mamh.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mamh on 15-11-1.
 *
 * 自定义一个TextView,有焦点的。
 * 这个配合ellipsize=“marquee”实现字幕的滚动
 */
public class FocusedTextView extends TextView {
    public FocusedTextView(Context context) {
        super(context);
    }

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 当前并没有焦点，只是欺骗系统
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
