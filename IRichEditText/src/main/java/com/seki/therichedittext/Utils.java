package com.seki.therichedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yuan on 2016/5/4.
 */
public class Utils {

    Context context;
    static Utils instance;
    public static Utils getInstance(){
        if(instance == null){
            instance = UtilsWrapper.utils;
        }
        return instance;
    }
    public void setContext(Context c){
        if(context == null){
            context = c;
        }else {
           Log("function setContext only need ini once");
        }
    }
    RequestQueue requestQueue;
    public RequestQueue getVollyQueue(){
        if(requestQueue ==null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
    private static class UtilsWrapper{
        public static Utils utils = new Utils();
    }
    public static void Log(String s){
        Log.d("IRichText",s);
    }
    public static void saveMyBitmap(final String path, final Bitmap mBitmap){
        if(mBitmap ==null){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(path);
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                try {
                    fOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
