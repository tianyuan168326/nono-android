package com.seki.noteasklite.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.VerifyInput;

import java.util.HashMap;

public class FindPasswordActivity extends BaseActivity {
    FloatingActionButton account_verfiy_email;
    TextInputLayout acount_email;
    TextInputLayout acount_verify_code;
    TextInputLayout account_new_password;
    TextInputLayout account_new_password_dup;
    @Override
    protected void registerWidget() {
        account_verfiy_email = $(R.id.account_verfiy_email);
        account_verfiy_email.setVisibility(View.INVISIBLE);
        acount_email = $(R.id.acount_email);
        acount_verify_code = $(R.id.acount_verify_code);
        acount_verify_code.setVisibility(View.INVISIBLE);
        account_new_password = $(R.id.account_new_password);
        account_new_password.setVisibility(View.INVISIBLE);
        account_new_password_dup = $(R.id.account_new_password_dup);
        account_new_password_dup.setVisibility(View.INVISIBLE);
        bindViewsToOnClickListenerById(R.id.account_verfiy_email);
        registerListener();
    }

    private void registerListener() {
        assert acount_email.getEditText()!=null;
        acount_email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(VerifyInput.isEmail(s.toString())){
                    emailFABOn();
                }else if(account_verfiy_email.isShown()){
                    emailFABOff();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void emailFABOff() {
        account_verfiy_email.clearAnimation();
        account_verfiy_email.setClickable(false);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.design_fab_out);
        animation.setFillAfter(true);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                account_verfiy_email.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("FAB", "anim2");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        account_verfiy_email.startAnimation(animation);
        ;
    }

    private void emailFABOn() {
        account_verfiy_email.clearAnimation();
        account_verfiy_email.setClickable(true);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.design_fab_in);
        animation.setFillAfter(true);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                account_verfiy_email.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        account_verfiy_email.startAnimation(animation);
        assert acount_verify_code.getEditText()!=null;
        acount_verify_code.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6){
                    account_new_password.setVisibility(View.VISIBLE);
                    account_new_password_dup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        assert account_new_password_dup.getEditText()!=null;
        account_new_password_dup.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty((CharSequence) account_new_password.getEditText().getText().toString())&&
                        account_new_password.getEditText().getText().toString().length()>5&&
                        account_new_password.getEditText().getText().toString().equals(s)){
                    changeFABState();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void changeFABState() {
        if(fab_state ==1){
            fab_state = 2;
            account_verfiy_email.clearAnimation();
            account_verfiy_email.setClickable(true);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_infinite);
            animation.setFillAfter(false);
            animation.setRepeatCount(1);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    account_verfiy_email.setImageResource(R.drawable.ic_done_white_24dp);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            account_verfiy_email.startAnimation(animation);
        }

    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password,"找回NONo");
    }
    //1:发送邮件
    //2:重置密码
    int fab_state  =1;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_verfiy_email:
                if(fab_state ==1){
                    sendVerifyEmail();
                }else{
                    doneAll();
                }
                break;
        }
    }

    private void doneAll() {

    }

    private void sendVerifyEmail() {
        account_verfiy_email.clearAnimation();
        account_verfiy_email.setClickable(false);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_infinite);
        animation.setFillAfter(false);
        animation.setRepeatCount(5);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //然后加上一层蒙版
                account_verfiy_email.setAlpha(0.5f);
                account_verfiy_email.setClickable(false);
                acount_verify_code.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        account_verfiy_email.startAnimation(animation);
        Snackbar.make($(R.id.root),"正在发送验证码邮件...",Snackbar.LENGTH_LONG).show();
    }
    public static void start(Context context){
        context.startActivity(new Intent().setClass(context,FindPasswordActivity.class));
    }
}
