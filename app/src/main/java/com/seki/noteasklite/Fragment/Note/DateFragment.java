package com.seki.noteasklite.Fragment.Note;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seki.noteasklite.Adapter.NoteDateItemAdapter;
import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.Bean.DeleteNoteBean;
import com.seki.noteasklite.DataUtil.Bean.NoteDateItemArray;
import com.seki.noteasklite.DataUtil.BusEvent.ChangeNoteGroupEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteDeleteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteInsertEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteReloadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteUpdateEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NotesDeleteEvent;
import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Delegate.EditNoteDelegate;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.Fragment.FluidBaseFragment;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.ColorUtils;
import com.seki.noteasklite.Util.DisplayUtil;
import com.seki.noteasklite.Util.TimeLogic;

import java.util.ArrayList;
import java.util.List;

import cn.aigestudio.datepicker.bizs.themes.DPTManager;
import cn.aigestudio.datepicker.bizs.themes.DPTheme;
import cn.aigestudio.datepicker.cons.DPMode;

/**
 * Created by yuan on 2016/5/4.
 */
public class DateFragment extends FluidBaseFragment {
    private RecyclerView recyclerView;
    private NoteDateItemAdapter noteAdapter;
    private List<NoteDateItemArray> list;
    private cn.aigestudio.datepicker.views.DatePicker date_picker;
    public DateFragment() {
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
        DPTManager.getInstance().initCalendar(new DPTheme() {
            @Override
            public int colorBG() {
                return Color.WHITE;
            }

            @Override
            public int colorBGCircle() {
//                return Color.WHITE;
                return ThemeController.getCurrentColor().getLightColor();
            }

            @Override
            public int colorTitleBG() {
                return ThemeController.getCurrentColor().getMainColor();
            }

            @Override
            public int colorTitle() {
                return Color.WHITE;
            }

            @Override
            public int colorToday() {
                return  ColorUtils.getAlphaColor(ThemeController.getCurrentColor().getAccentColor(),0.5f);
            }

            @Override
            public int colorG() {
                return Color.BLACK;
            }

            @Override
            public int colorF() {
                return Color.BLACK;
            }

            @Override
            public int colorWeekend() {
                return ThemeController.getCurrentColor().getMainColor();
            }

            @Override
            public int colorHoliday() {
                return ThemeController.getCurrentColor().getMainColor();
            }
        });
        View view=inflater.inflate(R.layout.fragment_date_note, container, false);

        return view;
    }
    String nowTime;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        installDatePicker();


    }

    @Override
    public void onStart() {
        DateFragment.super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                $(R.id.loading_bg).setVisibility(View.INVISIBLE);
                $(R.id.loading_bg).getLayoutParams().height = 0;
                $(R.id.loading_bg).getLayoutParams().width=0;
                $(R.id.loading_bg).requestLayout();
                ini();
                if(style == GroupListStyle.LIST){
                    getRecycleViewAdapter().clearItemMargin();
                    getRecycleViewAdapter().notifyDataSetChanged();
                }else if(style == GroupListStyle.TABLE){
                    getRecycleViewAdapter().setItemMargin(-1, DisplayUtil.dip2px(getActivity(),4),-1,DisplayUtil.dip2px(getActivity(),4));
                    getRecycleViewAdapter().notifyDataSetChanged();
                }
            }
        },300);
    }

    @Override
    public BaseRecycleViewAdapter getRecycleViewAdapter() {
        return noteAdapter;
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    private void ini() {
        list = new ArrayList<>();
        noteAdapter=new NoteDateItemAdapter(this.getActivity(),list);
        recyclerView=(RecyclerView)getView().findViewById(R.id.recycle_view);
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);
        refresh(nowTime);
        noteAdapter.setOnRecyclerViewListener(new NoteDateItemAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v, int position) {
                OpenNoteDelegate.start(getActivity(),new NoteAllArray(list.get(position).title,
                        list.get(position).detail,
                        list.get(position).group,
                        nowTime,
                        list.get(position).time,
                        list.get(position).sdfId,
                        list.get(position).isOnCloud,
                        list.get(position).uuid));
            }
            @Override
            public boolean onItemLongClick(final View v,final int position) {
                final String[] items=new String[2];
                items[1]=getString(R.string.action_delete);
                items[0]=getString(R.string.edit);
                new android.app.AlertDialog.Builder(getActivity()).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (list.get(position).group.compareTo("问题小记") != 0) {
                                    EditNoteDelegate.start(getActivity(),list.get(position).toNoteAllArray(),-1,-1);
                                }
                                break;
                            case 1:
                                NoteController.deleteNote(
                                        new DeleteNoteBean(
                                                list.get(position).sdfId,
                                                list.get(position).group
                                                ,list.get(position).uuid
                                        )
                                );
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
    }

    int fullDatePickerHeight;
    private void installDatePicker() {
        nowTime = TimeLogic.getNowTimeFormatly("yyyy/MM/dd");
        date_picker = $(R.id.date_picker);
        date_picker.setDate(Integer.valueOf(nowTime.split("/")[0]) ,Integer.valueOf(nowTime.split("/")[1]));
        date_picker.setFestivalDisplay(false);
        date_picker.setHolidayDisplay(false);
        date_picker.setMode(DPMode.SINGLE);
        date_picker.setOnDatePickedListener(new cn.aigestudio.datepicker.views.DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                nowTime  =DatePickerToNONoDate(date);
                refresh(nowTime);
            }
        });
        date_picker.post(new Runnable() {
            @Override
            public void run() {
                fullDatePickerHeight= date_picker.getMeasuredHeight();
            }
        });
    }
    private String DatePickerToNONoDate(String date){
        String[] dateList =  date.split("-");
        String y = dateList[0];
        String m = dateList[1];
        String d = dateList[2];
        if(Integer.valueOf(m)<10){
            return TextUtils.join("/",new String[]{y,"0"+m,d});
        }else{
            return TextUtils.join("/",new String[]{y,m,d});
        }
    }
    public void refresh(String date){
        new com.seki.noteasklite.AsyncTask.RefreshTask(getActivity(),noteAdapter)
                .setDate(date)
                .execute(list);
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }
    public void onEvent(NoteDeleteEvent event){
        refresh(nowTime);
    }
    public void onEvent(NotesDeleteEvent event){
        refresh(nowTime);
    }
    public void onEvent(NoteInsertEvent event){
        refresh(nowTime);
    }
    public void onEvent(NoteUpdateEvent event){
        refresh(nowTime);
    }
    public void onEvent(ChangeNoteGroupEvent event){
        refresh(nowTime);
    }
    public void onEvent(NoteReloadEvent event){
        refresh(nowTime);
    }
    @Override
    protected void themePatch() {
        super.themePatch();
        DPTManager.getInstance().initCalendar(new DPTheme() {
            @Override
            public int colorBG() {
                return Color.WHITE;
            }

            @Override
            public int colorBGCircle() {
//                return Color.WHITE;
                return ThemeController.getCurrentColor().getLightColor();
            }

            @Override
            public int colorTitleBG() {
                return ThemeController.getCurrentColor().getMainColor();
            }

            @Override
            public int colorTitle() {
                return Color.WHITE;
            }

            @Override
            public int colorToday() {
//                return ThemeController.getCurrentColor().getDarkColor();
                return  ColorUtils.getAlphaColor(ThemeController.getCurrentColor().getAccentColor(),0.5f);
            }

            @Override
            public int colorG() {
                return Color.BLACK;
            }

            @Override
            public int colorF() {
                return Color.BLACK;
            }

            @Override
            public int colorWeekend() {
                return ThemeController.getCurrentColor().getMainColor();
            }

            @Override
            public int colorHoliday() {
                return ThemeController.getCurrentColor().getMainColor();
            }
        });
        if(date_picker!=null){
            date_picker.refreshTheme();
        }
        if( noteAdapter !=null){
            noteAdapter.setThemeMain(ThemeController.getCurrentColor().getMainColor());
        }

    }

    //    IntEvaluator ev = new IntEvaluator();
