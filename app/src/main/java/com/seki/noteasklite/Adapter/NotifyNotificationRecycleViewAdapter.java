package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seki.noteasklite.Activity.Ask.AnswerDetailHTMLActivity;
import com.seki.noteasklite.Activity.Ask.InnerQuestionActivity;
import com.seki.noteasklite.Activity.UserInfoActivity;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.Bean.NotificationDataModel;
import com.seki.noteasklite.DataUtil.NotificationHistoryType;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.ColorUtils;
import com.seki.noteasklite.Util.EmojiPatch;
import com.seki.noteasklite.Util.TimeLogic;

import java.util.List;

/**
 * Created by yuan on 2015/8/5.
 */
public class NotifyNotificationRecycleViewAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<NotificationDataModel> notificationList;
    public NotifyNotificationRecycleViewAdapter(Context context, List<NotificationDataModel> notificationList)
    {
        this.context = context;
        this.notificationList = notificationList;
    }
    public interface OnRecycleViewListener
    {
        void OnItemClickListener(View v, int positon);
        boolean OnItemLongClickListener(View v, int positon);
    }
    private OnRecycleViewListener onRecycleViewListener;
    public void setOnRecycleViewListener(OnRecycleViewListener onRecycleViewListener)
    {
        this.onRecycleViewListener = onRecycleViewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater parentContextInflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case NotificationHistoryType.ANSWER_MYQUESTION:
                return new NewAnswerViewHolder(parentContextInflater.
                        inflate(R.layout.notify_notification_list_new_answer_item,parent,false));
            case NotificationHistoryType.EDIT_ANSWER:
                return new EditAnswerViewHolder(parentContextInflater
                .inflate(R.layout.notify_notification_list_edit_answer_item, parent, false));
            case NotificationHistoryType.NOTICE_ME:
                return new NoticeMeViewHolder(parentContextInflater
                .inflate(R.layout.notify_notification_list_notice_me,parent,false));
            case NotificationHistoryType.NOTICE_MYQUESTION:
                return new NoticeMyQuestionViewHolder(parentContextInflater
                .inflate(R.layout.notify_notification_list_notice_myquestion,parent,false));
            case NotificationHistoryType.VOTE_UP_ANSWER:
                return new AnswerVoteUpViewHolder(parentContextInflater
                .inflate(R.layout.notify_notification_list_answer_vote_up,parent,false));
            case NotificationHistoryType.VOTE_UP_QUESTION:
                return new QuestionVoteUpViewHolder(parentContextInflater
                .inflate(R.layout.notify_notification_list_question_vote_up,parent,false));
            case NotificationHistoryType.ANSWER_NEW_COMMENT:
                return new AnswerNewCommentViewHolder(parentContextInflater
                        .inflate(R.layout.notify_notification_answer_new_comment,parent,false));
            default:
                return new QuestionVoteUpViewHolder(parentContextInflater
                        .inflate(R.layout.notify_notification_list_question_vote_up,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AnswerVoteUpViewHolder){
            bindAnswerVoteUpViewHolder(holder, position);
        }else if(holder instanceof EditAnswerViewHolder){
            bindEditAnswerViewHolder(holder, position);
        }else if(holder instanceof NewAnswerViewHolder){
            bindNewAnswerViewHolder(holder, position);
        }else if(holder instanceof NoticeMeViewHolder){
            bindNoticeMeViewHolder(holder, position);
        }else if(holder instanceof NoticeMyQuestionViewHolder){
            bindNoticeMyQuestionViewHolder(holder, position);
        }else if(holder instanceof QuestionVoteUpViewHolder){
            bindQuestionVoteUpViewHolder(holder, position);
        }else if(holder instanceof AnswerNewCommentViewHolder){
            bindAnswerNewCommentViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return notificationList.get(position).notificationHistoryType;
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
    private class AnswerVoteUpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView answerVoteUpUserNameView;
        public TextView answerVoteUpAnswerAbstractView;
        public AnswerVoteUpViewHolder(View parentView)
        {
            super(parentView);
            answerVoteUpUserNameView = (TextView)parentView.findViewById(R.id.notify_notification_list_answer_vote_up_user_name);
            answerVoteUpAnswerAbstractView
                    =(TextView)parentView.findViewById(R.id.notify_notification_list_answer_vote_up_answer_abstract);
            answerVoteUpUserNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //when I click on othername
                }
            });
            answerVoteUpAnswerAbstractView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //when I click on question voted up
                }
            });
        }
        @Override
        public void onClick(View v) {
        }
    }
    private class EditAnswerViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView editAnswerItemUserName,editAnswerItemQuestionAbstract,editAnswerItemAnswer;
        public EditAnswerViewHolder(View parentView)
        {
            super(parentView);
            editAnswerItemUserName =(TextView)parentView.findViewById(R.id.notify_notification_list_edit_answer_item_user_name);
            editAnswerItemQuestionAbstract =(TextView)parentView.findViewById(R.id.notify_notification_list_edit_answer_item_question_abstract);
            editAnswerItemAnswer =(TextView)parentView.findViewById(R.id.notify_notification_list_edit_answer_item_answer);
        }
    }
    private class NewAnswerViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView answerItemUserName,answerItemQuestionAbstract,answerItemAnswerAbstract;
        public NewAnswerViewHolder(View parentView)
        {
            super(parentView);
            answerItemUserName =(TextView)parentView.findViewById(R.id.notify_notification_list_answer_item_user_name);
            answerItemQuestionAbstract =(TextView)parentView.findViewById(R.id.notify_notification_list_new_answer_item_question_abstract);
            answerItemAnswerAbstract =(TextView)parentView.findViewById(R.id.notify_notification_list_new_answer_item_answer_abstract);

        }
    }
    private class NoticeMeViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView noticeMeUserName;
        public NoticeMeViewHolder(View parentView)
        {
            super(parentView);
            noticeMeUserName =(TextView)parentView.findViewById(R.id.notify_notification_list_notice_me_user_name);
        }
    }
    private class NoticeMyQuestionViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView noticeMyquestionUserName,noticeMyquestionAbstract;
        public NoticeMyQuestionViewHolder(View parentView)
        {
            super(parentView);
            noticeMyquestionUserName =(TextView)parentView.findViewById(R.id.notify_notification_list_notice_myquestion_user_name);
            noticeMyquestionAbstract =(TextView)parentView.findViewById(R.id.notify_notification_list_notice_myquestion_abstract);
        }
    }
    private class QuestionVoteUpViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView questionVoteUpUserName,questionVoteUpQuestionAbstract;
        public QuestionVoteUpViewHolder(View parentView)
        {
            super(parentView);
            questionVoteUpUserName =(TextView)parentView.findViewById(R.id.notify_notification_list_question_vote_up_user_name);
            questionVoteUpQuestionAbstract =(TextView)parentView.findViewById(R.id.notify_notification_list_question_vote_up_question_abstract);
        }
    }
    private class AnswerNewCommentViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView user_name,answer_abstract,comment_abstract;
        public AnswerNewCommentViewHolder(View parentView)
        {
            super(parentView);
            user_name =(TextView)parentView.findViewById(R.id.user_name);
            answer_abstract =(TextView)parentView.findViewById(R.id.answer_abstract);
            comment_abstract =(TextView)parentView.findViewById(R.id.comment_abstract);
        }
    }
    private void processNotify(RecyclerView.ViewHolder viewHolder, final int i){
        final NotificationDataModel notification = notificationList.get(i);
        if(!notification.hasRead){
            viewHolder.itemView.setBackgroundColor(
                    ColorUtils.getLighterColor(ThemeController.getCurrentColor().mainColor,0.9f)
            );
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notification.hasRead){
                        return ;
                    }
                    notification.hasRead   = true;
                    NotificationDataModel.broadMinusNotification();
                    notifyDataSetChanged();
                }
            });
        }else{
            viewHolder.itemView.setBackgroundResource(R.color.md_text);
        }
    }
    private void bindAnswerVoteUpViewHolder(RecyclerView.ViewHolder viewHolder, final int i)
    {
        processNotify(viewHolder,i);
        final NotificationDataModel notification = notificationList.get(i);
        AnswerVoteUpViewHolder answerVoteUpViewHolder = (AnswerVoteUpViewHolder)viewHolder;
        answerVoteUpViewHolder.answerVoteUpAnswerAbstractView
                .setText(
                        EmojiPatch.fuckEmoji(notificationList.get(i).answerAbstract)

                );
        answerVoteUpViewHolder.answerVoteUpUserNameView
                .setText(
                        EmojiPatch.fuckEmoji
                                (notificationList.get(i).otherSideUserRealName)
                );

        answerVoteUpViewHolder.answerVoteUpAnswerAbstractView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("questionId", notificationList.get(i).questionId);
                intent.putExtra("question_raise_time", TimeLogic.timeLogic(notificationList.get(i).dataTime));
                intent.setClass(context, InnerQuestionActivity.class);
                context.startActivity(intent);
            }
        });

        answerVoteUpViewHolder.answerVoteUpUserNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent().setClass(context, UserInfoActivity.class)
