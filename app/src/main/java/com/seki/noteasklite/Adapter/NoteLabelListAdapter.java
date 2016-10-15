package com.seki.noteasklite.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.CustomControl.TintImageView;
import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;
import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.DisplayUtil;

import java.util.List;

/**
 * Created by yuan on 2016/6/9.
 */
public class NoteLabelListAdapter extends BaseRecycleViewAdapter {
    List<NoteLabelBean> list;
    public  interface OnRecyclerViewListener {
        void onItemClick(View v, int position);
        boolean onItemLongClick(View v, int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    public NoteLabelListAdapter(List<NoteLabelBean> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_item,parent,false);
        ViewGroup.MarginLayoutParams p  =(ViewGroup.MarginLayoutParams)view.getLayoutParams();
        if(AppPreferenceUtil.getGroupStyle() == GroupListStyle.TABLE){
            p.leftMargin  = DisplayUtil.dip2px(MyApp.getInstance().getApplicationContext(),2);
            p.rightMargin  = DisplayUtil.dip2px(MyApp.getInstance().getApplicationContext(),2);
        }else  if(AppPreferenceUtil.getGroupStyle() == GroupListStyle.LIST){
            p.leftMargin  = DisplayUtil.dip2px(MyApp.getInstance().getApplicationContext(),0);
            p.rightMargin  = DisplayUtil.dip2px(MyApp.getInstance().getApplicationContext(),0);
        }

        return new LabelViewHolder(view);
    }
     class LabelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
         public int position;
        public TextView label_name;
        public TextView label_note_num;
        public TintImageView label_icon;
        public LabelViewHolder(View itemView) {
            super(itemView);
            label_name = (TextView)itemView.findViewById(R.id.label_name);
            label_note_num = (TextView)itemView.findViewById(R.id.label_note_num);
            label_icon = (TintImageView)itemView.findViewById(R.id.label_icon);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(v,position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(v,position);
            }
            return false;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LabelViewHolder entityHolder = (LabelViewHolder) holder;
        entityHolder.position = position;
        entityHolder.label_name.setText(list.get(position).getLabel()
        );
        entityHolder.label_note_num.setText(String.valueOf(list.get(position).getNoteNum()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
