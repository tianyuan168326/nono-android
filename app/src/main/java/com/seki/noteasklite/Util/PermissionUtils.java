package com.seki.noteasklite.Util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan-tian01 on 2016/3/28.
 */
public class PermissionUtils {
    public static int PERMISSSION_WRITE_EXTERNAL_STORAGE = 2;
    public static int PERMISSSION_READ_EXTERNAL_STORAGE = 3;
    public static int PERMISSSION_VIBRATE = 5;
    public static int PERMISSSION_GET_TASKS= 6;
    public static int PERMISSSION_CAMERA = 7;
    public static int PERMISSSION_RECEIVE_BOOT_COMPLETED = 8;

    private static HashMap<String,Integer> permissionPair = new HashMap<>();
    private static  void checkPermission(Activity activity){
        makePermissionPair();
        for (Map.Entry<String,Integer> entry:
                permissionPair.entrySet()) {

            if (ContextCompat.checkSelfPermission(activity,
                    entry.getKey())
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        entry.getValue());
            }
        }
    }

    private static void makePermissionPair() {
        permissionPair.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,PERMISSSION_WRITE_EXTERNAL_STORAGE);
        permissionPair.put(Manifest.permission.READ_EXTERNAL_STORAGE,PERMISSSION_READ_EXTERNAL_STORAGE);
        permissionPair.put(Manifest.permission.VIBRATE,PERMISSSION_VIBRATE);
        permissionPair.put(Manifest.permission.GET_TASKS,PERMISSSION_GET_TASKS);
        permissionPair.put(Manifest.permission.CAMERA,PERMISSSION_CAMERA);
        permissionPair.put(Manifest.permission.RECEIVE_BOOT_COMPLETED,PERMISSSION_RECEIVE_BOOT_COMPLETED);
    }
}
