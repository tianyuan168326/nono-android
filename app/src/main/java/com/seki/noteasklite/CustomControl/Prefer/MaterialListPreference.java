package com.seki.noteasklite.CustomControl.Prefer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2016/6/17.
 */
public class MaterialListPreference extends ListPreference {
    public static class ColorPreferPair{
       public  String colorEntry;
        public String colorValue;

        public ColorPreferPair(String colorEntry, String colorValue) {
            this.colorEntry = colorEntry;
            this.colorValue = colorValue;
        }
    }
    private android.support.v7.app.AlertDialog.Builder mBuilder;
    private Context context;

    public MaterialListPreference(Context context) {
        super(context);
        this.context = context;
    }

    public MaterialListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void showDialog(Bundle state) {
        mBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        mBuilder.setTitle(getTitle());
        mBuilder.setIcon(getDialogIcon());
        int colorSkemeNum = getEntries().length;
        List<ColorPreferPair> pairList = new ArrayList<>();
        for(int i = 0;i<colorSkemeNum;i++){
            pairList.add(new ColorPreferPair(getEntries()[i].toString(),getEntryValues()[i].toString()));
        }
        ListView listView = new ListView(getContext());
        listView.setDivider(null);
        listView.setDividerHeight(0);
        ColorPreferAdapter adapter=new ColorPreferAdapter(pairList);
        adapter.setOnItemClickListener(new ColorPreferAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, View v) {
                if (position >= 0 && getEntryValues() != null) {
                    String value = getEntryValues()[position].toString();
                    if (callChangeListener(value))
                        setValue(value);
                }
            }
        });
        listView.setAdapter(adapter);
        mBuilder.setView(listView);

        mBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
//        mBuilder.negativeText(getNegativeButtonText());
//        mBuilder.items(getEntries());
//        mBuilder.itemsCallback(new MaterialDialog.ListCallback() {
//            @Override
//            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
//                onClick(null, DialogInterface.BUTTON_POSITIVE);
//                dialog.dismiss();
//
//                if (which >= 0 && getEntryValues() != null) {
//                    String value = getEntryValues()[which].toString();
//                    if (callChangeListener(value))
//                        setValue(value);
//                }
//            }
//        });
//
//        final View contentView = onCreateDialogView();
//        if (contentView != null) {
//            onBindDialogView(contentView);
//            mBuilder.customView(contentView);
//        }
//        else
//            mBuilder.content(getDialogMessage());

        mBuilder.show();
    }

}
