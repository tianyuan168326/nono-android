package com.seki.noteasklite.Controller;

import android.support.annotation.IntegerRes;
import android.util.EventLog;
import android.util.Pair;
import android.widget.Toast;

import com.google.gson.Gson;
import com.seki.noteasklite.DataUtil.Bean.AuthorBean;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;
import com.seki.noteasklite.DataUtil.BusEvent.EditCommunityItemEvent;
import com.seki.noteasklite.DataUtil.BusEvent.PostAnswerFailEvent;
import com.seki.noteasklite.DataUtil.BusEvent.PostAnswerSuccessEvent;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateTagEvent;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.NetWorkServices.ServiceFactory;
import com.seki.noteasklite.RetrofitHelper.RequestBody.AuthBody;

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
        ServiceFactory.getPostService().removePost(AuthBody.getAuthBodyMap(new Pair<String, String>("post_id",String.valueOf(id))))
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
        ServiceFactory.getPostService().changePostTags(AuthBody.getAuthBodyMap(new Pair<String, String>("post_id",id)
                ,new Pair<String, String>("tagList",tagListString)))
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
        ServiceFactory.getPostService().editPost(AuthBody.getAuthBodyMap(new Pair<String, String>("question_id",question_id)
                ,new Pair<String, String>("question_title",title)
                ,new Pair<String, String>("question_detail",content)
                ,new Pair<String, String>("tags",tagList)
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
        ServiceFactory.getPostService().newPost(AuthBody.getAuthBodyMap(
                new Pair<String, String>("access_token",MyApp.getInstance().userInfo.quickAskToken),
                new Pair<String, String>("user_name",MyApp.getInstance().userInfo.username),
                new Pair<String, String>("key_detail",answerDetail),
                new Pair<String, String>("question_id",questionId)
        ))
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
    public static void updatePostTags(int id, List<String> tagList){
        updatePostTags(String.valueOf(id),tagList);
    }
}
