package com.seki.noteasklite.Fragment.Note;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.seki.noteasklite.Activity.MainActivity;
import com.seki.noteasklite.Adapter.NoteAllItemAdapter;
import com.seki.noteasklite.Base.BaseMultiChoiceRecycleViewAdapter;
import com.seki.noteasklite.Base.BaseRecycleView;
import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.Controller.RecycleBinController;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DBHelpers.NoteRecycleBinHelper;
import com.seki.noteasklite.DataUtil.BusEvent.FadeGoneMainFABEvent;
import com.seki.noteasklite.DataUtil.BusEvent.FadeVisibleMainFABEvent;
import com.seki.noteasklite.DataUtil.BusEvent.InsertRecycleBinEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteReloadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.RemoveAllRecycleBinEvent;
import com.seki.noteasklite.DataUtil.BusEvent.RemoveRecycleBinEvent;
import com.seki.noteasklite.DataUtil.BusEvent.RemoveRecycleBinNotes;
import com.seki.noteasklite.DataUtil.BusEvent.RestoreRecycleBinNotes;
import com.seki.noteasklite.DataUtil.BusEvent.ThemeColorPairChangedEvent;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Fragment.FluidBaseFragment;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2016/4/27.
 */
public class RecycleBinFragment extends FluidBaseFragment {


    private BaseRecycleView recyclerView;
    private NoteAllItemAdapter noteAdapter;
    private List<NoteAllArray> recycleBinNoteList;
    private SwipeRefreshLayout recycle_view_refresher;

