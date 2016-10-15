package com.seki.noteasklite.ViewHolder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.seki.noteasklite.R;


/**
 * Created by yuan on 2015/9/18.
 */
public class FabGroupViewHolder extends RecyclerView.ViewHolder  {
    public int position;
    public TextView textView;
    public FloatingActionButton fab;

    public FabGroupViewHolder(View itemView) {
        super(itemView);
        textView=(TextView)itemView.findViewById(R.id.fab_menu_text);
        fab=(FloatingActionButton)itemView.findViewById(R.id.fab_menu_btn);
    }

}