package com.seki.noteasklite.DBHelpers;

import android.database.Cursor;
import android.text.TextUtils;

import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;
import com.seki.noteasklite.MyApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuan on 2016/4/27.
 */
public class NoteRecycleBinHelper {

    NoteRecycleBinAdapter dbAdapter;
    public NoteRecycleBinHelper(){
        dbAdapter = new NoteRecycleBinAdapter(MyApp.getInstance().applicationContext);
    }
    public List<NoteAllArray> getHistoryNote() {
        List<NoteAllArray> list = new ArrayList<>();
        synchronized (dbAdapter){
            dbAdapter.open();
            Cursor cr=dbAdapter.getAllRecycleNotes();
            if(cr!=null&&cr.moveToLast()) {
                do {
                    list.add(new NoteAllArray( cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_TITLE)),
                            cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_CONTENT)),
                            cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_GROUP)),
                            cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_DATE)),
                            cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_TIME)),
                            cr.getLong(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_ROWID)),
                            "false",
                            cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_UUID))
                    ));
                }while (cr.moveToPrevious());
            }
            dbAdapter.close();
        }
        return list;
    }
    public NoteAllArray getNoteById(long id){
        synchronized (dbAdapter){
            dbAdapter.open();
            Cursor cr=dbAdapter.getRecycleNoteById(id);
            if(cr!=null&&cr.moveToLast()) {
                do {
                        return new NoteAllArray( cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_TITLE)),
                                cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_CONTENT)),
                                cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_GROUP)),
                                cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_DATE)),
                                cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_TIME)),
                                cr.getLong(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_ROWID)),
                               "false",
                                cr.getString(cr.getColumnIndex(NoteRecycleBinAdapter.KEY_UUID))
                        );
                }while (cr.moveToPrevious());
            }
            dbAdapter.close();
        }
        return null;
    }

    public long insertNote(NoteDatabaseArray noteDatabaseArray){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat allFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        String date = dateFormat.format(now);
        String time = timeFormat.format(now);
        if(TextUtils.isEmpty(noteDatabaseArray.date)){
            noteDatabaseArray.date = date;
        }
        if(TextUtils.isEmpty(noteDatabaseArray.time)){
            noteDatabaseArray.time = time;
        }
        if(TextUtils.isEmpty(noteDatabaseArray.uuid) ){
            noteDatabaseArray.uuid = String.valueOf(System.currentTimeMillis()) ;
        }
        //Log.d(NONoConfig.TAG_NONo,"insert uuid in DB Helper"+noteDatabaseArray.uuid);
        long id;
        synchronized (dbAdapter){
            dbAdapter.open();
            id = dbAdapter.insertRecycleNote(noteDatabaseArray);
            dbAdapter.close();
        }
        return id;
    }
    public String getContentById(long id){
        String content = null;
        synchronized (dbAdapter) {
            dbAdapter.open();
            Cursor cr = dbAdapter.getRecycleNoteById(id);
            if (cr != null && cr.moveToFirst() && cr.getCount() > 0) {
                int index = cr.getColumnIndex(NoteRecycleBinAdapter.KEY_CONTENT);
                content = cr.getString(index);
            }
            dbAdapter.close();
        }
        return content;
    }
    public NoteAllArray deleteRecycleNoteById(long sdfId){
        NoteAllArray noteAllArray = getNoteById(sdfId);
        synchronized (dbAdapter) {
            dbAdapter.open();
            dbAdapter.deleteRecycleNote(sdfId);
            dbAdapter.close();
        }
        return noteAllArray;
    }

    public void deleteAllRecycleNote(){
        synchronized (dbAdapter) {
            dbAdapter.open();
            dbAdapter.deleteAllRecycleNote();
            dbAdapter.close();
        }
    }

    private static class NoteRecycleBinHelperHolder{
        public static NoteRecycleBinHelper noteDBHelper = new NoteRecycleBinHelper();
    }
    public static  NoteRecycleBinHelper getInstance(){
        return NoteRecycleBinHelperHolder.noteDBHelper;
    }
}
