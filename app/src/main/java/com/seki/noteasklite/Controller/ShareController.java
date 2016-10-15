package com.seki.noteasklite.Controller;

import android.content.Context;
import android.text.Html;
import android.util.Pair;
import android.widget.Toast;

import com.google.gson.Gson;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.Bean.ShareNoteResultBean;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;
import com.seki.noteasklite.DataUtil.BusEvent.NoteUploadEvent;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.NetWorkServices.ServiceFactory;
import com.seki.noteasklite.RetrofitHelper.RequestBody.AuthBody;
import com.seki.noteasklite.Util.ShareUtil;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yuan-tian01 on 2016/4/6.
 */
public class ShareController {
    public static void shareNoteText(Context context,String noteSrcContent){
        String plainText = ShareUtil.delHTMLTag(Html.fromHtml(noteSrcContent).toString()+"\r\n分享来自NONo笔记:http://fir.im/vqlj");
        plainText = plainText.replace("img{([\\s\\S]*)*}","");
        ShareUtil.shareText(context,plainText,"分享笔记到:");
    }
    public interface ShareLinkCallBack{
        public void onShareDone(String link);
    }
    public static void getShareLink(final NoteAllArray note, final ShareLinkCallBack shareLinkCallBack){
         new Thread(new Runnable() {
            @Override
            public void run() {
                note.content = NoteController.preProcessNoteContent(note.content);
                ServiceFactory.getShareService().getShareLink(AuthBody.getAuthBodyMap(new Pair<String, String>("note",new Gson().toJson(note))))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ShareNoteResultBean>() {
                            @Override
                            public void call(ShareNoteResultBean shareNoteResultBean) {
                                WonderFull.verify(shareNoteResultBean);
                                if (shareNoteResultBean.state_code == 0) {
                                    EventBus.getDefault().post(new NoteUploadEvent(note,true));
                                    NoteDBHelper.getInstance().updateCloudStateById(note.sdfId,"true");
                                    shareLinkCallBack.onShareDone(shareNoteResultBean.share_link);
                                } else {
                                    Toast.makeText(MyApp.getInstance().getApplicationContext(), "获取外链分享链接失败", Toast.LENGTH_SHORT).show();
                                    ;
                                    shareLinkCallBack.onShareDone(null);
                                }

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(MyApp.getInstance().getApplicationContext(), "获取外链分享链接失败", Toast.LENGTH_SHORT).show();
                                ;
                                shareLinkCallBack.onShareDone(null);
                            }
                        });
            }
         }).start();


    }
}
