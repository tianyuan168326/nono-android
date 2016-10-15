package com.seki.noteasklite.DBHelpers;

import android.database.Cursor;
import android.text.TextUtils;

import com.seki.noteasklite.Controller.NoteReelsController;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;
import com.seki.noteasklite.DataUtil.Search.LocalNoteArray;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Util.ShareUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by yuan on 2016/1/12.
 */
public class NoteDBHelper {
    NoteDBAdapter dbAdapter;
    public NoteDBHelper(){
        dbAdapter = new NoteDBAdapter(MyApp.getInstance().applicationContext);
    }
    public List<NoteAllArray> getHistoryNote() {
        List<NoteAllArray> list = new ArrayList<>();
        synchronized (dbAdapter){
            dbAdapter.open();
            Cursor cr=dbAdapter.getAllTitles();
            if(cr!=null&&cr.moveToLast()) {
                do {
                    list.add(new NoteAllArray( cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_TITLE)),
                            cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_CONTENT)),
                            cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_GROUP)),
                            cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_DATE)),
                            cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_TIME)),
                            cr.getLong(cr.getColumnIndex(NoteDBAdapter.KEY_ROWID)),
                            cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_IS_ON_CLOUD)),
                            cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_UUID))
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
            Cursor cr=dbAdapter.getNoteById(id);
            if(cr!=null&&cr.moveToLast()) {
                        return new NoteAllArray( cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_TITLE)),
                                cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_CONTENT)),
                                cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_GROUP)),
                                cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_DATE)),
                                cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_TIME)),
                                cr.getLong(cr.getColumnIndex(NoteDBAdapter.KEY_ROWID)),
                                cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_IS_ON_CLOUD)),
                                cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_UUID))
                        );
            }
            dbAdapter.close();
        }
        return null;
    }
    private int alarmHours = 24;
    public long insertNote(NoteDatabaseArray noteDatabaseArray){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat allFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        String date = dateFormat.format(now);
        String time = timeFormat.format(now);
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.HOUR, alarmHours);
        String notifyTime = allFormat.format(c.getTime());
        if(TextUtils.isEmpty(noteDatabaseArray.date)){
            noteDatabaseArray.date = date;
        }
        if(TextUtils.isEmpty(noteDatabaseArray.time)){
            noteDatabaseArray.time = time;
        }
        if(TextUtils.isEmpty(noteDatabaseArray.notifyTime)){
            noteDatabaseArray.notifyTime = notifyTime;
        }
        if(TextUtils.isEmpty(noteDatabaseArray.uuid) ){
            noteDatabaseArray.uuid = String.valueOf(System.currentTimeMillis()) ;
        }
        //Log.d(NONoConfig.TAG_NONo,"insert uuid in DB Helper"+noteDatabaseArray.uuid);
        long id;
        synchronized (dbAdapter){
            dbAdapter.open();
             id = dbAdapter.insertTitle(noteDatabaseArray);
            dbAdapter.close();
        }
        for (NoteAllArray note:
             getHistoryNote()) {
           // Log.d(NONoConfig.TAG_NONo,"all uuid in db "+note.content +note.uuid);
        }

        NoteReelsController.reelAddNote(noteDatabaseArray.group,1);
        return id;
    }
    public long updateNote(long sdfId,String groupName,NoteDatabaseArray noteDatabaseArray){

        NoteDBHelper.getInstance().deleteNoteById(sdfId,groupName);
        noteDatabaseArray.is_on_cloud = "false";
        long newId = NoteDBHelper.getInstance().insertNote(noteDatabaseArray);
        return newId;
    }
    public List<LocalNoteArray> searchNote(String keyWord){
        List<LocalNoteArray> list = new ArrayList<>();
        dbAdapter.open();
        Cursor cr=dbAdapter.getAllTitles();
        int KEY_TITLE_index = cr.getColumnIndex(NoteDBAdapter.KEY_TITLE);
        int KEY_CONTENT_index = cr.getColumnIndex(NoteDBAdapter.KEY_CONTENT);
        int KEY_GROUP_index = cr.getColumnIndex(NoteDBAdapter.KEY_GROUP);
        int KEY_DATE_index = cr.getColumnIndex(NoteDBAdapter.KEY_DATE);
        int KEY_TIME_index = cr.getColumnIndex(NoteDBAdapter.KEY_TIME);
        int KEY_ROWID_index = cr.getColumnIndex(NoteDBAdapter.KEY_ROWID);
        int KEY_IS_ON_CLOUD_index = cr.getColumnIndex(NoteDBAdapter.KEY_IS_ON_CLOUD);
        int KEY_UUID_index = cr.getColumnIndex(NoteDBAdapter.KEY_UUID);
        if(cr!=null&&cr.moveToLast()) {
            do {
                String title =  cr.getString(KEY_TITLE_index);
                String content =  cr.getString(KEY_CONTENT_index);
                String group =  cr.getString(KEY_GROUP_index);
                String date =  cr.getString(KEY_DATE_index);
                String time =  cr.getString(KEY_TIME_index);
                long rowid =  cr.getLong(KEY_ROWID_index);
                String is_cloud =  cr.getString(KEY_IS_ON_CLOUD_index);
                String uuid =  cr.getString(KEY_UUID_index);
                int titleStartIndex = title.indexOf(keyWord);
                int titleEndIndex = titleStartIndex==-1?-1:titleStartIndex+keyWord.length() ;
                content = ShareUtil.htmlToPlain(content);
                int contentStartIndex = content.indexOf(keyWord);
                int contentEndIndex = contentStartIndex==-1?-1:contentStartIndex+keyWord.length() ;

                int groupStartIndex = group.indexOf(keyWord);
                int groupEndIndex = groupStartIndex==-1?-1:groupStartIndex+keyWord.length() ;

                if(titleStartIndex!=-1
                        ||contentStartIndex!=-1
                        ||groupStartIndex!=-1){
                    LocalNoteArray localNoteArray = new LocalNoteArray();
                    localNoteArray.setContentEnd(contentEndIndex);
                    localNoteArray.setContentStart(contentStartIndex);
                    localNoteArray.setGroupEnd(groupEndIndex);
                    localNoteArray.setGroupStart(groupStartIndex);
                    localNoteArray.setTitleEnd(titleEndIndex);
                    localNoteArray.setTitleStart(titleStartIndex);
                    localNoteArray.setNoteAllArray(
                            new NoteAllArray(
                                    title
                                    ,content
                                    ,group
                                    ,date
                                    ,time
                                    ,rowid
                                    ,is_cloud
                                    ,uuid
                            )
                    );
                    list.add(localNoteArray);
                }
            }while (cr.moveToPrevious());
        }
        dbAdapter.close();
        return list;
    }
    public String getContentById(long id){
        String content = null;
        synchronized (dbAdapter) {
            dbAdapter.open();
            Cursor cr = dbAdapter.getTitleById(id);
            if (cr != null && cr.moveToFirst() && cr.getCount() > 0) {
                int index = cr.getColumnIndex(NoteDBAdapter.KEY_CONTENT);
                content = cr.getString(index);
            }
            dbAdapter.close();
        }
        return content;
    }
    public String getCloudStateById(long id){
        String is_on_cloud = null;
        synchronized (dbAdapter) {
            dbAdapter.open();

            Cursor cr = dbAdapter.getTitleById(id);
            if (cr != null && cr.getCount() >= 0) {
                is_on_cloud = cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_IS_ON_CLOUD));
            }
            dbAdapter.close();
        }
        return is_on_cloud;
    }
    public String getCloudState(long id,String state){
        String is_on_cloud = null;
        synchronized (dbAdapter) {
            dbAdapter.open();

            Cursor cr = dbAdapter.getTitleById(id);
            if (cr != null && cr.getCount() >= 0) {
                is_on_cloud = cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_IS_ON_CLOUD));
            }
            dbAdapter.close();
        }
        return is_on_cloud;
    }
    public int updateCloudStateById(long id,String state){
        int new_id = -1;
        synchronized (dbAdapter) {
            dbAdapter.open();
            Cursor cr = dbAdapter.getTitleById(id);
            if (cr != null && cr.getCount() >= 0) {
                new_id = dbAdapter.updateNoteById(id, new NoteDatabaseArray(
                        cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_GROUP)),
                        cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_DATE)),
                        cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_TIME)),
                        cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_TITLE)),
                        cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_CONTENT)),
                        state,
                        cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_NOTIFY)),
                        cr.getString(cr.getColumnIndex(NoteDBAdapter.KEY_UUID))
                ));
            }
            dbAdapter.close();
        }
        return new_id;
    }
    public String getDateById(long id){
        String time =null;
        synchronized (dbAdapter) {
            dbAdapter.open();
            Cursor cursor = dbAdapter.getTitleById(id);
            if (cursor != null && cursor.moveToLast()) {
                time = cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_DATE)) + " " + cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_TIME));
            }
            dbAdapter.close();
        }
        return time;
    }
    public NoteAllArray deleteNoteById(long sdfId,String group){
        NoteAllArray noteAllArray = getNoteById(sdfId);
        synchronized (dbAdapter) {
            dbAdapter.open();
            dbAdapter.deleteTitle(sdfId);
            dbAdapter.close();
        }
        NoteReelsController.reelDeleteNote(group,1);
        return noteAllArray;
    }
    public void updateGroupName(String oldG,String newG){
        synchronized (dbAdapter) {
            dbAdapter.open();
            dbAdapter.updateGroup(oldG,newG);
            dbAdapter.close();
        }
    }

    public void setNoteGroupList(long id, Set<String> groupSet) {

    }

    public  void changeGroup(long sdfId, String currentGroup) {
        synchronized (dbAdapter) {
            dbAdapter.open();
            dbAdapter.changeGroup(sdfId,currentGroup);
            dbAdapter.close();
        }
    }

    public List<NoteDatabaseArray> getNoteByReel(String reel){
        List<NoteDatabaseArray> list = new ArrayList<>();
        synchronized (dbAdapter) {
            dbAdapter.open();
            Cursor cursor = dbAdapter.getTitleByReel(reel);

            if (cursor.getCount() > 0 && cursor.moveToLast()) {
                do {
                    list.add(new NoteDatabaseArray(
                            cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_GROUP)),
                            cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_DATE)),
                            cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_TIME)),
                            cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_CONTENT)),
                            "false",
                            "",
                            cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_UUID))
                    ));
                } while (cursor.moveToPrevious());
            }
            dbAdapter.close();
        }
        return list;
    }
    public void deleteNoteByReels(String[] reels) {
        for (String reel:
                reels ) {
            List<NoteDatabaseArray> list = getNoteByReel(reel);
            for (NoteDatabaseArray array:
                 list) {
                NoteRecycleBinHelper.getInstance().insertNote(array);
            }
        }

        synchronized (dbAdapter) {
            dbAdapter.open();
            for (String reel:
                    reels) {
                dbAdapter.deleteTitleByReel(reel);
            }
            dbAdapter.close();
        }
    }


    private static class NoteDBHelperHolder{
        public static NoteDBHelper noteDBHelper = new NoteDBHelper();
    }
    public static  NoteDBHelper getInstance(){
        return NoteDBHelperHolder.noteDBHelper;
    }
}
