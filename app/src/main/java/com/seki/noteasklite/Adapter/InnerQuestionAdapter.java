package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.Activity.Ask.AnswerDetailHTMLActivity;
import com.seki.noteasklite.Base.BaseThemeRecycleViewAdapter;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.NetWorkUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Seki on 2015/10/11.
 */
public class InnerQuestionAdapter extends BaseThemeRecycleViewAdapter {
    private int headHeight = 0;
    public static interface OnRecyclerViewListener {
        void onItemClick(View v, int position);
        boolean onItemLongClick(View v, int position);
    }
    public void setHeadHeight(int headHeight){
        this.headHeight = headHeight;
    }
    public int getHeadHeight(){
        return headHeight;
    }
    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Context context;
    private List<Object> dataSet;
    
    
    public InnerQuestionAdapter(Context context,List<Object> dataSet){
        super();
        this.context=context;
        this.dataSet=dataSet;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HashMap<String,String> dataItem =  (HashMap<String,String>)dataSet.get(position);
        String answerRasierName = dataItem.get("answer_raiser_name");
        String answerAbstractText = dataItem.get("answer_abstract_text");
        String answerRaiserCollege=  dataItem.get("answer_raiser_college");
        String  answerRaiserSchool =  dataItem.get("answer_raiser_school");
        String answerRaiserHeadImg = dataItem.get("answer_raiser_head_img");
        MyViewHolder viewHolder=(MyViewHolder)holder;
        viewHolder.position=position;
        viewHolder.abstractText.setText(Html.fromHtml(answerAbstractText));
        viewHolder.item_raiser_name.setText(answerRasierName);
        if(mainColor!=-1){
            viewHolder.vote_num.setBackgroundColor(mainColor);
            viewHolder.comment_num.setBackgroundColor(mainColor);
        }
        viewHolder.vote_num.setText(dataItem.get("answer_hot_degree") == null?"0":dataItem.get("answer_hot_degree"));
        viewHolder.comment_num.setText(dataItem.get("answer_comment_num") == null?"0":dataItem.get("answer_comment_num"));
        compareSandDUpdateUI(viewHolder.innerQuestionListItemIsSameuniversity, answerRaiserSchool, MyApp.getInstance().userInfo.userUniversity);
        compareSandDUpdateUI(viewHolder.innerQuestionListItemIsSameschool, answerRaiserCollege, MyApp.getInstance().userInfo.userSchool);
        FrescoImageloadHelper.simpleLoadImageFromURL(viewHolder.headImg, answerRaiserHeadImg);
        viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWorkUtil.verifyWhichActivityToSwitch(context,
                        MyApp.getInstance().userInfo.userId,
                        dataItem.get("answer_raiser_id"));
            }
        });
        int ImageNum=Integer.parseInt(dataItem.get("answer_img_count"));
        switch (ImageNum)
        {
            case 0:
                viewHolder.image0.setVisibility(View.GONE);
                viewHolder.image1.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.image0.setVisibility(View.VISIBLE);
                viewHolder.image1.setVisibility(View.GONE);
                FrescoImageloadHelper.simpleLoadImageFromURL(viewHolder.image0, dataItem.get("answer_abstract_img0"));
                break;
            case 2:
                viewHolder.image0.setVisibility(View.VISIBLE);
                viewHolder.image1.setVisibility(View.VISIBLE);
                FrescoImageloadHelper.simpleLoadImageFromURL(viewHolder.image0, dataItem.get("answer_abstract_img0"));
                FrescoImageloadHelper.simpleLoadImageFromURL(viewHolder.image1, dataItem.get("answer_abstract_img1"));
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.question_inner_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void compareSandDUpdateUI(View view, String D, String S) {
        view.setVisibility(D.trim().equals(S.trim())?
                View.VISIBLE:View.GONE);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public int position;
        public TextView item_raiser_name;
        public TextView abstractText;
        public TextView innerQuestionListItemIsSameuniversity;
        public TextView innerQuestionListItemIsSameschool;

        public TextView vote_num;
        public TextView comment_num;
        public SimpleDraweeView image0;
        public SimpleDraweeView image1;
        public SimpleDraweeView headImg;
        public MyViewHolder(View itemView){
            super(itemView);
            this.image0=(SimpleDraweeView)itemView.findViewById(R.id.question_inner_image0);
            this.image1=(SimpleDraweeView)itemView.findViewById(R.id.question_inner_image1);
            this.headImg = (SimpleDraweeView)itemView.findViewById(R.id.question_inner_head_pic);
            this.innerQuestionListItemIsSameuniversity=(TextView)itemView.findViewById(R.id.inner_question_list_item_is_sameuniversity);
            this.abstractText=(TextView)itemView.findViewById(R.id.question_inner_text);
            this.item_raiser_name=(TextView)itemView.findViewById(R.id.question_inner_user_name);
            this.innerQuestionListItemIsSameschool=(TextView)itemView.findViewById(R.id.inner_question_list_item_is_sameschool);
            this.vote_num=(TextView)itemView.findViewById(R.id.vote_num);
            this.comment_num=(TextView)itemView.findViewById(R.id.comment_num);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String ,String > answerinfo= ((HashMap<String, String>) dataSet.get(position));
                    AnswerDetailHTMLActivity.start(
                            context, answerinfo.get("answer_id"), answerinfo.get("question_abstract"),
                            answerinfo.get("answer_raiser_head_img"), answerinfo.get("answer_raiser_name"), "",
                            answerinfo.get("answer_abstract_text"), answerinfo.get("answer_hot_degree"), answerinfo.get("answer_comment_num")
                    );
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(v,position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(v,position);
            }
            return false;
        }
    }
}
