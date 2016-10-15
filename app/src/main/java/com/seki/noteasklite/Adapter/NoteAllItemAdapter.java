package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seki.noteasklite.Base.BaseMultiChoiceRecycleViewAdapter;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Fragment.NONoPreferenceFragment;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.DisplayUtil;
import com.seki.noteasklite.Util.FuckBreaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Seki on 2015/10/11.
 */
public class NoteAllItemAdapter extends BaseMultiChoiceRecycleViewAdapter {
    public void beginAnim(String uuid) {
        for (NoteAllArray array:list){
            if(array.uuid .equals(uuid)){
               RotateAnimation anim =  new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
                ;
                anim.setRepeatCount(-1);
                anim.setDuration(500);
                arrayViewMap.get(array).startAnimation(anim);
            }
        }
    }

    public enum ViewType{
        TYPE_IMAGE_VIEW,
        TYPE_OTHER_VIEW
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<NoteAllArray> list;
    private Context context;
//    public NoteAllItemAdapter(Context context, List<NoteAllArray> list) {
//        this.list = list;
//        this.context = context;
//    }
    public NoteAllItemAdapter(Context context, List<NoteAllArray> list,int mainColor) {
        this.list = list;
        this.context = context;
        this.mainColor = mainColor;
    }
    static int divider_color = MyApp.getInstance().applicationContext.getResources().getColor(R.color.md_divider_color);;
    static int accent_color = MyApp.getInstance().applicationContext.getResources().getColor(R.color.colorAccent);;
    HashMap<NoteAllArray,ImageView> arrayViewMap = new HashMap<>();
    @Override
    public void realOnBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final GroupViewHolder holder = (GroupViewHolder) viewHolder;
        holder.position = i;
        final NoteAllArray array = list.get(i);
        if(mainColor != -1){
            holder.noteTitle.setTextColor(mainColor);
        }
        holder.noteTitle.setText(array.title);
        holder.noteDateAndTime.setText(array.date + " " + array.time);
        holder.noteDetail.setText(FuckBreaker.fuckBreakerAndSpace(array.content));
        holder.groupName.setText(array.group);
        holder.noteDetail.requestLayout();
        holder.itemView.requestLayout();
        if(array.isOnCloud.equals("false")){
            holder.noteCloudFlag.setColorFilter(divider_color, PorterDuff.Mode.SRC_ATOP);
        }else if(array.isOnCloud.equals("true")){
            holder.noteCloudFlag.setColorFilter(accent_color, PorterDuff.Mode.SRC_ATOP);
        }
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onRecyclerViewListener.onItemLongClick(viewHolder, i, ViewType.TYPE_IMAGE_VIEW);
//                return true;
//            }
//        });

        holder.noteCloudFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(array.uuid)){
                    Toast.makeText(context,"默认说明笔记不用存到云上哦~",Toast.LENGTH_SHORT).show();
                    return ;
                }
                arrayViewMap.put(array,holder.noteCloudFlag);
                NoteController.uploadNoteCloud(array);
            }
        });
    }

    public void makeNoteCloudFailed(NoteAllArray array) {

        final ImageView noteCloudFlag = arrayViewMap.get(array);
        if(noteCloudFlag == null){
            return;
        }
        noteCloudFlag.clearAnimation();
        arrayViewMap.remove(array);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(MyApp.getInstance().applicationContext, "未能创建云笔记，请稍后再试", Toast.LENGTH_SHORT).show();
                noteCloudFlag.setColorFilter(divider_color, PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        noteCloudFlag.startAnimation(scaleAnimation);
    }
    ImageView getImagetViewFromCache(NoteAllArray array){
        for (Map.Entry<NoteAllArray,ImageView> entry:
        arrayViewMap.entrySet()) {
            if(entry.getKey().uuid.equals(array.uuid)){
                return entry.getValue();
            }
        }
        return  null;
    }
    public void makeNoteCloudSuccess(NoteAllArray array) {
        final ImageView noteCloudFlag = getImagetViewFromCache(array);
        if(noteCloudFlag == null){
            return;
        }
        noteCloudFlag.clearAnimation();
        arrayViewMap.remove(array);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(MyApp.getInstance().applicationContext, "成功创建云笔记", Toast.LENGTH_SHORT).show();
                noteCloudFlag.setColorFilter(accent_color, PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        noteCloudFlag.startAnimation(scaleAnimation);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_all_note_item
                , viewGroup, false);
        ViewGroup.MarginLayoutParams p  =(ViewGroup.MarginLayoutParams)view.getLayoutParams();
        if(AppPreferenceUtil.getGroupStyle() == GroupListStyle.TABLE){
            p.leftMargin  = DisplayUtil.dip2px(context,2);
            p.rightMargin  = DisplayUtil.dip2px(context,2);
        }else  if(AppPreferenceUtil.getGroupStyle() == GroupListStyle.LIST){
            p.leftMargin  = DisplayUtil.dip2px(context,0);
            p.rightMargin  = DisplayUtil.dip2px(context,0);
        }
        return new GroupViewHolder(view,choiceBgColor,isAllowMultiChoice,onRecyclerViewListener,onModeChangedListener,positionList,
                isInMultiMode);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GroupViewHolder extends BaseMultiChoiceRecycleViewAdapter.BaseViewHolder {
        public int position;

        public TextView getNoteTitle() {
            return noteTitle;
        }

        public TextView noteTitle;
        public TextView groupName;
        public TextView noteDateAndTime;

        public TextView getNoteDetail() {
            return noteDetail;
        }

        public TextView noteDetail;
        public ImageView noteCloudFlag;
        public GroupViewHolder(View itemView,int choiceBgColor,boolean  isAllowMultiChoice,OnRecyclerViewListener onRecyclerViewListener
                ,OnChoiceModeChangedListener onModeChangedListener
                ,List<Integer> positionList
                , boolean []isInMultiMod) {
            super( itemView, choiceBgColor,  isAllowMultiChoice, onRecyclerViewListener
                    , onModeChangedListener
                    ,positionList
            , isInMultiMod);
            noteTitle = (TextView)itemView.findViewById(R.id.layout_all_note_item_title);

            groupName = (TextView) itemView.findViewById(R.id.layout_all_note_item_group);
            noteDateAndTime = (TextView) itemView.findViewById(R.id.layout_all_note_item_time);
            noteDetail = (TextView) itemView.findViewById(R.id.layout_all_note_item_abstract);
            noteDetail.setMaxLines(NONoPreferenceFragment.getCardLineNum());
            noteCloudFlag = (ImageView)itemView.findViewById(R.id.layout_all_note_item_cloud);
            if(isShowCloud){
                noteCloudFlag.setVisibility(View.VISIBLE);
            }else{
                noteCloudFlag.setVisibility(View.INVISIBLE);
            }
            itemView.setOnClickListener(this);
            noteCloudFlag.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
        }

        @Override
        public boolean onLongClick(View v) {
            return super.onLongClick(v);
        }
    }

    boolean isShowCloud = true;
    public void setShowCloud(boolean isShowCloud){
        this.isShowCloud = isShowCloud;
    }
    int mainColor = -1;
    //just chang attr,need invalidating
    public void setThemeMain(int color){
        mainColor = color;
        notifyDataSetChanged();
    }
}
