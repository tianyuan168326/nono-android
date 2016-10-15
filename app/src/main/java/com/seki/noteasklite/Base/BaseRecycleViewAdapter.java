package com.seki.noteasklite.Base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by yuan on 2015/9/18.
 */
public abstract class BaseRecycleViewAdapter extends RecyclerView.Adapter {

    int marginLeft = -1;
    int marginRight = -1;
    int marginTop  = -1;
    int marginBottom = -1;
    public void setItemMargin(int top,int left,int bottom,int right){
        marginTop = top;
        marginLeft = left;
        marginBottom = bottom;
        marginRight = right;
    }
    public void clearItemMargin(){
        marginTop = -1;
        marginLeft = -1;
        marginBottom = -1;
        marginRight = -1;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewGroup.LayoutParams p =  holder.itemView.getLayoutParams();
        ((ViewGroup.MarginLayoutParams)(p)).setMargins(
                marginLeft ==-1?((ViewGroup.MarginLayoutParams)(p)).leftMargin:marginLeft,
                marginTop ==-1?((ViewGroup.MarginLayoutParams)(p)).topMargin:marginTop,
                marginRight ==-1?((ViewGroup.MarginLayoutParams)(p)).rightMargin:marginRight,
                marginBottom ==-1?((ViewGroup.MarginLayoutParams)(p)).bottomMargin:marginBottom
        );
    }
}
