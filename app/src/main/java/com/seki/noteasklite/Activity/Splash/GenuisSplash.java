package com.seki.noteasklite.Activity.Splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.seki.noteasklite.Activity.Ask.LockActivity;
import com.seki.noteasklite.Activity.MainActivity;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

/**
 * Created by yuan on 2016/5/13.
 */
public class GenuisSplash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(NONoConfig.TAG_NONo,"4");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fake_main_activity);
        Toolbar toolBar = (Toolbar)findViewById (R.id.toolbar);
        if(toolBar == null){
            return ;
        }
        setSupportActionBar(toolBar);
        toolBar.setTitle("加载笔记中...");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolBar.setBackgroundDrawable(new ColorDrawable(ThemeController.getCurrentColor().getMainColor()));
        setTitle("加载笔记中...");
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ThemeController.getCurrentColor().getDarkColor());
        }
        MainActivity.start(this);
    }

    public static void start(Context context) {
        context.startActivity(new Intent().setClass(context,GenuisSplash.class));
    }
}
