package com.seki.noteasklite.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;

import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.MyApp;

/**
 * Created by yuan on 2016/5/4.
 */
public class AppPreferenceUtil {
    private static final  String PREFERS ="prefers";
    private static final  String HEAD_BG_PATH ="head_bg_path";
    public static SharedPreferences getSP(){
        return MyApp.getInstance().getApplicationContext().getSharedPreferences(PREFERS, Context.MODE_PRIVATE);
    }
    public static String getHeadBgPath(){
        return getSP().getString(HEAD_BG_PATH,null);
    }
    public static void setHeadBgPath(String head_bg_path){
        getSP().edit().putString(HEAD_BG_PATH,head_bg_path).apply();
    }
    private static final  String BACK_UP_DATE ="backupDate";
    public static boolean isNeedBackUp(){
        return true;
//        String  lastBackUpDate = getSP().getString(BACK_UP_DATE,"0");
//        long lastBackUpTS  = 0;
//        try{
//            lastBackUpTS = Long.valueOf(lastBackUpDate);
//        }catch (Exception e){e.printStackTrace();}
//        long currentTs = System.currentTimeMillis();
//        boolean isNeedBackUp = false;
//        if(currentTs - lastBackUpTS > 24*60*60*1000){
//            //开始备份
//            isNeedBackUp = true;
//            getSP().edit().putString(BACK_UP_DATE,String.valueOf(currentTs)).apply();
//        }
//        return isNeedBackUp;
    }
    public static final String CHECK_UPDATE_INTERVAL_DAY = "CHECK_UPDATE_INTERVAL_DAY";
    public static final String CHECK_UPDATE_INTERVAL_WEEK = "CHECK_UPDATE_INTERVAL_WEEK";
    public static final String CHECK_UPDATE_INTERVAL = "CHECK_UPDATE_INTERVAL";
    public static final String LAST_CHECH_ST = "LAST_CHECH_ST";
    public static boolean isNeedCheckUpdate(){
        String interval = getSP().getString(CHECK_UPDATE_INTERVAL,"now");
        long lastCheckUpdateST = Long.valueOf(getSP().getString(LAST_CHECH_ST,"0"));
        long currentST = System.currentTimeMillis();
        if(TextUtils.equals(CHECK_UPDATE_INTERVAL_DAY,interval)){
            if(currentST -lastCheckUpdateST>24*60*60*1000 ){
                return true;
            }else{
                return false;
            }
        }else if(TextUtils.equals(CHECK_UPDATE_INTERVAL_WEEK,interval)){
            if(currentST -lastCheckUpdateST>24*60*60*1000*7 ){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }
    public static void setCheckUpdateInterval(String interval){
        getSP().edit().putString(CHECK_UPDATE_INTERVAL,interval).apply();
        getSP().edit().putString(LAST_CHECH_ST,String.valueOf(System.currentTimeMillis())).apply();
    }
    private static final String CHECK_GROUP_LIST_MODE = "com.seki.noteasklite.Util.CHECK_GROUP_LIST_MODE";
    public static GroupListStyle getGroupStyle(){
        String styleString = getSP().getString(CHECK_GROUP_LIST_MODE,"");
        switch (styleString){
            case "LIST":
                return  GroupListStyle.LIST;
            case "TABLE":
                return  GroupListStyle.TABLE;
            default:
                return GroupListStyle.LIST;
        }
    }
    public static void setGroupStyle(GroupListStyle style){
        if(style == GroupListStyle.LIST){
            getSP().edit().putString(CHECK_GROUP_LIST_MODE,"LIST").apply();
        }else if(style == GroupListStyle.TABLE){
            getSP().edit().putString(CHECK_GROUP_LIST_MODE,"TABLE").apply();
        }
    }
    private static final String CHECH_SHOW_SAVE_DRAFT_SNACK = "com.seki.noteasklite.Util.CHECH_SHOW_SAVE_DRAFT_SNACK";
    public static boolean isShowSaveDraftSnack(){
        return  getSP().getBoolean(CHECH_SHOW_SAVE_DRAFT_SNACK,true);
    }
    public static  void disableShowSaveDraftSnack(){
        getSP().edit().putBoolean(CHECH_SHOW_SAVE_DRAFT_SNACK,false).apply();
    }

    private static final String DETAIL_ACTIIVTY_BACKGROUD= "com.seki.noteasklite.Util.DETAIL_ACTIIVTY_BACKGROUD";
    public static int getDetailBgColor(){
        return  getSP().getInt(DETAIL_ACTIIVTY_BACKGROUD, Color.WHITE);
    }
    public static int getDetailHeadBgColor(){
        return ColorUtils.getLighterColor(getDetailBgColor(),0.3f);
    }
    public static  void setDetailBgColor(int color){
        getSP().edit().putInt(DETAIL_ACTIIVTY_BACKGROUD,color).apply();
    }

    private static final String EDIT_ACTIIVTY_BACKGROUD= "com.seki.noteasklite.Util.EDIT_ACTIIVTY_BACKGROUD";
    public static int getEditBgColor(){
        return  getSP().getInt(EDIT_ACTIIVTY_BACKGROUD, Color.WHITE);
    }
    public static int getEditHeadBgColor(){
        return ColorUtils.getLighterColor(getEditBgColor(),0.3f);
    }
    public static  void setEditBgColor(int color){
        getSP().edit().putInt(EDIT_ACTIIVTY_BACKGROUD,color).apply();
    }

    private static final String KEY_DEFAULT_GGROUP = "com.seki.noteasklite.Util.KEY_DEFAULT_GGROUP";

    public static String getDefaultGroup(){
        return getSP().getString(KEY_DEFAULT_GGROUP,"默认分组");
    }
    public static void setDefaultGroup(String groupName){
        getSP().edit().putString(KEY_DEFAULT_GGROUP,groupName).apply();
    }
    private static final String KEY_SHOW_DEFAULT_GGROUP_SNACK = "com.seki.noteasklite.Util.KEY_SHOW_DEFAULT_GGROUP_SNACK";

    public static boolean isShowSetDefaultGroupSnack(){
        return getSP().getBoolean(KEY_SHOW_DEFAULT_GGROUP_SNACK,true);
    }
    public static void setShowSetDefaultGroupSnack(boolean isShow){
        getSP().edit().putBoolean(KEY_SHOW_DEFAULT_GGROUP_SNACK,isShow).apply();
    }
    private static final String KEY_SHOW_MARKDOWN_HELP = "com.seki.noteasklite.Util.KEY_SHOW_MARKDOWN_HELP";
    public static boolean isShowMarkDownEditHelp() {
         return getSP().getBoolean(KEY_SHOW_MARKDOWN_HELP,true);
    }

    public static void dontShowMarkDownEditHelp() {
        getSP().edit().putBoolean(KEY_SHOW_MARKDOWN_HELP,false).apply();
    }

    private static final String KEY_SHOW_DOUBLETAP2EDIT_HELP = "com.seki.noteasklite.Util.KEY_SHOW_DOUBLETAP2EDIT_HELP";
    public static boolean isShowDoubleTap2EditHelp() {
        return getSP().getBoolean(KEY_SHOW_DOUBLETAP2EDIT_HELP,true);
    }

    public static void dontShowDoubleTap2EditHelp() {
        getSP().edit().putBoolean(KEY_SHOW_DOUBLETAP2EDIT_HELP,false).apply();
    }

    private static final String KEY_SHOW_MARKDOWN_DETIAL_HELP = "com.seki.noteasklite.Util.KEY_SHOW_MARKDOWN_DETIAL_HELP";
    public static boolean isShowMarkDownDetailHelp() {
        return getSP().getBoolean(KEY_SHOW_MARKDOWN_DETIAL_HELP,true);
    }

    public static void dontShowMarkDownDetailHelp() {
        getSP().edit().putBoolean(KEY_SHOW_MARKDOWN_DETIAL_HELP,false).apply();
    }

    private static final String KEY_SHOW_BACK_TO_SAVE_HELP = "com.seki.noteasklite.Util.KEY_SHOW_BACK_TO_SAVE_HELP";
    public static boolean isShowBackToSaveHelp() {
        return getSP().getBoolean(KEY_SHOW_BACK_TO_SAVE_HELP,true);
    }

    public static void dontShowBackToSaveHelp() {
        getSP().edit().putBoolean(KEY_SHOW_BACK_TO_SAVE_HELP,false).apply();
    }
}
