package com.seki.noteasklite.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.seki.noteasklite.Config.NONoConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan-tian01 on 2016/4/16.
 */
public class NoteLabelListDBAdapter {
    public static final  String KEY_ROWID="_id";
    public static final  String KEY_NOTEID="note_id";
    public static final  String KEY_GROUPJSON="group_json";

    private static final String DATABASE_TABLE="notegroups";

    private final Context context ;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public Cursor getRowDataByNoteId(long noteId) {
        Cursor mCursor = db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_NOTEID, KEY_GROUPJSON},
                KEY_NOTEID + "=" + "'" + String.valueOf(noteId) + "'",null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToLast();
        }
        return mCursor;
    }
    public Cursor getAll() {
        Cursor mCursor = db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_NOTEID, KEY_GROUPJSON},
                null,null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToLast();
        }
        return mCursor;
    }
    public boolean updateGroupJsonByNoteId(long noteId,String groupJson){
        long id = -1;
        long affetedLines = 0;
        int count = getRowDataByNoteId(noteId).getCount();
        if(count == 0){
            id = insertGroupInfo(noteId,groupJson);
        }else{
            affetedLines = updateGroupInfo(noteId,groupJson);
        }
        if(id == -1 && affetedLines== 0){
            Log.e(NONoConfig.TAG_NONo,"update group set in sql error!");
            return false;
        }else{
            return true;
        }
    }

    private long updateGroupInfo(long noteId, String groupJson) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GROUPJSON,groupJson);
        return db.update(DATABASE_TABLE,contentValues,KEY_NOTEID+"=?",new String[]{String.valueOf(noteId)});
    }

    public long insertGroupInfo(long noteId,String groupJson){
        ContentValues contentValues =  new ContentValues();
        contentValues.put(KEY_NOTEID,noteId);
        contentValues.put(KEY_GROUPJSON,groupJson);
        return db.insert(DATABASE_TABLE,null,contentValues);
    }

    public NoteLabelListDBAdapter(Context ctx) {
        this.context = ctx;
        //this.set=set;
        DBHelper = new DatabaseHelper(context);
    }

    public NoteLabelListDBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public List<Integer> getNotesByLabel(String label) {
        List<Integer> list = new ArrayList<>();
        Cursor cur = db.query(DATABASE_TABLE,new String[]{KEY_NOTEID},KEY_GROUPJSON+" LIKE ?",new String[]{"%"+label+"%"},null,null,null);
        if(cur !=null && cur.moveToLast()){
            do{
                list.add(cur.getInt(cur.getColumnIndex(KEY_NOTEID)));
            }while (cur.moveToPrevious());
        }
        return list;
    }
    boolean isNoteIdIn(long id){
        Cursor c = db.query(DATABASE_TABLE,new String[]{KEY_ROWID},KEY_NOTEID+"=?",new String[]{String.valueOf(id)},null,null,null,"1");
        if(c.getCount() <1){
            return false;
        }
        return true;
    }
    public void updateLabels(long id, String labels) {
        if(isNoteIdIn(id)){
            ContentValues c = new ContentValues();
            c.put(KEY_GROUPJSON,labels);
            db.update(DATABASE_TABLE,c,KEY_NOTEID+"=?",new String[]{String.valueOf(id)
            });
        }else{
            ContentValues cv = new ContentValues();
            cv.put(KEY_NOTEID,id);
            cv.put(KEY_GROUPJSON,labels);
            db.insert(DATABASE_TABLE,null,cv);
        }
    }


}
