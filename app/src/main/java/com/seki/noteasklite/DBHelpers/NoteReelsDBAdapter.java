package com.seki.noteasklite.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.seki.noteasklite.DataUtil.NoteReelArray;

/**
 * Created by yuan on 2016/6/4.
 */
public class NoteReelsDBAdapter {
    public final String TABLE = "reels";

    public static final String KEY_ID  ="_id";
    public static final String KEY_REAL ="reel";
    public static final String KEY_NOTE_NUM  ="note_num";
    public static final String KEY_ABSTRACT  ="abstract";
    public static final String KEY_TITLE_PIC  ="title_pic_path";
    public static final String KEY_CREATE_TIME  ="create_time";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public NoteReelsDBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    public NoteReelsDBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }
    public void deleteReel(String reel) {
        db.delete(TABLE,KEY_REAL +"=?",new String[]{reel});
    }
    public void incrementNoteNum(String groupName, int noteNum) {
        groupName = "'"+groupName+"'";
        db.execSQL("update "+ TABLE
        +" set "+KEY_NOTE_NUM+"="+KEY_NOTE_NUM+" +" +String.valueOf(noteNum)+" where "
        +KEY_REAL+"="+groupName);
    }
    public void decreaseNoteNum(String groupName, int noteNum) {
        groupName = "'"+groupName+"'";
        db.execSQL("update "+ TABLE
                +" set "+KEY_NOTE_NUM+"="+KEY_NOTE_NUM+" -" +String.valueOf(noteNum)+" where "
                +KEY_REAL+" = "+groupName);
    }
    public Cursor getReels(){
       return db.query(TABLE,new String[]{KEY_ID, KEY_REAL,KEY_NOTE_NUM,KEY_ABSTRACT,KEY_TITLE_PIC,KEY_CREATE_TIME},null,null,null,null,KEY_REAL);
    }

    public int getReelNoteNum(String oldReel) {
        Cursor cur = db.query(TABLE,new String[]{KEY_NOTE_NUM},KEY_REAL+"=?",new String[]{oldReel},null,null,null);
        if(cur !=null && cur.moveToLast()){
            return cur.getInt(cur.getColumnIndex(KEY_NOTE_NUM));
        }else{
            return 0;
        }

    }
    public  boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                                      String dbfield, String fieldValue) {
        String Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue +"' LIMIT 1";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public void mayAddReel(String reelName) {
       // Cursor cur = db.query(,new String[]{KEY_ID},+"=?",new String[]{reelName},null,null,null);
        if(!CheckIsDataAlreadyInDBorNot(TABLE,KEY_REAL,reelName)){
            ContentValues cv = new ContentValues();
            cv.put(KEY_REAL,reelName);
            cv.put(KEY_TITLE_PIC,"");
            cv.put(KEY_ABSTRACT,"");
            cv.put(KEY_CREATE_TIME,String.valueOf(System.currentTimeMillis()));
            db.insert(TABLE,null,cv);
        }
    }

    public boolean contains(String reelName) {
        Cursor cur = db.query(TABLE,null,KEY_REAL+"=?",new String[]{reelName},null,null,KEY_ID+" DESC","1");
        if(cur == null || cur.getCount() ==0){
            return false;
        }
        return true;
    }

    public void addReel(NoteReelArray array) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_REAL,array.reel_title);
        cv.put(KEY_TITLE_PIC,array.reel_title_pic);
        cv.put(KEY_ABSTRACT,array.reel_abstract);
        cv.put(KEY_CREATE_TIME,array.reel_create_time);
        cv.put(KEY_NOTE_NUM,array.reel_note_num);
        db.insert(TABLE,null,cv);
    }

    public void alterReelName(String oldReel, String newReel) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_REAL,newReel);
        db.update(TABLE,cv,KEY_REAL+"=?",new String[]{oldReel});
    }

    public void updateReel(int id, NoteReelArray noteReelArray) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_REAL,noteReelArray.reel_title);
        cv.put(KEY_ABSTRACT,noteReelArray.reel_abstract);
        cv.put(KEY_TITLE_PIC,noteReelArray.reel_title_pic);
        db.update(TABLE,cv,KEY_ID+"=?",new String[]{String.valueOf(id) });
    }
}

