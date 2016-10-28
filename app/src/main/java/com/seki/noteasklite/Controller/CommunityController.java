package com.seki.noteasklite.Controller;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.gson.Gson;
import com.seki.noteasklite.DataUtil.Bean.AllCommentListBean;
import com.seki.noteasklite.DataUtil.Bean.OldNotificationsBean;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;
import com.seki.noteasklite.DataUtil.BusEvent.AllCommentEvent;
import com.seki.noteasklite.DataUtil.BusEvent.CloseWaitingDialogEvent;
import com.seki.noteasklite.DataUtil.BusEvent.EditCommunityItemEvent;
import com.seki.noteasklite.DataUtil.BusEvent.GetOldNotificationsDoneEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoticeUserStateChangedEvent;
import com.seki.noteasklite.DataUtil.BusEvent.OpenWaitingDialogEvent;
import com.seki.noteasklite.DataUtil.BusEvent.PostAnswerFailEvent;
import com.seki.noteasklite.DataUtil.BusEvent.PostAnswerSuccessEvent;
import com.seki.noteasklite.DataUtil.BusEvent.RefreshCommentEvent;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateTagEvent;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.NetWorkServices.ServiceFactory;
import com.seki.noteasklite.RetrofitHelper.RequestBody.AuthBody;
import com.seki.noteasklite.Util.NotifyHelper;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yuan on 2016/5/2.
 */
