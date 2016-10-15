package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seki.noteasklite.Activity.Note.ReelNoteActivity.NoteReelItemArray;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.Fragment.NONoPreferenceFragment;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.DisplayUtil;
import com.seki.noteasklite.Util.FuckBreaker;

import java.util.List;

/**
 * Created by 七升 on 2015/9/10.
 */
public class NoteGroupItemAdapter extends RecyclerView.Adapter {

	public static interface OnRecyclerViewListener {
		void onItemClick(View v, int position);
		boolean onItemLongClick(View v, int position);
	}

	private OnRecyclerViewListener onRecyclerViewListener;

	public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
		this.onRecyclerViewListener = onRecyclerViewListener;
	}

	private List<NoteReelItemArray> list;
	private Context context;

	public NoteGroupItemAdapter(Context context, List<NoteReelItemArray> list, int mainColor) {
		this.list = list;
		this.context=context;
		this.mainColor =mainColor;
	}
	public NoteGroupItemAdapter(Context context,List<NoteReelItemArray> list) {
		this.list = list;
		this.context=context;
		if(ThemeController.getCurrentColor().accentColor !=-1){
			mainColor = ThemeController.getCurrentColor().mainColor;
		}
	}
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
		GroupViewHolder holder = (GroupViewHolder) viewHolder;
		holder.position = i;
		NoteReelItemArray array=list.get(i);
        holder.noteTitle.setText(array.title);
		if(mainColor !=-1){
			holder.noteTitle.setTextColor(mainColor);
		}
        holder.noteDateAndTime.setText(array.date + " " + array.time);
        holder.noteDetail.setText(FuckBreaker.fuckBreakerAndSpace(array.detail));
        holder.groupName.setText("");
        holder.noteDetail.requestLayout();
		holder.itemView.requestLayout();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_all_note_item
				, viewGroup,false);
		ViewGroup.MarginLayoutParams p  =(ViewGroup.MarginLayoutParams)view.getLayoutParams();
		if(AppPreferenceUtil.getGroupStyle() == GroupListStyle.TABLE){
			p.leftMargin  = DisplayUtil.dip2px(context,2);
			p.rightMargin  = DisplayUtil.dip2px(context,2);
		}else  if(AppPreferenceUtil.getGroupStyle() == GroupListStyle.LIST){
			p.leftMargin  = DisplayUtil.dip2px(context,0);
			p.rightMargin  = DisplayUtil.dip2px(context,0);
		}
		return new GroupViewHolder(view) ;
	}

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
	public int getItemCount() {
		return list.size();
	}
	class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public int position;
        public TextView noteTitle;
        public TextView groupName;
        public TextView noteDateAndTime;
        public TextView noteDetail;
        public ImageView noteCloudFlag;
		public GroupViewHolder(View itemView) {
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
	int mainColor = -1;
	//just chang attr,need invalidating
	public void setThemeMain(int color){
		mainColor = color;
		notifyDataSetChanged();
	}
}
