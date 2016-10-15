package com.seki.noteasklite.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.seki.noteasklite.MyApp;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yuan-tian01 on 2016/4/16.
 */
//private Set<String> set;
public  class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="note";
    private static final int DATABASE_VERSION=4;
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notes (_id integer primary key autoincrement, "
                + "groups text not null, date text not null, "
                + "time text not null,title text not null,content text not null, notify text not null,is_on_cloud text not null" +
                ",uuid text not null);");
        //标签控制
        db.execSQL("create table notegroups (_id integer primary key autoincrement,note_id integer not null, group_json text not null);");
        db.execSQL("create table recycle_notes (_id integer primary key autoincrement, "
                + "groups text not null, date text not null, "
                + "time text not null,title text not null,content text not null" +
                ",uuid text not null);");
        //控制文集
        db.execSQL("create table reels (_id integer primary key autoincrement, "
                + "reel text not null, note_num integer default 0, "
                + "abstract text not null,title_pic_path text not null" +
                ",create_time text not null);");
        //控制标签
        db.execSQL("create table labels (_id integer primary key autoincrement, "
                + "label text not null, note_num integer default 0"+
                ",create_time text not null);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int versionIndex = oldVersion;versionIndex<newVersion;versionIndex++){
            switch (versionIndex-1){
                case 0:
                    upgrade0(db);
                    break;
                case 1:
                    upgrade1(db);
                    break;
                case 2:
                    upgrade2(db);
                    break;
            }
        }
    }
    public void upgrade0(SQLiteDatabase db){
        db.execSQL("create table if not exists notegroups (_id integer primary key autoincrement,note_id integer not null, group_json text not null);");
    }
    public void upgrade1(SQLiteDatabase db){
        db.execSQL("create table if not exists recycle_notes (_id integer primary key autoincrement, "
                + "groups text not null, date text not null, "
                + "time text not null,title text not null,content text not null" +
                ",uuid text not null);");
    }
    public void upgrade2(SQLiteDatabase db){
        //重建文集数据库
        db.execSQL("create table if not exists reels (_id integer primary key autoincrement, "
                + "reel text not null, note_num integer default 0, "
                + "abstract text not null,title_pic_path text not null" +
                ",create_time text not null);");
        SharedPreferences preferences = MyApp.getInstance().getSharedPreferences("groupState", Context.MODE_PRIVATE);
        Set<String> groupSet=new HashSet<>();
        groupSet=preferences.getStringSet("groups", groupSet);
            for (String group:
                    groupSet) {
                Cursor cur = db.query("notes",new String[]{"_id"},"groups=?",new String[]{group},null,null,null);
                int rowNum = cur.getCount();
                if(rowNum<1){
                    rowNum = 0;
                }
                ContentValues cv = new ContentValues();
                cv.put("reel",group);
                cv.put("note_num",rowNum);
                cv.put("abstract","");
                cv.put("title_pic_path","");
                cv.put("create_time",String.valueOf(System.currentTimeMillis()));
                db.insert("reels",null,cv);
            }
        //重建标签数据库
        db.execSQL("create table if not exists labels" +
                " (_id integer primary key autoincrement, "
                + "label text not null, note_num integer default 0"+
                ",create_time text not null);");
        SharedPreferences groupList = MyApp.getInstance().getApplicationContext().getSharedPreferences("groupInfo", Context.MODE_PRIVATE);
        Set<String> labelStrings = groupList.getStringSet("groupSet",new HashSet<String>());
        for (String label :
                labelStrings) {
            ContentValues cv = new ContentValues();
            cv.put("label",label);
            cv.put("create_time",String.valueOf(System.currentTimeMillis()));
            db.insert("exists",null,cv);
        }
    }
}
