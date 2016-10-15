package com.seki.noteasklite.Fragment.Ask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seki.noteasklite.Activity.Ask.AnswerDetailHTMLActivity;
import com.seki.noteasklite.Activity.Ask.InnerQuestionActivity;
import com.seki.noteasklite.Adapter.NoteAllItemAdapter;
import com.seki.noteasklite.Adapter.Search.LocalNoteAdapter;
import com.seki.noteasklite.Adapter.SearchAdapter;
import com.seki.noteasklite.Base.BaseFragment;
import com.seki.noteasklite.DataUtil.BusEvent.SearchNoteEvent;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.Search.LocalNoteArray;
import com.seki.noteasklite.DataUtil.SearchArray;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.NetWorkUtil;
import com.seki.noteasklite.Util.TimeLogic;

import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends BaseFragment {
	RecyclerView recyclerView;
	List<SearchArray> searchArrayslist=new ArrayList<>();
	List<LocalNoteArray> noteAllArrayslist=new ArrayList<>();
	RecyclerView.Adapter searchAdapter;
	public static final  int TYPE_NOTE=0;
	public static final int TYPE_CONTENT=1;
	public static final int TYPE_TOPIC=2;
	public static final int TYPE_USER=3;
	public ContentFragment() {
		// Required empty public constructor
	}

	public static ContentFragment newInstance(int fragment_type){
		ContentFragment contentFragment = new ContentFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", fragment_type);
		contentFragment.setArguments(bundle);
		return contentFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (Integer.valueOf(getArguments().getInt("type"))){
			case TYPE_NOTE:
				searchAdapter = new LocalNoteAdapter(getActivity(),noteAllArrayslist);
				break;
			default:
				searchAdapter=new SearchAdapter(getActivity(),searchArrayslist);
				break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view= inflater.inflate(R.layout.frament_content, container, false);
		recyclerView=(RecyclerView)view.findViewById(R.id.frg_content_recyclerview);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(searchAdapter);
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();

	}
	public void changeNoteSearchList(final List<LocalNoteArray> li){
		this.noteAllArrayslist=li;
		searchAdapter = new LocalNoteAdapter(getActivity(),noteAllArrayslist);
		final List<LocalNoteArray> list = noteAllArrayslist;
		((NoteAllItemAdapter)searchAdapter).setOnRecyclerViewListener(new NoteAllItemAdapter.OnRecyclerViewListener() {
			@Override
			public void onItemClick(View v, int position) {
				Intent intent = OpenNoteDelegate.start(getActivity(),
						new NoteAllArray(list.get(position).getNoteAllArray().title,
								list.get(position).getNoteAllArray().content,
								list.get(position).getNoteAllArray().group,
								list.get(position).getNoteAllArray().date,
								list.get(position).getNoteAllArray().time,
								list.get(position).getNoteAllArray().sdfId,
								list.get(position).getNoteAllArray().isOnCloud,
								list.get(position).getNoteAllArray().uuid

						));
				screenTransitAnimByPair(intent,
//						Pair.create(
//						(View) groupViewHolder.getNoteTitle(), R.id.note_detail_groupname
//						),
						Pair.create(v, R.id.note_detail_content));
			}

			@Override
			public boolean onItemLongClick(View v, int position) {
				return false;
			}
		});
		recyclerView.setAdapter(searchAdapter);
		searchAdapter.notifyDataSetChanged();
	}
	public void changeNormalSearchList(final List<SearchArray> li){
		this.searchArrayslist=li;
		searchAdapter=new SearchAdapter(getActivity(),this.searchArrayslist);

		//单项点击事件
		((SearchAdapter)searchAdapter).setOnRecyclerViewListener(new SearchAdapter.OnRecyclerViewListener() {
			@Override
			public void onItemClick(View v, int position, int type) {
				if(type==TYPE_CONTENT){
					switch(searchArrayslist.get(position).contentCategory)
					{
						case CONTENT_ANSWER:
							Intent intentToAnswerdDetail = new Intent();

							intentToAnswerdDetail.putExtra("key_id",searchArrayslist.get(position).Id);
							intentToAnswerdDetail.setClass(ContentFragment.this.getActivity(), AnswerDetailHTMLActivity.class);
							ContentFragment.this.getActivity().startActivity(intentToAnswerdDetail);
							break;
						case CONTENT_QUESTION:
							Intent intent = new Intent();
							intent.putExtra("questionId", ((String) searchArrayslist.get(position).Id));
							intent.putExtra("question_raise_time", TimeLogic.timeLogic(searchArrayslist.get(position).questionRaiserTime));
							intent.putExtra("question_inner_category", ((String) searchArrayslist.get(position).questionInnerCategory));
							intent.putExtra("question_outer_category", ((String) searchArrayslist.get(position).questionOuterCategory));
							intent.putExtra("question_title", ((String) searchArrayslist.get(position).contentTitle));
							intent.setClass(getActivity(), InnerQuestionActivity.class);
							startActivity(intent);
							break;
					}
				}
//				else if(type==TYPE_TAG){
//					Intent intent = new Intent();
//					if(list.get(position).tag.contains(" - ")){
//						intent.putExtra("category_name", list.get(position).tag.substring(list.get(position).tag.indexOf(" - ")+" - ".length())).
//								putExtra("category_enum", "inner_category").
//								setClass(getActivity(), CategorySumActivity.class);
//					}else {
//						intent.putExtra("category_name", list.get(position).tag).
//								putExtra("category_enum", "outer_category").
//								setClass(getActivity(), CategorySumActivity.class);
//					}
//					getActivity().startActivity(intent);
//				}
				else if(type==TYPE_USER){
					NetWorkUtil.verifyWhichActivityToSwitch(getActivity(),
							MyApp.getInstance().userInfo.userId,
							searchArrayslist.get(position).Id);
				}
			}

			@Override
			public boolean onItemLongClick(View v, int position, int type) {
				return false;
			}
		});
		recyclerView.setAdapter(searchAdapter);
		searchAdapter.notifyDataSetChanged();
	}
	public void onEvent(SearchNoteEvent event){
		changeNoteSearchList(event.getNoteList());
	}
}
