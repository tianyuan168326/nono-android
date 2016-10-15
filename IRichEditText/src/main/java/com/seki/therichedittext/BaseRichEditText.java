package com.seki.therichedittext;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Seki on 2016/5/2.
 * 此为富文本编辑器的基础类，当集成新样式需要富文本编辑器时，请使用该类
 */
public class BaseRichEditText extends AppCompatEditText {

    private boolean isUnderLine=false;
    private boolean isStrikethrough=false;
    private boolean isBold=false;
    private boolean isItalic=false;
    private boolean isSubscript=false;
    private boolean isSuperscript=false;
    private boolean isForegroundColor=false;
    private boolean isBackgroundColor=false;
    private int sizeFlag=MEDIUM_TEXT;
    private int colorFontBackground=0xffffffff;
    private int colorFontForeground=0xff000000;

    private boolean preSetHtml=false;

    public final static int SMALL_TEXT=0;
    public final static int MEDIUM_TEXT=1;
    public final static int LARGE_TEXT=2;

    private final float[] textMultiple={0.75f,1.0f,1.25f,0.7f};
    //private final float smallTextMultiple=0.75f;
    //private final float mediumTextMultiple=1.0f;
    //private final float largeTextMultiple=1.25f;
    //private final float scriptTextMultiple=0.7f;
    BaseContainer c;
    public void setContaner(BaseContainer c){
        this.c = c;
    }

    public BaseRichEditText(Context context) {
        super(context);
        init();
    }

    public BaseRichEditText(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public BaseRichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init();
    }
    //OnTouchListener myFuckListener;
    //@Override
    //public void setOnTouchListener(OnTouchListener l) {
    //    super.setOnTouchListener(l);
    //    myFuckListener =l;
    //}
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //myFuckListener.onTouch(this,event);
//        return true;
//    }

    public String getPlainText(){
        return getText().toString();
    }

    public String getHtmlText(){
        String t1=MyHtml.toHtml(getText());
        String t2=t1.replace("\n","");
        if(getText().toString().endsWith("\n\n")){
            t2=t2.substring(0,t2.lastIndexOf("<"))+"<br><br>"+"</p>";
            return t2;
        }
        return MyHtml.toHtml(getText()).replace("\n","");
    }

    public void setHtmlText(String htmlText) {
        //htmlText=htmlText.replace("<br>","<br>\n");
        //htmlText=htmlText.replace("</p>","</p>\n");
        preSetHtml=true;
        MyHtmlTagHandler handler = new MyHtmlTagHandler(getContext(),false);
        Spanned spanned = Html.fromHtml(htmlText.replace("</p>", "</p>\n"),new NetworkImageGetter(this,htmlText.replace("</p>", "</p>\n"),
                handler),handler);
        if (htmlText.endsWith("<br><br></p>")) {
            setText(spanned);
        } else if(htmlText.endsWith("<br></p>")){
            setText(spanned.subSequence(0, spanned.length() - "\n".length()));
        } else if(htmlText.endsWith("</p>")){
            setText(spanned.subSequence(0, spanned.length() - "\n\n".length()));
        }
        //patch-windstring
        else{
            setText(spanned);
        }
        preSetHtml=false;
    }

    private void init(){
        setLineSpacing(getLineHeight()+ DisplayUtil.dip2px(getContext(),8),0);
        setPadding(
                DisplayUtil.dip2px(getContext(),8),
                DisplayUtil.dip2px(getContext(),8),
                DisplayUtil.dip2px(getContext(),8),
                DisplayUtil.dip2px(getContext(),8)
        );
        setTag(BaseContainer.TYPE_TEXT);
        this.setBackgroundDrawable(null);
        addTextChangedListener(new RichEditTextWatcher());
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color=typedValue.data;
        colorFontBackground=color;
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        color=typedValue.data;
        colorFontForeground=color;
//        setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if(hasFocus){
//                    imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
//                }else {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
//                }
//            }
//        });
    }

    public void setSpan(Object what,int start,int end,int flags){
        getEditableText().setSpan(what,start,end,flags);
    }

