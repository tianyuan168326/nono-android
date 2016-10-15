package com.seki.noteasklite.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by yuan on 2015/11/15.
 */
public class SystemProcessor {
    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    public static  int getVersionCode(Context context){
        int versionCode = 0;
        try{
            versionCode  = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return versionCode;
    }
}
