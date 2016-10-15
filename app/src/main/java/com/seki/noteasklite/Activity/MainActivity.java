
package com.seki.noteasklite.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.konifar.fab_transformation.FabTransformation;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
import com.seki.noteasklite.Activity.Ask.CommunityEditActivity;
import com.seki.noteasklite.Activity.Ask.NotifyActivity;
import com.seki.noteasklite.Activity.Note.NoteReelEditActivity;
import com.seki.noteasklite.AsyncTask.LogOnAsyncTask;
import com.seki.noteasklite.Base.BaseActivityWithDrawer;
import com.seki.noteasklite.Base.BaseFragment;
import com.seki.noteasklite.Base.StateMachineCodeProcessHandle;
import com.seki.noteasklite.Controller.ImportNoteListener;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Controller.NotePersistenceController;
import com.seki.noteasklite.Controller.NoteReelsController;
import com.seki.noteasklite.Controller.RecycleBinController;
import com.seki.noteasklite.Controller.SaveNoteListener;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.CustomControl.BadgeView;
import com.seki.noteasklite.DataUtil.ActivityEnum;
import com.seki.noteasklite.DataUtil.Bean.NotificationDataModel;
import com.seki.noteasklite.DataUtil.BusEvent.AddLabelEvent;
import com.seki.noteasklite.DataUtil.BusEvent.DateFABGoneMainFABEvent;
import com.seki.noteasklite.DataUtil.BusEvent.DateFABVisibleMainFABEvent;
import com.seki.noteasklite.DataUtil.BusEvent.DoneImportNoteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.FadeGoneMainFABEvent;
import com.seki.noteasklite.DataUtil.BusEvent.FadeVisibleMainFABEvent;
import com.seki.noteasklite.DataUtil.BusEvent.HideAnimEvent;
import com.seki.noteasklite.DataUtil.BusEvent.LogOnSuccess;
import com.seki.noteasklite.DataUtil.BusEvent.ShowAnimEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ToogleGroup2ListEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ToogleGroup2TableEvent;
import com.seki.noteasklite.DataUtil.BusEvent.TransCommunityEvent;
import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.DataUtil.LogStateEnum;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;
import com.seki.noteasklite.DataUtil.NoteReelArray;
import com.seki.noteasklite.DataUtil.UserLogInfo;
import com.seki.noteasklite.Delegate.EditNoteDelegate;
import com.seki.noteasklite.Fragment.Ask.AskFragment;
import com.seki.noteasklite.Fragment.Note.AllFragment;
import com.seki.noteasklite.Fragment.Note.DateFragment;
import com.seki.noteasklite.Fragment.Note.LabelFragment;
import com.seki.noteasklite.Fragment.Note.RecycleBinFragment;
import com.seki.noteasklite.Fragment.Note.ReelFragment;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.OnGetImagePolicy.ImageProcessor;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.FIRUtils;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.GetPathFromUri4kitkat;
import com.seki.noteasklite.Util.SeriesLogOnInfo;
import com.seki.noteasklite.Util.ShareUtil;
import com.seki.noteasklite.Util.SystemProcessor;
import com.seki.noteasklite.Util.TimeLogic;
import com.umeng.analytics.MobclickAgent;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivityWithDrawer implements StateMachineCodeProcessHandle, Observer{

    public static boolean magicFlag = false;
    private UserLogInfo userLogInfo;
    private SimpleDraweeView menuUserHeadimg;
    private TextView userUserName;
    private TextView menuLogOnOrUniversityName;
    private TextView menuSignUpOrSchoolName;
    private Set<String> groupSet = new HashSet<>();
    private int transFragmentCounter = 0;
    private AppBarLayout appBarLayout;
    private LinearLayout fab_op_pannel;
    private LinearLayout btn_md;
    private LinearLayout btn_text;
    private LinearLayout btn_new_group;
    private LinearLayout btn_new_label;
    FloatingActionButton note_add_fab;
    View overlay;
    private int offset = 0;
    public static void start(Context context){
        context.startActivity(new Intent().setClass(context,MainActivity.class));
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |  View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }

                registerStateMachineCodeProcessHandle(MainActivity.this);
                openEventBus();
                setContentView(R.layout.activity_main, "全部");
                installCustomActionView();
                notifyNumChangedReceiver = new NotifyNumChangedReceiver();
                navigationView.setCheckedItem(R.id.nav_note_all);
                checkActivityState(savedInstanceState);
                checkIsFisrtAfterUpdate();
                checkFirstInTeaching();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        updateGroupSet();
                    }
                }).start();
                checkLogInfo();
                checkUpdate();
                MyApp.newNotificationNotifier.addObserver(MainActivity.this);
    }

    private void checkFirstInTeaching() {
        SharedPreferences sharedPreferences = getSharedPreferences("verifing",MODE_PRIVATE);
        String  is_first= sharedPreferences.getString("is_first",null);
        if(!TextUtils.equals(is_first,"true") ) {
            NoteReelsController.addReel(new NoteReelArray("探索NONo",
                    "一步步指引你探索NONo!",null,0,null));
            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title1),
                    getResources().getString(R.string.firstin_content1), "false", NoteDatabaseArray.MARK_FIRST_IN, null));
            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title2),
                    getResources().getString(R.string.firstin_content2), "false", NoteDatabaseArray.MARK_FIRST_IN, null));
            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title3),
                    getResources().getString(R.string.firstin_content3), "false", NoteDatabaseArray.MARK_FIRST_IN, null));
            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title4),
                    getResources().getString(R.string.firstin_content4), "false", NoteDatabaseArray.MARK_FIRST_IN, null));

            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title5),
                    getResources().getString(R.string.firstin_content5), "false", NoteDatabaseArray.MARK_FIRST_IN, null));
            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title6),
                    getResources().getString(R.string.firstin_content6), "false", NoteDatabaseArray.MARK_FIRST_IN, null));
            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title7),
                    getResources().getString(R.string.firstin_content7), "false", NoteDatabaseArray.MARK_FIRST_IN, null));
            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title8),
                    getResources().getString(R.string.firstin_content8), "false", NoteDatabaseArray.MARK_FIRST_IN, null));
            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title9),
                    getResources().getString(R.string.firstin_content9), "false", NoteDatabaseArray.MARK_FIRST_IN, null));

            NoteController.insertNote(new NoteDatabaseArray("探索NONo",
                    TimeLogic.getNowTimeFormatly("yyyy/MM/dd"),
                    TimeLogic.getNowTimeFormatly("HH:mm:ss"),
                    getResources().getString(R.string.firstin_title10),
                    getResources().getString(R.string.firstin_content10), "false", NoteDatabaseArray.MARK_FIRST_IN, null));
            sharedPreferences.edit().putString("is_first","true").apply();
        }
    }

    private View action_search;
    private View action_message;

    private void installCustomActionView() {
        action_search = $(R.id.action_search);
        action_message = $(R.id.action_message);

        action_message.setOnClickListener(this);
        action_messageBadge = new BadgeView(this);
        action_messageBadge.setTargetView(action_message);
        action_search.setOnClickListener(this);
    }


    private void checkUpdate() {
        if(AppPreferenceUtil.isNeedCheckUpdate()){
            FIRUtils.checkForUpdate(this,false);
        }
    }


    private void checkIsFisrtAfterUpdate() {
        if(false && SystemProcessor.getVersionCode(this)>getSharedPreferences("versionCodesp",MODE_PRIVATE).getInt("vc",0) ){
            getSharedPreferences("versionCodesp",MODE_PRIVATE).edit().putInt("vc",SystemProcessor.getVersionCode(this)).apply();
            new AlertDialog.Builder(this)
                    .setTitle("本次更新")
                    .setMessage("修复 笔记上传云端可能会重复的bug\n" +
                            "修复 表格样式时显示不正常的bug\n" +
                            "修复了其他的一些小问题\n" +
                            "更多的细节升级\n" )
                    .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
//    public void registerGroupSet() {
//        List<RFACLabelItem> items = new ArrayList<>();
//        for (int i = 0; i < MyApp.groupInfo.size(); i++) {
//            NoteSimpleArray key = MyApp.groupInfo.get(i);
//            items.add(new RFACLabelItem<Integer>().setLabel(key.title).setWrapper(i + 1)
//                    .setDrawable(new ColorDrawable(Color.TRANSPARENT)));
//        }
//        items.add(new RFACLabelItem<Integer>().setLabel("新的分组")
//                        .setWrapper(0)
//                        .setLabelColor(ContextCompat.getColor(this, R.color.colorAccent))
//                        .setDrawable(new ColorDrawable(Color.TRANSPARENT))
//                        .setIconNormalColor(Color.rgb(0x37, 0x47, 0x4f))
//                        .setIconPressedColor(Color.rgb(0x26, 0x32, 0x38))
//        );
//        mountPostNote();
//    }
    void mountPostQuestion(){
        note_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommunityEditActivity.start(MainActivity.this,null,null,null);
                //EditNoteDelegate.start(MainActivity.this,EditNoteDelegate.RICH_MODE);
            }
        });
    }
    void mountPostNote(){
        note_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(note_add_fab.isShown()){
                    FabTransformation.with(note_add_fab).setOverlay(overlay). transformTo(fab_op_pannel);
                }
            }
        });
    }
    private void checkLogInfo() {
        if (MyApp.userInfo.logStateEnum.equals(LogStateEnum.OFFLINE)) {
            startFirstLogOn();
        } else {
            startFirstControlsSetting();
        }
    }


    public FloatingActionButton getFloatingActionButton() {
        return this.note_add_fab;
    }

