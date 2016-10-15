package com.seki.noteasklite.Activity.Ask;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.dpizarro.pinview.library.PinView;
import com.seki.noteasklite.Activity.MainActivity;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Controller.NotePersistenceController;
import com.seki.noteasklite.CustomControl.WheelSurfaceView;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.PreferenceUtils;
import com.seki.noteasklite.Util.SeriesLogOnInfo;
import com.takwolf.android.lock9.Lock9View;

import java.util.HashMap;

public class LockActivity extends BaseActivity {
    TextView auth_hint;
    Lock9View auth_control;
    PinView  auth_control_pin;
    View root;
    @Override
    protected void registerWidget() {
        auth_hint = $(R.id.auth_hint);
        auth_control = $(R.id.auth_control);
        root = $(R.id.root);
        auth_control_pin = $(R.id.auth_control_pin);
        root.setVisibility(View.INVISIBLE);
        processLock();

    }

    private void processLock() {
        if(PreferenceUtils.getPrefBoolean(this,"is_password",false) ){
            auth_hint.setText("请绘制您的手势密码");
            root.setVisibility(View.VISIBLE);
            auth_control.setVisibility(View.VISIBLE);
            auth_control_pin.setVisibility(View.INVISIBLE);
            $(R.id.toolbar).setVisibility(View.INVISIBLE);
            $(R.id.loading_bg).setVisibility(View.INVISIBLE);
            auth_control.setCallBack(new Lock9View.CallBack() {
                @Override
                public void onFinish(String password) {
                    verifyPassWord(password);
                }
            });
        } else  if(PreferenceUtils.getPrefBoolean(this,"is_pin_password",false) ){
            auth_hint.setText("请输入你的PIN码");
            root.setVisibility(View.VISIBLE);
            auth_control_pin.setVisibility(View.VISIBLE);
            auth_control.setVisibility(View.INVISIBLE);
            $(R.id.toolbar).setVisibility(View.INVISIBLE);
            $(R.id.loading_bg).setVisibility(View.INVISIBLE);
            auth_control_pin.setOnCompleteListener(new PinView.OnCompleteListener() {
                @Override
                public void onComplete(boolean completed, String password) {
                    if(completed){
                        verifyPassWord(password);
                    }
                }
            });
        }
        else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.start(LockActivity.this);
                    finish();
                }
            },200);
        }
    }

    Handler handler = new Handler();

    private void verifyPassWord(String password) {
        if(SeriesLogOnInfo.verifyAuth(this,password)){
            root.setVisibility(View.INVISIBLE);
            $(R.id.toolbar).setVisibility(View.VISIBLE);
            $(R.id.loading_bg).setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.start(LockActivity.this);
                    finish();
                }
            },200);

        }else{
            verifyFail();

        }
    }

    private void verifyFail() {
        if(PreferenceUtils.getPrefBoolean(this,"is_password",false)){
            auth_hint.setTextColor(getResources().getColor(R.color.colorAccent));
            auth_hint.setText("手势不正确，请重新绘制！");
        }else if(PreferenceUtils.getPrefBoolean(this,"is_pin_password",false)){
            auth_hint.setTextColor(getResources().getColor(R.color.colorAccent));
            auth_hint.setText("PIN码不正确，请重新输入！");
        }

    }

    public static void start(Context context){
        context.startActivity(new Intent(context,LockActivity.class));
    }
    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }
    WheelSurfaceView loading_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock,"加载中...");
        if( getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        loading_bg = $(R.id.loading_bg);
        loading_bg.setZOrderOnTop(true);
        int[] colors=new int[]{android.R.attr.colorBackground};
        TypedArray typedArray=getTheme().obtainStyledAttributes(colors);
        colors[0]=typedArray.getColor(0,0xff000000);
        typedArray.recycle();
        loading_bg.setWindowColor(colors[0]);
        if(!checkPermissions()){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA},1);
        }
        NotePersistenceController. iniAutoBackUp();
    }
    public boolean checkPermissions(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK)==PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE)==PackageManager.PERMISSION_GRANTED
                ;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            for(int i:grantResults){
                if(i!=PackageManager.PERMISSION_GRANTED){
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setMessage("你拒绝了某些权限，稍后应用将退出！" +
                                    "目前应用需要的权限已经很少了，如果你担心安全问题，请直接联系开发者！")
                            .setPositiveButton("退出应用", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    System.exit(0);
                                }
                            })
                            .show();
                }
            }
        }
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        loading_bg.stop();
    }

    @Override
    protected void themePatch() {
        super.themePatch();
    }
}
