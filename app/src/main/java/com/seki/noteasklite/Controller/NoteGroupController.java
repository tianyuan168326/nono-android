package com.seki.noteasklite.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DBHelpers.NoteGroupDBHelper;
import com.seki.noteasklite.MyApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuan-tian01 on 2016/4/16.
 */
//控制标签的
public class NoteGroupController {
    private static Set<String > groupList = null;
    public static Set<String> getGroupList(){
        if(groupList == null){
            groupList = realGroupList();
        }
        return groupList;
    }
    public static Set<String> getGroupList(long noteId){
        return NoteGroupDBHelper.getInstance().getGroupList(noteId);
    }
    public static void setNoteGroupList(long id,Set<String> groupSet){
        NoteGroupDBHelper.getInstance().setNoteGroupList(id,groupSet);
    }
    private static Set<String> realGroupList() {
        SharedPreferences groupList = MyApp.getInstance().getApplicationContext().getSharedPreferences("groupInfo", Context.MODE_PRIVATE);
        return groupList.getStringSet("groupSet",new HashSet<String>());
    }
    public static void addGroup(String group){
        getGroupList().add(group);
        syncGroup(getGroupList());
    }
    private static void syncGroup(Set<String > groupSet){
        SharedPreferences groupList = MyApp.getInstance().getApplicationContext().getSharedPreferences("groupInfo", Context.MODE_PRIVATE);
        groupList.edit().putStringSet("groupSet",groupSet).apply();
    }
}
