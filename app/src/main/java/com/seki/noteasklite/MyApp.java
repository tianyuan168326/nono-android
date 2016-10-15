package com.seki.noteasklite;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.DataUtil.AppUserInfo;
import com.seki.noteasklite.DataUtil.Bean.AuthorBean;
import com.seki.noteasklite.DataUtil.Bean.NotificationDataModel;
import com.seki.noteasklite.DataUtil.NoteSimpleArray;
import com.seki.noteasklite.DataUtil.NotificationNotifier;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.fir.sdk.FIR;

public class MyApp extends Application {
	//标记网络状态
	//0 :无网络
	//1:3g
	//2:wifi
	public int netWorkState =0 ;
	//fuck fresco
	static{
		try {
			SoLoaderShim.loadLibrary("webp");
		} catch(UnsatisfiedLinkError nle) {
		}
	}
	public static class Cachepool{
		public WeakReference<Bitmap> reelImageCache  ;
	}
	static  Cachepool cachepool;
	public static Cachepool getCache(){

			if(cachepool ==null){
				cachepool = new Cachepool();
			}
		return cachepool;
	}
	public static MyApp getInstance()
	{
		return singleInstance;
	}
	private static  MyApp singleInstance =null;
	public static AppUserInfo userInfo = new AppUserInfo();
	public static AuthorBean authorBean = new AuthorBean();
	public RequestQueue volleyRequestQueue;
	public static Context applicationContext;
	private static List<NotificationDataModel> notificationDataModelList ;
	public static NotificationNotifier newNotificationNotifier = new NotificationNotifier();
	public Map<String,List<String> > subjectCategory = new HashMap<>();
	@Override
	public void onCreate() {
		super.onCreate();
		singleInstance = this;
		iniIM();
		new Thread(new Runnable() {
			@Override
			public void run() {
				NotificationDataModel.applicationContextRef = new WeakReference<Context>(getApplicationContext());
				volleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
				applicationContext = getApplicationContext();
				Fresco.initialize(singleInstance);
				MobclickAgent.openActivityDurationTrack(false);
			}
		}).start();
        iniMonitor();
        iniShareSDK();
		if(BuildConfig.DEBUG == true){
		Stetho.initialize(
				Stetho.newInitializerBuilder(this)
						.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
						.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
						.build());
	}
	}

    private void iniShareSDK() {
//        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
//        //微信 appid appsecret
//        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
//        //新浪微博 appkey appsecret
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//        // QQ和Qzone appid appkey
    }


    private void iniIM() {
		HuanXinHelper.getInstance().init(getApplicationContext());
		HuanXinUserManager.ini(getApplicationContext());
	}
	private void iniMonitor(){
		FIR.init(this);
	}

	public List<NotificationDataModel> getNotificationDataModelList()
	{
		if(notificationDataModelList == null)
		{
			SharedPreferences preferences = getSharedPreferences("gloable", Activity.MODE_PRIVATE);
			String notificationModeListJsonString = preferences.getString("notification_mode_list_json_string","");
			if(notificationModeListJsonString==null || notificationModeListJsonString.isEmpty() )
			{
				notificationDataModelList = new ArrayList<>();
				return notificationDataModelList;
			}
			notificationDataModelList = new Gson().fromJson(notificationModeListJsonString
					, new TypeToken<List<NotificationDataModel>>(){}.getType());
			return notificationDataModelList;

		}
		return notificationDataModelList;
	}
	public static void saveNotificationDataModelList() {
		String notificationModeListJsonString = new Gson().toJson(MyApp.getInstance().getNotificationDataModelList());
		MyApp.getInstance().getApplicationContext().getSharedPreferences("gloable", Activity.MODE_PRIVATE).edit().putString("notification_mode_list_json_string", notificationModeListJsonString)
				.apply();
	}
	public static void toast(String t){
		Toast.makeText(
				getInstance().getApplicationContext(),
				t,Toast.LENGTH_SHORT
		).show();

	}
}
