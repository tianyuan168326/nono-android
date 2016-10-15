package com.seki.noteasklite.Widget.Basic.Provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.Activity.MainActivity;
import com.seki.noteasklite.Activity.Note.NoteEditActivity;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Widget.Basic.Service.NoteListWidgetService;

/**
 * Created by yuan on 2016/5/8.
 */
public class NoteListWidgetProvider  extends AppWidgetProvider {
    public static final String COLLECTION_VIEW_ACTION = "com.seki.noteasklite.Widget.Provider.COLLECTION_VIEW_ACTION";
    public static final String COLLECTION_VIEW_EXTRA = "com.seki.noteasklite.Widget.Provider.COLLECTION_VIEW_EXTRA";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i=0;i<appWidgetIds.length;i++){
            Intent intent = new Intent(context, NoteListWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_note_list);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.note_list, intent);
            rv.setEmptyView(R.id.note_list, R.layout.empty_view);
            //新建笔记
            Intent editIntent = new Intent(context, NoteEditActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,editIntent, 0);
            rv.setOnClickPendingIntent(R.id.add_new_note, pendingIntent);
            //启动应用
            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent launchPendingIntent = PendingIntent.getActivity(context, 0,launchIntent, 0);
            rv.setOnClickPendingIntent(R.id.title_bar, launchPendingIntent);
            //快速打开笔记
            Intent noteListIntent = new Intent();
            noteListIntent.setAction(COLLECTION_VIEW_ACTION);
            noteListIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent noteListPendingIntent = PendingIntent.getBroadcast(context, 0, noteListIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.note_list, noteListPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(COLLECTION_VIEW_ACTION)){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            //接受来自 getVIew的Intent
            try{
                NoteAllArray noteAllArray = new Gson().fromJson(intent.getStringExtra(COLLECTION_VIEW_EXTRA),new TypeToken<NoteAllArray>(){}.getType()) ;
                OpenNoteDelegate.start(context,noteAllArray,(String)null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else  if(intent.getAction().equals(NoteController.NoteChangBroadCast)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NoteListWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.note_list);
        }else{
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NoteListWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }

    }
}
