package com.seki.noteasklite.Config;

import android.os.Environment;

import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Util.TimeLogic;

import java.io.File;

/**
 * Created by yuan-tian01 on 2016/3/8.
 */
public class NONoConfig {
    //官方账号ID
    public static final long OfficalID = 72;

    public static final String defaultHeadImg ="http://7xrcdn.com1.z0.glb.clouddn.com/nono_default_head.jpg-head";
    public static final String TAG_NONo ="NONo";
    public static String NONoURL="http://2.diandianapp.sinaapp.com";
    public static String makeNONoSonURL(String path){
        return NONoURL+path;
    }
    public static File getNONoDir(){
        if(Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)){
            File NONoRootDir = new File(Environment.getExternalStorageDirectory(),"NONo");
            if(!NONoRootDir.exists()){
                NONoRootDir.mkdirs();
            }
            return NONoRootDir;
        }else{
            return  MyApp.getInstance().getApplicationContext().getExternalFilesDir(null);
        }
        //一个权限都没有的话，怪我咯
    }
    public static File getNONoFilesDir(){
            return  MyApp.getInstance().getApplicationContext().getExternalFilesDir(null);
    }
    public static File getNONoTxtDir(){
        File file = new File(getNONoDir(),"noteBackupTxt");
        if(!file.isDirectory()){
            file.delete();
            file.mkdirs();
        }
        return file;
    }
    public static File getNONoPdfDir(){
        File file = new File(getNONoDir(),"notePdf");
        if(!file.isDirectory()){
            file.delete();
            file.mkdirs();
        }
        return file;
    }
    public static File getNONolongPicDir(){
        File file = new File(getNONoDir(),"长图");
        if(!file.isDirectory()){
            file.delete();
            file.mkdirs();
        }
        return file;
    }
    public static File getNONoTxtFileNow(){
        File file = new File(getNONoTxtDir(),"NONo笔记备份-" +TimeLogic.getNowTimeFormatly()+".txt");
        try{
            if(!file.exists()){
                file.createNewFile();
            }
        }catch (Exception e){
            file = null;
        }
        return file;
    }
    public static File getNONoAutoBackUpDir(){
        File file = new File(getNONoDir(),"自动备份" );
        try{
            if(!file.exists()){
                file.mkdirs();
            }
        }catch (Exception e){
            file = null;
        }
        return file;
    }
}
