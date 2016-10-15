package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.Bean.NoteDateItemArray;
import com.seki.noteasklite.Fragment.NONoPreferenceFragment;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FuckBreaker;

import java.util.List;

/**
 * Created by 七升 on 2015/9/10.
 */
public class NoteDateItemAdapter extends BaseRecycleViewAdapter {


    public void setThemeMain(int mainColor) {
        this.mainColor = mainColor;
        notifyDataSetChanged();
    }

    public static interface OnRecyclerViewListener {
		void onItemClick(View v, int position);
		boolean onItemLongClick(View v, int position);
	}

	private OnRecyclerViewListener onRecyclerViewListener;

	public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
		this.onRecyclerViewListener = onRecyclerViewListener;
	}

	private List<NoteDateItemArray> list;
	private Context context;

	public NoteDateItemAdapter(Context context, List<NoteDateItemArray> list) {
		this.list = list;
		this.context=context;
		if(ThemeController.getCurrentColor().accentColor !=-1){
			mainColor = ThemeController.getCurrentColor().mainColor;
		}
	}
	int mainColor = -1;
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
		DateViewHolder holder = (DateViewHolder) viewHolder;
		holder.position = i;
		NoteDateItemArray array=list.get(i);
        holder.noteTitle.setText(array.title);
        if(mainColor !=-1){
            holder.noteTitle.setTextColor(mainColor);
        }
        holder.noteDateAndTime.setText(array.time);
        holder.noteDetail.setText(FuckBreaker.fuckBreakerAndSpace(array.detail));
        holder.groupName.setText(array.group);
        holder.noteDetail.requestLayout();
		holder.itemView.requestLayout();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_all_note_item
				, viewGroup,false);
            //if(i==0){
            //    StaggeredGridLayoutManager.LayoutParams lp=(StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams();
            //    lp.setMargins(ABTextUtil.dip2px(viewGroup.getContext(), 8),ABTextUtil.dip2px(viewGroup.getContext(),8),ABTextUtil.dip2px(viewGroup.getContext(),8),ABTextUtil.dip2px(viewGroup.getContext(),8));
            //    view.setLayoutParams(lp);
            //}
		return new DateViewHolder(view) ;
	}

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
	public int getItemCount() {
		return list.size();
	}
	class DateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public int position;
        public TextView noteTitle;
        public TextView groupName;
        public TextView noteDateAndTime;
        public TextView noteDetail;
        public ImageView noteCloudFlag;
		public DateViewHolder(View itemView) {
			super(itemView);
            noteTitle = (TextView)itemView.findViewById(R.id.layout_all_note_item_title);

            groupName = (TextView) itemView.findViewById(R.id.layout_all_note_item_group);
            noteDateAndTime = (TextView) itemView.findViewById(R.id.layout_all_note_item_time);
            noteDetail = (TextView) itemView.findViewById(R.id.layout_all_note_item_abstract);
			noteDetail.setMaxLines(NONoPreferenceFragment.getCardLineNum());
            noteCloudFlag = (ImageView)itemView.findViewById(R.id.layout_all_note_item_cloud);
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
}
