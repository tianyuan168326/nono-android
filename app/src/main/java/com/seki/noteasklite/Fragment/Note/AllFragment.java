package com.seki.noteasklite.Fragment.Note;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.seki.noteasklite.Adapter.NoteAllItemAdapter;
import com.seki.noteasklite.Base.BaseMultiChoiceRecycleViewAdapter;
import com.seki.noteasklite.Base.BaseRecycleView;
import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Controller.NoteReelsController;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.Bean.AllNoteListBean;
import com.seki.noteasklite.DataUtil.Bean.DeleteNoteBean;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;
import com.seki.noteasklite.DataUtil.BusEvent.BeginNoteUploadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ChangeNoteGroupEvent;
import com.seki.noteasklite.DataUtil.BusEvent.DoneImportNoteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteDeleteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteInsertEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteReloadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteUpdateEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteUploadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NotesDeleteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ThemeColorPairChangedEvent;
import com.seki.noteasklite.DataUtil.BusEvent.TransCommunityEvent;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateGroupName;
import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.DataUtil.LogStateEnum;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.Fragment.FluidBaseFragment;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.NetWorkServices.NoteService;
import com.seki.noteasklite.NetWorkServices.ServiceFactory;
import com.seki.noteasklite.R;
import com.seki.noteasklite.RetrofitHelper.RequestBody.AuthBody;
import com.seki.noteasklite.Util.PdfConverter;
import com.seki.noteasklite.Util.ShareUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.greenrobot.event.EventBus;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends FluidBaseFragment {

    private BaseRecycleView recyclerView;
    private NoteAllItemAdapter noteAdapter;
    private List<NoteAllArray> bindingNoteList;
    private SwipeRefreshLayout recycle_view_refresher;

    public AllFragment() {
    }
    public static AllFragment newInstance(String title){
        AllFragment allFragment = new AllFragment();
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
    ActionMode actionMode ;
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingNoteList  = new ArrayList<>();
//        bindingNoteList = NoteDBHelper.getInstance().getHistoryNote();
        noteAdapter = new NoteAllItemAdapter(this.getActivity(), bindingNoteList, ThemeController.getCurrentColor().getMainColor());
        noteAdapter.setMultiChoiceMode(true);
        noteAdapter.setOnModeChangedListener(new BaseMultiChoiceRecycleViewAdapter.OnChoiceModeChangedListener() {
            @Override
            public void onNoSelect() {
                if(actionMode != null){
                    actionMode.finish();
                }
            }
            @Override
            public void onBeginSelect() {
                actionMode = getActivity().startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                         mode.getMenuInflater().inflate(R.menu.long_click_menu_fragment_all,menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.action_delete:
                                    List<DeleteNoteBean> delList = new ArrayList<DeleteNoteBean>();
                                    for (int position :
                                            noteAdapter.getSelectedPositionList() ) {
                                        delList.add(new DeleteNoteBean(
                                                bindingNoteList.get(position).sdfId,
                                                bindingNoteList.get(position).group,
                                                bindingNoteList.get(position).uuid
                                        ));

                                    }
                                    NoteController.deleteNote(
                                            delList.toArray(new DeleteNoteBean[1] )
                                    );
                                    if(actionMode != null){
                                        actionMode.finish();
                                    }
                                    return true;
                                case R.id.action_export_pdf:
                                    MyApp.toast("由于导出PDF极不稳定，正在维护！");
                                    if(actionMode != null){
                                        actionMode.finish();
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        noteAdapter.unSelectAll();
                    }
                });
            }
        });
        recyclerView = (BaseRecycleView) view.findViewById(R.id.recycle_view);

        recycle_view_refresher = (SwipeRefreshLayout) view.findViewById(R.id.recycle_view_refresher);
        recycle_view_refresher.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        recycle_view_refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            // true to go on
            if (!beforeRefresh()) {
                return;
            }
            NoteService noteService = ServiceFactory.getNoteService();
            HashMap<String,String> params = AuthBody.getAuthBodyMap();
            params.put("user_id",MyApp.getInstance().userInfo.userId);
            noteService.getAllNote(params ) .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .map(new Func1<AllNoteListBean, AllNoteListBean>() {
                     @Override
                     public AllNoteListBean call(AllNoteListBean allNoteListBean) {
                         WonderFull.verify(allNoteListBean);
                         return allNoteListBean;
                     }
                 })
                    .subscribe(
                            new Action1<AllNoteListBean>() {
                                @Override
                                public void call(AllNoteListBean allNoteListBean) {
                                    onGetNoteList(allNoteListBean);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Log.e(NONoConfig.TAG_NONo, throwable.toString());
                                    afterRefreshFailed();
                                }
                            }
                    );
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        addCoolAnimToRecycleView(recyclerView, noteAdapter);
        noteAdapter.setOnRecyclerViewListener(new NoteAllItemAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (v instanceof ImageView) {

                } else  {
                    OpenNoteDelegate.start(getActivity(),bindingNoteList.get(position),
                            position
                    );
                }
            }

            @Override
            public boolean onItemLongClick(View v, int position) {
                return false;
            }
        });
        installSwipeTouchListener();
        $(R.id.loading_bg).setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bindingNoteList .addAll(NoteDBHelper.getInstance().getHistoryNote()) ;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        $(R.id.loading_bg).setVisibility(View.INVISIBLE);
                        noteAdapter.notifyDataSetChanged();
                        recyclerView.setEmptyView($(R.id.empty_note));
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

    private void goCommunity() {
        EventBus.getDefault().post(new TransCommunityEvent());
    }

    private void installSwipeTouchListener() {
        if(style == GroupListStyle.LIST){
            //begin set swipe-2-delete
            SwipeableRecyclerViewTouchListener swipeTouchListener =
                    new SwipeableRecyclerViewTouchListener(recyclerView,
                            new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                @Override
                                public boolean canSwipeLeft(int position) {
                                    return false;
                                }

                                @Override
                                public boolean canSwipeRight(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {

                                }

                                @Override
                                public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        NoteAllArray item = bindingNoteList.get(position);
                                        bindingNoteList.remove(position);
                                        noteAdapter.notifyItemRemoved(position);
                                        NoteController.deleteNote(
                                                new DeleteNoteBean(
                                                        item.sdfId,item.group,item.uuid
                                                )

                                        );
                                        Snackbar.make(getView(),"已删除此笔记",Snackbar.LENGTH_SHORT).show();
                                    }
                                    noteAdapter.notifyDataSetChanged();
                                }
                            });
            recyclerView.addOnItemTouchListener(swipeTouchListener);
        }
    }

    private void parseXml2Pdf(String title, String content) {
        final AlertDialog.Builder dialogB = new AlertDialog.Builder(getActivity())
                .setTitle("云端转换富文本->pdf中...")
                .setView(R.layout.layout_more_progress);
        final AlertDialog[] dialog = new AlertDialog[1];
        PdfConverter.convert(title, content, new PdfConverter.OnGetPdfListener() {

            @Override
            public void onGetPdfFile(String path) {
                if(path == null){
                    dialogB.setTitle("云端转换失败!");
                    dialog[0].dismiss();
                    dialog[0] = dialogB.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog[0].dismiss();
                        }
                    },300);
                }else{
                    dialog[0].dismiss();
                    Toast.makeText(getActivity(),"Pdf文件保存在"+path,Toast.LENGTH_LONG).show();
                    ShareUtil.shareFile(getActivity(),path,"");
                }
            }

            @Override
            public void prepareGetPdfFile() {
                dialog[0] = dialogB.show();
            }
        });
    }

    public static void writeFile(String fileName, String fileContent)
    {
        try
        {
            File f = new File(fileName);
            if (!f.exists())
            {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"gbk");
            BufferedWriter writer=new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void onGetNoteList(AllNoteListBean allNoteListBean) {
        if (allNoteListBean == null) {
            afterRefreshFailed();
            return;
        }
        if (allNoteListBean.getState_code() < 0) {
            afterRefreshFailed();
        } else if (allNoteListBean.getState_code() == 0) {
            joinLocalCloud(allNoteListBean);
            EventBus.getDefault().post(new NoteReloadEvent());
//            joinLocalCloudGroupInfo(allNoteListBean);
        }
    }

    private void joinLocalCloudGroupInfo(AllNoteListBean noteList) {
        HashSet<String> noteGroupSet = new HashSet<String>() ;
        for (AllNoteListBean.DataEntity entity:
                noteList.getData()) {
            NoteReelsController.reelAddNote(entity.getNote_group(),1);
        }
        EventBus.getDefault().post(new NoteReloadEvent());
    }


    private void addCoolAnimToRecycleView(RecyclerView recyclerView, NoteAllItemAdapter noteAdapter) {
        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(noteAdapter);
        alphaAdapter.setDuration(200);
        alphaAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAdapter.setFirstOnly(false);
        recyclerView.setAdapter(alphaAdapter );
        recyclerView.setItemAnimator(new SlideInUpAnimator(new LinearOutSlowInInterpolator()));
    }

    private void joinLocalCloud(AllNoteListBean cloudNoteListBean) {

        int newNoteNum = 0;
        for (NoteAllArray note:
                bindingNoteList) {
            for(int i=0;i<cloudNoteListBean.getData().size();i++){
                if(note.uuid .equals(cloudNoteListBean.getData().get(i).getNote_uuid())){
                    cloudNoteListBean.getData().remove(cloudNoteListBean.getData().get(i));
                }
            }
        }
        for (AllNoteListBean.DataEntity noteBean :
                cloudNoteListBean.getData()){
            long id = NoteDBHelper.getInstance().insertNote(new NoteDatabaseArray(
                    noteBean.getNote_group(),
                    noteBean.getNote_date(),
                    noteBean.getNote_time(),
                    noteBean.getNote_title(),
                    noteBean.getNote_content(),
                    "true",
                    "",
                    noteBean.getNote_uuid()
            ));
            NoteAllArray newNote = new NoteAllArray(
                    noteBean.getNote_title(),
                    noteBean.getNote_content(),
                    noteBean.getNote_group(),
                    noteBean.getNote_date(),
                    noteBean.getNote_time(),
                    id,
                    "true",
                    noteBean.getNote_uuid()
            );
            bindingNoteList.add(0,newNote);
            newNoteNum++;
        }
        if(newNoteNum!=0){
            noteAdapter.notifyItemInserted(0);
            noteAdapter.notifyItemRangeChanged(0, newNoteNum);
        }
        if (recycle_view_refresher.isRefreshing()){
            recycle_view_refresher.setRefreshing(false);
        }
    }

    private void afterRefreshFailed() {
        if(recycle_view_refresher.isRefreshing()){
            recycle_view_refresher.setRefreshing(false);
        }
    }

    private boolean beforeRefresh() {
        if( LogStateEnum.OFFLINE.equals(MyApp.getInstance().userInfo.logStateEnum)){
            Toast.makeText(getActivity(),"请登录NONo(左侧菜单)以保存云笔记",Toast.LENGTH_LONG).show();
            if(recycle_view_refresher.isRefreshing()){
                recycle_view_refresher.setRefreshing(false);
            }
            return false;
        }
        recycle_view_refresher.setRefreshing(true);
        return true;
    }
    public void refrshListFromLocalDB(){
          Observable.create(
                  new Observable.OnSubscribe<Object>() {
                      @Override
                      public void call(Subscriber<? super Object> subscriber) {
                          List<NoteAllArray> array = NoteDBHelper.getInstance().getHistoryNote();
                          subscriber.onNext(array);
                          subscriber.onCompleted();
                      }
                  }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(
                        new Func1<Object, Object>() {
                            @Override
                            public Object call(Object newR) {
                                return joinOldAnNew(bindingNoteList,(List<NoteAllArray>)newR);
                            }
                        } )
                  .subscribe(
                          new Action1<Object>() {
                              @Override
                              public void call(Object noteList) {
                                  reInflatingRecycleViewAndScrollTo((List<NoteAllArray>) noteList, 0);
                              }
                          });
    }

    private void reInflatingRecycleViewAndScrollTo(List<NoteAllArray> noteList, int i) {
//        bindingNoteList.clear();
//        noteAdapter.notifyItemMoved(0,bindingNoteList.size());
        bindingNoteList.addAll(0, noteList);
        noteAdapter.notifyItemRangeInserted(0, noteList.size());
        noteAdapter.notifyItemRangeChanged(0, noteList.size());
        noteAdapter.notifyDataSetChanged();
        if(i<bindingNoteList.size()){
            recyclerView.scrollToPosition(i);
        }else{
            Log.e(NONoConfig.TAG_NONo,"scroll positon error!");
        }
    }

    private List<NoteAllArray> joinOldAnNew(List<NoteAllArray> bindingNoteList, List<NoteAllArray> newR) {
        for (int listIndex = 0; listIndex < bindingNoteList.size(); listIndex++) {
            for (int newListIndex = 0; newListIndex < newR.size(); newListIndex++) {
                if (newR.get(newListIndex).uuid.equals(
                        bindingNoteList.get(listIndex).uuid
                )) {
                    //旧列表的数据更新了
                    bindingNoteList.set(listIndex,newR.get(newListIndex));
                    //新列表的数据去掉了
                    newR.remove(newListIndex);
                }
            }
        }
        //返回新列表，在动画中，新旧数据合并
        return newR;
    }

//    public  void onEvent(NoteUpdateEvent eventData){
//        refrshListFromLocalDB();
//    }

    @Override
    public void onResume() {
        //refrshListFromLocalDB();
        super.onResume();
    }
    //fucking Handlers!
    public void onEvent(NoteUpdateEvent noteEvent) {
        int count = bindingNoteList.size();
        for(int index = 0;index<count;index++){
            if(bindingNoteList.get(index).sdfId == noteEvent.getOldNoteId()){
                bindingNoteList.get(index).content = noteEvent.getNoteContent();
                bindingNoteList.get(index).sdfId = noteEvent.getNewNoteId();
                bindingNoteList.get(index).title = noteEvent.getNoteDatabaseArray().Title;
                bindingNoteList.get(index).isOnCloud = noteEvent.getNoteDatabaseArray().is_on_cloud;
                bindingNoteList.get(index).date = noteEvent.getNoteDatabaseArray().date;
                bindingNoteList.get(index).time = noteEvent.getNoteDatabaseArray().time;
            }
        }
        refreshListOnMainTHread();
    }

    public void onEvent(NoteDeleteEvent noteEvent) {
        for(NoteAllArray item:bindingNoteList){
            if(item.sdfId == noteEvent.getNoteId()){
                int p=bindingNoteList.indexOf(item);
                bindingNoteList.remove(item);
                refreshListOnMainTHread();
                break;
            }
        }
    }
    private void refreshListOnMainTHread(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                noteAdapter.notifyDataSetChanged();
            }
        });
    }
    public void onEvent(NotesDeleteEvent noteEvent) {
        List<NoteAllArray> delingArray = new ArrayList<>();
        for(NoteAllArray item:bindingNoteList){
            for (long id :
                    noteEvent.iDs) {
                if(item.sdfId == id){
                    delingArray.add(item);
                }
            }
        }
        for (NoteAllArray array :
                delingArray  ) {
            bindingNoteList.remove(array);
        }
        refreshListOnMainTHread();
    }
    public void onEvent(NoteInsertEvent noteEvent) {
        NoteDatabaseArray noteDatabaseArray =  noteEvent.getNoteDatabaseArray();
        NoteAllArray noteallArray = new NoteAllArray(
                noteDatabaseArray.Title,
                noteDatabaseArray.content,
                noteDatabaseArray.group,
                noteDatabaseArray.date,
                noteDatabaseArray.time,
                noteEvent.getNoteId(),
                noteDatabaseArray.is_on_cloud,
                noteEvent.getNoteUuid());
        bindingNoteList.add(0,noteallArray);
        noteAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
    }
    public void onEvent(NoteUploadEvent noteEvent) {
        if(noteEvent.isUploadState()){
            noteAdapter.makeNoteCloudSuccess(noteEvent.getNoteAllArray());
        }else {
            noteAdapter.makeNoteCloudFailed(noteEvent.getNoteAllArray());
        }
        int count = bindingNoteList.size();
        for(int index = 0;index<count;index++){
            if(bindingNoteList.get(index).sdfId == noteEvent.getNoteAllArray().sdfId){
                bindingNoteList.get(index).content = noteEvent.getNoteAllArray().content;
               // bindingNoteList.get(index).sdfId = noteEvent.getNewNoteId();
                bindingNoteList.get(index).title = noteEvent.getNoteAllArray().title;
                bindingNoteList.get(index).isOnCloud = noteEvent.isUploadState()?"true":"false";
                bindingNoteList.get(index).date = noteEvent.getNoteAllArray().date;
                bindingNoteList.get(index).time = noteEvent.getNoteAllArray().time;
            }
        }
    }
    public void onEvent(UpdateGroupName event){
        for (NoteAllArray array :
                bindingNoteList) {
            if(array.group.equals(event.getOldG())){
                array.group = event.getNewG();
            }
        }
        noteAdapter.notifyDataSetChanged();
    }
    public void onEvent(ChangeNoteGroupEvent event){
        for (NoteAllArray array :
                bindingNoteList) {
            if(array.sdfId == (event.getSdfId())){
                array.group = event.getCurrentGroup();
            }
        }
        noteAdapter.notifyDataSetChanged();
    }
    public void onEvent(ThemeColorPairChangedEvent e){
        noteAdapter.setThemeMain(e.getCurrentColorPair().mainColor);
    }
    @SuppressWarnings("unused")
    public void onEvent(DoneImportNoteEvent event){
        refrshListFromLocalDB();
    }
    @SuppressWarnings("unused")
    public void onEvent(BeginNoteUploadEvent event){
        noteAdapter.beginAnim(event.uuid);
    }
    @SuppressWarnings("unused")
    public void onEvent(NoteReloadEvent event){
        bindingNoteList.clear();
        bindingNoteList.addAll(NoteDBHelper.getInstance().getHistoryNote());
        noteAdapter.notifyDataSetChanged();
    }
}