package com.seki.noteasklite.Adapter.Search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.Search.LocalNoteArray;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.ShareUtil;
import com.wangjie.androidbucket.utils.ABTextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuan-tian01 on 2016/4/7.
 */
public class LocalNoteAdapter  extends RecyclerView.Adapter {
    public enum ViewType{
        TYPE_IMAGE_VIEW,
        TYPE_OTHER_VIEW
    }
    public  interface OnRecyclerViewListener {
        void onItemClick(RecyclerView.ViewHolder v, int position,ViewType viewType);

        boolean onItemLongClick(RecyclerView.ViewHolder v, int position,ViewType viewType);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<LocalNoteArray> list;
    private Context context;

    public LocalNoteAdapter(Context context, List<LocalNoteArray> list) {
        this.list = list;
        this.context = context;
    }
    static int divider_color = MyApp.getInstance().applicationContext.getResources().getColor(R.color.md_divider_color);;
    static int accent_color = MyApp.getInstance().applicationContext.getResources().getColor(R.color.colorAccent);;
    static int bg_color = MyApp.getInstance().applicationContext.getResources().getColor(R.color.md_search_bg);;
    HashMap<NoteAllArray,ImageView> arrayViewMap = new HashMap<>();
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final GroupViewHolder holder = (GroupViewHolder) viewHolder;
        holder.position = i;
        final LocalNoteArray localNoteArray = list.get(i);
        final NoteAllArray array = localNoteArray.noteAllArray;

        SpannableStringBuilder titleBuilder = makeTitleBuilder(array.title,localNoteArray.titleStart,localNoteArray.titleEnd);
        holder.noteTitle.setText(titleBuilder);

        holder.noteDateAndTime.setText(array.date + " " + array.time);

        String plainContent = ShareUtil.htmlToPlain(array.content) ;
        SpannableStringBuilder contentAbstractBuilder = makeContentAbstarctBuilder(plainContent,localNoteArray.contentStart,localNoteArray.contentEnd);
        holder.noteDetail.setText(contentAbstractBuilder);


        SpannableStringBuilder groupBuilder = makeGroupBuilder(array.group,localNoteArray.groupStart,localNoteArray.groupEnd);
        holder.groupName.setText(groupBuilder);
        holder.noteDetail.requestLayout();
        holder.itemView.requestLayout();

        if(array.isOnCloud.equals("false")){
            holder.noteCloudFlag.setColorFilter(divider_color, PorterDuff.Mode.SRC_ATOP);
        }else if(array.isOnCloud.equals("true")){
            holder.noteCloudFlag.setColorFilter(accent_color, PorterDuff.Mode.SRC_ATOP);
        }

        arrayViewMap.put(array,holder.noteCloudFlag);
        holder.noteCloudFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(array.uuid)){
                    Toast.makeText(context,"默认说明笔记不用存到云上哦~",Toast.LENGTH_SHORT).show();
                    return ;
                }
                NoteController.uploadNoteCloud(array);
            }
        });
    }

    private SpannableStringBuilder makeGroupBuilder(String group, int groupStart, int groupEnd) {
        SpannableStringBuilder groupBuilder = new SpannableStringBuilder(group);
        if(groupStart == -1 || groupEnd == -1){
            return groupBuilder;
        }
        groupBuilder.setSpan(new BackgroundColorSpan(bg_color),groupStart,groupEnd,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return groupBuilder;
    }

    private SpannableStringBuilder makeTitleBuilder(String title, int titleStart, int titleEnd) {
        SpannableStringBuilder titleBuilder = new SpannableStringBuilder(title);
        if(titleStart == -1 || titleEnd == -1) {
            return titleBuilder;
        }
        titleBuilder.setSpan(new BackgroundColorSpan(bg_color),titleStart,titleEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return titleBuilder;
    }

    private SpannableStringBuilder makeContentAbstarctBuilder(String plainContent, int contentStart,
                                                              int contentEnd) {
        if(contentStart == -1 || contentEnd == -1){
            SpannableStringBuilder contentAbstractBuilder=  new SpannableStringBuilder(plainContent);
            return contentAbstractBuilder;
        }
        //重新计算的摘要start end
        int contentAbstractStart = 0;
        int contentAbstractEnd = 0;
        //在abstract里面搜索区域的 start end
        int contentColorStart = contentStart;
        int contentColorEnd = contentEnd;
        if(contentStart>20){
            contentAbstractStart = contentStart-20;
            contentColorStart=20;
        }else{
            contentAbstractStart = 0;
            //contentColorStart = contentAbstractStart;
        }
        contentColorEnd = contentColorStart+(contentEnd-contentStart);
        if(plainContent.length()>contentEnd +20){
            contentAbstractEnd = contentEnd+20;
        }else{
            contentAbstractEnd = plainContent.length();
        }
        String plainContentAbstract = plainContent.substring(contentAbstractStart,contentAbstractEnd);
        SpannableStringBuilder contentAbstractBuilder=  new SpannableStringBuilder(plainContentAbstract);
        contentAbstractBuilder.setSpan(new BackgroundColorSpan(bg_color),contentColorStart,contentColorEnd,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return contentAbstractBuilder;
    }

    public void makeNoteCloudFailed(NoteAllArray array) {

        final ImageView noteCloudFlag = arrayViewMap.get(array);
        if(noteCloudFlag == null){
            return;
        }
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
        if(i==0){
                StaggeredGridLayoutManager.LayoutParams lp=(StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams();

            lp.setMargins(ABTextUtil.dip2px(viewGroup.getContext(), 8),ABTextUtil.dip2px(viewGroup.getContext(),8),
                    ABTextUtil.dip2px(viewGroup.getContext(),8),ABTextUtil.dip2px(viewGroup.getContext(),8));
            view.setLayoutParams(lp);
        }
        return new GroupViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
        public GroupViewHolder(View itemView) {
            super(itemView);
            noteTitle = (TextView)itemView.findViewById(R.id.layout_all_note_item_title);
            groupName = (TextView) itemView.findViewById(R.id.layout_all_note_item_group);
            noteDateAndTime = (TextView) itemView.findViewById(R.id.layout_all_note_item_time);
            noteDetail = (TextView) itemView.findViewById(R.id.layout_all_note_item_abstract);
            noteCloudFlag = (ImageView)itemView.findViewById(R.id.layout_all_note_item_cloud);

            itemView.setOnClickListener(this);
//            noteDetail.setOnClickListener(this);
//            noteTitle.setOnClickListener(this);
            noteCloudFlag.setOnClickListener(this);

            itemView.setOnLongClickListener(this);
//            noteDetail.setOnLongClickListener(this);
//            noteTitle.setOnLongClickListener(this);
//            noteCloudFlag.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                if(!(v instanceof ImageView) ){
                    onRecyclerViewListener.onItemClick(GroupViewHolder.this, position,ViewType.TYPE_OTHER_VIEW);

                }else if(v instanceof ImageView){
                    onRecyclerViewListener.onItemClick(GroupViewHolder.this, position,ViewType.TYPE_IMAGE_VIEW);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemLongClick(GroupViewHolder.this, position, ViewType.TYPE_IMAGE_VIEW);
            }
            return true;
        }
    }
}
