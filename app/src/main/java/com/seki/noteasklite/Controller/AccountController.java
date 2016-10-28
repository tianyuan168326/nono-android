package com.seki.noteasklite.Controller;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.seki.noteasklite.DataUtil.AppUserInfo;
import com.seki.noteasklite.DataUtil.Bean.AuthorBean;
import com.seki.noteasklite.DataUtil.Bean.LogOnResponse;
import com.seki.noteasklite.DataUtil.Bean.QQLogOnBean;
import com.seki.noteasklite.DataUtil.Bean.QQLogOnResponse;
import com.seki.noteasklite.DataUtil.Bean.WonderFull;
import com.seki.noteasklite.DataUtil.BusEvent.CloseWaitingDialogEvent;
import com.seki.noteasklite.DataUtil.BusEvent.LogOnSuccess;
import com.seki.noteasklite.DataUtil.BusEvent.OpenWaitingDialogEvent;
import com.seki.noteasklite.DataUtil.BusEvent.UpDateUserInfoSuccessEvent;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateUserInfoFailedEvent;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.NetWorkServices.ServiceFactory;
import com.seki.noteasklite.R;
import com.seki.noteasklite.RetrofitHelper.RequestBody.AuthBody;
import com.seki.noteasklite.Util.EncryptUtils;
import com.seki.noteasklite.Util.NotifyHelper;
import com.seki.noteasklite.Util.SeriesLogOnInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.seki.noteasklite.Util.EncryptUtils.SHA1;

/**
 * Created by yuan on 2016/5/16.
 */
public class AccountController {
    public static void qqLogOn(final QQLogOnBean bean){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("openid",bean.openid);
        hashMap.put("sex",bean.sex);
        hashMap.put("userRealName",bean.userRealName);
        hashMap.put("headImgUrl",bean.headImgUrl);
        ServiceFactory.getAccountService().qqLogOn(hashMap)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QQLogOnResponse>() {
                               @Override
                               public void call(QQLogOnResponse r) {
                                   com.seki.noteasklite.Activity.Util.dismissDialog();
                                   WonderFull.verify(r);
                                    if(r.state_code !=0){
                                        Toast.makeText(MyApp.getInstance().getApplicationContext(),
                                            MyApp.getInstance().getApplicationContext().getString(R.string.logon_info_ero), Toast.LENGTH_SHORT).show();
                                         return ;
                                     }
                                   AppUserInfo userInfo = r.toUserInfo();
                                   userInfo.username = EncryptUtils.MD5("qq-"+bean.openid).substring(0,30).toLowerCase();
                                   userInfo.userpassword = EncryptUtils.MD5(userInfo.username).substring(0,16).toLowerCase();
                                   MyApp.getInstance().userInfo = userInfo;
                                   AccountController.rightAfterLogOn();
                                   EventBus.getDefault().post(new LogOnSuccess());

                                   }
                               }
                            , new Action1<Throwable>() {
                                @Override
                            public void call(Throwable throwable) {
                                    com.seki.noteasklite.Activity.Util.dismissDialog();
                            }
                        });
    }

    private static void rightAfterLogOn(){
        SeriesLogOnInfo.putInfo(MyApp.getInstance().getApplicationContext(), MyApp.userInfo.username, MyApp.userInfo.userpassword);
        EMClient.getInstance().login(MyApp.userInfo.userId,SHA1(MyApp.userInfo.userpassword) ,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", "登陆聊天服务器成功！");
                    }
                });
            }
            @Override
            public void onProgress(int progress, String status) {

            }
            @Override
            public void onError(int code, String message) {
                Log.d("main", "登陆聊天服务器失败！");
            }
        });
        MobclickAgent.onProfileSignIn(MyApp.userInfo.userId);
        NoteController.iniCloudSyncTask();
        ServiceFactory.getAccountService().getAllNote(AuthBody.getAuthBodyMap())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AuthorBean>() {
                    @Override
                    public void call(AuthorBean authorBean) {
                        MyApp.getInstance().authorBean.author = authorBean.author;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void LogOn(final String userName, final String userPassword,boolean isBackGroudTask){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("username",userName);
        hashMap.put("rawpassword",userPassword);
        if(!isBackGroudTask){
            EventBus.getDefault().post(new OpenWaitingDialogEvent());
        }
        ServiceFactory.getAccountService().LogOn(hashMap)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LogOnResponse>() {
                               @Override
                               public void call(LogOnResponse r) {
                                   EventBus.getDefault().post(new CloseWaitingDialogEvent());
                                   WonderFull.verify(r);
                                   if(r.state_code !=0){
                                       Toast.makeText(MyApp.getInstance().getApplicationContext(),
                                               MyApp.getInstance().getApplicationContext().getString(R.string.logon_info_ero), Toast.LENGTH_SHORT).show();
                                       return ;
                                   }
                                   AppUserInfo userInfo = r.toUserInfo();
                                   userInfo.username = userName;
                                   userInfo.userpassword = userPassword;
                                   MyApp.userInfo = userInfo;

                                   AccountController.rightAfterLogOn();
                                   EventBus.getDefault().post(new LogOnSuccess());

                               }
                           }
                        , new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                EventBus.getDefault().post(new CloseWaitingDialogEvent());
                            }
                        });
    }

    public static void updateUserInfo(final String _abstract, final String name, final String headImg){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("user_info_abstract",_abstract);
        hashMap.put("user_info_name",name);
        hashMap.put("user_info_headimg_url",headImg);
        EventBus.getDefault().post(new OpenWaitingDialogEvent());
        ServiceFactory.getAccountService().updateUserInfo(hashMap)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WonderFull>() {
                               @Override
                               public void call(WonderFull wonderFull) {
                                   EventBus.getDefault().post(new CloseWaitingDialogEvent());
                                   if(wonderFull.state_code == 0){
                                       NotifyHelper.makePlainToast(MyApp.getInstance().getApplicationContext().getString(R.string.updata_info_success));
                                       MyApp.userInfo.userHeadPicURL = headImg;
                                       MyApp.userInfo.userAbstract = _abstract;
                                       MyApp.userInfo.userRealName = name;
                                       EventBus.getDefault().post(new UpDateUserInfoSuccessEvent(name,_abstract,headImg));
                                   }else{
                                       NotifyHelper.makePlainToast(MyApp.getInstance().getApplicationContext().getString(R.string.user_info_erro));
                                       EventBus.getDefault().post(new UpdateUserInfoFailedEvent());
                                   }

                               }
                           }
                        , new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                EventBus.getDefault().post(new CloseWaitingDialogEvent());
                            }
                        });
    }

}
