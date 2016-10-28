package com.seki.noteasklite.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.CommunityController;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.AppUserInfo;
import com.seki.noteasklite.DataUtil.Bean.UserInfo;
import com.seki.noteasklite.DataUtil.BusEvent.NoticeUserStateChangedEvent;
import com.seki.noteasklite.Fragment.UserInfoFrg.ActivityFragment;
import com.seki.noteasklite.Fragment.UserInfoFrg.InfoFragment;
import com.seki.noteasklite.Fragment.UserInfoFrg.NoticeFragment;
import com.seki.noteasklite.HuanXinUserManager;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.MyCollapsingToolbarLayout;
import com.seki.noteasklite.R;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.noteasklite.ThirdWrapper.PowerStringRequest;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.yuantian.com.easeuitransplant.EaseUser;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener{
    SimpleDraweeView activityUserInfoHeadimg;
    TextView activityUserInfoRealName;
    TextView activityUserInfoAbstract;
    TextView activityUserInfoNoticedNum;
    TextView activityUserInfoNotice;
    //UserHomeData userHomeData;
    //一个标志位，来记录是不是自己，如果是自己，
    // 用于在resume（从编辑信息页面返回）的时候，更新内容
    private String isme="false";
    private int bgColor;
    private Bitmap headImgBitmap;
    private UserInfo userInfo = new UserInfo();
    private Button imButon;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    MyCollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openEventBus();
        userInfo.data = new UserInfo.Data();
        bgColor= ContextCompat.getColor(this,R.color.colorPrimary);
        setContentView(R.layout.activity_user_info, "");
        processIntent(getIntent());
    }

    private void setUpCollapsed(){
        collapsingToolbarLayout.setBackgroundColor(bgColor);
        collapsingToolbarLayout.setContentScrimColor(bgColor);
        collapsingToolbarLayout.setStatusBarScrimColor(colorBurn(bgColor));
        final AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
        ViewTreeObserver vto=collapsingToolbarLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT>=16){
                    collapsingToolbarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else {
                    collapsingToolbarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                float heightDp = getResources().getDisplayMetrics().heightPixels - 48 * getResources().getDisplayMetrics().density;
                if (collapsingToolbarLayout.getMeasuredHeight() > heightDp) {
                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
                    lp.height = (int) (collapsingToolbarLayout.getMeasuredHeight() + 48 * getResources().getDisplayMetrics().density);
                    tabLayout.requestLayout();
                }
            }
        });
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setOnListener(new MyCollapsingToolbarLayout.OnListener() {
            @Override
            public void collapsed() {
                findViewById(R.id.header).setVisibility(View.INVISIBLE);
                collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            }

            //
            @Override
            public void expand() {
                findViewById(R.id.header).setVisibility(View.VISIBLE);
                collapsingToolbarLayout.setCollapsedTitleTextColor(Color.TRANSPARENT);
            }
        });

    }
    @Override
    public void onClick(View v) {
        if(verifyState(this))
            return ;
        switch (v.getId()) {
            case R.id.activity_user_info_notice:
                if(isme!=null) {
                    if (isme.compareTo("true") == 0) {
                        startActivityForResult(new Intent(this,EditInfoActivity.class),0);
                    } else if (isme.compareTo("false") == 0) {
                        CommunityController.noticeOther(String.valueOf(userInfo.data.user_id),
                                !activityUserInfoNotice.getText().toString()
                                .equals("关注"));
                    }
                }
                break;
            case R.id.activity_user_info_im:
                startMessage();
                break;
        }
    }


    @Override
    protected void registerWidget() {
        activityUserInfoHeadimg  =findView(R.id.activity_user_info_headimg);
        activityUserInfoRealName  =findView(R.id.activity_user_info_real_name);
        activityUserInfoAbstract  =findView(R.id.activity_user_info_abstract);
        activityUserInfoNoticedNum  =findView(R.id.activity_user_info_noticed_num);
        activityUserInfoNotice = findView(R.id.activity_user_info_notice);
        imButon = findView(R.id.activity_user_info_im);
        imButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMessage();
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        collapsingToolbarLayout=(MyCollapsingToolbarLayout)findViewById(R.id.collapsing);
        activityUserInfoNotice.setOnClickListener(this);
        setUpCollapsed();
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected    HashMap<Integer,String> setUpOptionMenu() {
        setMenuResId(R.menu.menu_user_info);
        HashMap<Integer,String> idMethosNamePaire = new HashMap<Integer,String>();
        idMethosNamePaire.put(android.R.id.home, "onBackPressed");
        return idMethosNamePaire;
    }
    public static void start(Context context, String userId){
        context.startActivity(new Intent().setClass(context, UserInfoActivity.class)
                        .putExtra("otherUserId", Integer.valueOf(userId))
        );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        userInfo.data.user_id =  intent.getIntExtra("otherUserId", 0);
        if(userInfo.data.user_id == Integer.valueOf( MyApp.userInfo.userId)){
            readUserInfoBuffer();
            activityUserInfoNotice.setText("编辑");
            activityUserInfoNotice.setVisibility(View.VISIBLE);
            imButon.setVisibility(View.GONE);
            isme = "true";
        }
        readNetBuffer();
        if(userInfo.data.user_id == Integer.valueOf( MyApp.userInfo.userId)){
            activityUserInfoNotice.setText("编辑");
            activityUserInfoNotice.setVisibility(View.VISIBLE);
            imButon.setVisibility(View.GONE);
            isme = "true";
        }
    }

    private void readNetBuffer() {
        StringRequest getUserInfo = new PowerStringRequest(Request.Method.POST,
                NONoConfig.makeNONoSonURL("/quickask_get_userinfo_home.php"),
                new PowerListener() {
                    @Override
                    public void onCorrectResponse(String s) {
                        super.onCorrectResponse(s);
                        try{
                            userInfo =  new Gson().fromJson(s,new TypeToken<UserInfo>(){}.getType());
                        }catch(Exception e){
                            Toast.makeText(UserInfoActivity.this,"获取用户信息错误(等级：严重)，请联系开发者!",Toast.LENGTH_SHORT).show();
                        }

                        UpdateUI(userInfo);
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("me_id",MyApp.userInfo.userId);
                params.put("user_id",String.valueOf(userInfo.data.user_id));

                return params;
            }
        };
        MyApp.getInstance().volleyRequestQueue.add(getUserInfo);
    }
    void inspectUserInfo(){
        userInfo.data.user_info_headimg  = MyApp.getInstance().userInfo.userHeadPicURL;
        userInfo.data.user_info_real_name  = MyApp.getInstance().userInfo.userRealName;
        userInfo.data.user_id  =Integer.valueOf(MyApp.getInstance().userInfo.userId) ;
        userInfo.data.user_info_abstract  = MyApp.getInstance().userInfo.userAbstract;
    }
    @Override
    protected void onResume() {
        super.onResume();
        $(R.id.toolbar).setBackgroundColor(Color.TRANSPARENT);
        if("true".equals(isme)){
            inspectUserInfo();
        }
        if(userInfo.data == null){
            return;
        }
        if(userInfo.data.user_info_headimg == null){
            return;
        }
        FrescoImageloadHelper.LoadImageFromURLAndCallBack(activityUserInfoHeadimg, userInfo.data.user_info_headimg, this,
                new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

                    }

                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        headImgBitmap = bitmap;
                        bgColor = autoColor(headImgBitmap);
                        collapsingToolbarLayout.setBackgroundColor(bgColor);
                        collapsingToolbarLayout.setContentScrimColor(bgColor);
                        collapsingToolbarLayout.setStatusBarScrimColor(colorBurn(bgColor));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(colorBurn(bgColor));
                        }
                        //setColor(bgColor);
                    }
                });
        if(userInfo.data.user_info_real_name == null){
            return;
        }
        activityUserInfoRealName.setText(userInfo.data.user_info_real_name);
        getSupportActionBar().setTitle(userInfo.data.user_info_real_name);
        if(userInfo.data.user_info_abstract == null){
            return;
        }
        activityUserInfoAbstract.setText(userInfo.data.user_info_abstract);
    }



    private void UpdateUI(UserInfo userInfo) {
        FrescoImageloadHelper.LoadImageFromURLAndCallBack(activityUserInfoHeadimg, userInfo.data.user_info_headimg, this,
                new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

                    }

                    @Override
                    protected void onNewResultImpl(final Bitmap bitmap) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                headImgBitmap = bitmap;
                                bgColor = autoColor(headImgBitmap);
                                collapsingToolbarLayout.setBackgroundColor(bgColor);
                                collapsingToolbarLayout.setContentScrimColor(bgColor);
                                collapsingToolbarLayout.setStatusBarScrimColor(colorBurn(bgColor));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(colorBurn(bgColor));
                                }
                            }
                        });
                        //setColor(bgColor);
                    }
                });
        activityUserInfoRealName.setText(userInfo.data.user_info_real_name);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar !=null){
            actionBar.setTitle(userInfo.data.user_info_real_name);
        }

        activityUserInfoAbstract.setText(userInfo.data.user_info_abstract);
        activityUserInfoNoticedNum.setText(String.valueOf(userInfo.data.user_info_noticed_num));
        ((TextView)findView(R.id.activity_user_info_voted_on_num)).setText(String.valueOf(userInfo.data.user_info_voted_up_num));
        if(userInfo.data.user_id == Integer.valueOf(MyApp.getInstance().userInfo.userId)){
            activityUserInfoNotice.setText(R.string.edit);
        }else{
            switch(userInfo.data.user_info_friend_relation){
                case  1:
                    activityUserInfoNotice.setText(R.string.unnotice);
                    break;
                case 0:
                    activityUserInfoNotice.setText(R.string.notice);
                    break;
            }
        }

        Bundle args = new Bundle();
        args.putString("University", userInfo.data.user_info_university);
        args.putString("School", userInfo.data.user_info_school);
        args.putString("sex", userInfo.data.user_info_sex);
        updateInfoFrag(args);
    }

    private void readUserInfoBuffer() {
        FrescoImageloadHelper.LoadImageFromURLAndCallBack(activityUserInfoHeadimg,
                MyApp.userInfo.userHeadPicURL, this,
                new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

                    }

                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        headImgBitmap = bitmap;
                        bgColor = autoColor(headImgBitmap);
                        collapsingToolbarLayout.setBackgroundColor(bgColor);
                        collapsingToolbarLayout.setContentScrimColor(bgColor);
                        collapsingToolbarLayout.setStatusBarScrimColor(colorBurn(bgColor));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(colorBurn(bgColor));
                        }
                    }
                });
        getSupportActionBar().setTitle(MyApp.userInfo.userRealName);
        activityUserInfoRealName.setText(MyApp.userInfo.userRealName);
        AppUserInfo userInfo=MyApp.userInfo;
        activityUserInfoAbstract.setText(userInfo.userAbstract);
        Bundle args = new Bundle();
        args.putString("University", userInfo.userUniversity);
        args.putString("School", userInfo.userSchool);
        args.putString("sex", userInfo.userSex);
        updateInfoFrag(args);
    }

    public void updateInfoFrag(Bundle args){
        if(getSupportFragmentManager().getFragments()!=null) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof InfoFragment) {
                    ((InfoFragment) fragment).update(args);
                    break;
                }
            }
        }
    }
    public static boolean verifyState(Context context){
        if(MyApp.getInstance().userInfo.userId.equals("0")){
            LogOnActivity.start(context);
            return true;
        }
        return false;
    }
    public  void startMessage(){
        EaseUser  easeUser = new EaseUser(String.valueOf(userInfo.data.user_id));
        easeUser.setAvatar(userInfo.data.user_info_headimg);
        easeUser.setNick(userInfo.data.user_info_real_name);
        HuanXinUserManager.put(String.valueOf(userInfo.data.user_id),easeUser);
        startActivity(new Intent(UserInfoActivity.this, ChatActivity.class).putExtra("userId",
                String.valueOf(userInfo.data.user_id)).putExtra("name", userInfo.data.user_info_real_name));
    }

    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.argb(alpha,red, green, blue);
    }

    private int  autoColor(Bitmap bitmap) {
        Palette.Builder b = Palette.from(bitmap);
        Palette palette = b.generate();
        Palette.Swatch s0 = palette.getVibrantSwatch();
        if (s0 != null) {
            return s0.getRgb();
        }else {
            for(Palette.Swatch s:palette.getSwatches()){
                if(s!=null){
                    return s.getRgb();
                }
            }
        }
        return 0;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return InfoFragment.newInstance((userInfo.data.user_id));
                case 1:
                    return ActivityFragment.newInstance(userInfo.data.user_id);
                case 2:
                    return NoticeFragment.newInstance(userInfo.data.user_id);
//                case 3:
//                    return HobbyFragment.newInstance(userInfo.data.user_id);
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "资料";
                case 1:
                    return "动态";
                case 2:
                    return "关注";
            }
            return null;
        }
    }

    @Override
    protected void themePatch() {
        super.themePatch();
        ((MyCollapsingToolbarLayout)$(R.id.collapsing)).setContentScrimColor(ThemeController.getCurrentColor().mainColor);
    }
    @SuppressWarnings("unused")
    public void onEventMainThread(NoticeUserStateChangedEvent e){
        if(!TextUtils.equals(String.valueOf( userInfo.data.user_id ),e.userId)){
            return ;
        }
        activityUserInfoNotice.setText(e.noticeState?"取消关注":"关注");
    }
}
