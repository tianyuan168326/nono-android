package com.seki.noteasklite.RetrofitHelper.RequestBody;

import android.util.Pair;

import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Util.EncryptUtils;

import java.util.HashMap;

/**
 * Created by yuan-tian01 on 2016/3/30.
 */
public class AuthBody {
    public String id;
    public String ts;
    public String magic;
    public AuthBody(){
        String javaST = String.valueOf(System.currentTimeMillis());
        ts = javaST.substring(0, javaST.length() - 3);
        String firstLayer = String.valueOf(ts)+ MyApp.getInstance().userInfo.wonderFull;
        magic = EncryptUtils.MD5(firstLayer)+MyApp.getInstance().userInfo.wonderFull;
        id = MyApp.getInstance().userInfo.userId;
    }
    private static HashMap<String,String> getAuthBodyMap(){
        String javaST = String.valueOf(System.currentTimeMillis());
        String ts = javaST.substring(0, javaST.length() - 3);
        String firstLayer = String.valueOf(ts)+ MyApp.getInstance().userInfo.wonderFull;
        String magic = EncryptUtils.MD5(firstLayer)+MyApp.getInstance().userInfo.wonderFull;
        String id = MyApp.getInstance().userInfo.userId;
        HashMap<String,String> authMap = new HashMap<>();
        authMap.put("id",id);
        authMap.put("magic",magic);
        authMap.put("ts",ts);
        return authMap;
    }
    public static HashMap<String,String> getAuthBodyMap(Pair<String,String> ... pairs){
        HashMap<String,String> params = getAuthBodyMap();
        if(pairs == null){
            return params;
        }
        for (Pair<String, String> pair:
        pairs){
            params.put(pair.first,pair.second);
        }
        return params;
    }
}
