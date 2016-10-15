package com.seki.noteasklite.Fragment.Note;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.Activity.MainActivity;
import com.seki.noteasklite.Activity.Note.NoteEditActivity;
import com.seki.noteasklite.Activity.Note.NoteReelEditActivity;
import com.seki.noteasklite.Activity.Note.ReelNoteActivity;
import com.seki.noteasklite.Adapter.NoteReelAdapter;
import com.seki.noteasklite.Base.BaseRecycleView;
import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.NoteReelsController;
import com.seki.noteasklite.DBHelpers.NoteReelsDBHelper;
import com.seki.noteasklite.DataUtil.BusEvent.AddReelEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ChangeNoteGroupEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteDeleteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteInsertEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteReloadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateGroupName;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateReelEvent;
import com.seki.noteasklite.DataUtil.NoteReelArray;
import com.seki.noteasklite.Fragment.FluidBaseFragment;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2016/5/4.
 */
public class ReelFragment extends FluidBaseFragment {
    private BaseRecycleView recyclerView;
    private NoteReelAdapter noteAdapter;
    private List<NoteReelArray> list;
    public ReelFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.recycle_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        $(R.id.loading_bg).setVisibility(View.VISIBLE);
        if(getView().findViewById(R.id.recycle_view_refresher)!= null){
            ((SwipeRefreshLayout)getView().findViewById(R.id.recycle_view_refresher))
                    .setEnabled(false);
        }
        list=new ArrayList<>();

        noteAdapter=new NoteReelAdapter(this.getActivity(),list);
        recyclerView=(BaseRecycleView)view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setEmptyView($(R.id.empty_note));
        $(R.id.loading_bg).setVisibility(View.VISIBLE);
        final FloatingActionButton floatingActionButton = ((MainActivity)getActivity()).getFloatingActionButton();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                MainActivity.toggleFABAnimation(getActivity(), dy, floatingActionButton,true);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        noteAdapter.setOnRecyclerViewListener(new NoteReelAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v, int position) {
                try{
                    MyApp.getCache().reelImageCache = new WeakReference<Bitmap>(
                            ( (BitmapDrawable)((SimpleDraweeView)v.findViewById(R.id.banner))
                                    .getDrawable()).getBitmap()
                    );
                }catch (Exception e){
                }

                Intent intent = ReelNoteActivity.start(true,
                        getActivity(),
                        list.get(position).reel_title,
                        list.get(position).reel_title_pic,
                        list.get(position).reel_note_num
                );
//                ActivityOptionsCompatICS options = ActivityOptionsCompatICS.makeSceneTransitionAnimation(
//                        getActivity(),
//                        Pair.create((View)v, R.id.bg_image));
//                ActivityCompatICS.startActivity(getActivity(), intent, options.toBundle());
            }

            @Override
            public boolean onItemLongClick(final View v,final int position) {
                AlertDialog.Builder control=new AlertDialog.Builder(getActivity());
                String[] items =new String[4];
                items[0]="新建笔记";
                items[1]="删除文集";
                items[2]="编辑文集";
                items[3]="查看笔记";

                control.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                NoteEditActivity.start(getActivity(),list.get(position).reel_title);
                                break;
                            case 1:
                                NoteReelsController.deleteReels(new String[]{list.get(position).reel_title});
                                refresh();
                                EventBus.getDefault().post(new NoteReloadEvent());
                                break;
                            case 2:
                                NoteReelEditActivity.start(getActivity(),list.get(position));

                                break;
                            case 3:onItemClick(v,position);break;
                        }
                    }
                });
                control.show();
                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(NONoConfig.TAG_NONo,"begin run ...");
                list.addAll(NoteReelsDBHelper.getInstance().getAllReelArray());
                Log.d(NONoConfig.TAG_NONo,"after list add all ...");

                recyclerView.post (new Runnable() {
                    @Override
                    public void run() {
                        $(R.id.loading_bg).setVisibility(View.GONE);
                        noteAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public BaseRecycleViewAdapter getRecycleViewAdapter() {
        return noteAdapter;
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    public void refresh() {
        new RefreshTask().execute(list);
    }

    class RefreshTask extends AsyncTask<List<NoteReelArray>,List<NoteReelArray>,List<NoteReelArray>> {
        @Override
        protected List<NoteReelArray> doInBackground(List<NoteReelArray>... params) {
            params[0].clear();
            for (NoteReelArray key : NoteReelsDBHelper.getInstance().getAllReelArray()) {
                        params[0].add(key);
                    }
            return params[0];
        }
        @Override
        protected void onPostExecute(List<NoteReelArray> NoteSimpleArrays) {
            super.onPostExecute(NoteSimpleArrays);
            noteAdapter.notifyDataSetChanged();
        }
    }
//    @SuppressWarnings("unused")
//    public void onEvent(Object noteEvent){
//        refresh();
//    }
    @SuppressWarnings("unused")
    public void onEvent(UpdateGroupName event){
            for (NoteReelArray array :
                    list) {
                if(array.reel_title.equals(event.getOldG())){
                    array.reel_title = event.getNewG();
                }
            }
            noteAdapter.notifyDataSetChanged();
    }
    @SuppressWarnings("unused")
    public void onEvent(ChangeNoteGroupEvent event){
        refresh();
    }
    @SuppressWarnings("unused")
    public void onEvent(UpdateReelEvent event){
        for (NoteReelArray array :
                list) {
            if(array.id == event.id){
                array.reel_title = event.noteReelArray.reel_title;
                array.reel_abstract = event.noteReelArray.reel_abstract;
                array.reel_title_pic = event.noteReelArray.reel_title_pic;
                noteAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
    @SuppressWarnings("unused")
    public void onEvent(NoteInsertEvent event){
        refresh();
    }

    @SuppressWarnings("unused")
    public void onEvent(AddReelEvent event){
        list.add(event.getArray());
        noteAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void onEvent(NoteReloadEvent event){
        refresh();
    }

    public void onEvent(NoteDeleteEvent event){
        for (NoteReelArray array:
             list) {
            if(
            array.reel_title.equals(event.getNoteDatabaseArray().group
                    )){
                array.reel_note_num-=1;
                noteAdapter.notifyItemChanged(list.indexOf(array));
                break;
            }
        }
    }
}
