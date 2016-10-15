package com.seki.noteasklite.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yuan on 2016/6/8.
 */
public class NoteLabelDBAdapter {
    public static final String TABLE = "labels";
    public static final String KEY_ID = "_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_NOTE_NUM = "note_num";
    public static final String KEY_CREATE_TIME = "create_time";
    private final Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase  db;
    private List<NoteLabelBean> historyLabels;

    public NoteLabelDBAdapter(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
    }
    public NoteLabelDBAdapter open() throws SQLException{
        db = helper.getWritableDatabase();
        return this;
    }
    public void close(){
        db.close();
    }

    public List<NoteLabelBean> getHistoryLabels() {
        List<NoteLabelBean> list = new ArrayList<>();
        Cursor c = db.query(TABLE,new String[]{KEY_ID,KEY_LABEL,KEY_NOTE_NUM,KEY_CREATE_TIME},null,null,null,null,null);
        if(c!=null && c.moveToLast()){
            do{
                list.add(new NoteLabelBean(
                        c.getLong(c.getColumnIndex(KEY_ID)),
                        c.getString(c.getColumnIndex(KEY_LABEL)),
                        c.getInt(c.getColumnIndex(KEY_NOTE_NUM)),
                        c.getString(c.getColumnIndex(KEY_CREATE_TIME))
                ));
            }while (c.moveToPrevious());
        }
        return list;
    }

    public List<String> getHistoryLabelNames() {
        List<String> list = new ArrayList<>();
        Cursor c = db.query(TABLE,new String[]{KEY_LABEL},null,null,null,null,null);
        if(c!=null && c.moveToLast()){
            do{
                list.add(
                        c.getString(c.getColumnIndex(KEY_LABEL))
                );
            }while (c.moveToPrevious());
        }
        return list;
    }

    public long addLabel(String label) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_LABEL,label);
        cv.put(KEY_NOTE_NUM,0);
        cv.put(KEY_CREATE_TIME,String.valueOf(System.currentTimeMillis()));
        return db.insert(TABLE,null,cv);
    }

    public void labelsRemoveNote(List<String> labs) {
        for (String lab :
                labs) {
        db.execSQL("update "+TABLE+" set "+KEY_NOTE_NUM+" = "+KEY_NOTE_NUM+" -1"+" where "
        +KEY_LABEL+"=?",new String[]{lab}
        );
        }
    }

    public void labelsAddNote(Collection<? extends String> labs) {
        for (String lab :
                labs) {
            db.execSQL("update "+TABLE+" set "+KEY_NOTE_NUM+" = "+KEY_NOTE_NUM+" +1"+" where "
                    +KEY_LABEL+"=?",new String[]{lab}
            );
        }
    }
}