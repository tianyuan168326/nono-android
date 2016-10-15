package com.seki.noteasklite.Base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.seki.noteasklite.DataUtil.BusEvent.ThemeColorPairChangedEvent;
import com.umeng.analytics.MobclickAgent;

import ActivityOptionsICS.ActivityCompatICS;
import ActivityOptionsICS.ActivityOptionsCompatICS;
import de.greenrobot.event.EventBus;

/**
 * Created by yuan-tian01 on 2016/3/10.
 */
public abstract class BaseFragment extends Fragment {

    public void screenTransitAnimByPair(Intent intent,Pair<View, Integer>... views) {
        // isSceneAnim = true;
        ActivityOptionsCompatICS options = ActivityOptionsCompatICS.makeSceneTransitionAnimation(
                getActivity(), views);
        ActivityCompatICS.startActivity(getActivity(), intent, options.toBundle());
    }

    String clazz;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        clazz = this.getClass().getName();
        EventBus.getDefault().register(this);
    }
    public void onEvent(Object e){}
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(clazz); //统计页面，"MainScreen"为页面名称，可自定义
        themePatch();
    }
    protected   void themePatch(){};
    protected final <T extends View> T $(int resId) {
        try {
            return (T)(getView().findViewById(resId));
        }catch (ClassCastException e){
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
            throw e;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(clazz);
    }
    public void onEvent(ThemeColorPairChangedEvent e){
        themePatch();
    }

    public  boolean onBackPressed(){
        return false;
    }

}
