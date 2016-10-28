package com.seki.noteasklite.Base.RecycleViewLayoutManager;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by tianyuan on 16/10/28.
 */
public class ScrollableLinearLayoutManager extends LinearLayoutManager {
    private Context _context;
    public ScrollableLinearLayoutManager(Context context) {
        super(context);
        _context = context;
    }

    public ScrollableLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        _context = context;
    }

    public ScrollableLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller scroller = new LinearSmoothScroller(_context) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return ScrollableLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }
        };
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);

    }
}
