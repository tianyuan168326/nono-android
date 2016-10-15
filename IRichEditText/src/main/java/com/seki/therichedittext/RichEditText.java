package com.seki.therichedittext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Seki on 2016/5/2.
 */
public class RichEditText extends BaseContainer {

    public BaseRichEditText editText;

    public RichEditText(Context context) {
        super(context);
    }

    public RichEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initUI() {
        Context context=getContext();
        FrameLayout frameLayout=new FrameLayout(context);

        editText=new BaseRichEditText(context);


        editText.setContaner(this);
        frameLayout.addView(editText);
        this.addView(frameLayout);
    }

    @Override
    protected String toHtml() {
        return "<section id=\""+type+"\">"+editText.getHtmlText().trim()+"</section>";
    }

    @Override
    protected void setHtml(String html) {
        editText.setHtmlText(html);
    }

    @Override
    protected void setType() {
        type=TYPE_TEXT;
    }

    @Override
    protected boolean reqFocus() {
        return editText.requestFocus();
    }



    @Override
    protected boolean isEmpty() {
        return editText.getText().length()<=0;
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

    }

    @Override
    protected BaseRichEditText returnEdit() {
        return editText;
    }

    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }
}
