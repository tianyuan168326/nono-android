package com.seki.noteasklite.Widget.Basic.Provider;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2016/5/8.
 */
public class NoteListProvider implements RemoteViewsService.RemoteViewsFactory {
    List<NoteAllArray> noteList = new ArrayList<>();
    private Context context = null;
    private int appWidgetId;
    public NoteListProvider(Context applicationContext, Intent intent) {
        this.context = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        populateListItem();
    }
    private void populateListItem() {
        noteList = NoteDBHelper.getInstance().getHistoryNote();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        noteList = NoteDBHelper.getInstance().getHistoryNote();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.note_widget_row);
        NoteAllArray item =noteList.get(i);
        remoteView.setTextViewText(R.id.note_text, Html.fromHtml( item.content,null,null).toString().trim().replace("\n",""));
        // 设置 第position位的“视图”对应的响应事件
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(NoteListWidgetProvider.COLLECTION_VIEW_EXTRA,new Gson().toJson(item) );
        remoteView.setOnClickFillInIntent(R.id.note_text, fillInIntent);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
