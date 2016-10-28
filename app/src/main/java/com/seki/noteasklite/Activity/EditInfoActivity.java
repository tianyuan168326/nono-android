 package com.seki.noteasklite.Activity;

 import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.seki.noteasklite.Base.BaseActivity;
 import com.seki.noteasklite.Controller.AccountController;
 import com.seki.noteasklite.Controller.ThemeController;
 import com.seki.noteasklite.DataUtil.BusEvent.UpDateUserInfoSuccessEvent;
 import com.seki.noteasklite.DataUtil.BusEvent.UpdateUserInfoFailedEvent;
 import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.OnGetImagePolicy.IOnGetImagePolicy;
import com.seki.noteasklite.OnGetImagePolicy.ImageProcessor;
import com.seki.noteasklite.OnGetImagePolicy.OnGetImageByQiNiu;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.GetPathFromUri4kitkat;
 import com.seki.noteasklite.Util.NotifyHelper;

 import java.util.HashMap;


public class EditInfoActivity extends BaseActivity implements View.OnClickListener {
    SimpleDraweeView editInfoHeadpic;
    EditText editInfoAbstract;
    EditText editInfoName;
    TextView editInfoCommit;
    private String selectedHeadImgURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info, "头像和介绍");

    }
    private void registerEvents() {
        editInfoHeadpic.setOnClickListener(this);
        editInfoCommit.setOnClickListener(this);
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK && requestCode == 4) {
            Uri uri = data.getData();
            if (uri==null) {
                Toast.makeText(this, "不能正确获取文件", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.e("uri", uri.toString());
            String src = GetPathFromUri4kitkat.getPath(this, uri);
            if(src==null||src.length()<=0){
                Toast.makeText(this, "路径为空", Toast.LENGTH_SHORT).show();
                return;
            }
            //上传时转动的圆圈
            final ProgressWheel wheel = new ProgressWheel(this);
            wheel.setBarColor(getResources().getColor(R.color.colorAccent));
            new  OnGetImageByQiNiu(
                    new IOnGetImagePolicy.OnRealPath() {
                        @Override
                        public void realPath(String realPath) {
                            wheel.stopSpinning();
                            selectedHeadImgURL = realPath;
                            MyApp.userInfo.userHeadPicURL = selectedHeadImgURL;
                            FrescoImageloadHelper.simpleLoadImageFromURL(editInfoHeadpic, realPath);
                        }
                    }
            ){
                @Override
                public String preImageProcess(String srcPath) {
                    wheel.spin();
                    return   ImageProcessor.compressImage(EditInfoActivity.this,srcPath);
                }
            }
            .getRealImagePath(src);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info_headpic:
                AlertDialog.Builder builder=new AlertDialog.Builder(EditInfoActivity.this);
                builder.setItems(new CharSequence[]{"相机", "媒体库"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String state = Environment.getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                                    ContentValues values = new ContentValues();
                                    getImageByCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                                    );
                                    startActivityForResult(getImageByCamera, 5);
                                } else {
                                    Toast.makeText(EditInfoActivity.this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 1:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 4);
                                break;
                        }
                    }

                });
                builder.show();
                //startActivityForResult(new Intent(this, SelectPicActivity.class), 4);
                break;
            case R.id.edit_info_commit:
                AccountController.updateUserInfo(
                        editInfoAbstract.getText().toString(),
                        editInfoName.getText().toString(),
                        selectedHeadImgURL
                );
            break;
        }
    }

    @Override
    protected void registerWidget() {
         editInfoHeadpic = (SimpleDraweeView)findViewById(R.id.edit_info_headpic);
        editInfoAbstract = (EditText)findViewById(R.id.edit_info_abstract);
        editInfoName = (EditText)findViewById(R.id.edit_info_name);
        editInfoCommit = (TextView)findViewById(R.id.edit_info_commit);
        selectedHeadImgURL=MyApp.getInstance().userInfo.userHeadPicURL;
        registerEvents();
        loadUI();
    }
    void loadUI(){
        FrescoImageloadHelper.simpleLoadImageFromURL(editInfoHeadpic, selectedHeadImgURL);
        editInfoName.setText(MyApp.getInstance().userInfo.userRealName);
        editInfoAbstract.setText(MyApp.getInstance().userInfo.userAbstract);
        editInfoAbstract.setSelection(MyApp.getInstance().userInfo.userAbstract.length());
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUI();
    }

    @Override
    protected void registerAdapters() {

    }

    @Override
    protected HashMap<Integer, String> setUpOptionMenu() {
        return null;
    }

    @Override
    protected void themePatch() {
        super.themePatch();
        $(R.id.just_bg).setBackgroundColor(ThemeController.getCurrentColor().mainColor);
    }

    public void onEventMainThread(UpDateUserInfoSuccessEvent e){
        finish();
        NotifyHelper.makePlainToast(getString(R.string.updata_info_success));
    }

    public void onEventMainThread(UpdateUserInfoFailedEvent e){
        startActivity(new Intent().setClass(this, LogOnActivity.class));
    }
}