//    private void updateGroupSet() {
//        SharedPreferences preferences = getSharedPreferences("groupState", Activity.MODE_PRIVATE);
//        groupSet = preferences.getStringSet("groups", groupSet);
//        if (groupSet.size() <= 0) {
//            SharedPreferences.Editor editor = preferences.edit();
//            groupSet.add("生活随笔");
//            groupSet.add("新手指引");
//            editor.putStringSet("groups", groupSet);
//
//            editor.apply();
//        }
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                registerGroupSet();
//            }
//        });
//    }

    private void checkActivityState(Bundle savedInstanceState) {
        if (transFragmentCounter == 0) {
            if (savedInstanceState == null) {
                transFragment("全部");
            } else {
                transFragment(savedInstanceState.getString("fragment"));
            }
        }
    }

    /**
     * first time to log on,need to execute the logOn task
     */
    private void startFirstLogOn() {
        HashMap<String, String> loginfoHashMap = SeriesLogOnInfo.getInfo(MainActivity.this);
        final String userName = loginfoHashMap.get("username");
        final String userPassword = loginfoHashMap.get("userpassword");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LogOnAsyncTask(getApplicationContext(), userLogInfo, ActivityEnum.MainActivtyEnum, userName, userPassword).execute();
            }
        });
    }

    /**
     * first time to open main activity,need to set control state etc.
     */
    private void startFirstControlsSetting() {
        userLogInfo.menuLogOnOrUniversityName.setText(MyApp.userInfo.userUniversity);
        userLogInfo.menuSignUpOrSchoolName.setText(MyApp.userInfo.userSchool);
        FrescoImageloadHelper.removeCacheFromDisk(Uri.parse(MyApp.userInfo.userHeadPicURL).toString());
        FrescoImageloadHelper.simpleLoadImageFromURL(userLogInfo.menuUserHeadimg,
                MyApp.userInfo.userHeadPicURL);
        userLogInfo.userUserName.setText(MyApp.userInfo.userRealName);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fab_op_pannel.isShown()) {
            FabTransformation.with(note_add_fab).setOverlay(overlay). transformFrom(fab_op_pannel);
        }
        else {
            super.onBackPressed();
        }
    }
    private void closeFab(){
        if(fab_op_pannel.isShown()) {
            FabTransformation.with(note_add_fab).setOverlay(overlay). transformFrom(fab_op_pannel);
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.head_img_view:
                if (MyApp.userInfo.logStateEnum == LogStateEnum.OFFLINE) {
                    startActivity(new Intent()
                            .setClass(MainActivity.this,
                                    LogOnActivity.class));
                } else {
                    UserInfoActivity.start(this,MyApp.getInstance().userInfo.userId);
                }
                break;
            case R.id.note_fab_add:
//                toggleFabMenu();
                break;
            case R.id.head_setting:
                fuckSettingHandler();
                break;
            case R.id.action_search:
                searchHandler();
                break;
            case R.id.action_message:
                checkMessage();
                break;
            case R.id.action_toogle_table_list:
                ToogleGroupListStyle();
                break;
            case R.id.overlay:
                closeFab();
                break;
            case R.id.btn_md:
                EditNoteDelegate.start(MainActivity.this,EditNoteDelegate.MD_MODE);
                closeFab();
                break;
            case R.id.btn_text:
                EditNoteDelegate.start(MainActivity.this,EditNoteDelegate.RICH_MODE);
                closeFab();
                break;
            case R.id.btn_new_group:
                NoteReelEditActivity.start(this,null);
                break;
            case R.id.btn_new_label:
                EventBus.getDefault().post(new AddLabelEvent());
                break;
            default:
        }
    }
    ImageView action_toogle_table_list;
    GroupListStyle style = GroupListStyle.LIST;
    private void ToogleGroupListStyle() {
        if(style == GroupListStyle.LIST){
            style = GroupListStyle.TABLE;
            setGroupListStyle(GroupListStyle.TABLE);
            AppPreferenceUtil.setGroupStyle(GroupListStyle.TABLE);
        }else if(style == GroupListStyle.TABLE){
            style = GroupListStyle.LIST;
            setGroupListStyle(GroupListStyle.LIST);
            AppPreferenceUtil.setGroupStyle(GroupListStyle.LIST);
        }
    }
    private void setGroupListStyle(GroupListStyle style){
        this.style = style;
        if(style == GroupListStyle.LIST){
            action_toogle_table_list.setImageResource(R.drawable.ic_view_module_white_24dp);
            EventBus.getDefault().post(new ToogleGroup2ListEvent());
        }else if(style == GroupListStyle.TABLE){
            action_toogle_table_list.setImageResource(R.drawable.ic_list_white_24dp);

            EventBus.getDefault().post(new ToogleGroup2TableEvent());
        }
    }


    BadgeView action_messageBadge;
    ImageView headBgImgaeView;

    @Override
    protected void registerDrawer() {
        super.registerDrawer();
        headBgImgaeView=(ImageView)headerLayout.findViewById(R.id.nav_header_back);
        loadHeadBgImage();
        final ArrayList<String > imagePaths = new ArrayList<>();
        mountPostNote();
        headBgImgaeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FishBun.with(MainActivity.this)
                        .setAlbumThumnaliSize(150)//you can resize album thumnail size
                        .setActionBarColor(ThemeController.getCurrentColor().mainColor)           // only actionbar color
                        .setPickerCount(1)//you can restrict photo count
                        .setPickerSpanCount(5)
                        .setArrayPaths(imagePaths)
                        .setRequestCode(Define.ALBUM_REQUEST_CODE) //request code is 11. default == Define.ALBUM_REQUEST_CODE(27)
                        .setCamera(true)//you can use camera
                        .textOnImagesSelectionLimitReached("只允许选择一张图片!")
                        .textOnNothingSelected("未选择图片")
                        .setButtonInAlbumActiviy(true)
                        .startAlbum();
            }
        });
    }
    @Override
    protected void registerWidget() {
        View v = findViewById(R.id.note_fab_add);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int dy = offset - verticalOffset;
                offset = verticalOffset;
                toggleFABAnimation(MainActivity.this, dy, note_add_fab,true);
            }
        });
        action_toogle_table_list =  $(R.id.action_toogle_table_list);
        action_toogle_table_list.setOnClickListener(this);
        style = AppPreferenceUtil.getGroupStyle();
        setGroupListStyle(style);
        fab_op_pannel = $(R.id.fab_op_pannel);
        btn_md = $(R.id.btn_md);
        btn_text = $(R.id.btn_text);
        btn_new_group = $(R.id.btn_new_group);
        btn_new_label = $(R.id.btn_new_label);
        note_add_fab = $(R.id.note_fab_add);
        overlay = $(R.id.overlay);
        overlay.setOnClickListener(this);
        btn_md.setOnClickListener(this);
        btn_text.setOnClickListener(this);
        btn_new_group.setOnClickListener(this);
        btn_new_label.setOnClickListener(this);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
       // mGestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private void loadHeadBgImage() {
        try{
            headBgImgaeView.setImageDrawable(
                    new BitmapDrawable(
                            ImageProcessor.zoomImageMin(
                                   BitmapFactory.decodeFile(AppPreferenceUtil.getHeadBgPath())
                                    , getResources().getDisplayMetrics().widthPixels
                                    , getResources().getDisplayMetrics().widthPixels)
                    )
                    );
            headBgImgaeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }catch (Exception e){
            if(AppPreferenceUtil.getHeadBgPath()!=null){
                Toast.makeText(MainActivity.this, "原菜单背景图已损坏或被删除，加载默认背景", Toast.LENGTH_SHORT).show();
            }
            headBgImgaeView.setImageDrawable(ImageProcessor.zoomImageMin(
                    ContextCompat.getDrawable(this, R.drawable.navigation_header)
                    , getResources().getDisplayMetrics().widthPixels
                    , getResources().getDisplayMetrics().widthPixels));
        }

    }



    @Override
    protected void registerAdapters() {
    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        setMenuResId(R.menu.main);
        HashMap<Integer, String> idMethosNamePaire = new HashMap<Integer, String>();
        return idMethosNamePaire;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save_note:
                saveNoteHandler();
                break;
            case R.id.action_import_note:
                importNoteHandler();
                break;
            case R.id.action_save_note_txt:
                saveTxtHandler();
                break;
            case R.id.action_app_settings:
                fuckSettingHandler();
                break;
            case R.id.action_log_out:
                logOutHandler();
                break;
            case R.id.action_log_in:
                logInHandler();
                break;
            case R.id.action_delete_all:
                deleteAllRecycleBinHandler();
                break;
            case R.id.action_restore_all:
                restoreAllRecycleBinHandler();
                break;
            case R.id.action_pay_developer:
                payDeveloper();
                break;
            case R.id.action_share_nono:
//                final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//                        {
////                                SHARE_MEDIA.WEIXIN,
////                                SHARE_MEDIA.WEIXIN_CIRCLE,
////                                SHARE_MEDIA.SINA,
//                                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.DOUBAN
//                        };
//                new ShareAction(this).setDisplayList( displaylist )
//                        .withText( "推荐一款流畅好用的笔记软件:NONo笔记,http://coolapk.com/apk/com.seki.noteasklite" )
//                        .withTitle("NONo笔记")
//                        .withTargetUrl("http://coolapk.com/apk/com.seki.noteasklite")
//                        .withMedia( new UMImage(this,R.drawable.ic_launcher_noteasklite2))
//                        .setListenerList(new UMShareListener() {
//                            @Override
//                            public void onResult(SHARE_MEDIA share_media) {
//                                Toast.makeText(MainActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                                Toast.makeText(MainActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCancel(SHARE_MEDIA share_media) {
//                                Toast.makeText(MainActivity.this,"取消分享",Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .open();
            ShareUtil.shareText(this,"推荐一款流畅好用的笔记软件:NONo笔记,http://coolapk.com/apk/com.seki.noteasklite，" +
                    "很不错，希望大家下载下来试试用","通过");
                break;

        }
        return true;
    }

    private void payDeveloper() {
        final String url  = "https://qr.alipay.com/apn53z12n7xb6c1x4f";
        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        startActivity(intent);
    }

    private void logInHandler() {
        startActivity(new Intent()
                .setClass(MainActivity.this,
                        LogOnActivity.class));
    }

    private void saveTxtHandler() {
        //可以获取文字内容
        //ShareUtil.delHTMLTag(bindingNoteList.get(position).content)
        NoteController.saveNoteTxtAsync(new SaveNoteListener() {
            @Override
            public void onZipPath(final String path) {
                if(path == null){
                    Toast.makeText(MainActivity.this,"存储空间不足",Toast.LENGTH_SHORT).show();
                }else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("是否分享?")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ShareUtil.shareFile(MainActivity.this,path,"分享备份文件到");
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }
            }
        });
    }

    //恢复所有回收站笔记
    private void restoreAllRecycleBinHandler() {
        RecycleBinController.restoreAllRecycleBinNote();
    }
    //删除所有回收站笔记
    private void deleteAllRecycleBinHandler() {
        new AlertDialog.Builder(this)
                .setTitle("真的要永久删除所有笔记吗?不可恢复!")
                .setPositiveButton("不要", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("一定要", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RecycleBinController.removeAllRecycleBinNote();
                    }
                })
            .show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(fragmentType== FragmentType.RECYCLEBIN){
            showRecycleBinMenu(menu);
        }else{
            showNoteMenu(menu);
        }
        checkLogStateMenu(menu);
        return true;
    }

    private void checkLogStateMenu(Menu menu) {
        menu.findItem(R.id.action_log_in).setVisible(
               ! MyApp.userInfo.logStateEnum.equals(LogStateEnum.ONLINE));
        menu.findItem(R.id.action_log_out).setVisible(
                MyApp.userInfo.logStateEnum.equals(LogStateEnum.ONLINE));
    }

    private void showGroupToolMenu() {
        $(R.id.action_toogle_table_list).setVisibility(View.VISIBLE);
    }
    private void showNoteToolMenu() {
        $(R.id.action_toogle_table_list).setVisibility(View.VISIBLE);
    }
    private void showRecycleBinToolMenu() {
        $(R.id.action_toogle_table_list).setVisibility(View.VISIBLE);
    }
    private void showNoteMenu(Menu menu) {

        menu.findItem(R.id.action_save_note).setVisible(true);
        menu.findItem(R.id.action_import_note).setVisible(true);
        menu.findItem(R.id.action_app_settings).setVisible(true);
        menu.findItem(R.id.action_log_out).setVisible(true);

        menu.findItem(R.id.action_delete_all).setVisible(false);
        menu.findItem(R.id.action_restore_all).setVisible(false);


    }

    private void showRecycleBinMenu(Menu menu) {
        menu.findItem(R.id.action_save_note).setVisible(false);
        menu.findItem(R.id.action_import_note).setVisible(false);
        menu.findItem(R.id.action_app_settings).setVisible(false);
        menu.findItem(R.id.action_log_out).setVisible(false);
        menu.findItem(R.id.action_delete_all).setVisible(true);
        menu.findItem(R.id.action_restore_all).setVisible(true);
    }


    private void importNoteHandler() {
        //fucking system Intent!
        Toast.makeText(this,"请导入NONo/自动备份文件夹中的.zip文件！",Toast.LENGTH_LONG).show();
        showFileChooser();
    }
    public static  final int FILE_SELECT_CODE =3;
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult( Intent.createChooser(intent, "选择文件导入"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请安装文件管理器！",  Toast.LENGTH_SHORT).show();
        }
    }
    private void saveNoteHandler() {
        final AlertDialog.Builder  control = new AlertDialog.Builder(this);
        control.setView(R.layout.layout_more_progress);
        control.setTitle("正在归档所有笔记...");
        final Dialog d = control.show();
        NoteController.saveNoteZipAsync(new SaveNoteListener() {
            @Override
            public void onZipPath(final String path) {
                d.setTitle("归档完成！");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"笔记归档保存在了"+path,Toast.LENGTH_LONG).show();
                        ShareUtil.shareFile(MainActivity.this,path,"保存笔记归档文件到:");
                        d.dismiss();
                    }
                },500);
            }
        });
    }

    @SuppressWarnings("unused")
    private void searchHandler() {
        startActivity(new Intent().setClass(this, SearchActivity.class));
    }


    @SuppressWarnings("unused")
    private void logOutHandler() {
        resetDrawerUI();
        Toast.makeText(this,"账号注销成功",Toast.LENGTH_SHORT).show();
        MobclickAgent.onProfileSignOff();
        MyApp.userInfo.reset();
        SeriesLogOnInfo.clearInfo(this);
    }
    @SuppressWarnings("unused")
    private void fuckSettingHandler(){
       PreferenceActivity.start(this);
    }
    public static class LogOutEvent{

    }
    @SuppressWarnings("unused")
    public void onEventMainThread(LogOutEvent event) {
        Toast.makeText(this,"账号信息异常，请回到主界面侧滑重新登录",Toast.LENGTH_LONG).show();
        logOutHandler();
    }

    private void resetDrawerUI() {
        menuUserHeadimg.setImageResource(R.mipmap.ic_person_white_48dp);
        userUserName.setText(R.string.log_on_hint);
        menuLogOnOrUniversityName.setVisibility(View.GONE);
        menuSignUpOrSchoolName.setVisibility(View.GONE);
    }
    private void loadMenuUI(){
        if(TextUtils.isEmpty(MyApp.getInstance().userInfo.userHeadPicURL) ){
            return ;
        }
        FrescoImageloadHelper.simpleLoadImageFromURL(menuUserHeadimg, MyApp.getInstance().userInfo.userHeadPicURL);
        if(TextUtils.isEmpty(MyApp.getInstance().userInfo.userRealName)){
            return;
        }
        userUserName.setText(MyApp.getInstance().userInfo.userRealName);
        if(TextUtils.isEmpty(MyApp.getInstance().userInfo.userUniversity)){
            return;
        }
        menuLogOnOrUniversityName.setText(MyApp.getInstance().userInfo.userUniversity);
        if(TextUtils.isEmpty(MyApp.getInstance().userInfo.userSchool)){
            return;
        }
        menuSignUpOrSchoolName.setText(MyApp.getInstance().userInfo.userSchool);
    }
    @SuppressWarnings("unused")
    private void checkMessage() {
        startActivity(new Intent()
                .setClass(this, NotifyActivity.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        findViewById(R.id.note_fab_add).setVisibility(View.VISIBLE);
        btn_new_group.setVisibility(View.GONE);
        btn_new_label.setVisibility(View.GONE);
        mountPostNote();
        switch (item.getItemId()) {
            case R.id.nav_note_all:
                transFragment("全部");
                break;
            case R.id.nav_recycle_bin:
                transFragment("回收站");
                break;
            case R.id.nav_note_group:
                btn_new_group.setVisibility(View.VISIBLE);
                transFragment("文集");
                break;
            case R.id.nav_note_date:
                transFragment("日期");
                break;
            case R.id.nav_quickask_question:
                mountPostQuestion();
                transFragment("答记");
                //findViewById(R.id.note_fab_add).setVisibility(View.GONE);
                break;
            case R.id.nav_note_label:
                btn_new_label.setVisibility(View.VISIBLE);
                transFragment("标签");
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    boolean isRecycleBin  =false;
    boolean isDataButtonShow = false;
    public void transFragment(String title) {
        EventBus.getDefault().post(new DateFABGoneMainFABEvent());
        isRecycleBin =false;
        isDataButtonShow  = false;
        transFragmentCounter++;
        getSupportActionBar().setTitle(title);
        ArrayList<String> oddTitleArray = new ArrayList<>();
        oddTitleArray.add("全部");
        oddTitleArray.add("回收站");
        oddTitleArray.add("答记");
        fragmentType = FragmentType.UNKNOW;
        if (oddTitleArray.contains(title)) {
            processFragmentWithOddTitle(title);
        } else {
            processContentFragment(title);
        }
        afterTansformFragment();
    }

    private void afterTansformFragment() {
        if(fragmentType == FragmentType.GROUP){
            showGroupToolMenu();
        }else if(fragmentType == FragmentType.RECYCLEBIN){
            showRecycleBinToolMenu();
        }else{
            showNoteToolMenu();
        }

    }
    private void processContentFragment(String title) {
        if(TextUtils.isEmpty(title)){
            return;
        }
        BaseFragment contentFragment =  (BaseFragment) getSupportFragmentManager().findFragmentByTag(title);
        switch (title){
            case "文集":
                fragmentType = FragmentType.GROUP;
                if(contentFragment == null){
                    contentFragment  =new ReelFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, contentFragment, title).commit();
                }
                break;
            case "日期":
                fragmentType = FragmentType.DATE;
                isDataButtonShow = true;
                if(contentFragment == null){
                    contentFragment  =new DateFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, contentFragment, title).commit();
                }
                EventBus.getDefault().post(new DateFABVisibleMainFABEvent());
                break;
            case "标签":
                fragmentType = FragmentType.LABEL;
                if(contentFragment == null){
                    contentFragment  =new LabelFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, contentFragment, title).commit();
                }
                break;
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if( getSupportFragmentManager().getFragments()!=null){
            for (android.support.v4.app.Fragment f: getSupportFragmentManager().getFragments()
                    ) {
                transaction.hide(f);
            }
        }
        transaction.show(contentFragment);
        transaction.commit();
    }
    enum  FragmentType{
        ALL,
        RECYCLEBIN,
        GROUP,
        DATE,
        LABEL,
        ASK,
        UNKNOW
    }
    FragmentType fragmentType = FragmentType.UNKNOW;
    private void processFragmentWithOddTitle(String title) {
        BaseFragment contentFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(title);
        switch (title) {
            case "全部":
                fragmentType = FragmentType.ALL;
                if(contentFragment == null){
                    contentFragment  =new AllFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, contentFragment, title).commit();
                }
                break;
            case "答记":
                fragmentType = FragmentType.ASK;
                if(contentFragment == null){
                    contentFragment  =new AskFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, contentFragment, title).commit();
                }
                break;
            case "回收站":
                fragmentType = FragmentType.RECYCLEBIN;
                if(contentFragment == null){
                    contentFragment  =new RecycleBinFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, contentFragment, title).commit();
                }
                isRecycleBin = true;
                break;
        }
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if( getSupportFragmentManager().getFragments()!=null){
                for (android.support.v4.app.Fragment f: getSupportFragmentManager().getFragments()
                        ) {
                    transaction.hide(f);
                }
            }
            transaction.show(contentFragment);
            transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = GetPathFromUri4kitkat.getPath(MainActivity.this, uri);
                    final AlertDialog[] dialog = new AlertDialog[1];
                    final AlertDialog.Builder dialogB = new AlertDialog.Builder(this)
                            .setTitle("正在导入笔记...")
                            .setView(R.layout.layout_more_progress);
                    NotePersistenceController.importNote(path, new ImportNoteListener() {
                        @Override
                        public void beforeImport() {
                            dialog[0] = dialogB.show();dialog[0].setCancelable(false);
                        }

                        @Override
                        public void afterImport(int num) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    dialog[0].dismiss();
                                }
                            });
                            EventBus.getDefault().post(new DoneImportNoteEvent());
                        }
                    });
                }
                break;
            case Define.ALBUM_REQUEST_CODE:
                //更换图片背景
                if (resultCode == RESULT_OK) {
                    ArrayList<String > imagePaths = data.getStringArrayListExtra(Define.INTENT_PATH);
                    if(imagePaths.size() ==1){
                        AppPreferenceUtil.setHeadBgPath(imagePaths.get(0));
                        try{
                            headBgImgaeView.setImageDrawable(
                                    new BitmapDrawable(
                                            ImageProcessor.zoomImageMin(
                                                    BitmapFactory.decodeFile(imagePaths.get(0))
                                                    , getResources().getDisplayMetrics().widthPixels
                                                    , getResources().getDisplayMetrics().widthPixels)
                                    )
                                    );
                            headBgImgaeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }catch (Exception e){
                            headBgImgaeView.setImageDrawable(ImageProcessor.zoomImageMin(
                                    ContextCompat.getDrawable(this, R.drawable.navigation_header)
                                    , getResources().getDisplayMetrics().widthPixels
                                    , getResources().getDisplayMetrics().widthPixels));
                        }
                    }
                    //You can get image path(ArrayList<String>
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApp.saveNotificationDataModelList();
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
        unregisterReceiver(notifyNumChangedReceiver);
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putString("fragment", getSupportActionBar().getTitle().toString().trim());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStateMachineCode(int stateMachineCode) {
        switch (stateMachineCode) {
            case 1:
                transFragment("答记");
                break;
        }
    }

    @Override
    protected void registerWidgetWithDrawer() {
        headerLayout.findViewById(R.id.head_img_view).setOnClickListener(this);
        headerLayout.findViewById(R.id.head_setting).setOnClickListener(this);
        menuUserHeadimg = (SimpleDraweeView) headerLayout.findViewById(R.id.head_img_view);
        userUserName = (TextView) headerLayout.findViewById(R.id.user_name);
        menuLogOnOrUniversityName = (TextView) headerLayout.findViewById(R.id.university);
        menuSignUpOrSchoolName = (TextView) headerLayout.findViewById(R.id.school);
        userLogInfo = new UserLogInfo(menuUserHeadimg, userUserName, menuLogOnOrUniversityName, menuSignUpOrSchoolName);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable == MyApp.newNotificationNotifier && data.equals("newMessage")) {
            BadgeView bv = new BadgeView(this);
            bv.setTargetView(toolBar.findViewById(R.id.action_message));
            bv.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
            toolBar.findViewById(R.id.action_message).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark
            ));
        }
    }
    public void onEventMainThread(FadeVisibleMainFABEvent e){
        toggleFABAnimation(this,-1,note_add_fab,true);
    }
    public void onEventMainThread(FadeGoneMainFABEvent e){
        toggleFABAnimation(this,1,note_add_fab,true);
    }
    public void onEventMainThread(DateFABVisibleMainFABEvent e){
    }
    public void onEventMainThread(DateFABGoneMainFABEvent e){
    }
    public void onEventMainThread(TransCommunityEvent e){
        transFragment("答记");
    }
    public void onEventMainThread(LogOnSuccess e){
        menuLogOnOrUniversityName.setText(MyApp.getInstance().userInfo.userUniversity);
        menuSignUpOrSchoolName.setText(MyApp.getInstance().userInfo.userSchool);
        String userHeadPicURL = MyApp.getInstance().userInfo.userHeadPicURL;
        Fresco.getImagePipeline().evictFromMemoryCache(Uri.parse(userHeadPicURL));
        FrescoImageloadHelper.simpleLoadImageFromURL(menuUserHeadimg, userHeadPicURL);
        userUserName.setText(MyApp.getInstance().userInfo.userRealName);
        menuLogOnOrUniversityName.setFocusable(false);
        menuSignUpOrSchoolName.setFocusable(false);
    }
    public static void toggleFABAnimation(Context context, int dy, final View fab,boolean isShow) {
        //state为gone的控件表示不需要显示
        if(!isShow){
            fab.setVisibility(View.GONE);
            return ;
        }
        if(fab.getVisibility() == View.GONE){
            return ;
        }
        if (dy < 0) {
            if (true
//                    MainActivity.magicFlag && !fab.isClickable()
                    ) {
//                MainActivity.magicFlag = !MainActivity.magicFlag;
                fab.clearAnimation();
                fab.setClickable(true);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.design_fab_in);


//                animation.setFillAfter(true);

                animation.setDuration(200);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        fab.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.d("FAB", "anim2");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                fab.startAnimation(animation);
                Log.d("FAB", "state2");
                EventBus.getDefault().post(new ShowAnimEvent());
            }
        } else if(dy >0)  {
            if (true
//                    !MainActivity.magicFlag
                    ) {
                fab.clearAnimation();
//                MainActivity.magicFlag = !MainActivity.magicFlag;
                fab.setClickable(false);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.design_fab_out);
//                animation.setFillAfter(true);
                animation.setDuration(200);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fab.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                fab.startAnimation(animation);
                Log.d("FAB", "state1");
                EventBus.getDefault().post(new HideAnimEvent());
            }
        }
    }





    EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            // 刷新bottom bar消息未读数
            updateUnreadAddressLable();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    private void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadMsgCountTotal();
            }
        });
    }
    NotifyNumChangedReceiver  notifyNumChangedReceiver ;


    @Override
    protected void onStart() {
        super.onStart();
        loadMenuUI();
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        action_messageBadge.setBadgeCount(NotificationDataModel.unRead);
        int count = getUnreadMsgCountTotal();
        registerReceiver(notifyNumChangedReceiver,new IntentFilter(NotificationDataModel.intentFormMainUpDateNotiyNumTag));
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    public class NotifyNumChangedReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            action_messageBadge.setBadgeCount(NotificationDataModel.unRead);
        }
    }

    @Override
    protected void themePatch() {
        super.themePatch();

        if(ThemeController.getCurrentColor().accentColor!=-1){
            //fuck FAB
            View RFAB = $(R.id.note_fab_add);
            if(RFAB !=null && RFAB instanceof RapidFloatingActionButton){
                fuckRFAB((RapidFloatingActionButton)RFAB,ThemeController.getCurrentColor().accentColor);
                RFAB.invalidate();
            }
        }
    }
    private void fuckRFAB(RapidFloatingActionButton view, int accentColor) {
        view.setNormalColor(accentColor);
        view.setPressedColor(accentColor);
    }

}

