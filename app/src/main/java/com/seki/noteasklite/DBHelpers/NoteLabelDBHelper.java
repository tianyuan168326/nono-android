package com.seki.noteasklite.DBHelpers;

import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.MyApp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuan-tian01 on 2016/4/16.
 */
public class NoteLabelDBHelper {
    final NoteLabelListDBAdapter dbAdapter;
    final NoteLabelDBAdapter labelDBAdapter;
    public NoteLabelDBHelper(){
        dbAdapter = new NoteLabelListDBAdapter(MyApp.getInstance().applicationContext);
        labelDBAdapter = new NoteLabelDBAdapter(MyApp.getInstance().getApplicationContext());
    }

    public Set<String> getGroupList(long noteId) {
        String groupJson  =null;
        synchronized (dbAdapter){
            dbAdapter.open();
            Cursor cr = dbAdapter.getRowDataByNoteId(noteId);
            if (cr != null && cr.moveToFirst() && cr.getCount() > 0) {
                int index = cr.getColumnIndex(NoteLabelListDBAdapter.KEY_GROUPJSON);
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
                int group_json_index = cr.getColumnIndex(NoteLabelListDBAdapter.KEY_GROUPJSON);
                int note_id_index = cr.getColumnIndex(NoteLabelListDBAdapter.KEY_NOTEID);
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

    public List<NoteLabelBean> getHistoryLabels() {
        List <NoteLabelBean> list = new ArrayList<>();
        synchronized (labelDBAdapter){
            labelDBAdapter.open();
            list =  labelDBAdapter.getHistoryLabels();
            labelDBAdapter.close();
        }
        return  list;
    }

    public  List<Integer > getNotesByLabel(String label) {
        List <Integer> list = new ArrayList<>();
        synchronized (dbAdapter){
            dbAdapter.open();
            list =  dbAdapter.getNotesByLabel(label);
            dbAdapter.close();
        }
        return  list;
    }

    public List<String> getHistoryLabelNames() {
        List <String> list = new ArrayList<>();
        synchronized (labelDBAdapter){
            labelDBAdapter.open();
            list =  labelDBAdapter.getHistoryLabelNames();
            labelDBAdapter.close();
        }
        return  list;
    }

    public long addLabel(String newGroup) {
        long id  =-1;
        synchronized (labelDBAdapter){
            labelDBAdapter.open();
            id = labelDBAdapter.addLabel(newGroup);
            labelDBAdapter.close();
        }
        return id;
    }

    public void setNoteLabelList(long id, Collection<? extends String> currentNoteLabels) {
        if(currentNoteLabels == null){
            return;
        }
        synchronized (dbAdapter){
            dbAdapter.open();
            dbAdapter.updateLabels(id,new Gson().toJson(currentNoteLabels));
            dbAdapter.close();
        }
    }

    public void labelsRemoveNote(List<String> oldLabels) {
        if(oldLabels !=null){
            return;
        }
        synchronized (labelDBAdapter){
            labelDBAdapter.open();
            labelDBAdapter.labelsRemoveNote(oldLabels);
            labelDBAdapter.close();
        }
    }
    public void labelRemoveNote(String label) {

    }
    public void labelsAddNote(Collection<? extends String> currentNoteLabels) {
        synchronized (labelDBAdapter){
            labelDBAdapter.open();
            labelDBAdapter.labelsAddNote(currentNoteLabels);
            labelDBAdapter.close();
        }
    }
    public void labelAddNote( String label) {

    }
    private static class NoteGroupDBHelperHolder{
        public static NoteLabelDBHelper noteDBHelper = new NoteLabelDBHelper();
    }
    public static NoteLabelDBHelper getInstance(){
        return NoteGroupDBHelperHolder.noteDBHelper;
    }
}
