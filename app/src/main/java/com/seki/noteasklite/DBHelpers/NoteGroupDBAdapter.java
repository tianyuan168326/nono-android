package com.seki.noteasklite.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.seki.noteasklite.Config.NONoConfig;

/**
 * Created by yuan-tian01 on 2016/4/16.
 */
public class NoteGroupDBAdapter {
    public static final  String KEY_ROWID="_id";
    public static final  String KEY_NOTEID="note_id";
    public static final  String KEY_GROUPJSON="group_json";

    private static final String DATABASE_NAME="note";
    private static final String DATABASE_TABLE="notegroups";

    private static final int DATABASE_VERSION=1;

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

    public NoteGroupDBAdapter(Context ctx) {
        this.context = ctx;
        //this.set=set;
        DBHelper = new DatabaseHelper(context);
    }

    public NoteGroupDBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }
}
