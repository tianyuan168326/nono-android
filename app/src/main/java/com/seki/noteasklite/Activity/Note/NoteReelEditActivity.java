package com.seki.noteasklite.Activity.Note;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
import com.seki.noteasklite.Base.BaseActivity;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Controller.NoteReelsController;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteReelArray;
import com.seki.noteasklite.Delegate.EditNoteDelegate;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.TimeLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class NoteReelEditActivity extends BaseActivity {
    SimpleDraweeView banner;
    EditText reel_name_tv;
    EditText reel_abstract_tv;
    TextView reel_create_time_tv;
    FloatingActionButton reel_edit_done;
    ImageView reel_pic_switch;
    String imagePath;
    NoteReelArray noteReelArray;
    String dateFormat = "yyyy 年 MM 月 dd 日";
    @Override
    protected void registerWidget() {
        banner = $(R.id.banner);
        reel_name_tv = $(R.id.reel_name_tv);
        reel_abstract_tv = $(R.id.reel_abstract_tv);
        reel_create_time_tv = $(R.id.reel_create_time_tv);
        reel_edit_done = $(R.id.reel_edit_done);
        reel_pic_switch = $(R.id.reel_pic_switch);
        banner.setOnClickListener(this);
        reel_edit_done.setOnClickListener(this);
        reel_pic_switch.setOnClickListener(this);
        if(noteReelArray == null){
            reel_create_time_tv.setText(TimeLogic.getNowTimeFormatly(dateFormat));
        }else{
            reel_name_tv.setText(noteReelArray.reel_title);
            reel_abstract_tv.setText(noteReelArray.reel_abstract);
            try{
                FrescoImageloadHelper.simpleLoadImageFromURI(banner,Uri.fromFile(new File(noteReelArray.reel_title_pic)));
            }catch (Exception e){
            }
            reel_create_time_tv.setText(TimeLogic.getTimeToFormatString(Long.valueOf(noteReelArray.reel_create_time)
            ,dateFormat
            ));
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
        processIntent();
        setContentView(R.layout.activity_note_reel_edit,"编辑文集");
    }

    private void processIntent() {
        Intent  intent = getIntent();
        noteReelArray = intent.getParcelableExtra("reel");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reel_pic_switch:
            case R.id.banner:
                FishBun.with(this)
                        .setAlbumThumnaliSize(150)//you can resize album thumnail size
                        .setActionBarColor(ThemeController.getCurrentColor().mainColor)           // only actionbar color
                        .setPickerCount(1)//you can restrict photo count
                        .setPickerSpanCount(5)
                        .setRequestCode(Define.ALBUM_REQUEST_CODE) //request code is 11. default == Define.ALBUM_REQUEST_CODE(27)
                        .setCamera(true)//you can use camera
                        .textOnImagesSelectionLimitReached("只允许选择一张图片!")
                        .textOnNothingSelected("未选择图片")
                        .setButtonInAlbumActiviy(true)
                        .startAlbum();
                break;
            case R.id.reel_edit_done:
                if(TextUtils.isEmpty(reel_name_tv.getText().toString().trim())){
                    Toast.makeText(this,"文集名字不能为空",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(noteReelArray == null){
                    NoteReelsController.addReel(new NoteReelArray(
                            reel_name_tv.getText().toString().trim()
                            ,reel_abstract_tv.getText().toString().trim()
                            ,null,
                            0,
                            imagePath
                    ));
                }else{
                    NoteController.alterGroupName(noteReelArray.reel_title,reel_name_tv.getText().toString().trim());
                    NoteReelsController.updateReel(noteReelArray.id,new NoteReelArray(
                            reel_name_tv.getText().toString().trim(),
                            reel_abstract_tv.getText().toString().trim(),
                            imagePath
                    ));
                }

                new AlertDialog.Builder(this)
                        .setMessage("是否马上创建一则笔记?")
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditNoteDelegate.start(NoteReelEditActivity.this,
                                        new NoteAllArray("","",
                                                reel_name_tv.getText().toString(),
                                                null,
                                                null,
                                                -1,
                                                "false",
                                                null
                                                ));
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Define.ALBUM_REQUEST_CODE:
                //更换图片背景
                if (resultCode == RESULT_OK) {
                    ArrayList<String > imagePaths = data.getStringArrayListExtra(Define.INTENT_PATH);
                    if(imagePaths.size() ==1){
                        imagePath = imagePaths.get(0);
                        FrescoImageloadHelper.simpleLoadImageFromURI(banner,Uri.fromFile(new File(imagePath)));
                    }
                }
                break;
        }
    }

    public static Intent start(Context c, NoteReelArray noteReelArray) {
        Intent  intent = new Intent();
        intent.setClass(c,NoteReelEditActivity.class);
        intent.putExtra("reel",noteReelArray);
        c.startActivity(intent);
        return intent;
    }
}
