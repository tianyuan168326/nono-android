package com.seki.noteasklite.Fragment.Note;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seki.noteasklite.Adapter.Search.LocalNoteAdapter;
import com.seki.noteasklite.Base.BaseFragment;
import com.seki.noteasklite.DBHelpers.NoteLabelDBHelper;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.Search.LocalNoteArray;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.next.tagview.TagCloudView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchLableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchLableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchLableFragment extends BaseFragment {


    public SearchLableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchLableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchLableFragment newInstance() {
        SearchLableFragment fragment = new SearchLableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_lable, container, false);
    }
    View root;
    View lable_hint;
    TagCloudView lable_cloud_view;
    View lable_note_list_container;
    RecyclerView lable_note_list;
    CircleImageView lable_note_back;
    TextView tag_title;
    TextView lable_hint_text;
    List<LocalNoteArray> noteList = new ArrayList<>();
    LocalNoteAdapter localNoteAdapter ;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = $(R.id.root);
        lable_hint = $(R.id.lable_hint);
        lable_cloud_view = $(R.id.lable_cloud_view);
        lable_note_list_container = $(R.id.lable_note_list_container);
        lable_note_list = $(R.id.lable_note_list);
        tag_title = $(R.id.tag_title);
        lable_note_back = $(R.id.lable_note_back);
        lable_hint_text = $(R.id.lable_hint_text);
//        hideAll();

        registerRecycleView();
        lable_cloud_view.setTags(searchedLable);
        lable_cloud_view.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                enterLableTag(searchedLable.get(position));
            }
        });

        lable_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLableTags();
            }
        });

        lable_note_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLableTags();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllLable();
    }

    private void registerRecycleView() {
        localNoteAdapter = new LocalNoteAdapter(getActivity(),noteList);
        localNoteAdapter.setOnRecyclerViewListener(new LocalNoteAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder v, int position, LocalNoteAdapter.ViewType viewType) {
                LocalNoteAdapter.GroupViewHolder  groupViewHolder = (LocalNoteAdapter.GroupViewHolder)v;
                Intent intent  = OpenNoteDelegate.start(getActivity(),
                        new NoteAllArray(noteList.get(position).getNoteAllArray().title,
                                noteList.get(position).getNoteAllArray().content,
                                noteList.get(position).getNoteAllArray().group,
                                noteList.get(position).getNoteAllArray().date,
                                noteList.get(position).getNoteAllArray().time,
                                noteList.get(position).getNoteAllArray().sdfId,
                                noteList.get(position).getNoteAllArray().isOnCloud,
                                noteList.get(position).getNoteAllArray().uuid
                        ));
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
        lable_note_list.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
                );
        lable_note_list.setAdapter(localNoteAdapter);
    }

    private void enterLableTag(String lable) {
        showNoteList();
        tag_title.setText(lable+" 下的笔记");
        noteList.clear();
        List<NoteAllArray> noteAllArrayList =  NoteLabelDBHelper.getInstance().getNoteArrayByTag(lable);
        for (NoteAllArray array:
                noteAllArrayList) {
            LocalNoteArray local = new LocalNoteArray();
            local.setNoteAllArray(array);
            noteList.add(local);
        }
        localNoteAdapter.notifyDataSetChanged();
    }

    private void showNoteList() {
        root.setVisibility(View.VISIBLE);
        lable_hint.setVisibility(View.GONE);
        lable_note_list_container.setVisibility(View.VISIBLE);
        lable_cloud_view.setVisibility(View.GONE);
    }



    private void showLableTags() {
        root.setVisibility(View.VISIBLE);
        lable_hint.setVisibility(View.GONE);
        lable_note_list_container.setVisibility(View.GONE);
        lable_cloud_view.setVisibility(View.VISIBLE);
        lable_cloud_view.setTags(searchedLable);
    }
//    private void hideAll() {
//        root.setVisibility(View.VISIBLE);
//        lable_hint.setVisibility(View.VISIBLE);
//        lable_note_list_container.setVisibility(View.GONE);
//        lable_cloud_view.setVisibility(View.GONE);
//    }
//    private void collapseTagView() {
//        root.setVisibility(View.VISIBLE);
//        lable_hint.setVisibility(View.VISIBLE);
//        lable_cloud_view.setVisibility(View.GONE);
//    }

    List<String > searchedLable = new ArrayList<>();
    public void searchLable(String lable){
      //  HashSet<String > groupSet = (HashSet<String >) NoteGroupController.getGroupList();
        HashMap<Double,String> distanceStringPair = new HashMap<>();
//        for (String group :
//                groupSet) {
//            Double dis = 0.0;
//            if((dis = SimilarityUtil.sim(lable,group))>0.1){
//                distanceStringPair.put(dis,group);
//            }
//        }
        searchedLable.clear();
        for (Map.Entry<Double,String> entry:
                distanceStringPair.entrySet() ) {
            searchedLable.add(entry.getValue());
        }
        syncUI();
    }
    private void getAllLable(){
     //   HashSet<String > groupSet = (HashSet<String >) NoteGroupController.getGroupList();
        searchedLable.clear();
//        for (String entry:
//                groupSet ) {
//            searchedLable.add(entry);
//        }
        syncUI();
    }
    private void syncUI() {
        if(searchedLable.size() == 0){
            root.setVisibility(View.GONE);
        }else{
            root.setVisibility(View.VISIBLE);
//            if(searchedLable.size() == NoteGroupController.getGroupList().size()){
//                lable_hint_text.setText("要查看所有标签?");
//            }else{
//                lable_hint_text.setText("已经搜索到 " +String.valueOf(searchedLable.size()) +" 条相关标签");
//            }
            lable_hint.setVisibility(View.VISIBLE);
            lable_note_list_container.setVisibility(View.GONE);
            lable_cloud_view.setVisibility(View.GONE);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
