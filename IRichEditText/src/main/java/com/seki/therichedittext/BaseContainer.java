package com.seki.therichedittext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Seki on 2016/5/2.
 * Caution:继承此基类集成新样式控件的时候，务必保证BaseContainer->Layout->parallel views并更新final TYPE_**
 */
public abstract class BaseContainer extends FrameLayout {

    public final static int TYPE_TEXT=0x20;
    public final static int TYPE_PHOTO=0x21;
    public final static int TYPE_DOT=0x22;
    public final static int TYPE_TODO =0x23;
    public final static int TYPE_NUMERIC=0x24;
    protected int type;

    public BaseContainer(Context context) {
        super(context);
        init();
    }

    public BaseContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        initUI();
        setType();
    }

    protected abstract String toHtml();

    protected abstract void setHtml(String html);

    protected void setType(int type){
        this.type=type;
    }

    protected int getType(){
        return type;
    }

    protected abstract boolean isEmpty();
    //patch-windstring
    //@Override
    //public void clearFocus() {
    //    super.clearFocus();
    //    int childCount = getChildCount();
    //    for(int index =  0;index <childCount;index++){
    //        getChildAt(index).clearFocus();
    //    }
    //}

    protected abstract boolean reqFocus();

    protected abstract void setType();

    protected abstract void initUI();

    protected abstract void setEditable(boolean editable);

    protected abstract BaseRichEditText returnEdit();
    //public BaseRichEditText.OnDoubleClickListener getOnDoubleClickListener( ){
    //   return l;
    //}

    //@Override
    //public boolean dispatchTouchEvent(MotionEvent ev) {
    //    return super.dispatchTouchEvent(ev);
//  //      return true;
    //}
//
    //@Override
    //public boolean onInterceptTouchEvent(MotionEvent ev) {
    //    return true;
//  //      switch (ev.getAction()) {
//  //          case MotionEvent.ACTION_DOWN:
//  //              break;
//  //          case MotionEvent.ACTION_MOVE:
//  //              //必须要在MOVE中return才有效果，在这里return后UP事件也会被拦截
//  //              return true;
//  //          case MotionEvent.ACTION_UP:
//  //          case MotionEvent.ACTION_CANCEL:
//  //              break;
//  //      }
//  //      return super.onInterceptTouchEvent(ev);
    //}
//
    //OnTouchListener[] myFuckingListener = new OnTouchListener[1];
    //public void setFuckOnTouchListener(OnTouchListener l) {
    //    myFuckingListener[0] = l;
    //}

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        myFuckingListener[0].onTouch(this,event);
//        return true;
//       // return super.onTouchEvent(event);
//    }
}