//                                .putExtra("state", "2")
//                                .putExtra("otherUserId", notification.otherSideUserId)
//                );
                UserInfoActivity.start(context,notification.otherSideUserId);
            }
        });
    }
    private void bindEditAnswerViewHolder(RecyclerView.ViewHolder viewHolder,int i)
    {
        processNotify(viewHolder,i);
        final NotificationDataModel notification = notificationList.get(i);
        EditAnswerViewHolder editAnswerViewHolder = (EditAnswerViewHolder)viewHolder;

        editAnswerViewHolder.editAnswerItemAnswer
         .setText(
                 EmojiPatch.fuckEmoji(
                 notificationList.get(i).answerAbstract
                 )
         );

        editAnswerViewHolder.editAnswerItemQuestionAbstract
                .setText(
                        EmojiPatch.fuckEmoji(
                        notificationList.get(i).questionAbstract
                        )
                );

        editAnswerViewHolder.editAnswerItemUserName
                .setText(
                        EmojiPatch.fuckEmoji(
                        notificationList.get(i).answerAbstract
                        )
                );

        editAnswerViewHolder.editAnswerItemAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent().setClass(context, AnswerDetailHTMLActivity.class)
                                .putExtra("key_id", notification.answerId)
                );
            }
        });

        editAnswerViewHolder.editAnswerItemQuestionAbstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent().setClass(context, InnerQuestionActivity.class)
                                .putExtra("questionId", notification.questionId)
                                .putExtra("question_title", notification.questionAbstract)
                );
            }
        });
        editAnswerViewHolder.editAnswerItemUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent().setClass(context, UserInfoActivity.class)
