package com.seki.noteasklite.CustomControl;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by yuan-tian01 on 2016/3/9.
 */
public class WorkAroundLineaLayoutManager extends LinearLayoutManager {
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public WorkAroundLineaLayoutManager(Context context) {
        super(context);
    }

    public WorkAroundLineaLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WorkAroundLineaLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
}
