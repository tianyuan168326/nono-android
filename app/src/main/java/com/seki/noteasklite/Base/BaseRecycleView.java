package com.seki.noteasklite.Base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yuan on 2016/1/28.
 */
public class BaseRecycleView  extends RecyclerView{
    private View emptyView = null;
    private AdapterDataObserver emptyDataObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            Adapter adpter = getAdapter();
            if(adpter!=null && emptyView!=null && adpter.getItemCount() ==0){
                emptyView.setVisibility(View.VISIBLE);
                BaseRecycleView.this.setVisibility(View.GONE);
            }else if(emptyView!=null){
                emptyView.setVisibility(View.GONE);
                BaseRecycleView.this.setVisibility(View.VISIBLE);
            }else{
                BaseRecycleView.this.setVisibility(View.VISIBLE);
            }
        }
    };
    public void setEmptyView(View v){
        this.emptyView = v;
    }
    public BaseRecycleView(Context context) {
        super(context);
    }

    public BaseRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = this.getAdapter();
        if(oldAdapter !=null && emptyDataObserver !=null){
            oldAdapter.unregisterAdapterDataObserver(emptyDataObserver);
        }
        super.setAdapter(adapter);
        if(adapter !=null){
            adapter.registerAdapterDataObserver(emptyDataObserver);
        }
        emptyDataObserver.onChanged();
    }
}
