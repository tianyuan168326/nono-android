package com.seki.noteasklite.Fragment.Note;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.seki.noteasklite.Activity.Note.LabelNoteActivity;
import com.seki.noteasklite.Adapter.NoteLabelListAdapter;
import com.seki.noteasklite.Base.BaseRecycleView;
import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Controller.NoteLabelController;
import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;
import com.seki.noteasklite.DataUtil.BusEvent.AddLabelEvent;
import com.seki.noteasklite.DataUtil.BusEvent.AddNewLabelEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteLabelSearchDoneEvent;
import com.seki.noteasklite.Fragment.FluidBaseFragment;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2016/6/8.
 */
public class LabelFragment  extends FluidBaseFragment{
    EditText et_search_label;
    BaseRecycleView recycleView;
    List<NoteLabelBean > list = new ArrayList<>();
    NoteLabelListAdapter adapter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((SwipeRefreshLayout)$(R.id.recycle_view_refresher)).setEnabled(false);
        et_search_label = $(R.id.et_search_label);
        recycleView = $(R.id.recycle_view);
        adapter = new NoteLabelListAdapter(list);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.addAll(NoteLabelController.getHistoryLabels());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
        adapter.setOnRecyclerViewListener(new NoteLabelListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(list.get(position).getNoteNum() == 0){
                    MyApp.toast("没有笔记哦");
                    return ;
                }
                LabelNoteActivity.start(getActivity(),list.get(position));
            }

            @Override
            public boolean onItemLongClick(View v, int position) {

                return false;
            }
        });
        setUpEvent();
    }

    @Override
    public BaseRecycleViewAdapter getRecycleViewAdapter() {
        return adapter;
    }

    @Override
    public RecyclerView getRecycleView() {
        return recycleView;
    }

    private void setUpEvent() {

        ((EditText)$(R.id.et_search_label)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                NoteController.searchLabel(s.toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_label,container,false);
    }
    public void onEvent(NoteLabelSearchDoneEvent event){
        list.clear();
        if(event.labels == null){
            list.addAll(NoteLabelController.getHistoryLabels());
        }else{
            list.addAll(event.labels);
        }
        adapter.notifyDataSetChanged();
    }
    public void onEvent(AddNewLabelEvent e){
            list.add(0,e.noteLabelBean);
            adapter.notifyDataSetChanged();
    }
    public void onEvent(AddLabelEvent e){
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.edit,null,false);
        final EditText edit =  (EditText)root.findViewById(R.id.edit_view);
        new AlertDialog.Builder(getActivity())
                .setView(root)
                .setMessage("新的标签")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(edit.getText().toString().trim())){
                            MyApp.toast("标签不能为空!");
                            return;
                        }
                        NoteLabelController.addLabel(edit.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
