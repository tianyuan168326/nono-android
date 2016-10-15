package com.seki.noteasklite.ThirdWrapper;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Util.EncryptUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan-tian01 on 2016/3/10.
 */
public class PowerStringRequest extends StringRequest {
    private boolean isStrictMode = true;
    public PowerStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public PowerStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }
    public static String  ts= "";
    // for the fucking ts in php
    public static String getPHPTS(){
       String javaST = String.valueOf(System.currentTimeMillis());
        return javaST.substring(0,javaST.length()-3);
    }
    public static String makeSuperKey(){
         ts = getPHPTS();
        String firstLayer = String.valueOf(ts)+MyApp.getInstance().userInfo.wonderFull;
        String secondLayer = EncryptUtils.MD5(firstLayer)+MyApp.getInstance().userInfo.wonderFull;
        return secondLayer;
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        String magic = makeSuperKey();
        HashMap<String,String> primaryParams = new HashMap<>();
        primaryParams.put("id", MyApp.getInstance().userInfo.userId);
        primaryParams.put("ts",ts);
        primaryParams.put("magic",magic);
        return primaryParams;
    }
}
