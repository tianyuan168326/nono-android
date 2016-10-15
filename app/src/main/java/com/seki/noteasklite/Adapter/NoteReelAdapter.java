package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.DataUtil.NoteReelArray;
import com.seki.noteasklite.Fragment.NONoPreferenceFragment;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.DisplayUtil;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.FuckBreaker;

import java.util.List;

/**
 * Created by 七升 on 2015/9/10.
 */
public class NoteReelAdapter extends BaseRecycleViewAdapter {
	public static interface OnRecyclerViewListener {
		void onItemClick(View v, int position);
		boolean onItemLongClick(View v, int position);
	}

	private OnRecyclerViewListener onRecyclerViewListener;

	public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
		this.onRecyclerViewListener = onRecyclerViewListener;
	}

	private List<NoteReelArray> list;
	private Context context;

	public NoteReelAdapter(Context context, List<NoteReelArray> list) {
		this.list = list;
		this.context=context;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
		final GroupViewHolder holder = (GroupViewHolder) viewHolder;
		holder.position = i;
		final NoteReelArray array=list.get(i);
				try{
					Log.d(NONoConfig.TAG_NONo,new Gson().toJson(array));
					String reelImageUri = array.getUriString();
					Log.d(NONoConfig.TAG_NONo,reelImageUri);

					FrescoImageloadHelper.LoadFullImageFromURL(holder.reelView,reelImageUri);

				}catch (Exception e){
					e.printStackTrace();
				}

		String reelAbstract  = FuckBreaker.fuckBreakerAndSpace(array.reel_abstract);
		if(TextUtils.isEmpty(reelAbstract)){
			holder.newestNote.setVisibility(View.GONE);
		}else{
			holder.newestNote.setVisibility(View.VISIBLE);
			holder.newestNote.setText(reelAbstract);
		}
		holder.noteCounts.setText(" "+array.reel_note_num+" Notes");
		holder.groupName.setText(array.reel_title);
		holder.newestNote.requestLayout();
		holder.itemView.requestLayout();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_content
				, viewGroup,false);
		ViewGroup.MarginLayoutParams p  =(ViewGroup.MarginLayoutParams)view.getLayoutParams();
		if(AppPreferenceUtil.getGroupStyle() == GroupListStyle.TABLE){
			p.leftMargin  = DisplayUtil.dip2px(MyApp.getInstance().getApplicationContext(),2);
			p.rightMargin  = DisplayUtil.dip2px(MyApp.getInstance().getApplicationContext(),2);
		}else  if(AppPreferenceUtil.getGroupStyle() == GroupListStyle.LIST){
			p.leftMargin  = DisplayUtil.dip2px(MyApp.getInstance().getApplicationContext(),0);
			p.rightMargin  = DisplayUtil.dip2px(MyApp.getInstance().getApplicationContext(),0);
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
		public SimpleDraweeView reelView;
		public TextView groupName;
		public TextView noteCounts;
		public TextView newestNote;
		public GroupViewHolder(View itemView) {
			super(itemView);
			reelView = (SimpleDraweeView)itemView.findViewById(R.id.bg_image);
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
