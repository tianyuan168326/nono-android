package com.seki.noteasklite.Widget.Basic.Service;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.seki.noteasklite.Widget.Basic.Provider.NoteListProvider;

/**
 * Created by yuan on 2016/5/8.
 */
public class NoteListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new NoteListProvider(this.getApplicationContext(), intent));
    }

}