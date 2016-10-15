package com.seki.noteasklite.ThirdWrapper;

import com.android.volley.Response;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan-tian01 on 2016/3/10.
 */
public class PowerListener implements Response.Listener<String> {
    //-4
    public void onVerifyError(){
        EventBus.getDefault().post(new MainActivity.LogOutEvent());

    }
    //解析失败
    public void onJSONStringParseError(){

    }
    public void onCorrectResponse(String s){

    }
    @Override
    public void onResponse(String s) {
        JSONObject jsonObject;
        try{
             jsonObject = new JSONObject(s);
            try{
                int state_code = Integer.valueOf(jsonObject.getString("state_code"));
                switch (state_code){
                    case -9999:
                        onVerifyError();
                        return ;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                String wonderFull = jsonObject.getString("wonderFull");
                if(wonderFull.length()>8){
                    MyApp.getInstance().userInfo.wonderFull = wonderFull;
                }
            }catch (Exception e){e.printStackTrace();}

            onCorrectResponse(s);
        }catch (JSONException je){
            onJSONStringParseError();
        }

    }
}
