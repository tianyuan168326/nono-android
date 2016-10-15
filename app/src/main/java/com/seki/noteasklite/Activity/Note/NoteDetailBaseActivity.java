package com.seki.noteasklite.Activity.Note;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.seki.noteasklite.Activity.LogOnActivity;
import com.seki.noteasklite.Activity.Util;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Controller.NoteReelsController;
import com.seki.noteasklite.Controller.ShareController;
import com.seki.noteasklite.CustomControl.Share.SharePanelView;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.Bean.DeleteNoteBean;
import com.seki.noteasklite.DataUtil.BusEvent.ChangeNoteGroupEvent;
import com.seki.noteasklite.DataUtil.BusEvent.FadeGoneMainFABEvent;
import com.seki.noteasklite.DataUtil.BusEvent.FadeVisibleMainFABEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteUpdateEvent;
import com.seki.noteasklite.DataUtil.BusEvent.TransCommunityEvent;
import com.seki.noteasklite.DataUtil.LogStateEnum;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.FuckBreaker;
import com.seki.noteasklite.Util.NotifyHelper;
import com.seki.noteasklite.Util.ShareUtil;
import com.seki.therichedittext.ColorPanel;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2016/5/26.
 */
public abstract class NoteDetailBaseActivity extends BaseActivity {
    protected boolean magicFlag=false;
    protected FloatingActionButton editFab;
    protected NoteAllArray currentNoteInfo;
    protected   OpenNoteDelegate.NoteUIControl ui;
    //在总列表中的位置
    protected int indexInList;
    protected  String keyWord = "";
    protected void processIntent(Intent intent) {
        currentNoteInfo = intent.getParcelableExtra("openNote");
        indexInList = intent.getIntExtra("index", 0);
        keyWord = intent.getStringExtra("keyWord");
        ui = intent.getParcelableExtra("ui");
        if(currentNoteInfo.sdfId>0){
            currentNoteInfo.content = NoteDBHelper.getInstance().getContentById(currentNoteInfo.sdfId);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_fab:
                edit();
                break;
        }
    }

    protected abstract void edit();


