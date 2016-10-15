package com.seki.noteasklite;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.ViewParent;

/**
 * Created by Seki on 2016/2/1.
 */
public class MyCollapsingToolbarLayout extends CollapsingToolbarLayout {
    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    public MyCollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public MyCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            if (mOnOffsetChangedListener == null) {
                mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(mOnOffsetChangedListener);
        }
    }

    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
        @Override
        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) {
            final int scrollRange = layout.getTotalScrollRange();

            //if (Math.abs(verticalOffset) == scrollRange) {
            //    // If we have some pinned children, and we're offset to only show those views,
            //    // we want to be elevate
            //
            //
              ViewCompat.setElevation(layout, layout.getTargetElevation());
            //} else {
            //    // Otherwise, we're inline with the content
            //    ViewCompat.setElevation(layout, 0f);
            //}

            if(Math.abs(verticalOffset)>=getHeight()-56*getResources().getDisplayMetrics().density){
                if(listener!=null){
                    listener.collapsed();
                }
            }else {
                if(listener!=null){
                    listener.expand();
                }
            }
        }
    }

    public interface OnListener{
        void collapsed();
        void expand();
    }

    public void setOnListener(OnListener listener){
        this.listener=listener;
    }

    private OnListener listener=null;
}
