package com.seki.noteasklite.DBHelpers;

import android.database.Cursor;

import com.seki.noteasklite.DataUtil.NoteReelArray;
import com.seki.noteasklite.MyApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2016/6/4.
 */
public class NoteReelsDBHelper {


    NoteReelsDBAdapter adapter;

    public NoteReelsDBHelper() {
        this.adapter = new NoteReelsDBAdapter(MyApp.getInstance().getApplicationContext());
    }

    public void deleteReels(String[] strings) {
        synchronized (adapter){
            adapter.open();
            for (String reel:
                    strings ) {
                adapter.deleteReel(reel);
            }
            adapter.close();
        }
    }


    public void reelNoteExchange(String oldReel, String currentReel, int num) {
        reelAddNote(currentReel,num);
        getInstance().reelDeleteNote(oldReel,num);
    }
    public void reelNoteExchangeAll(String oldReel, String newReel) {
        int oldReelNum = getReelNoteNum(oldReel);
        getInstance().reelAddNote(newReel,oldReelNum);
        getInstance().reelDeleteNote(oldReel,oldReelNum);
    }

    private int getReelNoteNum(String oldReel) {
        int noteNum = 0;
        synchronized (adapter){
            adapter.open();
            noteNum = adapter.getReelNoteNum(oldReel);
            adapter.close();
        }
        return noteNum;
    }

    public List<NoteReelArray> getAllReelArray() {
        List<NoteReelArray> list= new ArrayList<>();
        synchronized (adapter){
            adapter.open();
            Cursor cur = adapter.getReels();
            if(cur!=null && cur.moveToLast()){
                do{
                    list.add(new NoteReelArray(
                            cur.getInt(cur.getColumnIndex(NoteReelsDBAdapter.KEY_ID)),
                            cur.getString(cur.getColumnIndex(NoteReelsDBAdapter.KEY_REAL)),
                            cur.getString(cur.getColumnIndex(NoteReelsDBAdapter.KEY_ABSTRACT)),
                            cur.getString(cur.getColumnIndex(NoteReelsDBAdapter.KEY_CREATE_TIME)),
                            cur.getInt(cur.getColumnIndex(NoteReelsDBAdapter.KEY_NOTE_NUM)),
                            cur.getString(cur.getColumnIndex(NoteReelsDBAdapter.KEY_TITLE_PIC))
                    ));
                }while(cur.moveToPrevious());
            }
            adapter.close();
        }
        return list;
    }

    public boolean contains(String reelName) {
        boolean isContain = false;
        synchronized (adapter){
            adapter.open();
            isContain = adapter.contains(reelName);
            adapter.close();
        }
        return isContain;
    }

    public void addReel(NoteReelArray array) {
        synchronized (adapter){
            adapter.open();
            adapter.addReel(array);
            adapter.close();
        }
    }

    public void alterReelName(String oldReel, String newReel) {
        synchronized (adapter){
            adapter.open();
            adapter.alterReelName(oldReel,newReel);
            adapter.close();
        }
    }

    public void updateReel(int id, NoteReelArray noteReelArray) {
        synchronized (adapter){
            adapter.open();
            adapter.updateReel(id,noteReelArray);
            adapter.close();
        }
    }


    private static class NoteReelsDBHelperHolder{
        public static NoteReelsDBHelper h = new NoteReelsDBHelper();
    }
    public static  NoteReelsDBHelper getInstance(){
        return NoteReelsDBHelperHolder.h;
    }

    public void reelAddNote(String groupName, int noteNum) {
        synchronized (adapter){
            adapter.open();
            adapter.mayAddReel(groupName);
            adapter.incrementNoteNum(groupName,noteNum);
            adapter.close();
        }
    }

    public void reelDeleteNote(String groupName, int noteNum) {
        synchronized (adapter){
            adapter.open();
            adapter.decreaseNoteNum(groupName,noteNum);
            adapter.close();
        }
    }
    public List<String> getReels() {
        List<String> list= new ArrayList<>();
        synchronized (adapter){
            adapter.open();
            Cursor cur = adapter.getReels();
            if(cur!=null && cur.moveToLast()){
                do{
                    list.add(cur.getString(cur.getColumnIndex(NoteReelsDBAdapter.KEY_REAL)));
                }while(cur.moveToPrevious());
            }
            adapter.close();
        }
        return list;
    }
}
