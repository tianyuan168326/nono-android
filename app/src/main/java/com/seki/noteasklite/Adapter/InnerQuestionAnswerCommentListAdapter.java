package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.DataUtil.Bean.AllCommentListBean;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.TimeLogic;

import java.util.List;

/**
 * Created by yuan on 2015/8/23.
 */
public class InnerQuestionAnswerCommentListAdapter extends RecyclerView.Adapter {
    List<AllCommentListBean.CommentEntity> innerQuestionAnswerCommentList;

    Context _context;
    public InnerQuestionAnswerCommentListAdapter(Context context,
            List<AllCommentListBean.CommentEntity> innerQuestionAnswerCommentList) {
        this.innerQuestionAnswerCommentList = innerQuestionAnswerCommentList;
        _context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InnerQuestionAnswerCommentListViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_question_answer_comment_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InnerQuestionAnswerCommentListViewHolder innerquestionanswercommentlistviewholder =
                (InnerQuestionAnswerCommentListViewHolder)(holder);
        final AllCommentListBean.CommentEntity innerquestionanswercommentlistviewholderdata =
                innerQuestionAnswerCommentList.get(position);
        innerquestionanswercommentlistviewholder.innerQuestionAnswerCommentListItemContent
                .setText(innerquestionanswercommentlistviewholderdata.comment_content);

        FrescoImageloadHelper.simpleLoadImageFromURL(innerquestionanswercommentlistviewholder.innerQuestionAnswerCommentListItemHeadimg,
                innerquestionanswercommentlistviewholderdata.user_headpic);
        innerquestionanswercommentlistviewholder.innerQuestionAnswerCommentListItemName
                .setText(innerquestionanswercommentlistviewholderdata.userRealname);
        innerquestionanswercommentlistviewholder.innerQuestionAnswerCommentListItemTime
                .setText(TimeLogic.timeLogic(innerquestionanswercommentlistviewholderdata.comment_time));

        innerquestionanswercommentlistviewholder.innerQuestionAnswerCommentListItemHeadimg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoActivity.start(_context,innerquestionanswercommentlistviewholderdata
                        .comment_raiser_id);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return innerQuestionAnswerCommentList.size();
    }

    private class InnerQuestionAnswerCommentListViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView innerQuestionAnswerCommentListItemHeadimg;
        public TextView innerQuestionAnswerCommentListItemName;
        public TextView innerQuestionAnswerCommentListItemContent;
        public TextView innerQuestionAnswerCommentListItemTime;
        public InnerQuestionAnswerCommentListViewHolder(View itemView) {
            super(itemView);
            innerQuestionAnswerCommentListItemHeadimg = (SimpleDraweeView)itemView.findViewById(R.id.inner_question_answer_comment_list_item_headimg);
            innerQuestionAnswerCommentListItemName = (TextView)itemView.findViewById(R.id.inner_question_answer_comment_list_item_name);
            innerQuestionAnswerCommentListItemContent = (TextView)itemView.findViewById(R.id.inner_question_answer_comment_list_item_content);
            innerQuestionAnswerCommentListItemTime = (TextView)itemView.findViewById(R.id.inner_question_answer_comment_list_item_time);
        }
    }
}