//                                .putExtra("state", "2")
//                                .putExtra("otherUserId", notification.otherSideUserId)
//                );
                UserInfoActivity.start(context,notification.otherSideUserId);
            }
        });
    }
    private void bindNewAnswerViewHolder(final RecyclerView.ViewHolder viewHolder, final int i)
    {
        processNotify(viewHolder,i);
        final NotificationDataModel notification = notificationList.get(i);
        final NewAnswerViewHolder newAnswerViewHolder =(NewAnswerViewHolder) viewHolder;
        newAnswerViewHolder.answerItemAnswerAbstract
                .setText(EmojiPatch.fuckEmoji(
                        notificationList.get(i).answerAbstract
                )
                );
        newAnswerViewHolder.answerItemQuestionAbstract
                .setText(EmojiPatch.fuckEmoji(
                        notificationList.get(i).questionAbstract
                )
                );
        newAnswerViewHolder.answerItemUserName
                .setText(
                        EmojiPatch.fuckEmoji(
                        notificationList.get(i).otherSideUserRealName
                        )
                );
        newAnswerViewHolder.answerItemQuestionAbstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("questionId", notificationList.get(i).questionId);
                intent.putExtra("question_raise_time", TimeLogic.timeLogic(notificationList.get(i).dataTime));
                intent.setClass(context, InnerQuestionActivity.class);
                context.startActivity(intent);
            }
        });
        newAnswerViewHolder.answerItemAnswerAbstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAnswerdDetail = new Intent();
                intentToAnswerdDetail.putExtra("key_id", notificationList.get(i).answerId);
                intentToAnswerdDetail.setClass(context, AnswerDetailHTMLActivity.class);
                context.startActivity(intentToAnswerdDetail);
                viewHolder.itemView.performClick();
            }
        });
        newAnswerViewHolder.answerItemUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent().setClass(context, UserInfoActivity.class)