public class CommunityController {
    public  static void removePost(String id){
        removePost(Integer.valueOf(id));
    }
    public  static void removePost(int id){
        ServiceFactory.getPostService().removePost(AuthBody.getAuthBodyMap(new Pair<>("post_id", String.valueOf(id))))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WonderFull>() {
                    @Override
                    public void call(WonderFull wonderFull) {
                        WonderFull.verify(wonderFull);
                        if (wonderFull.state_code == 0) {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "删除Post成功~", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "权限不足!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MyApp.getInstance().getApplicationContext(), "服务器生病了，请稍后再试~", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void updatePostTags(final String id, final List<String> tagList){
        String tagListString = new Gson().toJson(tagList);
        ServiceFactory.getPostService().changePostTags(AuthBody.getAuthBodyMap(new Pair<>("post_id", id)
                , new Pair<>("tagList", tagListString)))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WonderFull>() {
                    @Override
                    public void call(WonderFull wonderFull) {
                        WonderFull.verify(wonderFull);
                        if (wonderFull.state_code == 0) {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "更改讨论标签成功~", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new UpdateTagEvent(id,tagList));
                        } else {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "权限不足!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MyApp.getInstance().getApplicationContext(), "服务器生病了，请稍后再试~", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void editPost(final String question_id,final String title,final String content ,final String tagList){
        ServiceFactory.getPostService().editPost(AuthBody.getAuthBodyMap(new Pair<>("question_id", question_id)
                , new Pair<>("question_title", title)
                , new Pair<>("question_detail", content)
                , new Pair<>("tags", tagList)
        ))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WonderFull>() {
                    @Override
                    public void call(WonderFull wonderFull) {
                        WonderFull.verify(wonderFull);
                        if (wonderFull.state_code == 0) {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "乾坤大挪移成功~", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new EditCommunityItemEvent(question_id,title,content,tagList));
                        } else  if (wonderFull.state_code == -2){
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "乾坤大挪移失败，想必是服务器坏了!", Toast.LENGTH_SHORT).show();
                        }
                        else if (wonderFull.state_code == -1){
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "权限不足!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MyApp.getInstance().getApplicationContext(), "服务器生病了，请稍后再试~", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void postNew(String answerDetail,String questionId){
        ServiceFactory.getPostService().newPost(
                AuthBody.getAuthBodyMap(
                        new Pair<>("access_token", MyApp.getInstance().userInfo.quickAskToken),
                        new Pair<>("user_name", MyApp.getInstance().userInfo.username),
                        new Pair<>("key_detail", answerDetail),
                        new Pair<>("question_id", questionId))
        )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WonderFull>() {
                    @Override
                    public void call(WonderFull wonderFull) {
                        if(wonderFull.state_code ==0){
                            EventBus.getDefault().post(new PostAnswerSuccessEvent());
                        }else{
                            EventBus.getDefault().post(new PostAnswerFailEvent());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new PostAnswerFailEvent());
                    }
                });
    }
    @SuppressWarnings("unused")
    public static void updatePostTags(int id, List<String> tagList){
        updatePostTags(String.valueOf(id),tagList);
    }

    public static void postReplyNewComment(String key_id, final String reply_comment){
        if(TextUtils.isEmpty(reply_comment))
        {
            NotifyHelper.makePlainToast("评论不能为空~");
            return ;
        }
        EventBus.getDefault().post(new OpenWaitingDialogEvent());
        ServiceFactory.getPostService().newReplyComment(AuthBody.getAuthBodyMap(
                new Pair<>("access_token", MyApp.userInfo.quickAskToken),
                new Pair<>("user_name", MyApp.userInfo.username),
                new Pair<>("key_id", key_id),
                new Pair<>("answer_comment", reply_comment)))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WonderFull>() {
                    @Override
                    public void call(WonderFull wonderFull) {
                        EventBus.getDefault().post(new CloseWaitingDialogEvent());
                        if(wonderFull.state_code ==0){
                            Log.d("nono","add reply comment success!");
                            EventBus.getDefault().post(new RefreshCommentEvent(reply_comment));
                        }else{
                            NotifyHelper.makePlainToast("发表评论失败!");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new CloseWaitingDialogEvent());
                    }
                });
    }

    public static void getReplyCommentAll(String key_id){
        ServiceFactory.getPostService().getReplyCommentAll(AuthBody.getAuthBodyMap(
                new Pair<>("key_id", key_id)
                ))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AllCommentListBean>() {
                    @Override
                    public void call(AllCommentListBean commentList) {
                       EventBus.getDefault().post(new AllCommentEvent(commentList));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void noticeOther(final String other_user_id, final boolean prepareToNotice){
        HashMap<String,String> params = new HashMap<>();
        params.put("me_id",MyApp.userInfo.userId);
        params.put("user_id",other_user_id);
        params.put("is_tonotice",String.valueOf(prepareToNotice));
        ServiceFactory.getPostService().noticeOther(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<WonderFull>() {
                            @Override
                            public void call(WonderFull wonderFull) {
                                boolean nowNoticeState = !prepareToNotice;
                                EventBus.getDefault().post(new NoticeUserStateChangedEvent(other_user_id,nowNoticeState));
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                boolean nowNoticeState = prepareToNotice;
                                EventBus.getDefault().post(new NoticeUserStateChangedEvent(other_user_id,nowNoticeState));
                            }
                        }
                );
    }

    public static void getOldNotifications(){
        HashMap<String,String> params = new HashMap<>();
        params.put("user_name", MyApp.getInstance().userInfo.username);
        params.put("access_token", "");
        ServiceFactory.getPostService().getOldNotifications(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<OldNotificationsBean>() {
                            @Override
                            public void call(OldNotificationsBean oldNotificationsBean) {
                                EventBus.getDefault().post(new GetOldNotificationsDoneEvent(oldNotificationsBean.notificationsList));
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        }
                );
    }

    public static void voteForReply(final String key_id, final String vote_type, final String preVoteType){
        HashMap<String,String> params = new HashMap<>();
        params.put("answer_id", key_id);
        params.put("vote_type", vote_type);
        params.put("pre_vote_type", preVoteType);
        params.put("user_name", MyApp.getInstance().userInfo.username);
        ServiceFactory.getPostService().voteForReply(AuthBody.getAuthBodyMap(
                params
        ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<WonderFull>() {
                            @Override
                            public void call(WonderFull wonderFull) {

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        }
                );
    }

}
