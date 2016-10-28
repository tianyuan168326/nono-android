package com.seki.noteasklite.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.Controller.AccountController;
import com.seki.noteasklite.CustomControl.Share.SharePanelView;
import com.seki.noteasklite.DataUtil.Bean.QQLogOnBean;
import com.seki.noteasklite.DataUtil.BusEvent.LogOnSuccess;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.InfoEncoderHelper;
import com.seki.noteasklite.Util.NotifyHelper;
import com.seki.noteasklite.Util.VerifyInput;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.HashMap;

import ActivityOptionsICS.ActivityCompatICS;
import ActivityOptionsICS.ActivityOptionsCompatICS;

public class LogOnActivity extends BaseActivity implements View.OnClickListener {
    AppCompatImageView mLogOnButton;
    AppCompatButton mRegisterButton;
    String username,userpassword;
    EditText passwordEditText;
    ImageView bg;
    AppCompatTextView tv_find_password;
    AppCompatTextView tv_quick_log_on;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_log_on,"登陆");
        getDefinition();
        setActionBar();
        registerEvent();
    }
    public static void start(Context context){
        context.startActivity(new Intent().setClass(context,LogOnActivity.class));
    }
    private void setActionBar() {
        setTitle(getResources().getString(R.string.log_on));
    }
    void getDefinition()
    {
        mLogOnButton=(AppCompatImageView)this.findViewById(R.id.account_sign_in_button);
        mRegisterButton = (AppCompatButton)findViewById(R.id.account_register_button);
        passwordEditText=((EditText)this.findViewById(R.id.password));
        tv_find_password = ((AppCompatTextView) this.findViewById(R.id.tv_find_password));
        tv_quick_log_on = ((AppCompatTextView) this.findViewById(R.id.tv_quick_log_on));

    }
    public static Tencent mTencent;

    void registerEvent()
    {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(SharePanelView.QQ_API_ID, this);
        }
        mLogOnButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
        tv_find_password.setOnClickListener(this);
        tv_quick_log_on.setOnClickListener(this);
        final Animation arrowRight2Down = AnimationUtils.loadAnimation(this,R.anim.rotate_arrow_right_to_down);
        final Animation arrowDown2Right = AnimationUtils.loadAnimation(this,R.anim.rotate_arrow_down_to_right);
        mLogOnButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLogOnButton.setAnimation(arrowRight2Down);
                arrowRight2Down.startNow();
                arrowRight2Down.setFillAfter(true);
            }
        });
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    arrowRight2Down.cancel();
                    mLogOnButton.clearAnimation();
                    mLogOnButton.setAnimation(arrowDown2Right);
                    arrowDown2Right.startNow();
                    arrowDown2Right.setFillAfter(true);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void registerWidget() {
        bg = (ImageView)findViewById(R.id.bg);
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent=new Intent();
        //intent.setClass(this,MainActivity.class);
        //startActivity(intent);
        //finish();
    }
    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.account_sign_in_button:
                username=((EditText)this.findViewById(R.id.account)).getText().toString();
                userpassword=((EditText)this.findViewById(R.id.password)).getText().toString();
                if(!VerifyInput.isUserName(username))
                {
                    NotifyHelper.makeToastwithTextAndPic(this, getString(R.string.logon_length_erro), R.mipmap.ic_error_outline_black_48dp);
                    break;
                }
                AccountController.LogOn(username,InfoEncoderHelper.getMD5Str(userpassword),false);
                break;
            case R.id.account_register_button:
                Intent intent = new Intent().setClass(this, RegisterActivity.class);
                ActivityOptionsCompatICS options = ActivityOptionsCompatICS.makeSceneTransitionAnimation(
                            LogOnActivity.this,
                            Pair.create((View)bg, R.id.bg));
                ActivityCompatICS.startActivity(LogOnActivity.this, intent, options.toBundle());
                finish();
                break;
            case R.id.tv_find_password:
                //findPassword();
                break;
            case R.id.tv_quick_log_on:
                quickQQLogOn();
                break;
        }
    }
    boolean isServerSideLogin= false;
    private void quickQQLogOn() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            isServerSideLogin = false;
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(this);
                mTencent.login(this, "all", loginListener);
                isServerSideLogin = false;
                return;
            }
            mTencent.logout(this);
            updateUserInfo();
           // updateLoginButton();
        }
    }
    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            try{
                qqLogOnBean.openid = values.getString("openid");
            }catch (Exception e){}
            initOpenidAndToken(values);
            updateUserInfo();
            //updateLoginButton();
        }
    };
    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }
    QQLogOnBean qqLogOnBean  =new QQLogOnBean();
    UserInfo  mInfo;
    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    Log.d(NONoConfig.TAG_NONo,e.toString());
                }

                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject)response;
                    try{
                        qqLogOnBean.headImgUrl = json.getString("figureurl_qq_2");
                        qqLogOnBean.sex = json.getString("gender");
                        qqLogOnBean.userRealName = json.getString("nickname");

                        AccountController.qqLogOn(qqLogOnBean);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancel() {
                    //Log.d(NONoConfig.TAG_NONo,new Gson().toJson(response));
                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
            Util.showResultDialog(LogOnActivity.this, "Tencent对象为空", "登录失败");
        }
    }
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(LogOnActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(LogOnActivity.this, "返回为空", "登录失败");
                return;
            }
            // 有奖分享处理
//            handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(LogOnActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(LogOnActivity.this, "onCancel: ");
            Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }
    private void findPassword() {
        FindPasswordActivity.start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                bg = (ImageView)findViewById(R.id.bg);
//                Bitmap image = null;
//                AssetManager am = getResources().getAssets();
//                try
//                {
//                    InputStream is = am.open("butiful_bg.jpg");
//                    image = BitmapFactory.decodeStream(is);
//                    is.close();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//                image =  ImageHelper.fastblur( image, 12
//                );
//                final Bitmap finalImage = image;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        bg.setImageBitmap(finalImage);
//                    }
//                });
//            }
//        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // Log.d(TAG, "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Util.showProgressDialog(LogOnActivity.this,"正在登陆","登陆中...");
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @SuppressWarnings("unused")
    public void onEventMainThread(LogOnSuccess e){
        finish();
    }
}