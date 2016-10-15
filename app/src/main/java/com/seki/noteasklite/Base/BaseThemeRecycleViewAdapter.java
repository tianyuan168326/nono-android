package com.seki.noteasklite.Base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.seki.noteasklite.Controller.ThemeController;

/**
 * Created by yuan on 2016/4/29.
 */
public class BaseThemeRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public BaseThemeRecycleViewAdapter(){
        mainColor = ThemeController.getCurrentColor().mainColor;
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
    protected  int mainColor=-1;
    public void setThemeColor(int color){
        mainColor = color;
        notifyDataSetChanged();
    }

}