    public RecycleBinFragment() {
    }
    public static RecycleBinFragment newInstance(String title){
        RecycleBinFragment allFragment = new RecycleBinFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragment_title", title);
        allFragment.setArguments(bundle);
        return allFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycleBinNoteList = NoteRecycleBinHelper.getInstance().getHistoryNote();
        noteAdapter = new NoteAllItemAdapter(this.getActivity(), recycleBinNoteList,ThemeController.getCurrentColor().getMainColor());
        installMultiChoiceMode();
        noteAdapter.setShowCloud(false);
        recyclerView = (BaseRecycleView) view.findViewById(R.id.recycle_view);
        recyclerView.setEmptyView($(R.id.empty_recycle_bin));
        recycle_view_refresher = (SwipeRefreshLayout) view.findViewById(R.id.recycle_view_refresher);
        recycle_view_refresher.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        recycle_view_refresher.setEnabled(false);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter );
        noteAdapter.setOnRecyclerViewListener(new NoteAllItemAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public boolean onItemLongClick(View v, int position) {
                return false;
            }
        });
        //begin set swipe-2-delete
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }
                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                //左滑恢复
                                for (int position:reverseSortedPositions){
                                    restoreNote(position);
                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                //右滑删除
                                for (final int position:reverseSortedPositions){
                                    deleteNote(position);
                                }
                            }
                        });
        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }
    ActionMode mode;
    private void installMultiChoiceMode() {
        noteAdapter.setMultiChoiceMode(true);
        noteAdapter.setOnModeChangedListener(new BaseMultiChoiceRecycleViewAdapter.OnChoiceModeChangedListener() {
            @Override
            public void onNoSelect() {
                if(mode !=null){
                    mode.finish();
                }
            }

            @Override
            public void onBeginSelect() {
                mode = getActivity().startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.long_click_menu_fragment_recyclebin,menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_select_all:
                                noteAdapter.selectAll();
                                if(mode != null){
                                    mode.finish();
                                }
                                return true;
                            case R.id.action_delete:
                                final List<Integer> positionList = new ArrayList<Integer>();

                                positionList.addAll(noteAdapter.getSelectedPositionList())  ;

                                new AlertDialog.Builder(getActivity())
                                        .setMessage("确定永久删除这些笔记？不可恢复！")
                                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                List<Long> idList  = new ArrayList<Long>();
                                                for (int position :
                                                        positionList) {
                                                    idList.add(recycleBinNoteList.get(position).sdfId);
                                                }
                                                RecycleBinController.removeRecycleBinNotes(idList);
                                            }
                                        }).show();
                                if(mode != null){
                                    mode.finish();
                                }
                                return true;
                            case R.id.action_recover:
                                List<Long> idList  = new ArrayList<Long>();
                                List<Integer> positionList1 = noteAdapter.getSelectedPositionList();
                                for (int position :
                                        positionList1) {
                                    idList.add(recycleBinNoteList.get(position).sdfId);
                                }
                                RecycleBinController.restoreRecycleBinNotes(idList);
                                if(mode != null){
                                    mode.finish();
                                }
                                return true;
                        }
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        noteAdapter.unSelectAll();
                    }
                });
            }
        });
    }

    @Override
    public BaseRecycleViewAdapter getRecycleViewAdapter() {
        return noteAdapter;
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    private void deleteNote(final int position) {
        final NoteAllArray backUp = recycleBinNoteList.get(position);
        final long id = (recycleBinNoteList.get(position)).sdfId;
        recycleBinNoteList.remove(position);
        noteAdapter.notifyDataSetChanged();

        Snackbar snackBar = Snackbar.make(((MainActivity)getActivity()).getFloatingActionButton(),"正在永久删除这条笔记",Snackbar.LENGTH_LONG);
        snackBar.setAction("撤销", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycleBinNoteList.add(position,backUp);
                noteAdapter.notifyDataSetChanged();
            }
        });
        snackBar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                EventBus.getDefault().post(new FadeGoneMainFABEvent());

                if (event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_TIMEOUT || event ==
                        DISMISS_EVENT_CONSECUTIVE) {
                    RecycleBinController.removeRecycleBinNote(id);
                }

            }
        });
        EventBus.getDefault().post(new FadeVisibleMainFABEvent());
        snackBar.show();
    }

    private void restoreNote(int position) {
        long id = (recycleBinNoteList.get(position)).sdfId;
        RecycleBinController.restoreRecycleBinNote(id);
    }

    public void onEvent(InsertRecycleBinEvent e){
        recycleBinNoteList.add(new NoteAllArray(e.getArray(),e.getId()));
        noteAdapter.notifyDataSetChanged();
    }
    public void onEvent(RemoveRecycleBinEvent e){
        for(NoteAllArray array:recycleBinNoteList){
            if(array.sdfId == e.getId()){
                recycleBinNoteList.remove(array);
            }
        }
        noteAdapter.notifyDataSetChanged();
    }
    public void onEvent(RemoveRecycleBinNotes e){
        List<NoteAllArray> removingList = new ArrayList<>();
        for(NoteAllArray array:recycleBinNoteList){
            for (long id:e.recycleNoteIDs){
                if(array.sdfId == id){
                    removingList.add(array);
                }
            }
        }
        for (NoteAllArray a :
                removingList) {
            recycleBinNoteList.remove(a);
        }
        noteAdapter.notifyDataSetChanged();
    }
    public void onEvent(RestoreRecycleBinNotes e){
        List<NoteAllArray> restoringList = new ArrayList<>();
        for(NoteAllArray array:recycleBinNoteList){
            for (long id:e.idList){
                if(array.sdfId == id){
                    restoringList.add(array);
                }
            }
        }
        for (NoteAllArray array:
                restoringList ) {
            recycleBinNoteList.remove(array);
        }
        noteAdapter.notifyDataSetChanged();
    }
    public void onEvent(RemoveAllRecycleBinEvent e){
        recycleBinNoteList.clear();
        noteAdapter.notifyDataSetChanged();
    }
    public void onEvent(ThemeColorPairChangedEvent e){
        noteAdapter.setThemeMain(e.getCurrentColorPair().mainColor);
    }


    @SuppressWarnings("unused")
    public void onEvent(NoteReloadEvent event){
        recycleBinNoteList.clear();
        recycleBinNoteList.addAll(NoteRecycleBinHelper.getInstance().getHistoryNote());
        noteAdapter.notifyDataSetChanged();
    }
}
