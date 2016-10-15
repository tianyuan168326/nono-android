package com.seki.noteasklite.DBHelpers;

import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.MyApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuan-tian01 on 2016/4/16.
 */
public class NoteGroupDBHelper {
    NoteGroupDBAdapter dbAdapter;
    public NoteGroupDBHelper(){
        dbAdapter = new NoteGroupDBAdapter(MyApp.getInstance().applicationContext);
    }

    public Set<String> getGroupList(long noteId) {
        String groupJson  =null;
        synchronized (dbAdapter){
            dbAdapter.open();
            Cursor cr = dbAdapter.getRowDataByNoteId(noteId);
            if (cr != null && cr.moveToFirst() && cr.getCount() > 0) {
                int index = cr.getColumnIndex(NoteGroupDBAdapter.KEY_GROUPJSON);
                groupJson = cr.getString(index);
            }
            dbAdapter.close();
        }
        if(TextUtils.isEmpty(groupJson)){
            return new HashSet<>();
        }
        try{
            return new Gson().fromJson(groupJson,new TypeToken<HashSet<String>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public void setNoteGroupList(long id, Set<String> groupSet) {
        synchronized (dbAdapter){
            dbAdapter.open();
            dbAdapter.updateGroupJsonByNoteId(id,new Gson().toJson(groupSet));
            dbAdapter.close();
        }
    }
    public List<NoteAllArray>  getNoteArrayByTag(String tag){
        List<Long >  noteIdList = new ArrayList<>();
        List<NoteAllArray> noteAllArrayList = new ArrayList<>();
        synchronized (dbAdapter){
            dbAdapter.open();
            Cursor cr = dbAdapter.getAll();
            if (cr != null && cr.moveToLast() && cr.getCount() > 0) {
                int group_json_index = cr.getColumnIndex(NoteGroupDBAdapter.KEY_GROUPJSON);
                int note_id_index = cr.getColumnIndex(NoteGroupDBAdapter.KEY_NOTEID);
                do{
                    String groupJson = cr.getString(group_json_index);
                    if(groupJson.contains(tag)){
                        noteIdList.add(cr.getLong(note_id_index));
                    }
                }while (cr.moveToPrevious());
            }
            dbAdapter.close();
        }
        for (long noteId:
                noteIdList) {
            noteAllArrayList.add(

                    NoteDBHelper.getInstance().getNoteById(noteId)
            );
        }
        return noteAllArrayList;
    }
    private static class NoteGroupDBHelperHolder{
        public static NoteGroupDBHelper noteDBHelper = new NoteGroupDBHelper();
    }
    public static  NoteGroupDBHelper getInstance(){
        return NoteGroupDBHelperHolder.noteDBHelper;
    }
}
