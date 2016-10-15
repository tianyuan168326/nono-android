package com.seki.noteasklite.Adapter.Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by yuan-tian01 on 2016/4/16.
 */
public class LableAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<String> lableList ;

    public LableAdapter(Context context, ArrayList<String> lableList) {
        this.context = context;
        this.lableList = lableList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