//                                .putExtra("state", "2")
//                                .putExtra("otherUserId", notification.otherSideUserId)
//                );
                UserInfoActivity.start(context,notification.otherSideUserId);
                viewHolder.itemView.performClick();
            }
        });
    }
    private void bindNoticeMeViewHolder(final RecyclerView.ViewHolder viewHolder,int i)
    {
        processNotify(viewHolder,i);
        final NotificationDataModel notification = notificationList.get(i);
        NoticeMeViewHolder noticeMeViewHolder  = (NoticeMeViewHolder)viewHolder;
        noticeMeViewHolder.noticeMeUserName
                .setText(
                        EmojiPatch.fuckEmoji(
                        notificationList.get(i).otherSideUserRealName
                        )
                );
        noticeMeViewHolder.noticeMeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent().setClass(context, UserInfoActivity.class)
//                                .putExtra("state", "2")
//                                .putExtra("otherUserId", notification.otherSideUserId)
//                );
                UserInfoActivity.start(context,notification.otherSideUserId);
                viewHolder.itemView.performClick();
            }
        });
    }
    private void bindNoticeMyQuestionViewHolder(final RecyclerView.ViewHolder viewHolder,int i)
    {
        processNotify(viewHolder,i);
        final NotificationDataModel notification = notificationList.get(i);
        NoticeMyQuestionViewHolder noticeMyQuestionViewHolder = (NoticeMyQuestionViewHolder)viewHolder;
        noticeMyQuestionViewHolder.noticeMyquestionAbstract
                .setText(
                        EmojiPatch.fuckEmoji(
                        notificationList.get(i).questionAbstract
                        )
                );

        noticeMyQuestionViewHolder.noticeMyquestionUserName
                .setText(
                        EmojiPatch.fuckEmoji(
                        notificationList.get(i).otherSideUserRealName
                        )
                );

        noticeMyQuestionViewHolder.noticeMyquestionAbstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent().setClass(context, InnerQuestionActivity.class)
                                .putExtra("questionId", notification.questionId)
                                .putExtra("question_title", notification.questionAbstract)
                );
                viewHolder.itemView.performClick();
            }
        });
        noticeMyQuestionViewHolder.noticeMyquestionUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent().setClass(context, UserInfoActivity.class)
//                                .putExtra("state", "2")
//                                .putExtra("otherUserId", notification.otherSideUserId)
//                );
                UserInfoActivity.start(context,notification.otherSideUserId);
                viewHolder.itemView.performClick();
            }
        });
    }
    private void bindQuestionVoteUpViewHolder(final RecyclerView.ViewHolder viewHolder,int i)
    {
        processNotify(viewHolder,i);
        final NotificationDataModel notification = notificationList.get(i);
        QuestionVoteUpViewHolder questionVoteUpViewHolder =(QuestionVoteUpViewHolder)viewHolder;
        questionVoteUpViewHolder.questionVoteUpQuestionAbstract
                .setText(
                        EmojiPatch.fuckEmoji(
                        notification.questionAbstract
                        )
                );

        questionVoteUpViewHolder.questionVoteUpUserName
                .setText(
                        EmojiPatch.fuckEmoji(
                        notification.otherSideUserRealName
                        )
                );
        questionVoteUpViewHolder.questionVoteUpQuestionAbstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent().setClass(context, InnerQuestionActivity.class)
                                .putExtra("questionId", notification.questionId)
                                .putExtra("question_title", notification.questionAbstract)
                );
                viewHolder.itemView.performClick();
            }
        });
        questionVoteUpViewHolder.questionVoteUpUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent().setClass(context, UserInfoActivity.class)
//                                .putExtra("state", "2")
//                                .putExtra("otherUserId", notification.otherSideUserId)
//                );
                UserInfoActivity.start(context,notification.otherSideUserId);
                viewHolder.itemView.performClick();
            }
        });
    }


    private void bindAnswerNewCommentViewHolder(final RecyclerView.ViewHolder viewHolder, final int i)
    {
        processNotify(viewHolder,i);
        final NotificationDataModel notification = notificationList.get(i);
        AnswerNewCommentViewHolder localViewHolder =(AnswerNewCommentViewHolder)viewHolder;
        localViewHolder.user_name
                .setText(
                        EmojiPatch.fuckEmoji(
                        notification.otherSideUserRealName
                        )
                );
        localViewHolder.answer_abstract
                .setText(
                        EmojiPatch.fuckEmoji(
                        notification.answerAbstract
                        )
                        );
        localViewHolder.comment_abstract
                .setText(EmojiPatch.fuckEmoji(
                        notification.commentAbstract
                )
                );

        localViewHolder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent().setClass(context, UserInfoActivity.class)
//                                .putExtra("state", "2")
//                                .putExtra("otherUserId", notification.otherSideUserId)
//                );
                UserInfoActivity.start(context,notification.otherSideUserId);
                viewHolder.itemView.performClick();
            }
        });
        localViewHolder.answer_abstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAnswerdDetail = new Intent();
                intentToAnswerdDetail.putExtra("key_id", notificationList.get(i).answerId);
                intentToAnswerdDetail.setClass(context, AnswerDetailHTMLActivity.class);
                context.startActivity(intentToAnswerdDetail);
                viewHolder.itemView.performClick();
            }
        });
        localViewHolder.comment_abstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAnswerdDetail = new Intent();
                intentToAnswerdDetail.putExtra("key_id", notificationList.get(i).answerId);
                intentToAnswerdDetail.setClass(context, AnswerDetailHTMLActivity.class);
                context.startActivity(intentToAnswerdDetail);
                viewHolder.itemView.performClick();
            }
        });
    }
}
