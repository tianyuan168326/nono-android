package com.seki.noteasklite.Activity.Note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.seki.noteasklite.Adapter.NoteAllItemAdapter;
import com.seki.noteasklite.Base.BaseAcitivityWithRecycleView;
import com.seki.noteasklite.Controller.NoteLabelController;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yuan on 2016/6/9.
 */
public class LabelNoteActivity extends BaseAcitivityWithRecycleView{
    NoteLabelBean noteLabelBean;
    List<NoteAllArray> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processIntent();
        setContentView(R.layout.activity_label_note,noteLabelBean==null?"":noteLabelBean.getLabel());
    }

    private void processIntent() {
        Intent intent = getIntent();
        noteLabelBean = intent.getParcelableExtra("noteLabelBean");
        noteLabelBean = noteLabelBean ==null?new NoteLabelBean():noteLabelBean;
    }

    @Override
    protected RecyclerView.Adapter setRecyclerViewAdapter() {
        return new NoteAllItemAdapter(this,list, ThemeController.getCurrentColor().getMainColor());
    }

    @Override
    protected void afterSetUpRecycleView() {
        super.afterSetUpRecycleView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                list.addAll(NoteLabelController.getNotesByLabel(noteLabelBean.getLabel()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recycleViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
        ((NoteAllItemAdapter)recycleViewAdapter).setOnRecyclerViewListener(new NoteAllItemAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v, int position) {
                OpenNoteDelegate.start(LabelNoteActivity.this,list.get(position));
            }

            @Override
            public boolean onItemLongClick(View v, int position) {
                return false;
            }
        });
    }

    @Override
    protected void registerWidget() {
        ( (SwipeRefreshLayout) $(R.id.recycle_view_refresher)).setEnabled(false);

    }
    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    public static void start(Context c,NoteLabelBean noteLabelBean) {
        Intent intent = new Intent();
        intent.setClass(c,LabelNoteActivity.class);
        intent.putExtra("noteLabelBean",noteLabelBean);
        c.startActivity(intent);
    }
}