//    public void onEvent(ShowAnimEvent event){
//        if(date_picker.getHeight()>10){
//            return;
//        }
//        if(anim !=null && anim.isRunning()){
//            return ;
//        }
//         anim = new ValueAnimator().ofInt(0,10,20,30,40,50,60,70,80,90,100)
//                .setDuration(500) ;
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setEvaluator(new IntEvaluator());
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int frac = (int)animation.getAnimatedValue();
//                ViewGroup.LayoutParams p  =  date_picker.getLayoutParams();
//               p .height =ev.evaluate(frac/100,1,fullDatePickerHeight);
//                date_picker.setLayoutParams(p);
//            }
//        });
//        anim.setTarget(date_picker);
//        anim.start();
//    }
//    ValueAnimator anim;
//    public void onEvent(HideAnimEvent event){
//        if(date_picker.getHeight()<10){
//            return;
//        }
//        if(anim !=null && anim.isRunning()){
//            return ;
//        }
//        anim  = new ValueAnimator().ofInt(100,90,80,70,60,50,40,30,20,10,0)
//
//                .setDuration(500)
//                ;
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setEvaluator(new IntEvaluator());
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int frac = (int)animation.getAnimatedValue();
//                date_picker.getLayoutParams().height =ev.evaluate(frac/100,1,fullDatePickerHeight);
//                date_picker.requestLayout();
//            }
//        });
//        anim.setTarget(date_picker);
//        anim.start();
//    }
}
