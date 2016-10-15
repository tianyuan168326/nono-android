package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.Base.BaseThemeRecycleViewAdapter;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.DataUtil.QuestionItemData;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.TimeLogic;

import java.util.List;

/**
 * Created by yuan on 2015/7/28.
 */
public class QuestionRecycleViewAdapter extends BaseThemeRecycleViewAdapter {
    Context mContext;
    List<QuestionItemData> dataset;
    QuestionItemClickListener clickListener;
    QuestionItemLongClickListener longClickListener;
    public QuestionRecycleViewAdapter(Context context,List<QuestionItemData> dataset)
    {
        super();
        this.mContext=context;
        this.dataset=dataset;
    }
    public class FetchingViewHolder extends RecyclerView.ViewHolder
    {
        public FetchingViewHolder(View itemView) {
            super(itemView);
        }
    }
    public static  class NormalViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView titleTextView;
        public TextView questionRaiseTimeTextView;
        public TextView questionAnswerNumView;
        public TextView questionHotDegreeView;
        public TextView questionAbstractView;
        public TextView questionRaiseNameView;
        public SimpleDraweeView questionRaiserHeadImage;
        public ImageView questionOfficalState;
        QuestionItemClickListener clickListener;
        QuestionItemLongClickListener longClickListener;
        public NormalViewHolder(View convertview,QuestionItemClickListener clickListener,QuestionItemLongClickListener longClickListener)
        {
            super(convertview);
            this.clickListener =clickListener;
            this.longClickListener = longClickListener;
            this.titleTextView=(TextView)convertview.findViewById(R.id.question_title);
            this.questionRaiseTimeTextView = (TextView)convertview.findViewById(R.id.question_raise_time);
            this.questionRaiseNameView = (TextView)convertview.findViewById(R.id.question_raiser);
            this.questionAnswerNumView = (TextView)convertview.findViewById(R.id.question_answer_num);
            this.questionHotDegreeView = (TextView)convertview.findViewById(R.id.question_hot_degree);
            this.questionAbstractView = (TextView)convertview.findViewById(R.id.question_abstract);
            this.questionRaiserHeadImage = (SimpleDraweeView)convertview.findViewById(R.id.question_raiser_headimage);
            this.questionOfficalState = (ImageView)convertview.findViewById(R.id.question_offical_state);
            convertview.setOnClickListener(this);
            convertview.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(clickListener != null)
                clickListener.onItemClick(v,getPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if(longClickListener != null)
                longClickListener.onItemLongClick(v, getPosition());
            return true;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView;
        switch(viewType)
        {
            case QuestionItemData.QuestionItemType.TYPE_FETCHING:
                 convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_item_fetching
                        , parent,false);
                return new FetchingViewHolder(convertView);
            default:
                 convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_item, parent,false);
                NormalViewHolder vh = new NormalViewHolder(convertView,clickListener,longClickListener);
                return vh;
        }

    }
    public void setOnItemClickListener(QuestionItemClickListener clickListener)
    {
        this.clickListener = clickListener;
    }
    public void setOnItemLongClickListener( QuestionItemLongClickListener longClickListener)
    {
        this.longClickListener = longClickListener;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vhHolder, int position) {
        final QuestionItemData questionItemData;
        try
        {
            questionItemData= dataset.get(position);
        }
        catch (IndexOutOfBoundsException indexBoundError)
        {
            indexBoundError.printStackTrace();
            return ;
        }
        if(questionItemData.questionItemType == QuestionItemData.QuestionItemType.TYPE_FETCHING)
            return;
        NormalViewHolder normalViewHolder = (NormalViewHolder)vhHolder;
        normalViewHolder.titleTextView.setText(questionItemData.questionItemTitle.replace("\\s","").replace(" ","").replace("\n",""));
        if(mainColor !=-1){
            normalViewHolder.titleTextView.setTextColor(mainColor);
        }
        normalViewHolder.questionRaiseTimeTextView .setText(TimeLogic.timeLogic(questionItemData.questionRaiseTime));
        normalViewHolder.questionAnswerNumView.setText(questionItemData.questionAnswerNum);
        normalViewHolder.questionHotDegreeView .setText(questionItemData.questionHotDegree);
        normalViewHolder.questionRaiseNameView.setText(questionItemData.questionRaiserRealname);
        normalViewHolder.questionOfficalState.setVisibility(
                String.valueOf(NONoConfig.OfficalID).equals(questionItemData.questionRaiserId)? View.VISIBLE:
                        View.INVISIBLE
        );
        String plainAbstract = Html.fromHtml(questionItemData.questionAbstract).toString();
        plainAbstract = plainAbstract.replace("\\s","").replace(" ","").replace("\n","");
        normalViewHolder.questionAbstractView.setText(plainAbstract);
        FrescoImageloadHelper.simpleLoadImageFromURL(normalViewHolder.questionRaiserHeadImage, questionItemData.questionRaiserHeadImg);
    }

    @Override
    public int getItemViewType(int position) {
        return dataset.get(position).questionItemType;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
