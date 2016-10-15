package com.seki.therichedittext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Seki on 2016/5/2.
 * 这是十分重要的一个控件，所有样式的控件都通过此显示。
 * 有新样式时，请更新onEnterDown、onDelDown、getHtmlText、setHtmlText及新写函数add***Layout(BaseContainer container)
 */
public class RichEdit extends LinearLayout implements BaseRichEditText.OnSelectionSpanCallback,BaseRichEditText.AfterTextChangedCallback,PhotoLayout.OnPhotoDeleteListener{

    protected BaseContainer baseContainer =null;
    public static PhotoLayout pickImage=null;
    private boolean preSet=false;


    public RichEdit(Context context) {
        this(context, null);
    }

    public RichEdit(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichEdit(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                baseContainer.reqFocus();
            }
        });
        this.setOrientation(VERTICAL);
        RichEditText richEditText=new RichEditText(context);
        this.addView(richEditText);
        richEditText.editText.setOnSelectionSpanCallback(this);
        //richEditText.reqFocus();
        clearFocus();
        this.baseContainer =richEditText;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==PhotoLayout.REQ_IMAGE&&resultCode== Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (pickImage != null) {
                pickImage.setImage(GetPathFromUri4kitkat.getPath((Activity) getContext(), uri));
            }
        }else if(requestCode == Define. ALBUM_REQUEST_CODE&&resultCode== Activity.RESULT_OK){
            ArrayList<String>  path = data.getStringArrayListExtra(Define.INTENT_PATH);
            if(path.size() ==1){
                doCrop(path.get(0));
            }
        }
        else if(requestCode == RESULT_CHOOSE_CAMERA&&resultCode== Activity.RESULT_OK){
            String src = new File(getContext().getExternalCacheDir(), "camera_temp").getAbsolutePath();
            doCrop(src);
        }
        else if(requestCode ==  RESULT_CROP_RESULT&&resultCode== Activity.RESULT_OK && currentFileName !=null){
            final String cropedSrc=new File(getContext().getExternalFilesDir(null),currentFileName).getAbsolutePath();
            pickImage.setImage(cropedSrc);
        }
    }

    String currentFileName = null;

    private static final int RESULT_CROP_RESULT = 0x33;

    public static final int RESULT_CHOOSE_CAMERA =0x34;

    private void doCrop(String src){
        if (src == null || src.length() <= 0) {
            Toast.makeText(getContext(), "路径为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(src)), "image/*");
        intent.putExtra("scale", true);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        currentFileName = "temp"+String.valueOf(System.currentTimeMillis());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getContext().getExternalFilesDir(null), currentFileName)));
        ((AppCompatActivity)getContext()).startActivityForResult(intent, RESULT_CROP_RESULT);
    }

    public BaseContainer addBaseContainerMid(BaseContainer container){
        if(preSet){
            return null;
        }
        int index=container==null?-1:this.indexOfChild(container);
        if(container!=null){
            if(!(container instanceof PhotoLayout)){
                View view=container.findFocus();
                if(view !=null &&view instanceof BaseRichEditText){
                    int sel=((BaseRichEditText) view).getSelectionStart();
                    if(sel<((BaseRichEditText) view).length()){
                        SpannableStringBuilder span=new SpannableStringBuilder(((BaseRichEditText) view).getText());
                        //Spanned before=span.replace(sel,span.length(),"");
                       // Spanned after=span.replace(0,sel>=0?sel:0,"");
                        ((BaseRichEditText) view).setText(
                                span.subSequence(0,sel>=0?sel:0));
                        //span=new SpannableStringBuilder(((BaseRichEditText) view).getText());
                        switch (container.getType()){
                            case BaseContainer.TYPE_TEXT:
                                RichEditText richEditText=new RichEditText(getContext());
                                richEditText.editText.setText(
                                        span.subSequence(sel,span.length()));
                                richEditText.editText.setAfterTextChangedCallback(this);
                                richEditText.editText.setOnSelectionSpanCallback(this);
                                this.addView(richEditText,index+1);
                                richEditText.reqFocus();
                                return richEditText;
                            case BaseContainer.TYPE_DOT:
                                DotLayout dotLayout=new DotLayout(getContext());
                                dotLayout.editText.setText(
                                        span.subSequence(sel,span.length()));
                                dotLayout.editText.setAfterTextChangedCallback(this);
                                dotLayout.editText.setOnSelectionSpanCallback(this);
                                this.addView(dotLayout,index+1);
                                dotLayout.reqFocus();
                                return dotLayout;
                            case BaseContainer.TYPE_TODO:
                                TodoLayout todoLayout=new TodoLayout(getContext());
                                todoLayout.editText.setText(
                                        span.subSequence(sel,span.length()));
                                todoLayout.editText.setAfterTextChangedCallback(this);
                                todoLayout.editText.setOnSelectionSpanCallback(this);
                                this.addView(todoLayout,index+1);
                                todoLayout.reqFocus();
                                return  todoLayout;
                            case BaseContainer.TYPE_NUMERIC:
                                NumericLayout numericLayout=new NumericLayout(getContext());
                                numericLayout.editText.setText(
                                        span.subSequence(sel,span.length()));
                                numericLayout.editText.setAfterTextChangedCallback(this);
                                numericLayout.editText.setOnSelectionSpanCallback(this);
                                this.addView(numericLayout,index+1);
                                numericLayout.reqFocus();
                                updateNumericList();
                                return numericLayout;
                            default:return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    public RichEditText addRichEditText(@Nullable BaseContainer container){
        RichEditText richEditText=new RichEditText(getContext());
        richEditText.setFocusable(false);
        if(container==null){
            this.addView(richEditText);
        }else {
            this.addView(richEditText, this.indexOfChild(container) + 1);
            inheritSpan((BaseRichEditText)container.findViewWithTag(BaseContainer.TYPE_TEXT),(BaseRichEditText)richEditText.findViewWithTag(BaseContainer.TYPE_TEXT));
        }
        richEditText.editText.setOnSelectionSpanCallback(this);
        richEditText.editText.setAfterTextChangedCallback(this);
//        richEditText.clearFocus();
        richEditText.reqFocus();
        this.baseContainer =richEditText;
        return richEditText;
    }

    public PhotoLayout addPhotoLayout(@Nullable BaseContainer container){
        int index=container==null?-1:this.indexOfChild(container);
        addBaseContainerMid(container);
        PhotoLayout photoLayout=new PhotoLayout(getContext());
        this.addView(photoLayout, index + 1);
//        photoLayout.clearFocus();
        photoLayout.reqFocus();
        photoLayout.setOnPhotoDeleteListener(this);
        this.baseContainer=photoLayout;
        if((index+2)==this.getChildCount()) {
            addRichEditText(photoLayout);
        }
        if(container!=null&&container.isEmpty()&&container.getType()!=BaseContainer.TYPE_PHOTO){
            this.removeView(container);
        }
        return photoLayout;
    }

    public PhotoLayout addPhotoLayout(@Nullable BaseContainer container,boolean newEdit){
        int index=container==null?-1:this.indexOfChild(container);
        addBaseContainerMid(container);
        PhotoLayout photoLayout=new PhotoLayout(getContext());
        this.addView(photoLayout, index + 1);
        photoLayout.reqFocus();
        photoLayout.setOnPhotoDeleteListener(this);
        this.baseContainer=photoLayout;
        if(newEdit&&(index+2)==this.getChildCount()) {
            addRichEditText(photoLayout);
        }
        if(container!=null&&container.isEmpty()&&container.getType()!=BaseContainer.TYPE_PHOTO){
            this.removeView(container);
        }
        return photoLayout;
    }

    public TodoLayout addTodoLayout(@Nullable BaseContainer container){
        int index=container==null?-1:this.indexOfChild(container);
        BaseContainer containerMid=addBaseContainerMid(container);
        if(containerMid instanceof TodoLayout){
            ((TodoLayout) containerMid).editText.setSelection(0);
            if(container!=null&&container.isEmpty()&&container.getType()!=BaseContainer.TYPE_PHOTO&&container.getType()!=BaseContainer.TYPE_TODO
                    ){
                this.removeView(container);
            }
            if(container!=null) {
                inheritSpan((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT), (BaseRichEditText) containerMid.findViewWithTag(BaseContainer.TYPE_TEXT));
            }
            this.baseContainer=containerMid;
            return (TodoLayout) containerMid;
        }
        TodoLayout todoLayout=new TodoLayout(getContext());
        todoLayout.editText.setOnSelectionSpanCallback(this);
        todoLayout.editText.setAfterTextChangedCallback(this);
        this.addView(todoLayout,index+1);
        //if((index+2)==this.getChildCount()) {
        //    addRichEditText(todoLayout);
        //}
        if(container!=null&&container.isEmpty()&&container.getType()!=BaseContainer.TYPE_PHOTO//&&container.getType()!=BaseContainer.TYPE_TODO
                ){
            this.removeView(container);
        }
        if(container!=null) {
            inheritSpan((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT), (BaseRichEditText) todoLayout.findViewWithTag(BaseContainer.TYPE_TEXT));
        }
        todoLayout.reqFocus();
//        todoLayout.clearFocus();
        this.baseContainer=todoLayout;
        return todoLayout;
    }

    public DotLayout addDotLayout(@Nullable BaseContainer container){
        int index=container==null?-1:this.indexOfChild(container);
        BaseContainer containerMid=addBaseContainerMid(container);
        if(containerMid instanceof DotLayout){
            ((DotLayout) containerMid).editText.setSelection(0);
            if(container!=null&&container.isEmpty()&&container.getType()!=BaseContainer.TYPE_PHOTO&&container.getType()!=BaseContainer.TYPE_DOT
                    ){
                this.removeView(container);
            }
            if(container!=null) {
                inheritSpan((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT), (BaseRichEditText) containerMid.findViewWithTag(BaseContainer.TYPE_TEXT));
            }
            this.baseContainer=containerMid;
            return (DotLayout)containerMid;
        }
        DotLayout dotLayout=new DotLayout(getContext());
        dotLayout.editText.setOnSelectionSpanCallback(this);
        dotLayout.editText.setAfterTextChangedCallback(this);
        this.addView(dotLayout,index+1);
        //if((index+2)==this.getChildCount()) {
        //    addRichEditText(todoLayout);
        //}
        if(container!=null&&container.isEmpty()&&container.getType()!=BaseContainer.TYPE_PHOTO//&&container.getType()!=BaseContainer.TYPE_DOT
                ){
            this.removeView(container);
        }
        if(container!=null) {
            inheritSpan((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT), (BaseRichEditText) dotLayout.findViewWithTag(BaseContainer.TYPE_TEXT));
        }
        dotLayout.reqFocus();
//        dotLayout.clearFocus();
        this.baseContainer=dotLayout;
        return dotLayout;
    }

    public NumericLayout addNumericLayout(@Nullable BaseContainer container){
        int index=container==null?-1:this.indexOfChild(container);
        BaseContainer containerMid=addBaseContainerMid(container);
        if(containerMid instanceof NumericLayout){
            ((NumericLayout) containerMid).editText.setSelection(0);
            if(container!=null&&container.isEmpty()&&container.getType()!=BaseContainer.TYPE_PHOTO&&container.getType()!=BaseContainer.TYPE_NUMERIC
                    ){
                this.removeView(container);
            }
            if(container!=null) {
                inheritSpan((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT), (BaseRichEditText) containerMid.findViewWithTag(BaseContainer.TYPE_TEXT));
            }
            this.baseContainer=containerMid;
            return (NumericLayout)containerMid;
        }
        NumericLayout numericLayout=new NumericLayout(getContext());
        numericLayout.editText.setOnSelectionSpanCallback(this);
        numericLayout.editText.setAfterTextChangedCallback(this);
        this.addView(numericLayout,index+1);
        //if((index+2)==this.getChildCount()) {
        //    addRichEditText(todoLayout);
        //}
        if(container!=null&&container.isEmpty()&&container.getType()!=BaseContainer.TYPE_PHOTO//&&container.getType()!=BaseContainer.TYPE_NUMERIC
                ){
            this.removeView(container);
        }
        if(container!=null) {
            inheritSpan((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT), (BaseRichEditText) numericLayout.findViewWithTag(BaseContainer.TYPE_TEXT));
        }
        updateNumericList();
        numericLayout.reqFocus();
//        numericLayout.clearFocus();
        this.baseContainer=numericLayout;
        return numericLayout;
    }

    public void updateNumericList(){
        int index=0;
        for(int i=0;i<getChildCount();i++){
            BaseContainer container=(BaseContainer) getChildAt(i);
            if(container.getType()==BaseContainer.TYPE_NUMERIC){
                index+=1;
                ((NumericLayout)container).setNumeric(index);
            }
        }
    }

    public void inheritSpan(BaseRichEditText parent,BaseRichEditText child){
        if(parent!=null&&child!=null){
            child.setColorFontBackground(parent.getColorFontBackground());
            child.setColorFontForeground(parent.getColorFontForeground());
            child.setIsForegroundColor(parent.isForegroundColor());
            child.setIsBackgroundColor(parent.isBackgroundColor());
            child.setIsBold(parent.isBold());
            child.setIsItalic(parent.isItalic());
            child.setIsUnderLine(parent.isUnderLine());
            child.setIsStrikethrough(parent.isStrikethrough());
            child.setSizeFlag(parent.getSizeFlag());
            child.setIsSubscript(parent.isSubscript());
            child.setIsSuperscript(parent.isSuperscript());
        }
    }

    public void onEnterDown(BaseContainer container){
        switch (container.getType()){
            case BaseContainer.TYPE_TODO:addTodoLayout(container);break;
            case BaseContainer.TYPE_DOT:addDotLayout(container);break;
            case BaseContainer.TYPE_NUMERIC:addNumericLayout(container);break;
        }
    }

    public void onDelDown(BaseContainer container){
        int index=this.indexOfChild(container);
        switch (container.getType()) {
            case BaseContainer.TYPE_TEXT:
                if (index > 0) {
                    if (((BaseContainer) this.getChildAt(index - 1)).getType() == BaseContainer.TYPE_PHOTO) {
                        this.removeViewAt(index - 1);
                        final BaseRichEditText editText=((BaseContainer) this.getChildAt(index - 1)).returnEdit();
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                editText.setSelection(0);
                                editText.requestFocus();
                            }
                        });
                    } else {
                        final BaseRichEditText editText=((BaseContainer) this.getChildAt(index - 1)).returnEdit();
                        final int length=editText.length();
                        SpannableStringBuilder builder=new SpannableStringBuilder(editText.getText());
                        builder.append(container.returnEdit().getText());
                        editText.setText(builder);
                        //editText.append(container.returnEdit().getText());
                        this.removeViewAt(index);
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                editText.setSelection(length);
                                editText.requestFocus();
                            }
                        });
                    }
                }
                break;
            case BaseContainer.TYPE_TODO:
            case BaseContainer.TYPE_DOT:
                ((LinearLayout) container.getChildAt(0)).removeViewAt(0);
                container.setType(BaseContainer.TYPE_TEXT);
                break;
            case BaseContainer.TYPE_NUMERIC:
                ((LinearLayout) container.getChildAt(0)).removeViewAt(0);
                container.setType(BaseContainer.TYPE_TEXT);
                updateNumericList();
                break;
        }
        index-=1;
        if(index>=0){
            BaseContainer view=(BaseContainer) this.getChildAt(index);
            if(view instanceof RichEditText){
                ((RichEditText) view).editText.setSelection(((RichEditText) view).editText.length());
            }else if(view instanceof DotLayout){
                ((DotLayout) view).editText.setSelection(((DotLayout) view).editText.length());
            }else if(view instanceof TodoLayout){
                ((TodoLayout) view).editText.setSelection(((TodoLayout) view).editText.length());
            }else if(view instanceof NumericLayout){
                ((NumericLayout) view).editText.setSelection(((NumericLayout) view).editText.length());
            }
        }
    }

    public String getHtmlText() {
        StringBuilder stringBuilder = new StringBuilder();
        //boolean isLastDot=false;
        //boolean isLastNumeric=false;
        for (int i = 0; i < getChildCount(); i++) {
            BaseContainer container = (BaseContainer) getChildAt(i);
            //stringBuilder.append("<section>");
            stringBuilder.append(container.toHtml());
            //stringBuilder.append("</section>");
            //switch (container.getType()) {
            //    case BaseContainer.TYPE_TEXT:
            //        if(isLastDot){
            //            stringBuilder.append("</ul>");
            //            isLastDot=!isLastDot;
            //        }
            //        if(isLastNumeric){
            //            stringBuilder.append("</ol>");
            //            isLastNumeric=!isLastNumeric;
            //        }
            //        stringBuilder.append(((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT)).getHtmlText());
            //        break;
            //    case BaseContainer.TYPE_PHOTO:
            //        if(isLastDot){
            //            stringBuilder.append("</ul>");
            //            isLastDot=!isLastDot;
            //        }
            //        if(isLastNumeric){
            //            stringBuilder.append("</ol>");
            //            isLastNumeric=!isLastNumeric;
            //        }
            //        String imgPath=((PhotoLayout)container).imgPath;
            //        if(!imgPath.isEmpty()) {
            //            stringBuilder.append("<img src=\""+imgPath+"\"/>");
            //        }
            //        break;
            //    case BaseContainer.TYPE_NUMERIC:
            //        if(isLastDot){
            //            stringBuilder.append("</ul>");
            //            isLastDot=!isLastDot;
            //        }
            //        if(!isLastNumeric){
            //            stringBuilder.append("<ol start=\""+((NumericLayout)container).getNumeric()+"\">");
            //            isLastNumeric=!isLastNumeric;
            //        }
            //        stringBuilder.append("<li>");
            //        stringBuilder.append(((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT)).getHtmlText());
            //        stringBuilder.append("</li>");
            //        break;
            //    case BaseContainer.TYPE_DOT:
            //        if(isLastNumeric){
            //            stringBuilder.append("</ol>");
            //            isLastNumeric=!isLastNumeric;
            //        }
            //        if(!isLastDot){
            //            isLastDot=!isLastDot;
            //            stringBuilder.append("<ul>");
            //        }
            //        stringBuilder.append("<li>");
            //        stringBuilder.append(((BaseRichEditText) container.findViewWithTag(BaseContainer.TYPE_TEXT)).getHtmlText());
            //        stringBuilder.append("</li>");
            //        break;
            //}
        }
        //if(isLastDot){
        //    stringBuilder.append("</ul>");
        //}
        //if(isLastNumeric){
        //    stringBuilder.append("</ol>");
        //}
        return stringBuilder.toString();
    }

    @Override
    public void callback(boolean isUnderLine, boolean isStrikethrough, boolean isBold, boolean isItalic, boolean isSubscript, boolean isSuperscript, boolean isForegroundColor, int colorFontForeground,boolean isBackgroundColor,int colorFontBackground, int sizeFlag) {
        if(selectionSpanCallback!=null){
            selectionSpanCallback.callback(
                    isUnderLine,
                    isStrikethrough,
                    isBold,
                    isItalic,
                    isSubscript,
                    isSuperscript,
                    isForegroundColor,
                    colorFontForeground,
                    isBackgroundColor,
                    colorFontBackground,
                    sizeFlag);
        }
    }

    interface OnSelectionSpanCallback{
        void callback(boolean isUnderLine,
                      boolean isStrikethrough,
                      boolean isBold,
                      boolean isItalic,
                      boolean isSubscript,
                      boolean isSuperscript,
                      boolean isForegroundColor,
                      int colorFontForeground,
                      boolean isBackgroundColor,
                      int colorFontBackground,
                      int sizeFlag);
    }

    private OnSelectionSpanCallback selectionSpanCallback=null;

    public void setOnSelectionSpanCallback(OnSelectionSpanCallback  selectionSpanCallback){
        this.selectionSpanCallback=selectionSpanCallback;
    }

    public void setHtmlText(String html,boolean editable){
        preSet=true;
        if(!html.contains("<section") || !html.contains("</section>")){
            html = patchOldHtml(html);
        }
        removeAllViewsInLayout();
        Pattern p = Pattern.compile("<section.*?>.*?</section>");
        Matcher m = p.matcher(html);
        BaseContainer container=null;
        while(m.find()) {
            String t=m.group();
            t=t.substring(t.indexOf("id=\"")+"id=\"".length(),t.lastIndexOf("<"));
            int type;
            try {
                type=Integer.valueOf(t.substring(0,t.indexOf("\"")).trim());
            }catch (NumberFormatException e){
                e.printStackTrace();
                continue;
            }
            t=t.substring(t.indexOf(">")+1);
            switch (type){
                case BaseContainer.TYPE_TEXT:container=addRichEditText(container);container.setHtml(t);container.setEditable(editable);break;
                case BaseContainer.TYPE_DOT:container=addDotLayout(container);container.setHtml(t);container.setEditable(editable);break;
                case BaseContainer.TYPE_NUMERIC:container=addNumericLayout(container);container.setHtml(t);container.setEditable(editable);break;
                case BaseContainer.TYPE_TODO:container=addTodoLayout(container);container.setHtml(t);container.setEditable(editable);break;
                case BaseContainer.TYPE_PHOTO:container=addPhotoLayout(container,false);container.setHtml(t);container.setEditable(editable);break;
            }

        }
        if(((BaseContainer)this.getChildAt(this.getChildCount()-1)).getType()==BaseContainer.TYPE_PHOTO){
            addRichEditText((BaseContainer)this.getChildAt(this.getChildCount()-1));
        }
        preSet=false;
    }

    private String patchOldHtml(String html) {
        //补充section
        String temp  = TextUtils.concat("<section id=\"32\">",html,"</section>").toString();
        //fuck image tag
        Pattern p = Pattern.compile("<img\\s+src=[^>]*>");
        Matcher m = p.matcher(temp);
        String newString  ="";
        int start = 0;
        int end = 0;
        while (m.find()){
            String t =m.group();
            newString += temp.substring(start,m.start());
//            newString  +="<section id=\"33\">";
            start = m.start();
            if(t.substring(t.length()-1,t.length())!="/"){
                newString+=t.substring(0,t.length()-1);
                newString +="/>";
            }else{
                newString  +=temp.substring(start,m.end());
            }
//            newString +="</section>";
            end = m.end();
        }
        newString+=temp.substring(end,temp.length());
        //fuck \n
        newString  =newString.replace("\n","");
//        newString = newString.replace("<img\\s+src=\"[^>]*\">","");
        return newString;
    }

    public void setEditable(boolean editable){
        for(int i=0;i<getChildCount();i++){
            BaseContainer container=(BaseContainer) getChildAt(i);
            container.setEditable(editable);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (afterTextChangedCallback != null && !preSet) {
            afterTextChangedCallback.afterTextChanged(s);
        }
    }

    public interface AfterTextChangedCallback{
        void afterTextChanged(Editable s);
    }

    AfterTextChangedCallback afterTextChangedCallback=null;

    public void setAfterTextChangedCallback(AfterTextChangedCallback afterTextChangedCallback){
        this.afterTextChangedCallback=afterTextChangedCallback;
    }

    @Override
    public void onPhotoDelete(PhotoLayout layout) {
        removeView(layout);
        if(getChildCount()==0){
            addRichEditText(null);
        }
    }

    OnLayoutChangeListener layoutChangeListener=null;

    @Override
    public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
        layoutChangeListener=listener;
        super.addOnLayoutChangeListener(layoutChangeListener);
    }
}
