package com.seki.therichedittext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Seki on 2016/5/2.
 * 完整的富文本控件示例
 * 要更换xml请根据init函数的写法更换
 * 有新SpanButton时，请更新onSpanClick
 */
public class EditView extends LinearLayout implements SpanToolbar.OnSpanClickListener,SpanToolbar.OnGroundLongClickListener,SpanToolbar.OnTextSpanClickListener,RichEdit.OnSelectionSpanCallback{

    private SpanToolbar spanToolbar;
    private RichEdit richEdit;

    public EditView(Context context) {
        this(context, null);
    }

    public EditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void receiveSend(Intent intent){
            String action = intent.getAction();
        if(action!=null) {
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    handleSendText(intent); // Handle text being sent
                } else if (type.startsWith("image/")) {
                    handleSendImage(intent); // Handle single image being sent
                }
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                if (type.startsWith("image/")) {
                    handleSendMultipleImages(intent); // Handle multiple images being sent
                }
            } else {
                // Handle other intents, such as being started from the home screen
                return;
            }
        }
    }

    private void handleSendText(Intent intent){
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            richEdit.removeAllViews();
            richEdit.addRichEditText(null).returnEdit().setText(sharedText);
        }
    }

    private void handleSendImage(Intent intent){
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            richEdit.removeAllViews();
            richEdit.addPhotoLayout(null).setImage(GetPathFromUri4kitkat.getPath((Activity) getContext(), imageUri));
        }
    }

    private void handleSendMultipleImages(Intent intent){
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            richEdit.removeAllViews();
            for(Uri imageUri:imageUris){
                richEdit.addPhotoLayout(null).setImage(GetPathFromUri4kitkat.getPath((Activity) getContext(), imageUri));
            }
        }
    }

    private void initView(){
        setBackgroundColor(Color.WHITE);
        View view=View.inflate(getContext(),R.layout.edit_view,this);
        spanToolbar=(SpanToolbar) view.findViewById(R.id.span_toolbar);
        richEdit=(RichEdit)view.findViewById(R.id.rich_edit);
        spanToolbar.setOnSpanClickListener(this);
        spanToolbar.setOnTextSpanClickListener(this);
        spanToolbar.setOnGroundLongClickListener(this);
        richEdit.setOnSelectionSpanCallback(this);
    }

    public String getHtmlText(){
        return richEdit.getHtmlText();
    }

    public void setHtml(String html){
        richEdit.setHtmlText(html,true);
    }

    @Override
    public void onTextSpanClick(SpanButton.SpanTextSizeButton v) {
        View view=richEdit.findFocus();
        BaseRichEditText editText;
        if(view instanceof BaseRichEditText){
            editText=(BaseRichEditText)view;
            editText.changeSize();editText.applyRelativeSpan();v.setTextSize(editText.getSizeFlag());
        }
    }

    @Override
    public boolean onGroundLongClick(final SpanButton v) {
        View view=richEdit.findFocus();
        final BaseRichEditText editText;
        if(view instanceof BaseRichEditText){
            editText=(BaseRichEditText)view;
        }else {
            return false;
        }
        switch (v.getSpan()) {
            case SpanButton.SPAN_FOREGROUND:
            case SpanButton.SPAN_BACKGROUND:
                new ColorPanel(getContext(), v.getGroundColor()).setOnColorChoseCallback(new ColorPanel.OnColorChoseCallback() {
                    @Override
                    public void onColorChose(@ColorInt int color) {
                        v.setGroundColor(color);
                        v.setColor(color);
                        if(v.getSpan()==SpanButton.SPAN_BACKGROUND){
                            editText.setColorFontBackground(color);
                            editText.setIsBackgroundColor(true);
                        }else if(v.getSpan()==SpanButton.SPAN_FOREGROUND){
                            editText.setColorFontForeground(color);
                            editText.setIsForegroundColor(true);
                        }
                    }
                });
        }
        return false;
    }

    @Override
    public void onSpanClick(SpanButton v) {
        View view=richEdit.findFocus();
        BaseRichEditText editText;
        if(view instanceof BaseRichEditText){
            editText=(BaseRichEditText)view;
        }else {
            return;
        }
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color=typedValue.data;
        switch (v.getSpan()){
            case SpanButton.SPAN_BOLD:editText.toggleBold();if(editText.isBold()){editText.applyStyleSpan(Typeface.BOLD);v.setColor(color);}else {editText.removeSelectionSpan(Typeface.BOLD);v.setColor(0xff757575);}break;
            case SpanButton.SPAN_ITALIC:editText.toggleItalic();if(editText.isItalic()){editText.applyStyleSpan(Typeface.ITALIC);v.setColor(color);}else {editText.removeSelectionSpan(Typeface.ITALIC);v.setColor(0xff757575);}break;
            case SpanButton.SPAN_STRIKETHROUGH:editText.toggleStrikethrough();if(editText.isStrikethrough()){editText.applySelectionSpan(StrikethroughSpan.class);v.setColor(color);}else {editText.removeSelectionSpan(StrikethroughSpan.class);v.setColor(0xff757575);}break;
            case SpanButton.SPAN_UNDERLINE:editText.toggleUnderLine();if(editText.isUnderLine()){editText.applySelectionSpan(UnderlineSpan.class);v.setColor(color);}else {editText.removeSelectionSpan(UnderlineSpan.class);v.setColor(0xff757575);}break;
            case SpanButton.SPAN_FOREGROUND:editText.toggleForegroundColor();if(editText.isForegroundColor()){editText.applyColorSpan(ForegroundColorSpan.class,v.getGroundColor());v.setColor(v.getGroundColor());}else {editText.removeSelectionSpan(ForegroundColorSpan.class);v.setColor(0xff757575);}break;
            case SpanButton.SPAN_BACKGROUND:editText.toggleBackgroundColor();if(editText.isBackgroundColor()){editText.applyColorSpan(BackgroundColorSpan.class,v.getGroundColor());v.setColor(v.getGroundColor());}else {editText.removeSelectionSpan(BackgroundColorSpan.class);v.setColor(0xff757575);}break;
            case SpanButton.SPAN_SUBSCRIPT:editText.toggleSubscript();if(editText.isSubscript()){editText.applyScriptSpan(SubscriptSpan.class);v.setColor(color);}else {editText.removeSelectionSpan(SubscriptSpan.class);v.setColor(0xff757575);}break;
            case SpanButton.SPAN_SUPERSCRIPT:editText.toggleSuperscript();if(editText.isSuperscript()){editText.applyScriptSpan(SuperscriptSpan.class);v.setColor(color);}else {editText.removeSelectionSpan(SuperscriptSpan.class);v.setColor(0xff757575);}break;
            case SpanButton.SPAN_TODO:richEdit.addTodoLayout((BaseContainer) richEdit.findFocus().getParent().getParent());break;
            case SpanButton.SPAN_DOT:richEdit.addDotLayout((BaseContainer) richEdit.findFocus().getParent().getParent());break;
            case SpanButton.SPAN_NUMERIC:richEdit.addNumericLayout((BaseContainer) richEdit.findFocus().getParent().getParent());break;
            case SpanButton.SPAN_PHOTO:richEdit.addPhotoLayout((BaseContainer) richEdit.findFocus().getParent().getParent());break;
            //case SpanButton.SPAN_TEXTSIZE:editText.changeSize();editText.applyRelativeSpan();break;
            //case SpanButton.SPAN_BOLD:editText.toggleBold();if(editText.isBold()){editText.applyStyleSpan(Typeface.BOLD);v.setColor(color);}else {editText.removeSelectionSpan(Typeface.BOLD);v.setColor(0xff757575);}break;
        }
    }

    @Override
    public void callback(boolean isUnderLine, boolean isStrikethrough, boolean isBold, boolean isItalic, boolean isSubscript, boolean isSuperscript, boolean isForegroundColor,int colorFontForeground, boolean isBackgroundColor, int colorFontBackground,int sizeFlag) {
        LinearLayout container=(LinearLayout) spanToolbar.getChildAt(0);
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color=typedValue.data;
        ((SpanButton)container.findViewWithTag(SpanButton.SPAN_UNDERLINE)).setColor(isUnderLine?color:0xff757575);
        ((SpanButton)container.findViewWithTag(SpanButton.SPAN_STRIKETHROUGH)).setColor(isStrikethrough?color:0xff757575);
        ((SpanButton)container.findViewWithTag(SpanButton.SPAN_BOLD)).setColor(isBold?color:0xff757575);
        ((SpanButton)container.findViewWithTag(SpanButton.SPAN_ITALIC)).setColor(isItalic?color:0xff757575);
        ((SpanButton)container.findViewWithTag(SpanButton.SPAN_SUBSCRIPT)).setColor(isSubscript?color:0xff757575);
        ((SpanButton)container.findViewWithTag(SpanButton.SPAN_SUPERSCRIPT)).setColor(isSuperscript?color:0xff757575);
        ((SpanButton)container.findViewWithTag(SpanButton.SPAN_FOREGROUND)).setColor(isForegroundColor?colorFontForeground:0xff757575).setGroundColor(colorFontForeground);
        ((SpanButton)container.findViewWithTag(SpanButton.SPAN_BACKGROUND)).setColor(isBackgroundColor?colorFontBackground:0xff757575).setGroundColor(colorFontBackground);
        ((SpanButton.SpanTextSizeButton)container.findViewWithTag(SpanButton.SPAN_TEXTSIZE)).setTextSize(sizeFlag);
        //((SpanButton)container.findViewWithTag(SpanButton.SPAN_UNDERLINE)).setColor(isUnderLine?color:0xff757575);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        richEdit.onActivityResult(requestCode,resultCode,data);
    }

    //递归获取焦点
    public void forceRefocus(){
        traserFocus(this);
    }

    public void setAfterTextChangedCallback(RichEdit.AfterTextChangedCallback afterTextChangedCallback,OnLayoutChangeListener onLayoutChangeListener){
        richEdit.setAfterTextChangedCallback(afterTextChangedCallback);
        richEdit.addOnLayoutChangeListener(onLayoutChangeListener);
    }

    public void cancelAfterTextChangedCallback(){
        richEdit.afterTextChangedCallback=null;
        richEdit.removeOnLayoutChangeListener(richEdit.layoutChangeListener);
    }

    public void setFocus(int index){
        richEdit.getChildAt(index).requestFocus();
    }

    public void setSelection(int index,int sel){
        BaseContainer container=((BaseContainer)richEdit.getChildAt(index));
        container.reqFocus();
        sel=sel>0?sel:0;
        try {
            if (container instanceof RichEditText) {
                ((RichEditText) container).editText.setSelection(sel, sel);
            } else if (container instanceof DotLayout) {
                ((DotLayout) container).editText.setSelection(sel, sel);
            } else if (container instanceof NumericLayout) {
                ((NumericLayout) container).editText.setSelection(sel, sel);
            } else if (container instanceof TodoLayout) {
                ((TodoLayout) container).editText.setSelection(sel, sel);
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    private void traserFocus(View view) {
        view.requestFocus();
        int childCount = 0;
        if(view instanceof ViewGroup){
            childCount = ((ViewGroup)view).getChildCount();
            for(int index  =0;index <childCount;index++){
                traserFocus(((ViewGroup)view).getChildAt(index));
            }
        }
    }

    public int getNowFocusIndex(){
        if(findFocus()==null){
            return -1;
        }
        try{
            return richEdit.indexOfChild((BaseContainer)findFocus().getParent().getParent());
        }catch (ClassCastException e){
            return -1;
        }
    }

    public int getNowSel(){
        int nowSel=-1;
        View view=findFocus();
        if(view==null){
            return -1;
        }
        if(view instanceof BaseRichEditText){
            nowSel=((BaseRichEditText) view).getSelectionStart();
        }
        return nowSel;
    }
}
