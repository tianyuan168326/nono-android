package com.seki.noteasklite.Controller;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.seki.noteasklite.Activity.LogOnActivity;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.Bean.DeleteNoteBean;
import com.seki.noteasklite.DataUtil.Bean.NoteLabelBean;
import com.seki.noteasklite.DataUtil.Bean.ShareNote;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;
import com.seki.noteasklite.DataUtil.BusEvent.BeginNoteUploadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ChangeNoteGroupEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteDeleteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteInsertEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteLabelSearchDoneEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteUpdateEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteUploadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NotesDeleteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.SearchNoteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateGroupName;
import com.seki.noteasklite.DataUtil.LogStateEnum;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;
import com.seki.noteasklite.DataUtil.Search.LocalNoteArray;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.NetWorkServices.NoteService;
import com.seki.noteasklite.NetWorkServices.ServiceFactory;
import com.seki.noteasklite.OnGetImagePolicy.IOnGetImagePolicy;
import com.seki.noteasklite.OnGetImagePolicy.ImageProcessor;
import com.seki.noteasklite.OnGetImagePolicy.OnGetImageByQiniuSync;
import com.seki.noteasklite.RetrofitHelper.RequestBody.AuthBody;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.noteasklite.ThirdWrapper.PowerStringRequest;
import com.seki.noteasklite.Util.NetWorkUtil;
import com.seki.noteasklite.Util.PreferenceUtils;
import com.seki.noteasklite.Util.SimilarityUtil;
import com.seki.noteasklite.Util.StringProcessor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by yuan-tian01 on 2016/4/1.
 */
