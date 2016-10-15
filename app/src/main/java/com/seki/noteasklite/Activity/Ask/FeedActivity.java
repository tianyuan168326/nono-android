package com.seki.noteasklite.Activity.Ask;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.ImageHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class FeedActivity extends BaseActivity {
    public static void start(Context context){
        context.startActivity(new Intent()
        .setClass(context,FeedActivity.class));
    }
    @Override
    protected void registerWidget() {

        findViewById(R.id.connect_me).setOnClickListener(this);
        findViewById(R.id.copy_qqqun).setOnClickListener(this);
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed,"关于反馈");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connect_me:
                UserInfoActivity.start(this,"72");
                break;
            case R.id.copy_qqqun:
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "543817980"));
                Toast.makeText(this,"内测QQ群号复制成功！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try
        {
            InputStream is = am.open("butiful_bg.jpg");
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        ImageHelper.fastblurSrc( image, 12);
        ((ImageView)findViewById(R.id.root)).setImageBitmap(image);
    }
}

