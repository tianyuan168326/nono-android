package com.seki.noteasklite.DataUtil.Bean;

import android.text.TextUtils;

import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Activity.MainActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan-tian01 on 2016/3/30.
 */
public class WonderFull {
    public  String wonderFull;
    public int state_code;
    public static Object verify(WonderFull wonderFull){
        if(wonderFull.state_code == -9999){
            EventBus.getDefault().post(new MainActivity.LogOutEvent());
            return false;
        }
        if(!TextUtils.isEmpty(wonderFull.wonderFull)&& wonderFull.wonderFull.length()>8){
            MyApp.getInstance().userInfo.wonderFull = wonderFull.wonderFull;
        }
        return wonderFull;
    }
}
