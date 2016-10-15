package com.seki.noteasklite.Fragment.Ask;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seki.noteasklite.OnGetImagePolicy.IOnGetImagePolicy;
import com.seki.noteasklite.OnGetImagePolicy.ImageProcessor;
import com.seki.noteasklite.OnGetImagePolicy.OnGetImageByQiNiu;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.GetPathFromUri4kitkat;
import com.seki.noteasklite.Util.NotifyHelper;

import java.io.File;

/**
 * Created by yuan-tian01 on 2016/2/27.
 */
public class RegisterMoreFragment extends Fragment implements View.OnClickListener{
    SimpleDraweeView head_img_view;
    TextView head_img_sex_mask;
    TextView male;
    TextView female;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_more,container,false);
    }
    private void chooseMedia(Context context){
        final Context contextWrapper = context;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setItems(new CharSequence[]{"相机", "媒体库"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                            //ContentValues values = new ContentValues();
                            File file = new File(contextWrapper.getExternalCacheDir(), "camera_temp");
                            if (file.exists()) {
                                file.delete();
                            }
                            getImageByCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(contextWrapper.getExternalCacheDir(), "camera_temp"))
                                    //contextWrapper.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                            );
                            startActivityForResult(getImageByCamera, 2);
                        } else {
                            Toast.makeText(contextWrapper, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        getActivity().startActivityForResult(intent, 1);
                        break;
                }
            }

        });
        builder.show();
    }
    IOnGetImagePolicy nGetImagePolicy  = new OnGetImageByQiNiu(new IOnGetImagePolicy.OnRealPath() {
        @Override
        public void realPath(String realPath) {
            moreInfo.setHeadImgUrl(realPath);
            FrescoImageloadHelper.simpleLoadImageFromURL(head_img_view, realPath);
            if(progressWindow !=null){
                progressWindow.dismiss();
                progressWindow =null;
            }

        }
    }){
        @Override
        public String preImageProcess(String srcPath) {
            return ImageProcessor.compressImage(getActivity(), srcPath);
        }
    };
    PopupWindow progressWindow = null;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri==null) {
                    Toast.makeText(getContext(), "不能正确获取文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                String src = GetPathFromUri4kitkat.getPath(getActivity(), uri);
                progressWindow = NotifyHelper.popUpWaitingAnimation(getActivity(),progressWindow);
                nGetImagePolicy.getRealImagePath(src);
                if(src==null||src.length()<=0){
                    Toast.makeText(getContext(), "路径为空", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }else if(requestCode==2) {
            if (resultCode == Activity.RESULT_OK) {
                String src = new File(getActivity().getExternalCacheDir(), "camera_temp").getAbsolutePath();
                progressWindow = NotifyHelper.popUpWaitingAnimation(getActivity(),progressWindow);
                nGetImagePolicy.getRealImagePath(src);
            }
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        head_img_view = (SimpleDraweeView)getView().findViewById(R.id.head_img_view);
        head_img_sex_mask = (TextView)getView().findViewById(R.id.head_img_sex_mask);
        male = (TextView)getView().findViewById(R.id.male);
        female = (TextView)getView().findViewById(R.id.female);
        head_img_view.setOnClickListener(this);
        head_img_sex_mask.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);

    }
    public static RegisterMoreFragment newInstance(){
        return new RegisterMoreFragment();
    }
    MoreInfo moreInfo  = new MoreInfo();
    public MoreInfo getMoreInfo() {
        return moreInfo;
    }

    @Override
    public void onClick(View v) {
        int delataXFromSexToParentCenter =((LinearLayout) male.getParent()).getWidth()/2-((male.getLeft()+male.getRight())/2);
        android.view.animation.Interpolator interpolator = new BounceInterpolator();
        AnimationSet maleAnimationSet = new AnimationSet(false);
        AnimationSet femaleAnimationSet = new AnimationSet(false);
        final TranslateAnimation maleTanslationAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
                Animation.ABSOLUTE,delataXFromSexToParentCenter,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,0.0f);
        maleTanslationAnimation.setDuration(1000);
        maleTanslationAnimation.setFillAfter(true);
        maleTanslationAnimation.setInterpolator(interpolator);
        final TranslateAnimation femaleTanslationAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
                Animation.ABSOLUTE,(-1)*delataXFromSexToParentCenter,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,0.0f);
        femaleTanslationAnimation.setDuration(1000);
        femaleTanslationAnimation.setFillAfter(true);
        femaleTanslationAnimation.setInterpolator(interpolator);

        final  AlphaAnimation upAlphaAnimation = new AlphaAnimation(0,1);
        upAlphaAnimation.setDuration(1000);
        upAlphaAnimation.setFillAfter(true);
        upAlphaAnimation.setInterpolator(interpolator);
        final  AlphaAnimation downAlphaAnimation = new AlphaAnimation(1,0);
        downAlphaAnimation.setDuration(1000);
        downAlphaAnimation.setFillAfter(true);
        downAlphaAnimation.setInterpolator(interpolator);
        switch (v.getId()){
            case R.id.head_img_view:
                if(getActivity()!=null){
                    chooseMedia(getActivity());
                }
                break;
            case R.id.head_img_sex_mask:
                if(!TextUtils.isEmpty( head_img_sex_mask.getText())){
                    head_img_sex_mask.setText("");
                    male.setVisibility(View.VISIBLE);
                    female.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.male:

                maleAnimationSet.addAnimation(maleTanslationAnimation);
                maleAnimationSet.addAnimation(upAlphaAnimation);
                maleAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        male.setVisibility(View.GONE);
                        head_img_sex_mask.setText("♂");
                        head_img_sex_mask.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                male.startAnimation(maleAnimationSet);


                femaleAnimationSet.addAnimation(femaleTanslationAnimation);
                femaleAnimationSet.addAnimation(downAlphaAnimation);
                femaleAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        female.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                female.startAnimation(femaleAnimationSet);
                moreInfo.setSex("male");
                break;
            case R.id.female:
                maleAnimationSet.addAnimation(maleTanslationAnimation);
                maleAnimationSet.addAnimation(downAlphaAnimation);
                maleAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        male.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                male.startAnimation(maleAnimationSet);


                femaleAnimationSet.addAnimation(femaleTanslationAnimation);
                femaleAnimationSet.addAnimation(upAlphaAnimation);
                femaleAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        female.setVisibility(View.GONE);
                        head_img_sex_mask.setText("♀");
                        head_img_sex_mask.setTextColor(getResources().getColor(R.color.colorAccent));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                female.startAnimation(femaleAnimationSet);
                moreInfo.setSex("female");
                break;
        }
    }

    public static class MoreInfo extends RegisterAccountFragment.BaseInfo{
        String headImgUrl;
        String sex;

        public MoreInfo(String headImgUrl, String sex) {
            this.headImgUrl = headImgUrl;
            this.sex = sex;
        }

        public MoreInfo() {
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

}
