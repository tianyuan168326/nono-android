package com.seki.noteasklite.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seki.noteasklite.Fragment.Ask.RegisterAccountFragment;
import com.seki.noteasklite.Fragment.Ask.RegisterCompulsoryFragment;
import com.seki.noteasklite.Fragment.Ask.RegisterMoreFragment;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.ImageHelper;
import com.seki.noteasklite.Util.InfoEncoderHelper;
import com.seki.noteasklite.Util.NotifyHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class RegisterActivity extends AppCompatActivity implements OnClickListener {
    public static final int REGISTERNAMEEXIST=1000;
    public static final int REGISTERSUCCESS=1001;
    public static final int REGISTERFAIL=-1;
    ProgressBar progBarRegister;
    StringBuffer notifyInfo;
    FloatingActionButton register_fab ;
    AppCompatImageView next_btn;
    FrameLayout container;
    TextView info_tv;
    ImageView bg;
    List<WeakReference<Fragment>> fragmentCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.register_activity);
        getDefinition();
        setToolBar();
        progBarRegister.setVisibility(View.GONE);
        fragmentCache = new ArrayList<>();
        fragmentCache.add(new WeakReference<Fragment>(registerAccountFragment));
    }
    private void setToolBar() {
//        Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.register_tool_bar);
//        setTitle(getString(R.string.register));
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    RegisterAccountFragment registerAccountFragment = new RegisterAccountFragment();
    void getDefinition()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,registerAccountFragment).commit();
        progBarRegister=(ProgressBar)this.findViewById(R.id.register_progress);
        register_fab = (FloatingActionButton)this.findViewById(R.id.register_fab);
        next_btn = (AppCompatImageView)findViewById(R.id.next_btn);
        container = (FrameLayout)findViewById(R.id.container);
        info_tv = (TextView)findViewById(R.id.info_tv);
        register_fab.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        bg = (ImageView)findViewById(R.id.bg);
        Bitmap image = null;
        image = BitmapFactory.decodeResource(getResources(),R.drawable.butiful_bg);
        ImageHelper.fastblurSrc( image, 12);
        bg.setImageBitmap(image);
    }

    private void registerFabIn() {
        register_fab.clearAnimation();
        register_fab.setClickable(true);
        Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.design_fab_in);
        animation.setFillAfter(true);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                register_fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("FAB", "anim2");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        register_fab.startAnimation(animation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent().setClass(this, LogOnActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.register_menu:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
    AllAccountInfo allAccountInfo = new AllAccountInfo();
    private void doRegister(){
        RegisterTask registertask = new RegisterTask();
        registertask.execute(
                allAccountInfo.getAccount().register_username,
                allAccountInfo.getAccount().register_password,
                allAccountInfo.getAccount().register_email,
                allAccountInfo.getCompulsory().register_university,
                allAccountInfo.getCompulsory().register_subject,
                allAccountInfo.getCompulsory().register_realname,
                allAccountInfo.getMore().getHeadImgUrl(),
                allAccountInfo.getMore().getSex()
        );
    }

    int currentPageIndex=  0;
    RegisterCompulsoryFragment registerCompulsoryFragment = new RegisterCompulsoryFragment();
    RegisterMoreFragment registerMoreFragment = new RegisterMoreFragment();
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_fab:
//                doRegister();
                next_btn.performClick();
                break;

            case R.id.register_subject:

                break;
            case R.id.next_btn:
                switch (currentPageIndex) {
                    case 0:
                        RegisterAccountFragment.AccountInfo accountInfo = registerAccountFragment.getAccountInfo();
                        if (!TextUtils.isEmpty(accountInfo.errorInfo)) {
                            info_tv.setText(accountInfo.errorInfo);
                            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                            alphaAnimation.setDuration(1000);
                            alphaAnimation.setInterpolator(new BounceInterpolator());
                            info_tv.startAnimation(alphaAnimation);
                        } else {
                            allAccountInfo.setAccount(accountInfo);
                            currentPageIndex = 1;
                            fragmentCache.add(new WeakReference<Fragment>(registerCompulsoryFragment));
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,registerCompulsoryFragment).commit();
                        }
                        break;
                    case 1:
                        RegisterCompulsoryFragment.CompulsoryInfo compulsoryInfo = registerCompulsoryFragment.getCompulsoryInfo();
                        if (!TextUtils.isEmpty(compulsoryInfo.errorInfo)) {
                            info_tv.setText(compulsoryInfo.errorInfo);
                            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                            alphaAnimation.setDuration(1000);
                            alphaAnimation.setInterpolator(new BounceInterpolator());
                            info_tv.startAnimation(alphaAnimation);
                        } else {
                            allAccountInfo.setCompulsory(compulsoryInfo);
                            currentPageIndex = 2;
                            next_btn.setVisibility(View.GONE);
                            register_fab.setVisibility(View.VISIBLE);
                            fragmentCache.add(new WeakReference<Fragment>(registerMoreFragment));
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,registerMoreFragment).commit();
                        }
                        break;
                    case 2:
                        RegisterMoreFragment.MoreInfo moreInfo = registerMoreFragment.getMoreInfo();
                        if (moreInfo == null) {
                            info_tv.setText("请检查信息！");
                            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                            alphaAnimation.setDuration(1000);
                            alphaAnimation.setInterpolator(new BounceInterpolator());
                            info_tv.startAnimation(alphaAnimation);
                        } else {
                            allAccountInfo.setMore(moreInfo);
                            if(allAccountInfo.getAccount()!=null && allAccountInfo.getCompulsory()!=null){
                                doRegister();
                            }

                        }
                        break;
                }
                break;
        }
    }
    public class RegisterTask extends AsyncTask<String,Void,String> {
        protected String doInBackground(String... data) {
            StringBuilder builder = new StringBuilder();
            try {
                HttpClient client = new DefaultHttpClient();

                HttpPost post = new HttpPost("http://diandianapp.sinaapp.com/register.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_name", data[0]));
                params.add(new BasicNameValuePair("user_password", InfoEncoderHelper.getMD5Str(data[1])));
                params.add(new BasicNameValuePair("user_email", data[2]));
                params.add(new BasicNameValuePair("user_university", data[3]));
                params.add(new BasicNameValuePair("user_subject", data[4]));
                params.add(new BasicNameValuePair("user_realname", data[5]));
                params.add(new BasicNameValuePair("user_head_img_url", data[6]));
                params.add(new BasicNameValuePair("user_sex", data[7]));
                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                    builder.append(s);

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return builder.toString() ;
        }

        @Override
        protected void onPostExecute(String builderString) {
            // TODO Auto-generated method
            try{
                JSONObject jsonObject = new JSONObject(builderString);

                String stateCodeStr = jsonObject.getString("state_code");
                int  resultCode= Integer.parseInt(stateCodeStr);
                switch(resultCode)
                {
                    case -2:
                        NotifyHelper.makeToastwithTextAndPic(RegisterActivity.this, getString(R.string.register_username_exist), R.drawable.ic_error_outline_black_48dp);
                        break;
                    case 0:
                        NotifyHelper.makeToastwithTextAndPic(RegisterActivity.this, getString(R.string.register_success), R.drawable.ic_error_outline_black_48dp);
                        startActivity(new Intent().setClass(RegisterActivity.this,LogOnActivity.class));
                        finish();
                        break;
                    default:
                        NotifyHelper.makeToastwithTextAndPic(RegisterActivity.this, getString(R.string.register_failed), R.drawable.ic_error_outline_black_48dp);
                        break;
                }

            }
            catch (Exception e){e.printStackTrace();}
            progBarRegister.setVisibility(View.GONE);
            View rootView=RegisterActivity.this.getWindow().getDecorView();
            rootView.setFocusable(true);
        }

        @Override
        protected void onPreExecute() {
            progBarRegister.setVisibility(View.VISIBLE);
            View rootView=RegisterActivity.this.getWindow().getDecorView();
            rootView.setFocusable(false);
            super.onPreExecute();
        }
    }
    private  class MyBallAdapter extends  FragmentPagerAdapter{
        Map<Integer,Fragment> pagerMap = new HashMap<>();
        public MyBallAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    RegisterAccountFragment registerAccountFragment = RegisterAccountFragment.newInstance();
                    pagerMap.put(position,registerAccountFragment);
                    return registerAccountFragment;
                case 1:
                    RegisterCompulsoryFragment registerCompulsoryFragment = RegisterCompulsoryFragment.newInstance();
                    pagerMap.put(position,registerCompulsoryFragment);
                    return registerCompulsoryFragment;
                case 2:
                    RegisterMoreFragment registerMoreFragment = RegisterMoreFragment.newInstance();
                    pagerMap.put(position,registerMoreFragment);
                    return registerMoreFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
        public Fragment getCacheFragment(int position){
            return pagerMap.get(position);
        }
        @Override
        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
            pagerMap.remove(position);
        }
    }
    class AllAccountInfo{
        private RegisterMoreFragment.MoreInfo more;
        private RegisterCompulsoryFragment.CompulsoryInfo compulsory;
        private RegisterAccountFragment.AccountInfo account;

        public AllAccountInfo() {
            more = new RegisterMoreFragment.MoreInfo();
            compulsory = new RegisterCompulsoryFragment.CompulsoryInfo();
            account  = new RegisterAccountFragment.AccountInfo();
        }

        public void setMore(RegisterMoreFragment.MoreInfo more) {
            this.more = more;
        }

        public void setCompulsory(RegisterCompulsoryFragment.CompulsoryInfo compulsory) {
            this.compulsory = compulsory;
        }

        public void setAccount(RegisterAccountFragment.AccountInfo account) {
            this.account = account;
        }

        public RegisterMoreFragment.MoreInfo getMore() {
            return more;
        }

        public RegisterCompulsoryFragment.CompulsoryInfo getCompulsory() {
            return compulsory;
        }

        public RegisterAccountFragment.AccountInfo getAccount() {
            return account;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(fragmentCache.get(2).get() !=null){
           if(fragmentCache.get(2).get() instanceof RegisterMoreFragment){
               fragmentCache.get(2).get().onActivityResult(requestCode,resultCode,data);
           }
       }
    }
}
