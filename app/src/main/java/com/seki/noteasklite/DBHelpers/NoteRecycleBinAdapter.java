package com.seki.noteasklite.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

/**
 * Created by yuan on 2016/4/27.
 */
public class NoteRecycleBinAdapter {
    public static final String KEY_ROWID="_id";
    public static final String KEY_GROUP="groups";
    public static final String KEY_DATE="date";
    public static final String KEY_TIME="time";
    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_UUID="uuid";
    private static final String DATABASE_TABLE="recycle_notes";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public NoteRecycleBinAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public NoteRecycleBinAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertRecycleNote(NoteDatabaseArray array) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_GROUP, array.group);
        initialValues.put(KEY_DATE, array.date);
        initialValues.put(KEY_TIME, array.time);
        initialValues.put(KEY_TITLE,array.Title);
        initialValues.put(KEY_CONTENT, array.content);
        if(TextUtils.isEmpty(array.uuid)){
            array.uuid = String.valueOf(System.currentTimeMillis());
        }
        initialValues.put(KEY_UUID, array.uuid);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    public void insertRecycleNoteHelper(NoteDatabaseArray array) {
        this.open();
        this.insertRecycleNote(array);
        this.close();
    }
    public boolean deleteRecycleNote(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteRecycleNoteByDate(String date) {
        return db.delete(DATABASE_TABLE, KEY_DATE + "=" + "'" + date + "'", null) > 0;
    }
    public boolean deleteRecycleNoteeByGroup(String group) {
        return db.delete(DATABASE_TABLE, KEY_GROUP + "=" + "'" + group + "'", null) > 0;
    }

    public Cursor getAllRecycleNotes() {
        return db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_GROUP, KEY_DATE, KEY_TIME, KEY_TITLE,KEY_CONTENT,KEY_UUID},
                null, null, null, null, null);
    }

    public Cursor getRecycleNoteById(long rowid) throws SQLException {
        Cursor mCursor = db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_GROUP, KEY_DATE, KEY_TIME, KEY_TITLE,KEY_CONTENT,KEY_UUID},
                KEY_ROWID + "=" + "'" + rowid + "'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToLast();
        }
        return mCursor;
    }

    public void deleteAllRecycleNote() {
        db.delete(DATABASE_TABLE,KEY_ROWID+">0",new String[]{});
    }
}
