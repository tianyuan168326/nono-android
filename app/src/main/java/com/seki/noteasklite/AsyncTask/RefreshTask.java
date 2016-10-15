package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.seki.noteasklite.DBHelpers.NoteDBAdapter;
import com.seki.noteasklite.DataUtil.Bean.NoteDateItemArray;

import java.util.List;

/**
 * Created by yuan on 2015/10/27.
 */
public class RefreshTask extends AsyncTask<List<NoteDateItemArray>,List<NoteDateItemArray>,Cursor> {
    private Context context;
    private RecyclerView.Adapter recycleViewAdapter;
    private String date;
    public RefreshTask(Context context,RecyclerView.Adapter recycleViewAdapter){
        this.context = context;
        this.recycleViewAdapter = recycleViewAdapter;
    }
    public RefreshTask setDate(String date){
        this.date = date;
        return this;
    }
    @Override
    protected Cursor doInBackground(List<NoteDateItemArray>... params) {
        params[0].clear();
        NoteDBAdapter noteDBAdapter=new NoteDBAdapter(context);
        noteDBAdapter.open();
        //Cursor cursor =noteDBAdapter.getTitleByDate(context.getIntent().getStringExtra("Date"));
        Cursor cursor =noteDBAdapter.getTitleByDate(this.date);
        if(cursor.getCount()>0&&cursor.moveToLast()){
            do{
                params[0].add(new NoteDateItemArray(cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_TITLE))
                        ,cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_TIME))
                        , cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_CONTENT))
                        , cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_GROUP))
                        , cursor.getInt(cursor.getColumnIndex(NoteDBAdapter.KEY_ROWID))
                        ,cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_IS_ON_CLOUD))
                        ,cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_UUID))
                ));
            }while (cursor.moveToPrevious());
        }
        noteDBAdapter.close();
        return cursor;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        recycleViewAdapter.notifyDataSetChanged();
        ((AppCompatActivity)context).getSupportActionBar().setSubtitle(cursor.getCount() + " Notes");
    }
}