    public void removeSpan(Object what,int start,int end) {
        int s = getEditableText().getSpanStart(what);
        int e = getEditableText().getSpanEnd(what);
        getEditableText().removeSpan(what);
        end = end > e ? e : end;
        start = start > s ? start : s;
        if (what instanceof AbsoluteSizeSpan) {
            setSpan(new AbsoluteSizeSpan(
                    ((AbsoluteSizeSpan) what).getSize(), false), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new AbsoluteSizeSpan(
                    ((AbsoluteSizeSpan) what).getSize(), false), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (what instanceof RelativeSizeSpan) {
            setSpan(new RelativeSizeSpan(((RelativeSizeSpan) what).getSizeChange()), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new RelativeSizeSpan(((RelativeSizeSpan) what).getSizeChange()), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (what instanceof StyleSpan) {
            if (((StyleSpan) what).getStyle() == Typeface.BOLD) {
                setSpan(new StyleSpan(Typeface.BOLD), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                setSpan(new StyleSpan(Typeface.BOLD), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (((StyleSpan) what).getStyle() == Typeface.ITALIC) {
                setSpan(new StyleSpan(Typeface.ITALIC), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                setSpan(new StyleSpan(Typeface.ITALIC), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else if (what instanceof ForegroundColorSpan) {
            setSpan(new ForegroundColorSpan(((ForegroundColorSpan) what).getForegroundColor()), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new ForegroundColorSpan(((ForegroundColorSpan) what).getForegroundColor()), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (what instanceof BackgroundColorSpan) {
            setSpan(new ForegroundColorSpan(((BackgroundColorSpan) what).getBackgroundColor()), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new ForegroundColorSpan(((BackgroundColorSpan) what).getBackgroundColor()), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (what instanceof UnderlineSpan) {
            setSpan(new UnderlineSpan(), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new UnderlineSpan(), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (what instanceof StrikethroughSpan) {
            setSpan(new StrikethroughSpan(), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new StrikethroughSpan(), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (what instanceof SubscriptSpan) {
            setSpan(new SubscriptSpan(), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new SubscriptSpan(), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (what instanceof SuperscriptSpan) {
            setSpan(new SuperscriptSpan(), s, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setSpan(new SuperscriptSpan(), end, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public boolean getSelectionSpan(Class type,int selStart,int selEnd){
        if(selStart==selEnd){
            if(selStart>1){
                selStart=selStart-1;
            }else {
                Object[] spans = getEditableText().getSpans(selStart, selStart, type);
                if (type == BackgroundColorSpan.class) {
                    if(spans.length>0){
                        BackgroundColorSpan span=(BackgroundColorSpan)spans[0];
                        colorFontBackground=span.getBackgroundColor();
                        return true;
                    }else {
                        return false;
                    }
                } else if (type == ForegroundColorSpan.class) {
                    if(spans.length>0){
                        ForegroundColorSpan span=(ForegroundColorSpan)spans[0];
                        colorFontForeground=span.getForegroundColor();
                        return true;
                    }else {
                        return false;
                    }
                } else {
                    return spans.length > 0;
                }
            }
        }
        for(int i=selStart;i<selEnd;i++) {
            Object[] spans = getEditableText().getSpans(selStart, selStart+1, type);
            if(spans.length<=0){
                return false;
            }
        }
        return true;
    }

    /**
     * @param style Typeface of StyleSpan.
     * */
    public boolean getSelectionSpan(int style,int selStart,int selEnd){
        if(selStart==selEnd){
            if(selStart>1){
                selStart=selStart-1;
            }else {
                StyleSpan[] spans = getEditableText().getSpans(selStart, selStart, StyleSpan.class);
                boolean is=false;
                for(StyleSpan span:spans){
                    if(span.getStyle()==style||span.getStyle()==Typeface.BOLD_ITALIC){
                        is=true;
                        break;
                    }
                }
                return is;
            }
        }
        for(int i=selStart;i<selEnd;i++) {
            StyleSpan[] spans = getEditableText().getSpans(selStart, selStart+1, StyleSpan.class);
            if(spans.length<=0){
                return false;
            }else {
                boolean is=false;
                for(StyleSpan span:spans){
                    if(span.getStyle()==style||span.getStyle()==Typeface.BOLD_ITALIC){
                        is=true;
                        break;
                    }
                }
                if(!is){
                    return false;
                }
            }
        }
        return true;
    }

    public void removeSelectionSpan(Class type){
        int start=getSelectionStart();
        int end=getSelectionEnd();
        if(end<=start){
            return;
        }
        if(type==SubscriptSpan.class||type==SuperscriptSpan.class){
            RelativeSizeSpan[] relativeSizeSpans=getEditableText().getSpans(start,end,RelativeSizeSpan.class);
            for(RelativeSizeSpan span:relativeSizeSpans){
                removeSpan(span,start,end);
            }
            SubscriptSpan[] subscriptSpans=getEditableText().getSpans(start,end,SubscriptSpan.class);
            for(SubscriptSpan span:subscriptSpans){
                removeSpan(span,start,end);
            }
            SuperscriptSpan[] superscriptSpans=getEditableText().getSpans(start,end,SuperscriptSpan.class);
            for(SuperscriptSpan span:superscriptSpans){
                removeSpan(span,start,end);
            }
        }else {
            Object[] spans = getEditableText().getSpans(start, end, type);
            for (Object span : spans) {
                removeSpan(span, start, end);
            }
        }
    }

    /**
     * @param style Typeface of StyleSpan.
     * */
    public void removeSelectionSpan(int style){
        int start=getSelectionStart();
        int end=getSelectionEnd();
        if(end<=start){
            return;
        }
        StyleSpan[] spans = getEditableText().getSpans(start, end, StyleSpan.class);
        for (StyleSpan span:spans) {
            if(span.getStyle()==style) {
                removeSpan(span, start, end);
            }
        }
    }

    /**
     * @param type UnderlineSpan and StrikethroughSpan
     * */
    public void applySelectionSpan(Class type){
        int selStart=getSelectionStart();
        int selEnd=getSelectionEnd();
        if(selEnd-selStart>0){
            Object[] ss = getEditableText().getSpans(selStart, selEnd, type);
            for (Object span:ss) {
                removeSpan(span,selStart,selEnd);
            }
            if (type == UnderlineSpan.class) {
                //toggleUnderLine();
                if(isUnderLine){
                    setSpan(new UnderlineSpan(),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (type == StrikethroughSpan.class) {
                //toggleStrikethrough();
                if(isStrikethrough){
                    setSpan(new StrikethroughSpan(),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        //}else if(selEnd==selStart){
        //    if (type == UnderlineSpan.class) {
        //        toggleUnderLine();
        //    } else if (type == StrikethroughSpan.class) {
        //        toggleStrikethrough();
        //    }
        }
    }

    /**
     * @param style Typeface of StyleSpan.
     * */
    public void applyStyleSpan(int style){
        int selStart=getSelectionStart();
        int selEnd=getSelectionEnd();
        if(selEnd-selStart>0){
            StyleSpan[] ss = getEditableText().getSpans(selStart, selEnd, StyleSpan.class);
            for (StyleSpan span:ss) {
                if(span.getStyle()==style) {
                    removeSpan(span, selStart, selEnd);
                }
            }
            if (style==Typeface.BOLD) {
                //toggleBold();
                if(isBold){
                    setSpan(new StyleSpan(style),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (style==Typeface.ITALIC) {
                //toggleItalic();
                if(isItalic){
                    setSpan(new StyleSpan(style),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        //}else if(selEnd==selStart){
        //    if (style==Typeface.BOLD) {
        //        toggleBold();
        //    } else if (style==Typeface.ITALIC) {
        //        toggleItalic();
        //    }
        }
    }

    /**
     * @param type SubscriptSpan and SuperscriptSpan
     * */
    public void applyScriptSpan(Class type){
        int selStart=getSelectionStart();
        int selEnd=getSelectionEnd();
        if(selEnd-selStart>0){
            Object[] ss = getEditableText().getSpans(selStart, selEnd, type);
            for (Object span:ss) {
                removeSpan(span,selStart,selEnd);
            }
            ss=getEditableText().getSpans(selStart,selEnd,RelativeSizeSpan.class);
            for (RelativeSizeSpan span:(RelativeSizeSpan[])ss) {
                removeSpan(span,selStart,selEnd);
            }
            if (type == SubscriptSpan.class) {
                //toggleSubscript();
                if(isSubscript){
                    setSpan(new SubscriptSpan(),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    setSpan(new RelativeSizeSpan(textMultiple[3]),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (type==SuperscriptSpan.class) {
                //toggleSuperscript();
                if(isSuperscript){
                    setSpan(new SuperscriptSpan(),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    setSpan(new RelativeSizeSpan(textMultiple[3]),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        //}else{
        //    if (type == SubscriptSpan.class) {
        //        toggleSubscript();
        //    } else if (type==SuperscriptSpan.class) {
        //        toggleSuperscript();
        //    }
        }
    }

    public void applyColorSpan(Class type, @ColorInt int color){
        int selStart=getSelectionStart();
        int selEnd=getSelectionEnd();
        if(selEnd-selStart>0){
            Object[] ss = getEditableText().getSpans(selStart, selEnd, type);
            for (Object span:ss) {
                removeSpan(span,selStart,selEnd);
            }
            if (type == BackgroundColorSpan.class) {
                //toggleBackgroundColor();
                if(isBackgroundColor){
                    colorFontBackground=color;
                    setSpan(new BackgroundColorSpan(color),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (type == ForegroundColorSpan.class) {
                //toggleForegroundColor();
                if(isForegroundColor){
                    colorFontForeground=color;
                    setSpan(new ForegroundColorSpan(color),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }else if(selEnd==selStart){
            if (type == BackgroundColorSpan.class) {
                //toggleBackgroundColor();
                colorFontBackground=color;
            } else if (type == ForegroundColorSpan.class) {
                //toggleForegroundColor();
                colorFontForeground=color;
            }
        }
    }


    public void applyRelativeSpan(){
        int selStart=getSelectionStart();
        int selEnd=getSelectionEnd();
        if(selEnd-selStart>0){
            List<pairStartEnd> list=new ArrayList<>();
            RelativeSizeSpan[] ss = getEditableText().getSpans(selStart, selEnd, RelativeSizeSpan.class);
            for (RelativeSizeSpan span:ss) {
                if (span.getSizeChange() == textMultiple[3]) {
                    int s = getEditableText().getSpanStart(span);
                    int e = getEditableText().getSpanEnd(span);
                    list.add(new pairStartEnd(s,e));
                } else {
                    removeSpan(span, selStart, selEnd);
                }
            }
            for(int i=0;i<list.size();i++){
                pairStartEnd pairStartEnd=list.get(i);
                processChangeSize(selStart,
                        pairStartEnd.start>selStart?pairStartEnd.start:selStart);
                selStart=pairStartEnd.end;
                if(selEnd<selStart){
                    break;
                }
                if(i==list.size()-1){
                    if(selStart<selEnd){
                        processChangeSize(selStart,selEnd);
                    }
                }
            }
            if(list.size()==0){
                processChangeSize(selStart,selEnd);
            }
            //changeSize();
            //setSpan(new RelativeSizeSpan(textMultiple[sizeFlag]),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //}else if(selEnd==selStart){
        //    //changeSize();
        }
    }

    public void applyRelativeSpan(float proportion){
        int selStart=getSelectionStart();
        int selEnd=getSelectionEnd();
        if(selEnd-selStart>0){
            RelativeSizeSpan[] ss = getEditableText().getSpans(selStart, selEnd, RelativeSizeSpan.class);
            for (RelativeSizeSpan span:ss) {
                removeSpan(span, selStart, selEnd);
            }
            setSpan(new RelativeSizeSpan(proportion),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void applyAbsoluteSpan(int px){
        int selStart=getSelectionStart();
        int selEnd=getSelectionEnd();
        if(selEnd-selStart>0){
            AbsoluteSizeSpan[] ss = getEditableText().getSpans(selStart, selEnd, AbsoluteSizeSpan.class);
            for (AbsoluteSizeSpan span:ss) {
                removeSpan(span, selStart, selEnd);
            }
            setSpan(new AbsoluteSizeSpan(px),selStart,selEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void processChangeSize(int start,int end) {
        if (sizeFlag != MEDIUM_TEXT) {
            setSpan(new RelativeSizeSpan(textMultiple[sizeFlag]), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class pairStartEnd{
        public int start;
        public int end;
        public pairStartEnd(int start,int end){
            this.start=start;
            this.end=end;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DEL:
                if (getSelectionStart() ==0 && getSelectionEnd()== 0) {
                    ((RichEdit)this.getParent().getParent().getParent()).onDelDown((BaseContainer) this.getParent().getParent());
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
                BaseContainer container=(BaseContainer) this.getParent().getParent();
                if(container.getType()!=BaseContainer.TYPE_TEXT) {
                    ((RichEdit)this.getParent().getParent().getParent()).onEnterDown(container);
                    return true;
                }
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {

        super.onSelectionChanged(selStart, selEnd);
        selStart=selStart<0?0:selStart;
        selEnd=selEnd<0?0:selEnd;
        if(selStart>selEnd) {
            int temp = selEnd;
            selEnd = selStart;
            selStart = temp;
        }
        if(length()==0){
            return;
        }
        isUnderLine = getSelectionSpan(UnderlineSpan.class,selStart,selEnd);
        isStrikethrough = getSelectionSpan(StrikethroughSpan.class,selStart,selEnd);
        isBold = getSelectionSpan(Typeface.BOLD,selStart,selEnd);
        isItalic = getSelectionSpan(Typeface.ITALIC,selStart,selEnd);
        isSubscript = getSelectionSpan(SubscriptSpan.class,selStart,selEnd);
        isSuperscript = getSelectionSpan(SuperscriptSpan.class,selStart,selEnd);
        isForegroundColor = getSelectionSpan(ForegroundColorSpan.class,selStart,selEnd);
        isBackgroundColor=getSelectionSpan(BackgroundColorSpan.class,selStart,selEnd);
        sizeFlag=MEDIUM_TEXT;
        RelativeSizeSpan[] spans;
        if(selStart==selEnd){
            if(selStart>1){
                selStart=selStart-1;
                spans= getEditableText().getSpans(selStart, selStart+1, RelativeSizeSpan.class);
            }else {
                spans = getEditableText().getSpans(selStart, selStart, RelativeSizeSpan.class);
            }
        }else {
            spans= getEditableText().getSpans(selStart, selStart+1, RelativeSizeSpan.class);
        }
        if(spans.length>0){
            float size=spans[0].getSizeChange();
            if(size==textMultiple[SMALL_TEXT]){
                sizeFlag=SMALL_TEXT;
            }else if(size==textMultiple[LARGE_TEXT]){
                sizeFlag=LARGE_TEXT;
            }else {
                sizeFlag=MEDIUM_TEXT;
            }
        }
        if(selectionSpanCallback!=null) {
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

    class RichEditTextWatcher implements TextWatcher{
        int start=0;
        int end=0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.start = start;
            this.end = start + count;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(preSetHtml){
                return;
            }
            if(!isSubscript&&!isSuperscript) {
                if(sizeFlag!=MEDIUM_TEXT) {
                    setSpan(new RelativeSizeSpan(textMultiple[sizeFlag]), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }else {
                RelativeSizeSpan[] spans=s.getSpans(start,end,RelativeSizeSpan.class);
                for(RelativeSizeSpan span:spans){
                    removeSpan(span,start,end);
                }
            }
            if(isUnderLine) {
                setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(isStrikethrough) {
                setSpan(new StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(isBold) {
                setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(isItalic) {
                setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(isSubscript) {
                Object[] obj = s.getSpans(start, end, SubscriptSpan.class);
                if (obj.length == 0) {
                    setSpan(new RelativeSizeSpan(textMultiple[3]), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    setSpan(new SubscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            if(isSuperscript) {
                Object[] obj = s.getSpans(start, end, SuperscriptSpan.class);
                if (obj.length == 0) {
                    setSpan(new RelativeSizeSpan(textMultiple[3]), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            if(isBackgroundColor) {
                setSpan(new BackgroundColorSpan(colorFontBackground), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(isForegroundColor) {
                setSpan(new ForegroundColorSpan(colorFontForeground), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(afterTextChangedCallback!=null){
                afterTextChangedCallback.afterTextChanged(s);
            }
        }
    }

    interface AfterTextChangedCallback{
        void afterTextChanged(Editable s);
    }

    AfterTextChangedCallback afterTextChangedCallback=null;

    public void setAfterTextChangedCallback(AfterTextChangedCallback afterTextChangedCallback){
        this.afterTextChangedCallback=afterTextChangedCallback;
    }

    public void toggleBold() {
        this.isBold = !isBold;
    }

    public void toggleForegroundColor() {
        isForegroundColor = !isForegroundColor;
    }

    public void toggleBackgroundColor() {
        isBackgroundColor = !isBackgroundColor;
    }

    public void toggleItalic() {
        this.isItalic = !isItalic;
    }

    public void toggleStrikethrough() {
        this.isStrikethrough = !isStrikethrough;
    }

    public void toggleSubscript() {
        this.isSubscript = !isSubscript;
        isSuperscript=false;
    }

    public void toggleSuperscript() {
        this.isSuperscript = !isSuperscript;
        isSubscript=false;
    }

    public void toggleUnderLine() {
        this.isUnderLine = !isUnderLine;
    }

    public void changeSize(){
        sizeFlag=sizeFlag>=LARGE_TEXT?SMALL_TEXT:sizeFlag+1;
    }

    public void setIsUnderLine(boolean isUnderLine) {
        this.isUnderLine = isUnderLine;
    }

    public void setIsSuperscript(boolean isSuperscript) {
        this.isSuperscript = isSuperscript;
    }

    public void setIsSubscript(boolean isSubscript) {
        this.isSubscript = isSubscript;
    }

    public void setIsStrikethrough(boolean isStrikethrough) {
        this.isStrikethrough = isStrikethrough;
    }

    public void setIsBold(boolean isBold) {
        this.isBold = isBold;
    }

    public void setIsForegroundColor(boolean isForegroundColor) {
        this.isForegroundColor = isForegroundColor;
    }

    public void setIsBackgroundColor(boolean isBackgroundColor) {
        this.isBackgroundColor = isBackgroundColor;
    }

    public void setColorFontBackground(int color){
        colorFontBackground=color;
    }

    public void setColorFontForeground(int color){
        colorFontForeground=color;
    }

    public void setIsItalic(boolean isItalic) {
        this.isItalic = isItalic;
    }

    public void setSizeFlag(int i) {
        if (i != SMALL_TEXT && i != MEDIUM_TEXT && i != LARGE_TEXT) {
            sizeFlag = MEDIUM_TEXT;
        } else {
            sizeFlag = i;
        }
    }

    public boolean isBold() {
        return isBold;
    }

    public boolean isForegroundColor() {
        return isForegroundColor;
    }

    public boolean isBackgroundColor() {
        return isBackgroundColor;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public boolean isStrikethrough() {
        return isStrikethrough;
    }

    public boolean isSubscript() {
        return isSubscript;
    }

    public boolean isSuperscript() {
        return isSuperscript;
    }

    public boolean isUnderLine() {
        return isUnderLine;
    }

    public int getSizeFlag(){
        return sizeFlag;
    }

    public int getColorFontBackground(){
        return colorFontBackground;
    }

    public int getColorFontForeground(){
        return colorFontForeground;
    }

    public int getSizePx(){
        return (int)(getTextSize()*textMultiple[sizeFlag]);
    }

    public int getSizePx(float proportion){
        return (int)(getTextSize()*proportion);
    }



//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //return false;
//        if(!isFocusable()){
//
//        }
//        return super.onTouchEvent(event);
//    }

    public interface OnDoubleClickListener{
        void onDoubleClick(View v, MotionEvent e);
    }
//
    private OnDoubleClickListener doubleClickListener=null;
//
    public void setOnDoubleClickListener(OnDoubleClickListener doubleClickListener){
        this.doubleClickListener=doubleClickListener;
    }
}
