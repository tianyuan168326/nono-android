package com.seki.noteasklite.Activity.Note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dpizarro.pinview.library.PinView;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Fragment.NONoPreferenceFragment;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.PreferenceUtils;
import com.seki.noteasklite.Util.SeriesLogOnInfo;
import com.takwolf.android.lock9.Lock9View;

import java.util.HashMap;

/**
 * Created by yuan-tian01 on 2016/4/15.
 */
public class AuthActivity  extends BaseActivity{
    public static final int R_SET_PW_SUCCESS = 1;
    public static final int R_SET_PW_FAIL = 2;
    public static final int R_CLEAR_PW_SUCCESS = 3;
    public static final int R_CLEAR_PW_FAIL = 4;
    Lock9View authControl;
    TextView authHint;
    PinView pinAuthControl;

    View authPannel;
    View pinAuthPannel;

    boolean hasFinishOne  = false;
    String firstPassWord = null;
    int code = -1;
    public static final String CODE_TAG = "com.seki.noteasklite.Activity.Note.CODE_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processIntent();
        setContentView(R.layout.activity_auth,"设置手势密码");
    }

    private void processIntent() {
        code = getIntent().getIntExtra(CODE_TAG,-1);
    }

    private void clearAuth() {
        authHint.setText("请绘制原来的手势密码图案");
        authControl.setCallBack(new Lock9View.CallBack() {
            @Override
            public void onFinish(String password) {
                boolean r = SeriesLogOnInfo.verifyAuth(AuthActivity.this,password);
                if(r){
                    onLockClearSuccess();
                }else{
                    Toast.makeText(AuthActivity.this,"原密码不正确，清除手势密码失败！",Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    private void onLockClearSuccess() {
        switch (code){
            case NONoPreferenceFragment.RESULT_GRAPHICS_PASSWORD:
                PreferenceUtils.setPrefBoolean(this,"is_password",false);
                break;
            case NONoPreferenceFragment.RESULT_PIN_PASSWORD:
                PreferenceUtils.setPrefBoolean(this,"is_pin_password",false);
                break;
        }
        SeriesLogOnInfo.clearAuth(AuthActivity.this);
        Toast.makeText(AuthActivity.this,
                code == NONoPreferenceFragment.RESULT_GRAPHICS_PASSWORD?
                "清除手势密码成功":"清除PIN码成功"
                ,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("state",R_CLEAR_PW_SUCCESS);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void makeNewAuth() {
        authControl.setCallBack(new Lock9View.CallBack() {
            @Override
            public void onFinish(String password) {
                if(!hasFinishOne){
                    firstPassWord = password;
                    hasFinishOne = true;
                    authHint.setText("请再次绘制手势图案密码！");
                }else{
                    if(firstPassWord.equals(password)){
                        authHint.setText("设置手势密码成功！");
                        onLockSetSuccess(password);
                    }else{
                        authHint.setText("两次手势密码不一致，请重新绘制！");
                        resetControlState();
                    }
                }
            }
        });
    }

    @Override
    protected void registerWidget() {
        authControl = $(R.id.auth_control);
        pinAuthControl = $(R.id.auth_control_pin);
        authPannel = $(R.id.auth_pannel);
        pinAuthPannel = $(R.id.pin_auth_pannel);

        switch(code){
            case NONoPreferenceFragment.RESULT_GRAPHICS_PASSWORD:
                enterGraphicsAuthMode();
                break;
            case NONoPreferenceFragment.RESULT_PIN_PASSWORD:
                enterPinAuthMode();
                break;
        }

    }

    private void clearPinAuth() {
        authHint.setText("请输入原来的PIN码");
        pinAuthControl.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, String password) {
                pinAuthControl.clear();
                if(completed){

                    boolean r = SeriesLogOnInfo.verifyAuth(AuthActivity.this,password);
                    if(r){
                        onLockClearSuccess();
                    }else{
                        Toast.makeText(AuthActivity.this,"原密码不正确，清除PIN码失败！",Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    }

                }
            }
        });
    }

    private void makePinNewAuth() {
        pinAuthControl.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, String password) {
                pinAuthControl.clear();
                if(completed){
                    if(!hasFinishOne){
                        firstPassWord = password;
                        hasFinishOne = true;
                        authHint.setText("请再次输入PIN码！");
                    }else{
                        if(firstPassWord.equals(password)){
                            authHint.setText("设置PIN码成功！");
                            onLockSetSuccess(password);
                        }else{
                            authHint.setText("两次PIN码不一致，请重新输入！");
                            resetControlState();
                        }
                    }

                }
            }
        });

    }

    private void enterPinAuthMode() {
        authHint = $(R.id.pin_auth_hint);
        pinAuthPannel.setVisibility(View.VISIBLE);
        authPannel.setVisibility(View.INVISIBLE);
        authHint.setText("请输入PIN码");
        if(PreferenceUtils.getPrefBoolean(this,"is_pin_password",false) == false){
            makePinNewAuth();
        }else{
            clearPinAuth();
        }
    }

    private void enterGraphicsAuthMode() {
        authHint = $(R.id.auth_hint);
        authPannel.setVisibility(View.VISIBLE);
        pinAuthPannel.setVisibility(View.INVISIBLE);
        authHint.setText("请绘制手势密码");
        if(PreferenceUtils.getPrefBoolean(this,"is_password",false) == false){
            makeNewAuth();
        }else{
            clearAuth();
        }
    }


    public static void  start(Context context){
        context.startActivity(new Intent().setClass(context,AuthActivity.class));
    }
    public static void  startForResult(Activity context, int code){
        context.startActivityForResult(new Intent().setClass(context,AuthActivity.class),code);

    }
    public static void  startForResult(PreferenceFragment context, int code){
        context.startActivityForResult(new Intent().
                setClass(context.getActivity(),AuthActivity.class)
                .putExtra(CODE_TAG,code)
                ,code);
    }
    private void onLockSetSuccess(String password) {
        PreferenceUtils.setPrefBoolean(this,
                code==NONoPreferenceFragment.RESULT_GRAPHICS_PASSWORD?
                "is_password":
                        "is_pin_password"
                ,true);
        Toast.makeText(this,
                code==NONoPreferenceFragment.RESULT_GRAPHICS_PASSWORD?
                "设置手势密码成功！":"设置PIN码成功！"
                ,Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("state",R_SET_PW_SUCCESS);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        },200);
        SeriesLogOnInfo.saveAuth(this,password);
    }


    private void resetControlState() {
         hasFinishOne  = false;
         firstPassWord = null;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                authHint.setText(
                        code==NONoPreferenceFragment.RESULT_GRAPHICS_PASSWORD?
                        "请绘制手势图案密码":
                                "请输入PIN码"
                );
            }
        },500);
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
