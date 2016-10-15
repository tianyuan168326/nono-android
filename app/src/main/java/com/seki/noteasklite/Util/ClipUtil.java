package com.seki.noteasklite.Util;

import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.seki.noteasklite.MyApp;

/**
 * Created by yuan-tian01 on 2016/4/6.
 */
public class ClipUtil {
    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static void copy(String content, Context context)
    {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        Toast.makeText(MyApp.getInstance().getApplicationContext(),"内容已被复制到剪切板",Toast.LENGTH_SHORT).show();
    }
    /**
     * 实现粘贴功能
     * add by wangqianzhou
     * @param context
     * @return
     */
    public static String paste(Context context)
    {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
