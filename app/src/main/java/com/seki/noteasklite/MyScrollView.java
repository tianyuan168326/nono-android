package com.seki.noteasklite;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Seki on 2015/10/14.
 */
public class MyScrollView extends NestedScrollView {

    private OnScrollListener listener;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        listener.onScrollChanged(l, t, oldl, oldt);
    }

    public MyScrollView(Context context) {
        super(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnScrollListener{
        void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }
}