    @SuppressWarnings("unused")
    protected void deleteNote() {
        //NoteDBHelper.getInstance().deleteNoteById(currentNoteInfo.sdfId, currentNoteInfo.group);
        AlertDialog isCleanCloudDialog=  new AlertDialog.Builder(this)
                .setMessage("是否同步删除云端笔记?")
                .setPositiveButton("是的喵!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteAllArray noteAllArray = NoteController.deleteNote(
                                new DeleteNoteBean(
                                        currentNoteInfo.sdfId, currentNoteInfo.group,currentNoteInfo.uuid
                                )
                                ,true);
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("才不呢!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteAllArray noteAllArray =  NoteController.deleteNote(
                                new DeleteNoteBean(
                                        currentNoteInfo.sdfId, currentNoteInfo.group,null
                                )
                                );
                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }
    @SuppressWarnings("unused")
    protected void changeGroup(){
        List<String> allGroup  = NoteReelsController.getReels();
        allGroup.remove(currentNoteInfo.group);
        if(allGroup.size()==0){
            Toast.makeText(this,"你只有一个文集，快去新建一个新的文集吧~",Toast.LENGTH_LONG).show();
            return ;
        }
        final String[] groups = allGroup.toArray(new String[1]);

        AlertDialog.Builder  groupDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("移动笔记到")
                .setItems(groups, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String currentGroup = groups[which];
                        NoteController.changeGroup(currentNoteInfo.sdfId,currentNoteInfo.group,currentGroup);
                    }
                });
        groupDialogBuilder.show();

    }
    public void changeBgColor(){
        new ColorPanel(this, Color.WHITE).setOnColorChoseCallback(new ColorPanel.OnColorChoseCallback() {
            @Override
            public void onColorChose(@ColorInt int color) {
                AppPreferenceUtil.setDetailBgColor(color);
                loadBgColor();
            }
        });
    }
    protected   void loadBgColor(){
        if(getNestedScrollView()!=null){
            getNestedScrollView().setBackgroundColor(AppPreferenceUtil.getDetailBgColor());
        }

        real_toolbar.setBackgroundColor(AppPreferenceUtil.getDetailHeadBgColor());
    }
    public class NoteHeadContentViewPair{
        public View headView;
        public View contentView;

        public NoteHeadContentViewPair(View headView, View contentView) {
            this.headView = headView;
            this.contentView = contentView;
        }
    }
    protected abstract NoteHeadContentViewPair getSnapShotViews();
    PopupWindow popupWindow ;
    protected void shareNoteImage() {
        final NoteHeadContentViewPair viewPair = getSnapShotViews();
        //contentTV.setText(Html.fromHtml(currentNoteInfo.content+"<br>分享来自NONo笔记", imageGetter, htmlTagHandler), TextView.BufferType.SPANNABLE);
        popupWindow = NotifyHelper.popUpWaitingAnimation(this,popupWindow);
        final TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(viewPair.contentView.getPaddingLeft(),0,0,0);
        tv.layout(0,0,findViewById(R.id.real_toolbar).getWidth(),findViewById(R.id.real_toolbar).getHeight()/2);
        tv.setBackgroundColor(getResources().getColor(android.R.color.white));
        tv.setTextColor(getResources().getColor(R.color.md_second_color));
        tv.setTextSize(getResources().getDimension(R.dimen.md_caption_text_size)/6);
        tv.setText("分享来自 NONo笔记 APP,via nonobiji.com");
        final Bitmap bitmap = ShareUtil.createViewBitmap(viewPair.headView,viewPair.contentView,tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String imagePath = ShareUtil.saveViewToPic(NoteDetailBaseActivity.this,bitmap);
                NoteDetailBaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NoteDetailBaseActivity.this, "图片保存在" + imagePath, Toast.LENGTH_LONG).show();
                        NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
                        ShareUtil.shareImage(NoteDetailBaseActivity.this,imagePath,"分享NONo笔记图片");
                    }
                });

            }
        }).start();
    }
    protected void saveImage() {
        NoteHeadContentViewPair viewPair = getSnapShotViews();
        //contentTV.setText(Html.fromHtml(currentNoteInfo.content+"<br>分享来自NONo笔记", imageGetter, htmlTagHandler), TextView.BufferType.SPANNABLE);
        popupWindow = NotifyHelper.popUpWaitingAnimation(this,popupWindow);
        final Bitmap bitmap = ShareUtil.createViewBitmap(viewPair.headView,viewPair.contentView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String imagePath = ShareUtil.saveViewToPic(NoteDetailBaseActivity.this,bitmap);
                NoteDetailBaseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NoteDetailBaseActivity.this, "图片保存在" + imagePath, Toast.LENGTH_LONG).show();
                        NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
                    }
                });

            }
        }).start();
    }
    protected abstract  void shareText();
    protected abstract NestedScrollView  getNestedScrollView();
    protected  void afterRegisterWidgets(){
        loadBgColor();

        if(TextUtils.isEmpty(keyWord)){
            renderNormalNote();
        }else{
            renderHighLightNote();
        }
    }
    protected abstract  void renderNormalNote();
    protected abstract  void renderHighLightNote();
    @Override
    public void setContentView(int layoutResID, String title) {
        super.setContentView(layoutResID, title);
        afterRegisterWidgets();
    }
    protected TextView note_detail_groupname;
    protected TextView note_detail_time;
    //the title zone
    View real_toolbar;
    @Override
    protected void registerWidget() {
        note_detail_groupname =$(R.id.note_detail_groupname) ;
        note_detail_groupname.setText(currentNoteInfo.title);
        real_toolbar =$(R.id.real_toolbar) ;
        note_detail_time =$(R.id.note_detail_time) ;
        editFab = $(R.id.edit_fab);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showHelp(editFab);
            }
        },1000);

        bindViewsToOnClickListenerById(R.id.note_detail_content,R.id.edit_fab);
        NestedScrollView nestedScrollView = getNestedScrollView();
        if(nestedScrollView == null){
            Log.e(NONoConfig.TAG_NONo,"note detail activity must have at lease one NestedScrollView");
        }else {
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int dy = scrollY - oldScrollY;
                    if (dy < 0) {
                        if (magicFlag) {
                            magicFlag = !magicFlag;
                            editFab.setVisibility(View.VISIBLE);
                        }
                    } else if (dy > 0) {
                        if (!magicFlag) {
                            magicFlag = !magicFlag;
                            editFab.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
        //iniFAB();
        if(((TextView)$(R.id.note_detail_text_num)).getText().toString().equals("0")){
            showTextNum(currentNoteInfo.content);
        }
    }

    protected abstract void showHelp(View shareFab);

    protected void showTextNum(final String htmlContent) {
        if(htmlContent ==null){
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView)$(R.id.note_detail_text_num))
                        .setText(String.valueOf(
                                FuckBreaker.fuckBreaker(htmlContent).length()
                        ));
            }
        },700);
    }
    private void iniFAB() {
//        shareFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View shareLayout = LayoutInflater.from(NoteDetailBaseActivity.this).inflate(R.layout.dlg_share_note,null,false);
//                View root_text = shareLayout.findViewById(R.id.root_text);
//                View root_image = shareLayout.findViewById(R.id.root_image);
//                final AlertDialog dlg =new AlertDialog.Builder(NoteDetailBaseActivity.this).setView(shareLayout)
//                        .show();
//                root_text.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dlg.dismiss();
//                        shareText();
//                    }
//                });
//                root_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dlg.dismiss();
//                        shareImage();
//                    }
//                });
//            }
//        });
    }
     protected  abstract  void  setNewContent(String content);
    public void onEventMainThread(NoteUpdateEvent event) {
        currentNoteInfo.content = event.getNoteContent();
        setNewContent(currentNoteInfo.content);

        currentNoteInfo.title = event.getNoteDatabaseArray().Title;
        currentNoteInfo.date  = event.getNoteDatabaseArray().date;
        currentNoteInfo.time  = event.getNoteDatabaseArray().time;
        note_detail_groupname.setText(currentNoteInfo.title);
        note_detail_time.setText(currentNoteInfo.date+" "+currentNoteInfo.time);
        //setSpanClickable();
        currentNoteInfo.sdfId = event.getNewNoteId();
        //setResult(RESULT_OK, data);
    }

    public void onEventMainThread(ChangeNoteGroupEvent event) {
        if(currentNoteInfo.group.equals(event.getOldGroup())){
            currentNoteInfo.group = event.getCurrentGroup();
            toolBar.setTitle(currentNoteInfo.group);
            setTitle(currentNoteInfo.group);
        }
        //setResult(RESULT_OK, data);
    }

    @Override
    public void onBackPressed() {
//        if(revealView.isShown()){
//            FabTransformation.with(shareFab)
//                    .transformFrom(revealView);
//            return;
//        }
        super.onBackPressed();

    }
    protected  void shareNote(){
        SharePanelView.openPanel(this)
                .setShareInterface(new SharePanelView.ShareInterface() {
                    @Override
                    public void shareImage() {
                        shareNoteImage();
                    }

                    @Override
                    public void sharePlainText() {
                        shareText();
                    }

                    @Override
                    public void shareWeChat() {
                        if(MyApp.userInfo.logStateEnum == LogStateEnum.OFFLINE){
                            LogOnActivity.start(NoteDetailBaseActivity.this);
                            MyApp.toast("登陆以外链形式分享富文本笔记");
                            return ;
                        }
                        popupWindow = NotifyHelper.popUpWaitingAnimation(NoteDetailBaseActivity.this,popupWindow);
                        ShareController.getShareLink(currentNoteInfo, new ShareController.ShareLinkCallBack() {
                            @Override
                            public void onShareDone(String link) {
                                NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
                                if(link == null){
                                    return;
                                }
                                WXWebpageObject webpage = new WXWebpageObject();
                                webpage.webpageUrl  =link;
                                WXMediaMessage msg = new WXMediaMessage(webpage);
                                msg.title = currentNoteInfo.title;
                                msg.description = TextUtils.substring(currentNoteInfo.content,0,50);
                                msg.thumbData = Util.bmpToByteArray(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_noteasklite2),true);
                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = String.valueOf(System.currentTimeMillis());
                                req.message = msg;
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                                SharePanelView.getIWXAPI().sendReq(req);
                            }
                        });
                    }

                    @Override
                    public void shareWeChatCircle() {
                        if(MyApp.userInfo.logStateEnum == LogStateEnum.OFFLINE){
                            LogOnActivity.start(NoteDetailBaseActivity.this);
                            MyApp.toast("登陆以外链形式分享富文本笔记");
                            return ;
                        }
                        popupWindow = NotifyHelper.popUpWaitingAnimation(NoteDetailBaseActivity.this,popupWindow);
                        ShareController.getShareLink(currentNoteInfo, new ShareController.ShareLinkCallBack() {
                            @Override
                            public void onShareDone(String link) {
                                NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
                                if(link == null){
                                    return;
                                }
                                WXWebpageObject webpage = new WXWebpageObject();
                                webpage.webpageUrl  =link;
                                WXMediaMessage msg = new WXMediaMessage(webpage);
                                msg.title = currentNoteInfo.title;
                                msg.description = TextUtils.substring(currentNoteInfo.content,0,50);
                                msg.thumbData = Util.bmpToByteArray(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_noteasklite2),true);
                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = String.valueOf(System.currentTimeMillis());
                                req.message = msg;
                                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                SharePanelView.getIWXAPI().sendReq(req);
                            }
                        });
                    }

                    @Override
                    public void shareWeibo() {
                        if(MyApp.userInfo.logStateEnum == LogStateEnum.OFFLINE){
                            LogOnActivity.start(NoteDetailBaseActivity.this);
                            MyApp.toast("登陆以外链形式分享富文本笔记");
                            return ;
                        }
                    }

                    @Override
                    public void shareNONo() {
                        if(MyApp.userInfo.logStateEnum == LogStateEnum.OFFLINE){
                            LogOnActivity.start(NoteDetailBaseActivity.this);
                            MyApp.toast("登陆以外链形式分享富文本笔记");
                            return ;
                        }
                        if(currentNoteInfo.title.endsWith(".md")){
                            EventBus.getDefault().post(new FadeGoneMainFABEvent());
                            Snackbar.make(NoteDetailBaseActivity.this.findViewById(android.R.id.content),"MarkDown笔记暂时不能分享到社区",Snackbar.LENGTH_SHORT)
                                    .setCallback(new Snackbar.Callback() {
                                        @Override
                                        public void onDismissed(Snackbar snackbar, int event) {
                                            super.onDismissed(snackbar, event);
                                            EventBus.getDefault().post(new FadeVisibleMainFABEvent());
                                        }
                                    })
                                    .show();
                            return;
                        }
                        new AlertDialog.Builder(NoteDetailBaseActivity.this)
                                .setMessage("是否将美美的笔记分享到答记模块?")
                                .setNegativeButton("不用", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        NoteController.shareNote(currentNoteInfo);
                                        EventBus.getDefault().post(new TransCommunityEvent());
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void shareQQ() {
                        if(MyApp.userInfo.logStateEnum == LogStateEnum.OFFLINE){
                            LogOnActivity.start(NoteDetailBaseActivity.this);
                            MyApp.toast("登陆以外链形式分享富文本笔记");
                            return ;
                        }
                        popupWindow = NotifyHelper.popUpWaitingAnimation(NoteDetailBaseActivity.this,popupWindow);
                        ShareController.getShareLink(currentNoteInfo, new ShareController.ShareLinkCallBack() {
                            @Override
                            public void onShareDone(String link) {
                                NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
                                if(link == null){
                                    return;
                                }
                                final Bundle params = new Bundle();
                                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                                params.putString(QQShare.SHARE_TO_QQ_TITLE,  currentNoteInfo.title);
                                params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  TextUtils.substring(currentNoteInfo.content,0,50));
                                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  link);
                                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://http://o8r5rbkhb.bkt.clouddn.com/logo.png");
                                params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "NONo笔记");
                                SharePanelView.getTencentQQApi().shareToQQ(NoteDetailBaseActivity.this, params, new IUiListener() {
                                    @Override
                                    public void onComplete(Object o) {
                                        MyApp.toast("分享成功");
                                    }

                                    @Override
                                    public void onError(UiError uiError) {
                                        MyApp.toast("分享错误");
                                    }

                                    @Override
                                    public void onCancel() {
                                        MyApp.toast("取消分享");
                                    }
                                });
                            }
                        });

                    }

                    @Override
                    public void shareQQZone() {
                        if(MyApp.userInfo.logStateEnum == LogStateEnum.OFFLINE){
                            LogOnActivity.start(NoteDetailBaseActivity.this);
                            MyApp.toast("登陆以外链形式分享富文本笔记");
                            return ;
                        }
                        popupWindow = NotifyHelper.popUpWaitingAnimation(NoteDetailBaseActivity.this,popupWindow);
                        ShareController.getShareLink(currentNoteInfo, new ShareController.ShareLinkCallBack() {
                            @Override
                            public void onShareDone(String link) {
                                NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
                                if(link == null){
                                    return;
                                }
                                final Bundle params = new Bundle();
                                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                                params.putString(QQShare.SHARE_TO_QQ_TITLE,  currentNoteInfo.title);
                                params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  TextUtils.substring(currentNoteInfo.content,0,50));
                                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  link);
                                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://http://o8r5rbkhb.bkt.clouddn.com/logo.png");
                                params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "NONo笔记");
                                SharePanelView.getTencentQQApi().shareToQQ(NoteDetailBaseActivity.this, params, new IUiListener() {
                                    @Override
                                    public void onComplete(Object o) {
                                        MyApp.toast("分享成功");
                                    }

                                    @Override
                                    public void onError(UiError uiError) {
                                        MyApp.toast("分享错误");
                                    }

                                    @Override
                                    public void onCancel() {
                                        MyApp.toast("取消分享");
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void copyLink() {
                        if(MyApp.userInfo.logStateEnum == LogStateEnum.OFFLINE){
                            LogOnActivity.start(NoteDetailBaseActivity.this);
                            MyApp.toast("登陆以外链形式分享富文本笔记");
                            return ;
                        }
                        popupWindow = NotifyHelper.popUpWaitingAnimation(NoteDetailBaseActivity.this,popupWindow);
                        ShareController.getShareLink(currentNoteInfo, new ShareController.ShareLinkCallBack() {
                            @Override
                            public void onShareDone(String link) {
                                NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
                                if(link == null){
                                    return;
                                }
                                ClipboardManager clipboard = (ClipboardManager)
                                        getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboard.setPrimaryClip(ClipData.newPlainText("NONo-link",link));
                                MyApp.toast("链接已复制到你的剪切板，快去复制分享吧！");
                            }
                        });
                    }
                });
    }
    @Override
    protected void themePatch() {
        super.themePatch();
        if(ui != null){
            if(getSupportActionBar() !=null){
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ui.toolBarColor));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ui.getStatusBarColor());
            }
//            note_detail_groupname.setTextColor(ui.toolBarColor);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharePanelView.getTencentQQApi().onActivityResult(requestCode, resultCode, data);
    }


}
