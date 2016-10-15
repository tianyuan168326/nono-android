package com.seki.noteasklite.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.seki.noteasklite.DataUtil.NoteDatabaseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 七升 on 2015/9/13.
 */
public class NoteDBAdapter {

	public static final String KEY_ROWID="_id";
	public static final String KEY_GROUP="groups";
	public static final String KEY_DATE="date";
	public static final String KEY_TIME="time";
	public static final String KEY_TITLE="title";
	public static final String KEY_CONTENT="content";
	public static final String KEY_IS_ON_CLOUD="is_on_cloud";
	public static final String KEY_NOTIFY="notify";
	public static final String KEY_UUID="uuid";

	private static final String DATABASE_TABLE="notes";


	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public void updateGroup(String oldG, String newG) {
		String sql = "update notes set groups=? where groups=?";
		db.execSQL(sql,new String[]{newG,oldG});
	}



	public NoteDBAdapter(Context ctx) {
		this.context = ctx;
		//this.set=set;
		DBHelper = new DatabaseHelper(context);
	}

	public NoteDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}
	public int updateNoteById(long id,NoteDatabaseArray array){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GROUP, array.group);
		initialValues.put(KEY_DATE, array.date);
		initialValues.put(KEY_TIME, array.time);
		initialValues.put(KEY_TITLE,array.Title);
		initialValues.put(KEY_CONTENT, array.content);
		initialValues.put(KEY_IS_ON_CLOUD, array.is_on_cloud);
		initialValues.put(KEY_NOTIFY, array.notifyTime);
		//add uuid to mark the note,just an timestamp
		if(TextUtils.isEmpty(array.uuid)){
			array.uuid  = String.valueOf(System.currentTimeMillis());
		}
		initialValues.put(KEY_UUID, array.uuid);
		return db.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=?", new String[]{String.valueOf(id)});
	}
	public long insertTitle(NoteDatabaseArray array) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GROUP, array.group);
		initialValues.put(KEY_DATE, array.date);
		initialValues.put(KEY_TIME, array.time);
		initialValues.put(KEY_TITLE,array.Title);
		initialValues.put(KEY_CONTENT, array.content);
		initialValues.put(KEY_IS_ON_CLOUD, array.is_on_cloud);
		initialValues.put(KEY_NOTIFY, array.notifyTime);
		if(NoteDatabaseArray.MARK_FIRST_IN.equals(array.notifyTime)  ||NoteDatabaseArray.MARK_FIRST_IN2.equals(array.notifyTime) ){
			array.uuid = "0";
		}
        if(TextUtils.isEmpty(array.uuid)){
            array.uuid = String.valueOf(System.currentTimeMillis());
        }
		initialValues.put(KEY_UUID, array.uuid);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	public void insertTtileHelper(NoteDatabaseArray array) {
		this.open();
		this.insertTitle(array);
		this.close();
	}
	public boolean deleteTitle(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean deleteTitleByDate(String date) {
		return db.delete(DATABASE_TABLE, KEY_DATE + "=" + "'" + date + "'", null) > 0;
	}
	public boolean deleteTitleByReel(String group) {
		return db.delete(DATABASE_TABLE, KEY_GROUP + "=" + "'" + group + "'", null) > 0;
	}

	public Cursor getAllTitles() {
		return db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_GROUP, KEY_DATE, KEY_TIME, KEY_TITLE,KEY_CONTENT,KEY_IS_ON_CLOUD,KEY_NOTIFY,KEY_UUID},
                null, null, null, null, null);
	}

	public Cursor getTitleById(long rowid) throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_GROUP, KEY_DATE, KEY_TIME, KEY_TITLE,KEY_CONTENT,KEY_IS_ON_CLOUD,KEY_NOTIFY,KEY_UUID},
				KEY_ROWID + "=" + "'" + rowid + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToLast();
		}
		return mCursor;
	}

	public Cursor getTitleByReel(String group) throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_GROUP, KEY_DATE, KEY_TIME, KEY_TITLE,KEY_CONTENT,KEY_IS_ON_CLOUD,KEY_NOTIFY,KEY_UUID},
				KEY_GROUP + "=" + "'" + group + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToLast();
		}
		return mCursor;
	}

	public Cursor getTitleByDate(String date) throws SQLException {
		Cursor mCursor = db.query( DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_GROUP, KEY_DATE, KEY_TIME, KEY_TITLE,KEY_CONTENT,KEY_IS_ON_CLOUD,KEY_NOTIFY,KEY_UUID},
				KEY_DATE + "=" + "'" + date + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToLast();
		}
		return mCursor;
	}

	public List<String> getDates(){
		List<String> list=new ArrayList<>();
		Cursor cursor=db.query(true,DATABASE_TABLE, new String[]{KEY_DATE}, null, null, null, null, null, null);
		if (cursor!=null&&cursor.moveToLast()){
			do{
				list.add(cursor.getString(0));
			}while (cursor.moveToPrevious());
		    cursor.close();
        }
			//db.execSQL("select count(*) from (select distinct "+KEY_GROUP+" from "+DATABASE_TABLE+" )",arg);
		return list;
	}

	public List<String> getGroups(){
		List<String> list=new ArrayList<>();
		Cursor cursor=db.query(true, DATABASE_TABLE, new String[]{KEY_GROUP}, null, null, null, null, null, null);
		if (cursor!=null&&cursor.moveToLast()){
			do{
				list.add(cursor.getString(0));
			}while (cursor.moveToPrevious());
            cursor.close();
		}
		//db.execSQL("select count(*) from (select distinct "+KEY_GROUP+" from "+DATABASE_TABLE+" )",arg);
		return list;
	}


	public int changeGroup(long sdfId, String currentGroup) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_GROUP,currentGroup);
		return db.update(DATABASE_TABLE,contentValues,KEY_ROWID+"=?",new String[]{String.valueOf(sdfId)});
	}

	public Cursor getNoteById(long id) {
		return db.query(DATABASE_TABLE,new String[]{KEY_ROWID, KEY_GROUP, KEY_DATE, KEY_TIME, KEY_TITLE,KEY_CONTENT,KEY_IS_ON_CLOUD,KEY_NOTIFY,KEY_UUID},
				KEY_ROWID+"=?",new String[]{String.valueOf(id)},null,null,null,"1");
	}
}
