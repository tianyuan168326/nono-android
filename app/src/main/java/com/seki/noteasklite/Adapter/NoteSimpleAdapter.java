package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.DataUtil.NoteSimpleArray;
import com.seki.noteasklite.Fragment.NONoPreferenceFragment;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FuckBreaker;
import com.wangjie.androidbucket.utils.ABTextUtil;

import java.util.List;

/**
 * Created by 七升 on 2015/9/10.
 */
public class NoteSimpleAdapter extends BaseRecycleViewAdapter {
	public static interface OnRecyclerViewListener {
		void onItemClick(View v, int position);
		boolean onItemLongClick(View v, int position);
	}

	private OnRecyclerViewListener onRecyclerViewListener;

	public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
		this.onRecyclerViewListener = onRecyclerViewListener;
	}

	private List<NoteSimpleArray> list;
	private Context context;

	public NoteSimpleAdapter(Context context, List<NoteSimpleArray> list) {
		this.list = list;
		this.context=context;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
		GroupViewHolder holder = (GroupViewHolder) viewHolder;
		holder.position = i;
		NoteSimpleArray array=list.get(i);
		holder.newestNote.setText(FuckBreaker.fuckBreakerAndSpace(array.newestNote));
		holder.noteCounts.setText(" "+array.counts+" Notes");
		holder.groupName.setText(array.title);
		holder.newestNote.requestLayout();
		holder.itemView.requestLayout();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_content
				, viewGroup,false);
        //if(i==0){
        //    StaggeredGridLayoutManager.LayoutParams lp=(StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams();
        //    lp.setMargins(ABTextUtil.dip2px(viewGroup.getContext(),8),ABTextUtil.dip2px(viewGroup.getContext(),8),ABTextUtil.dip2px(viewGroup.getContext(),8),ABTextUtil.dip2px(viewGroup.getContext(),8));
        //    view.setLayoutParams(lp);
        //}
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
		public TextView groupName;
		public TextView noteCounts;
		public TextView newestNote;
		public GroupViewHolder(View itemView) {
			super(itemView);
			groupName=(TextView)itemView.findViewById(R.id.layout_name);
			noteCounts=(TextView)itemView.findViewById(R.id.layout_counts);
			newestNote=(TextView)itemView.findViewById(R.id.layout_newest);
			newestNote.setMaxLines(NONoPreferenceFragment.getCardLineNum());
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
