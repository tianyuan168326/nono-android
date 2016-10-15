package com.seki.noteasklite;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.sumimakito.quickkv.QuickKV;
import com.github.sumimakito.quickkv.database.KeyValueDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.Config.NONoConfig;
import com.yuantian.com.easeuitransplant.EaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan-tian01 on 2016/3/7.
 */
public class HuanXinUserManager {
    public static QuickKV quickKVWeakReference;
    public static void ini(Context context){
        quickKVWeakReference =  (new QuickKV(context));
    }
    public static void put(final String id, final EaseUser easeUser){
        MyApp.getInstance().volleyRequestQueue.add(new StringRequest(Request.Method.POST,
                "http://2.diandianapp.sinaapp.com/quickask_get_userinfo_home.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        com.seki.noteasklite.DataUtil.Bean.UserInfo userInfo = new com.seki.noteasklite.DataUtil.Bean.UserInfo();
                        userInfo.data = new com.seki.noteasklite.DataUtil.Bean.UserInfo.Data();
                        userInfo.data.user_info_real_name = "佚名";
                        userInfo.data.user_info_headimg= NONoConfig.defaultHeadImg;
                        try{
                            userInfo =
                                    new Gson().fromJson(s, new TypeToken<com.seki.noteasklite.DataUtil.Bean.UserInfo>() {
                                    }.getType());
                        }catch (JsonSyntaxException jse){
                            Log.d(NONoConfig.TAG_NONo,"oops....,can not fingure out the user on NONo,may be admin on HuanXin");
                            userInfo.data.user_info_real_name = "外星人";
                        }
                        easeUser.setNick(userInfo.data.user_info_real_name);
                        easeUser.setAvatar(userInfo.data.user_info_headimg);

                        KeyValueDatabase keyValueDatabase = quickKVWeakReference.getDatabase();
                        keyValueDatabase.put(id, easeUser);
                        keyValueDatabase.persist();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", id);
                return params;
            }
        });

    }
    public static  EaseUser get(String id){
        KeyValueDatabase keyValueDatabase = quickKVWeakReference.getDatabase();
        return (EaseUser)keyValueDatabase.get(id);
    }
    public static void update(String id,EaseUser easeUser){
        KeyValueDatabase keyValueDatabase = quickKVWeakReference.getDatabase();
        keyValueDatabase.remove(id);
        keyValueDatabase.put(id,easeUser);
        keyValueDatabase.persist();
    }
}
