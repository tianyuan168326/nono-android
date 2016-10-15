package com.seki.noteasklite.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SeriesLogOnInfo {
	public static String LOGONINFO="com.example.quickask.logoninfo";
	public static String ISLOGSUCCESS="com.example.quickask.islogsuccess";
	public static void putInfo(Context context,String name,String password)
	{
		Editor logonInfoEditor=context.getSharedPreferences(LOGONINFO, Activity.MODE_PRIVATE).edit();
		logonInfoEditor.putString("username", name);
		logonInfoEditor.putString("userpassword", password);
		logonInfoEditor.apply();
	}
	public static void saveAuth(Context context,String password)
	{
		String fuckPW = EncryptUtils.MD5(EncryptUtils.MD5(password));
		Editor logonInfoEditor=context.getSharedPreferences(LOGONINFO, Activity.MODE_PRIVATE).edit();
		logonInfoEditor.putString("authPassword", fuckPW);
		logonInfoEditor.apply();
	}
	public static void clearAuth(Context context)
	{
		Editor logonInfoEditor=context.getSharedPreferences(LOGONINFO, Activity.MODE_PRIVATE).edit();
		logonInfoEditor.clear();
		logonInfoEditor.apply();
	}
	public static boolean verifyAuth(Context context,String pw)
	{
		SharedPreferences prefer=context.getSharedPreferences(LOGONINFO, Activity.MODE_PRIVATE);
		String srcPW = prefer.getString("authPassword","");
		if(EncryptUtils.MD5(EncryptUtils.MD5(pw)).equals(srcPW)){
			return true;
		}else{
			return false;
		}
	}
	public static void clearInfo(Context context)
	{
		Editor logonInfoEditor=context.getSharedPreferences(LOGONINFO, Activity.MODE_PRIVATE).edit();
		logonInfoEditor.putString("username","");
		logonInfoEditor.putString("userpassword","");
		logonInfoEditor.apply();
	}
	public static HashMap<String, String> getInfo(Context context)
	{
		SharedPreferences prefer=context.getSharedPreferences(LOGONINFO, Activity.MODE_PRIVATE);
        String name= prefer.getString("username","");
        String password= prefer.getString("userpassword","");
        HashMap<String, String> infoMap=new HashMap<String, String>();
        infoMap.put("username", name);
        infoMap.put("userpassword", password);
        return infoMap;
	}
	public static void setLogState(Context context,boolean islogSuccess)
	{
		Editor isLogSuccessEditor=context.getSharedPreferences(ISLOGSUCCESS, Activity.MODE_PRIVATE).edit();
		isLogSuccessEditor.putBoolean("islogsuccess",islogSuccess);
		isLogSuccessEditor.commit();
	}
	
}
