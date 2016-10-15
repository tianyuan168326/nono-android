package com.seki.therichedittext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Seki on 2016/5/2.
 */
public class PhotoLayout extends BaseContainer implements View.OnClickListener{

    private AppCompatImageView imageView;
    private AppCompatEditText editText;
    private LinearLayout preContainer;
    private LinearLayout btnContainer;
    private String imgPath="";
    private String note="";

    public final static int REQ_IMAGE=0x10;

    public PhotoLayout(Context context) {
        super(context);
    }

    public PhotoLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initUI() {
        initPreContainer();
        initImgContainer();
        initBtnContainer();
        int paddingVertical=(int)(getResources().getDisplayMetrics().density*4);
        this.setPadding(0,paddingVertical,0,paddingVertical);
    }

    private void initBtnContainer(){
        btnContainer=new LinearLayout(getContext());
        btnContainer.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity=Gravity.START|Gravity.BOTTOM;
        btnContainer.setLayoutParams(lp);
        AppCompatButton btnNote=new AppCompatButton(getContext());
        btnNote.setText(getContext().getString(R.string.photo_note));
        btnNote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setVisibility(VISIBLE);
                btnContainer.setVisibility(GONE);
                editText.requestFocus();
            }
        });
        btnContainer.addView(btnNote);
        AppCompatButton btnChange=new AppCompatButton(getContext());
        btnChange.setText(getContext().getString(R.string.photo_change));
        btnChange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoLayout.this.onClick(preContainer);
            }
        });
        btnContainer.addView(btnChange);
        AppCompatButton btnDelete=new AppCompatButton(getContext());
        btnDelete.setText(getContext().getString(R.string.photo_delete));
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onPhotoDelete(PhotoLayout.this);
                }
            }
        });
        btnContainer.addView(btnDelete);
        btnContainer.setVisibility(GONE);
        this.addView(btnContainer);
    }

    private void initPreContainer(){
        preContainer=new LinearLayout(getContext());
        preContainer.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity=Gravity.CENTER_VERTICAL;
        preContainer.setLayoutParams(lp);
        preContainer.setOnClickListener(this);
        AppCompatImageView tmpImageView=new AppCompatImageView(getContext());
        tmpImageView.setBackgroundResource(R.mipmap.ic_image_black_24dp);
        AppCompatTextView tmpTextView=new AppCompatTextView(getContext());
        LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.gravity=Gravity.CENTER_VERTICAL;
        tmpImageView.setLayoutParams(lp1);
        tmpTextView.setLayoutParams(lp1);
        tmpTextView.setText(getContext().getString(R.string.select_photo));
        preContainer.addView(tmpImageView);
        preContainer.addView(tmpTextView);
        this.addView(preContainer);
    }

    private void initImgContainer(){
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView=new AppCompatImageView(getContext());
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        linearLayout.addView(imageView);
        imageView.setOnClickListener(this);
        editText=new AppCompatEditText(getContext());
        linearLayout.addView(editText);
        editText.setVisibility(GONE);
        editText.setTextAppearance(getContext(),R.style.NoteTextAppearance);
        editText.setGravity(Gravity.CENTER);
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_DEL&&editText.getText().toString().isEmpty()){
                    editText.setVisibility(GONE);
                }
                return false;
            }
        });
        this.addView(linearLayout);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(preContainer)){
            chooseMedia(getContext());
//            Intent i = new Intent(
//                    Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            ((Activity) getContext()).startActivityForResult(i, REQ_IMAGE);
            RichEdit.pickImage=this;
        }else if(v.equals(imageView)){
            if(btnContainer.getVisibility()!=VISIBLE){
                btnContainer.setVisibility(VISIBLE);
            }else {
                btnContainer.setVisibility(GONE);
            }
        }
    }

    private void chooseMedia(final Context context){
        final Context contextWrapper = context;
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setItems(new CharSequence[]{"相机", "媒体库",getContext().getString(R.string.photo_delete)
//                "从图片网址"
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                            File file=new File(contextWrapper.getExternalCacheDir(),"camera_temp");
                            if(file.exists()){
                                file.delete();
                            }
                            getImageByCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(contextWrapper.getExternalCacheDir(),"camera_temp"))
                                    //contextWrapper.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                            );
                            ((Activity)getContext()).startActivityForResult(getImageByCamera, RichEdit.RESULT_CHOOSE_CAMERA);
                        } else {
                            Toast.makeText(contextWrapper, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        FishBun.with((AppCompatActivity)getContext())
                                .setAlbumThumnaliSize(150)//you can resize album thumnail size
                                // .setActionBarColor(ThemeController.getCurrentColor().mainColor)           // only actionbar color
                                .setPickerCount(1)//you can restrict photo count
                                .setPickerSpanCount(5)
                                .setRequestCode(Define.ALBUM_REQUEST_CODE) //request code is 11. default == Define.ALBUM_REQUEST_CODE(27)
                                .setCamera(true)//you can use camera
                                .textOnImagesSelectionLimitReached("只允许选择一张图片!")
                                .textOnNothingSelected("未选择图片")
                                .setButtonInAlbumActiviy(true)
                                .startAlbum();
                        break;
                    case 2:
                        if(listener!=null){
                            listener.onPhotoDelete(PhotoLayout.this);
                        }
                        break;
//                    case 3:
//                        if(editDialog == null){
//                            dialog.dismiss();
//                            editDialog = new AlertDialog.Builder(getActivity()).setView(editText).setPositiveButton(
//                                    "输好了~", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            EditText edit = (EditText)inputView.findViewById(R.id.edit_url);
//                                            processUrlImage(edit.getText().toString());
//                                        }
//                                    }
//                            ).show();
//                        }
//                        break;
                }
            }

        });
        builder.show();
    }
    public void setImage(final String imgPath) {
        if(imgPath.isEmpty()){
            preContainer.setVisibility(VISIBLE);
            imageView.setImageDrawable(null);
            btnContainer.setVisibility(GONE);
            return;
        }
        preContainer.setVisibility(GONE);
        btnContainer.setVisibility(GONE);
        if (imageView.getWidth() == 0) {
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }else {
                        imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    layoutImage(imageView,imgPath);
                    PhotoLayout.this.imgPath = imgPath;
                }
            });
        } else {
            layoutImage(imageView,imgPath);
            this.imgPath = imgPath;
        }
    }

    private void layoutImage(final AppCompatImageView imageView, String source) {
        final File file;
        //file = new File(source);
        if(source.startsWith("http")){
            file = new File(imageView.getContext().getApplicationContext().getExternalFilesDir(null), source.substring(source.lastIndexOf("/"), source.length()));
        }else{
            file = new File(source);
        }
        if (file.exists()){
            //存在直接加载
                        imageView.setImageBitmap(
                    ImageProcessor.zoomImageMax(
                            ImageProcessor.decodeResizedImageFromBitmapAndReturn(file.getAbsolutePath(), imageView.getWidth(), imageView.getWidth()),
                            imageView.getWidth(), 0
                    ));
        }else{
            //不存在的话，下载到cache目录
            try{
                Utils.getInstance().setContext(imageView.getContext());;
                Utils.getInstance().getVollyQueue().add(new ImageRequest(source, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Utils.saveMyBitmap(file.getAbsolutePath(),bitmap);
                        imageView.setImageBitmap(
                                ImageProcessor.zoomImageMax(
                                        bitmap,
                                        imageView.getWidth(), 0
                                ));
                    }
                }, 1080, 1920, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }));
            }catch (Exception e){
                Toast.makeText(getContext(),"你一定是清理缓存了...,,现在图片加载不出来了...",Toast.LENGTH_LONG).show();
            }

        }
    }

    public void setNote(String note) {
        editText.setText(note);
        if (note.isEmpty()) {
            editText.setVisibility(GONE);
        } else {
            editText.setVisibility(VISIBLE);
        }
    }

    @Override
    protected String toHtml() {
        return "<section id=\""+type+"\">"+"<img src=\""+imgPath+"\"/><p style=\"color:#757575; font-size:0.78em;\">"+
                editText.getText().toString().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br>")
                +"</p></section>";
    }
    //patch-windstring
    //for the suck of old code~!
       @Override
    protected void setHtml(String html) {
//        if(html.startsWith("<img src")&& html.endsWith("/>")){
//           int index  =html.indexOf("\"");
//            int lastIndex = html.lastIndexOf("\"");
//            try{
//                String imagePath = html.substring(index+1,lastIndex);
//                setImage(imagePath);
//            }catch (Exception e){}
//        }else{
            try{
                html=html.substring(html.indexOf("src=\"")+"src=\"".length());
                setImage(html.substring(0,html.indexOf("\"")).trim());
                html=html.substring(html.indexOf(">")+1);
                html=html.substring(html.indexOf(">")+1);
                setNote(html.substring(0,html.indexOf("</p>"))
                        .replace("<br>","\n")
                        .replace("&gt;",">")
                        .replace("&lt;","<")
                        .replace("&amp;","&"));
            }catch (Exception e){}
//        }

    }

    @Override
    protected BaseRichEditText returnEdit() {
        return null;
    }

    @Override
    protected void setType() {
        type=TYPE_PHOTO;
    }

    @Override
    protected boolean reqFocus() {
        return requestFocus();
    }

    @Override
    protected boolean isEmpty() {
        return imageView.getDrawable()==null;
    }

    @Override
    protected void setEditable(final boolean editable) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                if(editable){
                    editText.setFocusableInTouchMode(true);
                }
                editText.setFocusable(editable);
                if(!editable){
                    editText.clearFocus();
                }
            }
        });
        imageView.post(new Runnable() {
            @Override
            public void run() {
                //editText.setBackgroundDrawable(null);
                imageView.setFocusable(editable);
                if (!editable) {
                    btnContainer.setVisibility(GONE);
                    preContainer.setVisibility(GONE);
                    btnContainer.getChildAt(0).setVisibility(GONE);
                    btnContainer.getChildAt(2).setVisibility(GONE);
                    btnContainer.getChildAt(1).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File file = new File(imgPath);
                            if (!file.exists()) {
                                file=new File(getContext().getExternalFilesDir(null),imgPath.substring(imgPath.lastIndexOf("/")+1));
                            }
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file), "image/*");
                            getContext().startActivity(intent);
                        }
                    });
                    ((AppCompatButton)btnContainer.getChildAt(1)).setText(getContext().getString(R.string.view_photo));
                }
            }
        });

    }

    public interface OnPhotoDeleteListener{
        void onPhotoDelete(PhotoLayout layout);
    }

    private OnPhotoDeleteListener listener=null;

    public void setOnPhotoDeleteListener(OnPhotoDeleteListener listener){
        this.listener=listener;
    }
}
