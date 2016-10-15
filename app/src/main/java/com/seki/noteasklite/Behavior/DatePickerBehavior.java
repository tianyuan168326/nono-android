package com.seki.noteasklite.Behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by yuan on 2016/5/24.
 */
public class DatePickerBehavior extends CoordinatorLayout.Behavior<View> {
    public DatePickerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
//        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
//    }
//
//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
////        ViewGroup.LayoutParams p = child.getLayoutParams();
////        p.height -=dy;
////        child.setLayoutParams(p);
//    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if(dependency instanceof FrameLayout){
            if( ((FrameLayout)dependency).getFocusedChild() instanceof RecyclerView ){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, final View child, final View dependency) {
        child.post(new Runnable() {
            @Override
            public void run() {
                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
                if(p.height<0)
                    return;
                p.height -=((FrameLayout)dependency).getFocusedChild().getScrollY();
                child.setLayoutParams(p);
            }
        });
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
