package com.seki.noteasklite.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.DataUtil.BusEvent.ThemeColorPairChangedEvent;
import com.seki.noteasklite.Fragment.NONoPreferenceFragment;
import com.seki.noteasklite.R;

import java.util.HashMap;

/**
 * Created by yuan-tian01 on 2016/4/3.
 */
public class PreferenceActivity extends BaseActivity {
    @Override
    protected void registerWidget() {

    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference,"NONo个性化");
        FragmentManager fragmentManager = getFragmentManager();
                 FragmentTransaction transaction = fragmentManager.beginTransaction();
                 NONoPreferenceFragment prefFragment = new NONoPreferenceFragment();
                 transaction.add(R.id.prefFragment, prefFragment);
                 transaction.commit();
    }
    public static void start(Context context ){
        context.startActivity(new Intent()
        .setClass(context,PreferenceActivity.class));
    }

    @Override
    public void onClick(View v) {

    }
    public void onEventMainThread(ThemeColorPairChangedEvent e){
        //themePatch();
        finish();
    }
}