//manage all the message!
public class NoteController {
    public static final String NoteChangBroadCast = "com.seki.noteasklite.Controller.NoteController.NoteChangBroadCast";
    public static NoteAllArray deleteNote( DeleteNoteBean deleteNoteBean){
        MyApp.getInstance().getApplicationContext().sendBroadcast(new Intent(NoteChangBroadCast));
        return deleteNote(deleteNoteBean,false);
    }
    public static void deleteNote( DeleteNoteBean[] deleteNoteBeans){
        MyApp.getInstance().getApplicationContext().sendBroadcast(new Intent(NoteChangBroadCast));
         deleteNote(deleteNoteBeans,false);
    }
    public static NoteAllArray deleteNote(DeleteNoteBean deleteNoteBean,boolean alwaysDelCloud ){
        NoteAllArray noteAllArray = NoteDBHelper.getInstance().deleteNoteById(deleteNoteBean.id, deleteNoteBean.group);
        //放入回收站
        RecycleBinController.addRecycleBinNote(new NoteDatabaseArray(noteAllArray));
        EventBus.getDefault().post(new NoteDeleteEvent(new NoteDatabaseArray(noteAllArray),deleteNoteBean.id));
        Toast.makeText(MyApp.getInstance().getApplicationContext(), "已转移至回收站", Toast.LENGTH_SHORT).show();
        boolean is_delete_sync_cloud = PreferenceUtils.getPrefBoolean(MyApp.getInstance().getApplicationContext(),"is_delete_sync_cloud",false);
        if(TextUtils.isEmpty(deleteNoteBean.uuid)){
            return noteAllArray;
        }
        if(!is_delete_sync_cloud && !alwaysDelCloud)
            return noteAllArray;
        if(MyApp.getInstance().userInfo.logStateEnum == LogStateEnum.OFFLINE){
            return noteAllArray;
        }
        if(!NetWorkUtil.isNetworkAvailable(MyApp.getInstance().getApplicationContext())){
            Toast.makeText(MyApp.getInstance().getApplicationContext(),"手机没有联网，云端笔记继续保存",Toast.LENGTH_SHORT).show();;
        }
        NoteService noteService = ServiceFactory.getNoteService();
        noteService.deleteNote(AuthBody.getAuthBodyMap(new Pair<String, String>("note_uuid",String.valueOf(deleteNoteBean.uuid))))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WonderFull>() {
                    @Override
                    public void call(WonderFull wonderFull) {
                        WonderFull.verify(wonderFull);
                        if (wonderFull.state_code == 0) {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "删除云端成功", Toast.LENGTH_SHORT).show();
                            ;
                        } else {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "暂时删除云端笔记异常,请联系作者", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MyApp.getInstance().getApplicationContext(), "暂时删除云端笔记异常,请联系作者~", Toast.LENGTH_SHORT).show();
                        ;
                    }
                });
        return noteAllArray;
    }
    //delete many notes!
    public static void  deleteNote(DeleteNoteBean [] deleteNoteBeans ,boolean alwaysDelCloud ){
        List<String> onCloudNoteUUIDList = new ArrayList<>();
        NoteDatabaseArray [] noteDatabaseArrays = new NoteDatabaseArray[deleteNoteBeans.length];
        long [] iDs = new long[deleteNoteBeans.length];
        int index = 0;
        for (DeleteNoteBean deleteNoteBean:
                deleteNoteBeans) {
            if(deleteNoteBean == null){
                continue;
            }
            NoteAllArray noteAllArray = NoteDBHelper.getInstance().deleteNoteById(deleteNoteBean.id, deleteNoteBean.group);
            if(noteAllArray == null){
                continue;
            }
            if(("true").equals(noteAllArray.isOnCloud)){
                onCloudNoteUUIDList.add(noteAllArray.uuid);
            }
            //放入回收站
            RecycleBinController.addRecycleBinNote(new NoteDatabaseArray(noteAllArray));
            noteDatabaseArrays[index] = new NoteDatabaseArray(noteAllArray);
            iDs[index] = deleteNoteBean.id;
            index++;
        }
        EventBus.getDefault().post(new NotesDeleteEvent(noteDatabaseArrays,iDs));
        Toast.makeText(MyApp.getInstance().getApplicationContext(), "已转移至回收站", Toast.LENGTH_SHORT).show();
        boolean is_delete_sync_cloud = PreferenceUtils.getPrefBoolean(MyApp.getInstance().getApplicationContext(),"is_delete_sync_cloud",false);

        if(!is_delete_sync_cloud && !alwaysDelCloud)
            return ;
        if(MyApp.getInstance().userInfo.logStateEnum == LogStateEnum.OFFLINE){
            return ;
        }
        if(!NetWorkUtil.isNetworkAvailable(MyApp.getInstance().getApplicationContext())){
            Toast.makeText(MyApp.getInstance().getApplicationContext(),"没有网络，暂不能删除云端笔记",Toast.LENGTH_SHORT).show();;
        }
        NoteService noteService = ServiceFactory.getNoteService();
        noteService.deleteNotes(AuthBody.getAuthBodyMap(new Pair<String, String>("note_uuid_list",new Gson().toJson(onCloudNoteUUIDList))))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WonderFull>() {
                    @Override
                    public void call(WonderFull wonderFull) {
                        WonderFull.verify(wonderFull);
                        if (wonderFull.state_code == 0) {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "删除云端成功", Toast.LENGTH_SHORT).show();
                            ;
                        } else {
                            Toast.makeText(MyApp.getInstance().getApplicationContext(), "暂时删除云端笔记异常,请联系作者", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MyApp.getInstance().getApplicationContext(), "暂时删除云端笔记异常,请联系作者~", Toast.LENGTH_SHORT).show();
                        ;
                    }
                });

    }
    public static  long insertNote(NoteDatabaseArray noteDatabaseArray,boolean syncUI){
        if(noteDatabaseArray.uuid == null){
            noteDatabaseArray.uuid = String.valueOf(System.currentTimeMillis());
        }
        if(TextUtils.isEmpty(noteDatabaseArray.Title) && TextUtils.isEmpty(noteDatabaseArray.content)){
            return -1;
        }
        if(TextUtils.isEmpty(noteDatabaseArray.content)){
            noteDatabaseArray.content = " ";
        }
        boolean is_auto_title = PreferenceUtils.getPrefBoolean(MyApp.getInstance().getApplicationContext(),"is_auto_title",true);
        if(is_auto_title){
            if (StringProcessor.isEmpty(noteDatabaseArray.Title)) {
                String plaintext = Html.fromHtml(noteDatabaseArray.content).toString().trim().replace("\n","");
                noteDatabaseArray.Title = plaintext.substring(0,
                        plaintext.length()>7?7:plaintext.length()
                );
            }
        }else{
            if (StringProcessor.isEmpty(noteDatabaseArray.Title)) {
                noteDatabaseArray.Title = "";
            }
        }
        long id;
        id = NoteDBHelper.getInstance().insertNote(noteDatabaseArray);
        if(syncUI){
            EventBus.getDefault().post(new NoteInsertEvent(noteDatabaseArray,id,noteDatabaseArray.uuid));
            MyApp.getInstance().getApplicationContext().sendBroadcast(new Intent(NoteChangBroadCast));
        }
        return id;
    }
    public static  long insertNote(NoteDatabaseArray noteDatabaseArray){
        if(noteDatabaseArray.uuid == null){
            noteDatabaseArray.uuid = String.valueOf(System.currentTimeMillis());
        }
        if(TextUtils.isEmpty(noteDatabaseArray.Title) && TextUtils.isEmpty(noteDatabaseArray.content)){
            return -1;
        }
        if(TextUtils.isEmpty(noteDatabaseArray.content)){
            noteDatabaseArray.content = " ";
        }
        boolean is_auto_title = PreferenceUtils.getPrefBoolean(MyApp.getInstance().getApplicationContext(),"is_auto_title",true);
        if(is_auto_title){
            if (StringProcessor.isEmpty(noteDatabaseArray.Title)) {
                String plaintext = Html.fromHtml(noteDatabaseArray.content).toString().trim().replace("\n","");
                noteDatabaseArray.Title = plaintext.substring(0,
                        plaintext.length()>7?7:plaintext.length()
                );
            }
        }else{
            if (StringProcessor.isEmpty(noteDatabaseArray.Title)) {
                noteDatabaseArray.Title = "";
            }
        }
        long id;
        id = NoteDBHelper.getInstance().insertNote(noteDatabaseArray);
        EventBus.getDefault().post(new NoteInsertEvent(noteDatabaseArray,id,noteDatabaseArray.uuid));
        MyApp.getInstance().getApplicationContext().sendBroadcast(new Intent(NoteChangBroadCast));
        return id;
    }
    //only for local update for the sake of losing connect
    public static long updateNoteLocal(final long sdfId, String groupName, final NoteDatabaseArray noteDatabaseArray, String oldUUid){
        noteDatabaseArray.uuid = oldUUid;
        final long newId;
        noteDatabaseArray.is_on_cloud = "false";
        newId = NoteDBHelper.getInstance().updateNote(sdfId,groupName,noteDatabaseArray);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new NoteUpdateEvent(
                        newId,
                        noteDatabaseArray.content,
                        noteDatabaseArray,
                        sdfId,
                        noteDatabaseArray.uuid
                ));
            }
        });
        if(Looper.myLooper() == Looper.getMainLooper()){
            Toast.makeText(MyApp.getInstance().getApplicationContext(), "转储在本地喵~", Toast.LENGTH_SHORT).show();
        }
        return newId;
    }
     public static long updateNote(long sdfId,String groupName,NoteDatabaseArray noteDatabaseArray,String oldUUid){
         Date now = new Date();
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
         SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
         String date = dateFormat.format(now);
         String time = timeFormat.format(now);
         noteDatabaseArray.date = date;
         noteDatabaseArray.time = time;
         noteDatabaseArray.uuid = oldUUid;
         long newId;
         newId = updateNoteLocal(sdfId,groupName,noteDatabaseArray,oldUUid);
         if(PreferenceUtils.getPrefBoolean(MyApp.getInstance().getApplicationContext(),"is_edit_auto_sync",false) && MyApp.userInfo.logStateEnum == LogStateEnum.ONLINE){
             uploadNoteCloud(new NoteAllArray(noteDatabaseArray,newId));
         }
         MyApp.getInstance().getApplicationContext().sendBroadcast(new Intent(NoteChangBroadCast));
         return newId;
     }
    public static long updateNote(long sdfId,String groupName,NoteDatabaseArray noteDatabaseArray,String oldUUid,boolean syncUI){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(now);
        String time = timeFormat.format(now);
        noteDatabaseArray.date = date;
        noteDatabaseArray.time = time;
        noteDatabaseArray.uuid = oldUUid;
        long newId;
        newId = updateNoteLocal(sdfId,groupName,noteDatabaseArray,oldUUid);
        if(syncUI){
            if(PreferenceUtils.getPrefBoolean(MyApp.getInstance().getApplicationContext(),"is_edit_auto_sync",false)){
                uploadNoteCloud(new NoteAllArray(noteDatabaseArray,newId));
            }
            MyApp.getInstance().getApplicationContext().sendBroadcast(new Intent(NoteChangBroadCast));
        }
        return newId;
    }
    public static void shareNote(final NoteAllArray array){
        if(!NetWorkUtil.isNetworkAvailable(MyApp.getInstance().getApplicationContext())){
            Toast.makeText(MyApp.getInstance().applicationContext, "连接网络后享笔记心情吧~", Toast.LENGTH_SHORT).show();
            NoteController.updateNoteLocal(array.sdfId,array.group,new NoteDatabaseArray(array),array.uuid);
            return;
        }
        if(MyApp.getInstance().userInfo.logStateEnum.equals(LogStateEnum.OFFLINE) ){
            Toast.makeText(MyApp.getInstance().applicationContext, "登陆后分享笔记心情~", Toast.LENGTH_SHORT).show();
            MyApp.getInstance().applicationContext.startActivity(
                    new Intent().setClass(MyApp.getInstance().applicationContext, LogOnActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            return;
        }
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String processedContent =  preProcessNoteContent(array.content);
                subscriber.onNext(processedContent);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<String>() {
                            @Override
                            public void call(String content) {
                                realShareNote(content,array);
                                //reInflatingRecycleViewAndScrollTo((List<NoteAllArray>) noteList, 0);
                            }
                        });
    }
    public static void shareNote(final NoteAllArray array,final String tagList){
        if(!NetWorkUtil.isNetworkAvailable(MyApp.getInstance().getApplicationContext())){
            Toast.makeText(MyApp.getInstance().applicationContext, "连接网络后享笔记心情吧~", Toast.LENGTH_SHORT).show();
            NoteController.updateNoteLocal(array.sdfId,array.group,new NoteDatabaseArray(array),array.uuid);
            return;
        }
        if(MyApp.getInstance().userInfo.logStateEnum.equals(LogStateEnum.OFFLINE) ){
            Toast.makeText(MyApp.getInstance().applicationContext, "登陆后分享笔记心情~", Toast.LENGTH_SHORT).show();
            MyApp.getInstance().applicationContext.startActivity(
                    new Intent().setClass(MyApp.getInstance().applicationContext, LogOnActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            return;
        }
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String processedContent =  preProcessNoteContent(array.content);
                subscriber.onNext(processedContent);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<String>() {
                            @Override
                            public void call(String content) {
                                realShareNote(content,tagList,array);
                                //reInflatingRecycleViewAndScrollTo((List<NoteAllArray>) noteList, 0);
                            }
                        });
    }
    private static void realShareNote(final String content,final String tagList, final NoteAllArray array) {
        MyApp.getInstance().volleyRequestQueue.add(new PowerStringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/add_question.php")
                ,
                new PowerListener() {
                    @Override
                    public void onCorrectResponse(String s) {
                        super.onCorrectResponse(s);
                        Toast.makeText(MyApp.getInstance().getApplicationContext(),"分享笔记心情成功!",Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new ShareNote(array));
                    }
                    @Override
                    public void onJSONStringParseError() {
                        super.onJSONStringParseError();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", MyApp.getInstance().userInfo.quickAskToken);
                params.put("user_name", MyApp.getInstance().userInfo.username);
                params.put("question_title",
                        array.title
                );
                params.put("question_detail", content);
                params.put("tags", tagList);
                params.putAll(super.getParams());
                return params;
            }
        });
    }

    private static void realShareNote(final String content, final NoteAllArray array) {
        MyApp.getInstance().volleyRequestQueue.add(new PowerStringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/add_question.php")
                ,
                new PowerListener() {
                    @Override
                    public void onCorrectResponse(String s) {
                        super.onCorrectResponse(s);
                        Toast.makeText(MyApp.getInstance().getApplicationContext(),"分享笔记心情成功!",Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new ShareNote(array));
                    }
                    @Override
                    public void onJSONStringParseError() {
                        super.onJSONStringParseError();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", MyApp.getInstance().userInfo.quickAskToken);
                params.put("user_name", MyApp.getInstance().userInfo.username);
                params.put("question_title",
                        array.title
                );
                params.put("question_detail", content);
                //params.put("tags", tagsList);
                params.putAll(super.getParams());
                return params;
            }
        });
    }

    public static void uploadNoteCloud(final NoteAllArray array){
        if("true".equals(array.isOnCloud)){
            return ;
        }
        if(!NetWorkUtil.isNetworkAvailable(MyApp.getInstance().getApplicationContext())){
            Toast.makeText(MyApp.getInstance().applicationContext, "连接网络后创建云笔记吧~", Toast.LENGTH_SHORT).show();
            NoteController.updateNoteLocal(array.sdfId,array.group,new NoteDatabaseArray(array),array.uuid);
            return;
        }
        if(MyApp.getInstance().userInfo.logStateEnum.equals(LogStateEnum.OFFLINE) ){
            Toast.makeText(MyApp.getInstance().applicationContext, "登陆后创建云笔记~", Toast.LENGTH_SHORT).show();
//            MyApp.getInstance().applicationContext.startActivity(
//                    new Intent().setClass(MyApp.getInstance().applicationContext, LogOnActivity.class)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            );
            return;
        }
        EventBus.getDefault().post(new BeginNoteUploadEvent(array.uuid));
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String processedContent =  preProcessNoteContent(array.content);
                subscriber.onNext(processedContent);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<String>() {
                            @Override
                            public void call(String content) {
                                realUploadNote(content,array);
                                //reInflatingRecycleViewAndScrollTo((List<NoteAllArray>) noteList, 0);
                            }
                        });
    }

    public static void changeGroup(long sdfId,String oldReel, String currentReel) {
        NoteDBHelper.getInstance().changeGroup(sdfId,currentReel);
        NoteReelsController.reelNoteExchange(oldReel,currentReel,1);

        EventBus.getDefault().post(new ChangeNoteGroupEvent(currentReel,oldReel,sdfId));
    }

    public static String getNoteUuid(long noteTempId) {
        NoteAllArray array = NoteDBHelper.getInstance().getNoteById(noteTempId);
        if(array ==null){
            return null;
        }else{
            return  array.uuid;
        }
    }




    private static class StringWrapper{
        public String s;
    }

    public static String  preProcessNoteContent(final String content) {
        List<String > localFilePathList = NotePersistenceController.extractNoteImagePaths(content);
        if(localFilePathList.size() ==0) {
            return content;
        }
        //process the fuck async Method to sync!!!

        final CountDownLatch latch = new CountDownLatch(localFilePathList.size());
        final StringWrapper stringWrapper = new StringWrapper();
        stringWrapper.s = content;
        for (final String realpath :
                localFilePathList) {
            if(realpath.startsWith("http")){
                continue;
            }
            new OnGetImageByQiniuSync(new IOnGetImagePolicy.OnRealPath() {
                @Override
                public void realPath(String altedPath) {
                    if(TextUtils.isEmpty(altedPath)){
                        Toast.makeText(MyApp.getInstance().getApplicationContext(),"原始图片已丢失！跳过此图片！",Toast.LENGTH_SHORT).show();
                    }else{
                        stringWrapper.s =  stringWrapper.s.replace(realpath,altedPath);
                    }
                    latch.countDown();
                }
            }){
                @Override
                public String preImageProcess(String srcPath) {
                    String processedPath =  ImageProcessor.compressImage(MyApp.getInstance().getApplicationContext(),srcPath);
                    return processedPath;
                }
            }.getRealImagePath(realpath);
        }
        try{
            latch.await();
        }catch (Exception e){

        }
        return stringWrapper.s;
    }
    private static  void realUploadNote(final String content, final NoteAllArray array) {
        StringRequest uploadCloudRequest = new PowerStringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/note/uploadNote.php")
                , new PowerListener(){
            @Override
            public void onCorrectResponse(String s) {
                super.onCorrectResponse(s);
                int stateCode = -1;
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                    stateCode = jsonObject.getInt("state_code");
                } catch (Exception e) {

                }
                if (stateCode == 0) {
                    array.isOnCloud="true";
                    Toast.makeText(MyApp.getInstance().getApplicationContext(), "成功自动保存一条笔记到云，喵~", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new NoteUploadEvent(array,true));
                    NoteDBHelper.getInstance().updateCloudStateById(array.sdfId,"true");
                } else {
                    NoteController.updateNoteLocal(array.sdfId,array.group,new NoteDatabaseArray(array),array.uuid);
                    EventBus.getDefault().post(new NoteUploadEvent(array,false));

                }
            }

            @Override
            public void onJSONStringParseError() {
                NoteController.updateNoteLocal(array.sdfId,array.group,new NoteDatabaseArray(array),array.uuid);
                super.onJSONStringParseError();
            }

            @Override
            public void onResponse(String s) {
                super.onResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NoteController.updateNoteLocal(array.sdfId,array.group,new NoteDatabaseArray(array),array.uuid);
                EventBus.getDefault().post(new NoteUploadEvent(array,false));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",MyApp.getInstance().userInfo.userId);
                params.put("title", array.title);
                params.put("group", array.group);
                params.put("content", content);
                params.put("date", array.date);
                params.put("time", array.time);
                params.put("uuid", array.uuid);
                params.putAll(super.getParams());
                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(uploadCloudRequest);
    }

    public static void iniCloudSyncTask() {
        boolean is_auto_backup = PreferenceUtils.getPrefBoolean(MyApp.getInstance().getApplicationContext(),"is_auto_backup",false);
        if(!is_auto_backup){
            return ;
        }
        if(!NetWorkUtil.isWifi(MyApp.getInstance().getApplicationContext())){
            return ;
        }
        List<NoteAllArray> noteDatabaseArray = NoteDBHelper.getInstance().getHistoryNote();
        for (NoteAllArray array:
                noteDatabaseArray) {
            if("0".equals(array.uuid))
            {
                continue;
            }
            NoteController.uploadNoteCloud(array);
        }
    }
    private static  ExecutorService searchService = null;
    private static Future<Void> oldSearchTask = null;
    public static void searchNote(final String keyWord){
        if(searchService ==null){
            searchService  = new ThreadPoolExecutor(1,1,0L, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>());
        }
        if(oldSearchTask !=null){
            oldSearchTask.cancel(true);
        }
        oldSearchTask = searchService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if(TextUtils.isEmpty(keyWord)){
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new SearchNoteEvent(null,new ArrayList<LocalNoteArray>()));
                                }
                            });

                }else{
                    final List<LocalNoteArray> noteList = NoteDBHelper.getInstance().searchNote(keyWord);
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new SearchNoteEvent(keyWord,noteList));
                                }
                            });
                }
                return null;
            }
        });
    }
    public static void searchLabel(final String s) {
        if(searchService ==null){
            searchService  = new ThreadPoolExecutor(1,1,0L, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>());
        }
        if(oldSearchTask !=null){
            oldSearchTask.cancel(true);
        }
        oldSearchTask = searchService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if(TextUtils.isEmpty(s)){
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new NoteLabelSearchDoneEvent(null));
                                }
                            });
                    return null;
                }
                final List<NoteLabelBean> labels =NoteLabelController.getHistoryLabels();
                for (NoteLabelBean label :
                        labels) {
                    Double dis = 0.0;
                    if((dis = SimilarityUtil.sim(label.getLabel(),s))<0.1){
                        labels.remove(label);
                    }
                }
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(new NoteLabelSearchDoneEvent(labels));
                            }
                        });
                return null;
            }
        });

    }
    public static void alterGroupName(String oldReel, String newReel) {
        NoteDBHelper.getInstance().updateGroupName(oldReel,newReel);
        EventBus.getDefault().post(new UpdateGroupName(oldReel,newReel));
        NoteReelsController.alterReelName(oldReel,newReel);
    }
    public static void saveNoteZipAsync(final SaveNoteListener l){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String zipFilePath =  NotePersistenceController.saveNoteFiles();
                subscriber.onNext(zipFilePath);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<String>() {
                            @Override
                            public void call(String path) {
                                l.onZipPath(path);
                            }
                        });
    }
    public static void saveNoteTxtAsync(final SaveNoteListener l) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String txtFilePath =  NotePersistenceController.saveNoteTxtFile();
                subscriber.onNext(txtFilePath);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<String>() {
                            @Override
                            public void call(String path) {
                                Toast.makeText(MyApp.getInstance().getApplicationContext(),"TXT备份文件保存在"+path,Toast.LENGTH_LONG).show();
                                l.onZipPath(path);
                            }
                        });
    }
}
