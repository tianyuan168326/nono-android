package com.seki.noteasklite.Base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yuan on 2015/9/18.
 */
public class BaseRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public BaseRecycleViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
