package com.seki.noteasklite.CustomControl;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.seki.noteasklite.R;

import java.lang.reflect.Field;

/**
 * Created by yuan-tian01 on 2016/3/13.
 */
public class SuperViewPager extends ViewPager {
    public SuperViewPager(Context context) {
        super(context);
    }

    public boolean is_scroll() {
        return is_scroll;
    }

    public void setIs_scroll(boolean is_scroll) {
        this.is_scroll = is_scroll;
    }

    boolean is_scroll;

    public SuperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini(context, attrs);
    }
    private void ini(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperViewPager);
        is_scroll = a.getBoolean(R.styleable.SuperViewPager_is_scroll,true);
        a.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!is_scroll) {
            return true;
        }
//        Class clazz = super.getClass();
//        try{
//            Field field = clazz.getDeclaredField("mLastMotionX");
//            field.setAccessible(true);
//            int mLastMotionX = field.getInt(this);
//            ev.setLocation(mLastMotionX,ev.getY());
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        return super.onTouchEvent(ev);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
