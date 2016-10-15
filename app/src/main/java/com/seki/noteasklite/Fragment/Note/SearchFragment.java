package com.seki.noteasklite.Fragment.Note;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seki.noteasklite.Adapter.Search.LocalNoteAdapter;
import com.seki.noteasklite.Base.BaseFragment;
import com.seki.noteasklite.DataUtil.BusEvent.SearchNoteEvent;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.Search.LocalNoteArray;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan-tian01 on 2016/4/6.
 */
public class SearchFragment extends BaseFragment {
    RecyclerView recyclerView;
    List<LocalNoteArray> noteAllArrayslist=new ArrayList<>();
    RecyclerView.Adapter searchAdapter;
    SearchLableFragment search_lable_fragment;
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(){
        SearchFragment contentFragment = new SearchFragment();
        return contentFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.frament_content, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=(RecyclerView)view.findViewById(R.id.frg_content_recyclerview);
        StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
       // recyclerView.setHasFixedSize(true);
        searchAdapter = new LocalNoteAdapter(getActivity(),noteAllArrayslist);
        recyclerView.setAdapter(searchAdapter);
        ((LocalNoteAdapter)searchAdapter).setOnRecyclerViewListener(new LocalNoteAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder v, int position, LocalNoteAdapter.ViewType viewType) {
                LocalNoteAdapter.GroupViewHolder  groupViewHolder = (LocalNoteAdapter.GroupViewHolder)v;
                Intent intent =  OpenNoteDelegate.start(getActivity(), new NoteAllArray(noteAllArrayslist.get(position).getNoteAllArray().title,
                        noteAllArrayslist.get(position).getNoteAllArray().content,
                        noteAllArrayslist.get(position).getNoteAllArray().group,
                        noteAllArrayslist.get(position).getNoteAllArray().date,
                        noteAllArrayslist.get(position).getNoteAllArray().time,
                        noteAllArrayslist.get(position).getNoteAllArray().sdfId,
                        noteAllArrayslist.get(position).getNoteAllArray().isOnCloud,
                        noteAllArrayslist.get(position).getNoteAllArray().uuid

                ),keyWord);
                screenTransitAnimByPair(intent, Pair.create(
                        (View) groupViewHolder.getNoteTitle(), R.id.note_detail_groupname
                        ),
                        Pair.create((View) groupViewHolder.getNoteDetail(), R.id.note_detail_content));
            }

            @Override
            public boolean onItemLongClick(RecyclerView.ViewHolder v, int position, LocalNoteAdapter.ViewType viewType) {
                return false;
            }
        });
        search_lable_fragment = SearchLableFragment.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.search_lable_fragment_container,search_lable_fragment).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    public void changeNoteSearchList(final List<LocalNoteArray> li){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                noteAllArrayslist.clear();
                noteAllArrayslist.addAll(li);
                searchAdapter.notifyDataSetChanged();
            }
        });
    }
    private String  keyWord  ="";
    public void onEvent(SearchNoteEvent event){
        changeNoteSearchList(event.getNoteList());
        keyWord = event.getKeyWord();
        search_lable_fragment.searchLable(keyWord);
    }
}
