package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.DataUtil.SearchArray;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FrescoImageloadHelper;

import java.util.List;

/**
 * Created by 七升 on 2015/8/3.
 */

/**
 * void bindUser  需要修改
 */
public class SearchAdapter extends RecyclerView.Adapter{

	private Context context;
	private int TYPE_CONTENT=0;
	private int TYPE_TAG=1;
	private int TYPE_USER=2;
	private List<SearchArray> list;


	public static interface OnRecyclerViewListener {
		void onItemClick(View v, int position, int type);
		boolean onItemLongClick(View v, int position, int type);
	}

	private OnRecyclerViewListener onRecyclerViewListener;

	public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
		this.onRecyclerViewListener = onRecyclerViewListener;
	}

	public SearchAdapter(Context context, List<SearchArray> list){
		this.context=context;
		this.list=list;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
		if(viewHolder instanceof ContentHolder){
			bindContent(viewHolder,i);
		}else if(viewHolder instanceof TagHolder){
			bindTag(viewHolder, i);
		}else if(viewHolder instanceof UserHolder){
			bindUser(viewHolder,i);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if(list.get(position)!=null){
			return
					list.get(position).searchType;
		}else{
			return -1;
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		if(i==TYPE_CONTENT){
			view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_content, null);
			view.setLayoutParams(lp);
			return new ContentHolder(view);
		}else if(i==TYPE_TAG){
			view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_tag, null);
			view.setLayoutParams(lp);
			return new TagHolder(view);
		}else if(i==TYPE_USER){
			view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_user, null);
			view.setLayoutParams(lp);
			return new UserHolder(view);
		}
		return null;
	}

	class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public int position;
		public TextView tvTitle;
		private TextView tvContent;

		public ContentHolder (View itemView) {
			super(itemView);
			tvTitle=(TextView)itemView.findViewById(R.id.search_content_title);
			tvContent=(TextView)itemView.findViewById(R.id.search_content_content);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (null != onRecyclerViewListener) {
				onRecyclerViewListener.onItemClick(v,position,0);
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if (null != onRecyclerViewListener) {
				return onRecyclerViewListener.onItemLongClick(v,position,0);
			}
			return false;
		}
	}

	class TagHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public int position;
		public TextView textView;

		public TagHolder (View itemView) {
			super(itemView);
			textView=(TextView)itemView.findViewById(R.id.search_tag);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (null != onRecyclerViewListener) {
				onRecyclerViewListener.onItemClick(v,position,1);
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if (null != onRecyclerViewListener) {
				return onRecyclerViewListener.onItemLongClick(v,position,1);
			}
			return false;
		}
	}

	class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public int position;
		public SimpleDraweeView imageView;
		public TextView tvName;
		public TextView tvAbstract;

		public UserHolder (View itemView) {
			super(itemView);
			imageView=(SimpleDraweeView)itemView.findViewById(R.id.search_user_headpic);
			tvName=(TextView)itemView.findViewById(R.id.search_user_name);
			tvAbstract=(TextView)itemView.findViewById(R.id.search_user_abstract);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (null != onRecyclerViewListener) {
				onRecyclerViewListener.onItemClick(v,position,2);
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if (null != onRecyclerViewListener) {
				return onRecyclerViewListener.onItemLongClick(v,position,2);
			}
			return false;
		}
	}

	private void bindContent(RecyclerView.ViewHolder viewHolder,int i){
		ContentHolder holder=(ContentHolder)viewHolder;
		holder.position=i;
		holder.tvTitle.setText(list.get(i).contentTitle);
		holder.tvContent.setText(list.get(i).contentDetail);
	}

	private void bindTag(RecyclerView.ViewHolder viewHolder,int i){
		TagHolder holder=(TagHolder)viewHolder;
		holder.position=i;
		holder.textView.setText(list.get(i).tag);
	}

	private void bindUser(RecyclerView.ViewHolder viewHolder,int i){
		UserHolder holder=(UserHolder)viewHolder;
		holder.position=i;
		holder.tvName.setText(list.get(i).userName);
		holder.tvAbstract.setText(list.get(i).userAbstract);

		//此处需要获取头像并更新
		//holder.imageView.setImageResource(R.drawable.ic_launcher);
		FrescoImageloadHelper.simpleLoadImageFromURL(holder.imageView, list.get(i).userHeadImgRUL);
	}
}
