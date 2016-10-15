package com.seki.noteasklite.CustomControl;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.GetPathFromUri4kitkat;

import java.io.File;

/**
 * Created by Seki on 2016/5/21.
 */
public class MarkdownEditText extends LinearLayout implements View.OnClickListener{

    private String currentFileName=null;
    private static final int REQ_IMAGE=0x10;
    private static final int RESULT_CROP_RESULT = 0x33;
    private static final int RESULT_CHOOSE_CAMERA =0x34;

    private AppCompatEditText editText;

    public MarkdownEditText(Context context) {
        this(context, null);
    }

    public MarkdownEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkdownEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public String getText(){
        return editText.getText().toString();
    }
    public void  setText(String text){
            if(text!=null){
                editText.setText(text);
            }
    }
    private void init(){
        Context context=getContext();
        View view=View.inflate(context, R.layout.markdown_edittext,this);
        editText=(AppCompatEditText)view.findViewById(R.id.markdown_edit);
        LinearLayout linearLayout=(LinearLayout) view.findViewById(R.id.span_toolbar);
        for(int i=0;i<linearLayout.getChildCount();i++){
            linearLayout.getChildAt(i).setOnClickListener(this);
        }
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (KeyEvent.ACTION_DOWN == event.getAction()) {
                            int index = getParagraphStart();
                            String text = editText.getText().toString().substring(index);
                            int index2 = text.indexOf("- ");
                            if (index2 >= 0 && text.substring(0, index2).trim().isEmpty()) {
                                if (editText.getSelectionStart() < editText.getSelectionEnd()) {
                                    editText.getEditableText().replace(editText.getSelectionStart(), editText.getSelectionEnd(), "\n" + text.substring(0, index2) + "- ");
                                } else {
                                    editText.getEditableText().insert(editText.getSelectionStart(), "\n" + text.substring(0, index2) + "- ");
                                }
                                return true;
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.emphasis){
            addMarkAtCursor("*");
        }else if(id==R.id.title){
            addMarkAtStart("#",false);
        }else if(id==R.id.link){
            addLink();
        }else if(id==R.id.image){
            chooseMedia(getContext());
        }else if(id==R.id.quote){
            addMarkAtStart(">",true);
        }else if(id==R.id.code){
            addMarkUpAndDown("```");
        }else if(id==R.id.dot){
            addMarkAtStart("-",true);
        }else if(id==R.id.indent){
            addMarkAtStart("\t");
        }else if(id==R.id.mark){
            addMarkAtCursor("`");
        }else if(id==R.id.del){
            addMarkAtCursor("~~");
        }
    }

    private int getParagraphStart(){
        int index=editText.getSelectionStart();
        String text=editText.getText().toString();
        index=text.substring(0,index).lastIndexOf("\n")+1;
        return index;
    }

    private void addMarkUpAndDown(String s){
        addMarkAtCursor("\n"+s+"\n\n"+s+"\n");
        editText.setSelection(editText.getSelectionStart()-(s+"\n\n").length());
    }

    private void addMarkAtCursor(String s){
        int index=editText.getSelectionStart();
        editText.getEditableText().insert(index,s);
    }

    private void addMarkAtStart(String s, boolean mustWithSpace){
        int i=getParagraphStart();
        if(editText.length()==i){
            editText.getEditableText().insert(i,s+" ");
            return;
        }
        char c=editText.getText().charAt(i);
        if((c==' '||c==s.charAt(0))&&!mustWithSpace){
            editText.getEditableText().insert(i,s);
        }else {
            editText.getEditableText().insert(i,s+" ");
        }
    }

    private void addMarkAtStart(String s){
        int i=getParagraphStart();
        editText.getEditableText().insert(i,s);
    }

    private void addImage(String path){
        addMarkAtCursor("\n![image]("+path+")");
    }

    private void addLink(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        final View view= LayoutInflater.from(getContext()).inflate(R.layout.dialog_link,null);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton(getContext().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text=((AppCompatEditText)view.findViewById(R.id.text)).getText().toString().trim();
                String link=((AppCompatEditText)view.findViewById(R.id.link)).getText().toString().trim();
                if(link.isEmpty()){
                    return;
                }else {
                    text=text.isEmpty()?"link":text;
                    addMarkAtCursor("["+text+"]("+link+")");
                }
            }
        });
        builder.setNegativeButton(getContext().getString(android.R.string.cancel),null);
        builder.show();
    }

    private void chooseMedia(final Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setItems(new CharSequence[]{"相机", "媒体库","从图片网址"
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                            File file=new File(context.getExternalCacheDir(),"camera_temp");
                            if(file.exists()){
                                file.delete();
                            }
                            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(context.getExternalCacheDir(),"camera_temp"))
                                    //contextWrapper.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                            );
                            ((Activity)getContext()).startActivityForResult(getImageByCamera, RESULT_CHOOSE_CAMERA);
                        } else {
                            Toast.makeText(context, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        ((Activity) getContext()).startActivityForResult(i, REQ_IMAGE);
                        break;
                    case 2:
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        final AppCompatEditText editText=new AppCompatEditText(getContext());
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                        editText.setText("http://");
                        editText.setSelection(editText.length());
                        builder.setTitle("link");
                        builder.setView(editText);
                        builder.setCancelable(false);
                        builder.setPositiveButton(getContext().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String link=editText.getText().toString().trim();
                                if(link.isEmpty()){
                                    return;
                                }else {
                                    addImage(link);
                                }
                            }
                        });
                        builder.setNegativeButton(getContext().getString(android.R.string.cancel),null);
                        builder.show();
                        break;

                }
            }

        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==Activity.RESULT_OK){
                if(requestCode==REQ_IMAGE) {
                    Uri uri = data.getData();
                    doCrop(GetPathFromUri4kitkat.getPath((Activity) getContext(), uri));
                //}else if(requestCode == Define. ALBUM_REQUEST_CODE&&resultCode== Activity.RESULT_OK){
                //    ArrayList<String>  path = data.getStringArrayListExtra(Define.INTENT_PATH);
                //    if(path.size() ==1){
                //        doCrop(path.get(0));
                //    }
                }
                else if(requestCode == RESULT_CHOOSE_CAMERA){
                    String src = new File(getContext().getExternalCacheDir(), "camera_temp").getAbsolutePath();
                    doCrop(src);
                }
                else if(requestCode ==  RESULT_CROP_RESULT&& currentFileName !=null){
                    final String cropedSrc=new File(getContext().getExternalFilesDir(null),currentFileName).getAbsolutePath();
                    addImage(cropedSrc);
                }
            }
    }

    private void doCrop(String src){
        if (src == null || src.length() <= 0) {
            Toast.makeText(getContext(), "路径为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(src)), "image/*");
        intent.putExtra("scale", true);
        currentFileName = "temp"+String.valueOf(System.currentTimeMillis());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getContext().getExternalFilesDir(null), currentFileName)));
        ((Activity)getContext()).startActivityForResult(intent, RESULT_CROP_RESULT);
    }

}
