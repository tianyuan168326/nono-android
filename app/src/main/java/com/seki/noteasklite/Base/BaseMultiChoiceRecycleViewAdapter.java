package com.seki.noteasklite.Base;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2016/6/12.
 */
public class BaseMultiChoiceRecycleViewAdapter extends BaseRecycleViewAdapter {

    public static interface OnRecyclerViewListener {
        void onItemClick(View v, int position);
        boolean onItemLongClick(View v, int position);
    }
    public static interface OnChoiceModeChangedListener{
        public void onNoSelect();
        public void onBeginSelect();
    }
    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    protected OnRecyclerViewListener onRecyclerViewListener;
    protected OnChoiceModeChangedListener onModeChangedListener;

    public List<Integer> getSelectedPositionList() {
        return positionList;
    }

    protected List<Integer> positionList  = new ArrayList<>();
    //must
    public void setMultiChoiceMode(boolean multiChoiceMode) {
        this.isAllowMultiChoice = multiChoiceMode;
        if(choiceBgColor == -1){
           choiceBgColor = MyApp.getInstance().getApplicationContext().getResources().getColor(R.color.colorDividerLight);
        }
    }
    public void setOnModeChangedListener(OnChoiceModeChangedListener l ){
        this.onModeChangedListener = l;
    }
    protected boolean isAllowMultiChoice = false;
    public void setChoiceBgColor(int choiceBgColor) {
        this.choiceBgColor = choiceBgColor;
    }

    protected int choiceBgColor = -1;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
    @Deprecated
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        realOnBindViewHolder(holder,position);
        ((BaseViewHolder)holder).position = position;
        if(switch_SelectAll){
            ((BaseViewHolder)holder).setSelected();
        }
        if(switch_UnSelelAll){
            ((BaseViewHolder)holder).unSelected();
        }
    }
    public void  realOnBindViewHolder(RecyclerView.ViewHolder holder, int position){}
    boolean switch_SelectAll = false;
    boolean switch_UnSelelAll = false;
    public void selectAll(){
        switch_SelectAll = true;
        switch_UnSelelAll = false;
        notifyDataSetChanged();
    }
    public void unSelectAll(){
        switch_SelectAll = false;
        switch_UnSelelAll = true;
        try{
            notifyDataSetChanged();
        }catch (Exception e){}

    }
    protected boolean []isInMultiMode = new boolean[]{false};
    public  static class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        int choiceBgColor = -1;
        int normalBgColor = -1;
       public  int position;
        OnRecyclerViewListener onRecyclerViewListener;
        boolean isAllowMultiChoice = false;
        OnChoiceModeChangedListener onChoiceModeChangedListener;
        List<Integer> positionList;
        boolean []isInMultiMode;
        public BaseViewHolder(View itemView,int choiceBgColor,boolean  isAllowMultiChoice,OnRecyclerViewListener onRecyclerViewListener
        ,OnChoiceModeChangedListener onModeChangedListener
        ,List<Integer> positionList
        , boolean []isInMultiMode) {
            super(itemView);
            this.choiceBgColor = choiceBgColor;
            this.isAllowMultiChoice = isAllowMultiChoice;
            this.onRecyclerViewListener = onRecyclerViewListener;
            this.onChoiceModeChangedListener = onModeChangedListener;
            this.positionList = positionList;
            this.isInMultiMode = isInMultiMode;
            Drawable backGroud = itemView.getBackground();
            if (backGroud instanceof ColorDrawable) {
                normalBgColor = ((ColorDrawable) backGroud).getColor();
            }
        }
        boolean isSel = false;

        public void setSelected(){
            if(choiceBgColor !=-1){
                itemView.setBackgroundColor(choiceBgColor);
                isSel = true;
                positionList.add((Integer)(position) );
            }
            if(positionList.size() == 1){
                onChoiceModeChangedListener.onBeginSelect();
            }
            if(positionList.size() ==0){
                isInMultiMode[0] = false;
            }else{
                isInMultiMode[0] = true;
            }
        }
        public void unSelected(){
            itemView.setBackgroundColor(normalBgColor);
            isSel = false;
            positionList.remove((Integer)(position) );
            if(positionList.size() ==0){

                onChoiceModeChangedListener.onNoSelect();
            }
            if(positionList.size() ==0){
                isInMultiMode[0] = false;
            }else{
                isInMultiMode[0] = true;
            }
        }
        public void toogleSelected(){
            if(onChoiceModeChangedListener == null){
                throw new IllegalArgumentException("onChoiceModeChangedListener must be set!");
            }
            if(isSel){
                unSelected();
            }else{
                setSelected();
            }
        }
        @Override
        public void onClick(View v) {
            if(isInMultiMode[0]){
                toogleSelected();
                return ;
            }
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(v,position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(isAllowMultiChoice){
                toogleSelected();
                return true;
            }
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(v,position);
            }
            return false;
        }
    }
    @Override
    public int getItemCount() {
        return 0;
    }
}
