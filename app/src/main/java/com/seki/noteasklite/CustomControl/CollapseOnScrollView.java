package com.seki.noteasklite.CustomControl;

/**
 * Created by yuan on 2016/5/24.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.OverScroller;

import com.seki.noteasklite.R;

/**
 * Created by kklassen on 21.11.2014.
 */
public class CollapseOnScrollView extends NestedScrollView {

    private RecyclerView mLv;
    private float mLastY;
    private GestureDetector mGestureDetector;
    private Flinger mFlinger;
    private int mSlop;

    private View mExpandOnDragView;
    private int mExpandOnDragHeight;

    private View mCollapsibleView;

    private View mPinnedView;
    private int mPinnedViewHeight;

    public CollapseOnScrollView(Context context) {
        super(context);
        init(null);
    }

    public CollapseOnScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CollapseOnScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setVerticalScrollBarEnabled(false);

        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mFlinger = new Flinger();
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                mFlinger.start((int) velocityY);
                return true;
            }
        });

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CollapseOnScrollView);
        final int pinnedViewId = typedArray.getResourceId(R.styleable.CollapseOnScrollView_stayVisibleId, -1);
        final int expandOnDragId = typedArray.getResourceId(R.styleable.CollapseOnScrollView_expandOnDragId, -1);
        typedArray.recycle();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                if (pinnedViewId >= 0) {
                    mPinnedView = findViewById(pinnedViewId);
                    mPinnedViewHeight = mPinnedView.getHeight();
                }

                if (expandOnDragId >= 0) {
                    mExpandOnDragView = findViewById(expandOnDragId);
                    mExpandOnDragHeight = mExpandOnDragView.getHeight();
                    //mExpandOnDragView.getLayoutParams().height = 0;
                    mExpandOnDragView.getLayoutParams().height = mExpandOnDragHeight;
                }

//                mLv.getLayoutParams().height = getHeight() - mPinnedViewHeight;
                mLv.getLayoutParams().height = getHeight() - mExpandOnDragHeight;
                mExpandOnDragView.requestLayout();
                mLv.requestLayout();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewGroup root = (ViewGroup) getChildAt(0);
        mCollapsibleView = root.getChildAt(0);

        mLv =  (RecyclerView)root.getChildAt(2);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                if (!mFlinger.isFinished()) {
                    mFlinger.stopFling();
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if((mLastY - y)>=0 && (! isFirstShown() || mExpandOnDragView.getHeight()==0 )){
                    return false;
                }
                int scrollY = getScrollY();
                if((mLastY - y)<=0 && ! isFirstShown()  ){
                    return false;
                }
//                if((mLastY - y)>0 && Math.abs(mLastY - y) > 2*mSlop){
//                    this.fullScroll(NestedScrollView.FOCUS_UP);
//                    fullExpand();
//                    return false;
//                }
                if (Math.abs(mLastY - y) > mSlop ) {
                    if(y<mCollapsibleView.getBottom()&&y>mCollapsibleView.getTop()&&mLv.getTop()>=mCollapsibleView.getBottom()){
                        return false;
                    }
                    return true;
                }else{
                    return false;
                }
        }
        return false;
    }

    private void fullExpand() {
        this.post(new Runnable() {
            @Override
            public void run() {
                mExpandOnDragView.getLayoutParams().height = mExpandOnDragHeight;
                mLv.getLayoutParams().height = getHeight() - mExpandOnDragHeight;
                mExpandOnDragView.requestFocus();
                mLv.requestLayout();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mLv.getLayoutParams().height == getHeight() - mExpandOnDragHeight){
            mLv.getLayoutParams().height = getHeight();
        }
        boolean isPrecessed = true;
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastY = event.getY();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float distance = mLastY - event.getY();
            int roundedDistance = Math.round(distance);
            isPrecessed= scroll(roundedDistance, true);
            mLastY = event.getY();
        }
        return isPrecessed;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (direction < 0) {
//            return isCollapsed();
            return true;
        } else {
            //return isExpanded();
            return true;
        }
    }

    private boolean isCollapsed() {
        if (mPinnedView == null) {
            return getScrollY() >= mCollapsibleView.getBottom();
        } else {
            return getScrollY() >= mPinnedView.getTop();
        }
    }

    private boolean isExpanded() {
        return getScrollY() <= 0;
    }

    private boolean scroll(int distance, boolean isDragging) {
        boolean isPrecessed = true;
        int remainingDistance = distance;
        boolean isExpand  =isExpanded();
        boolean isListTop = isListReachedTop();
        if (mExpandOnDragView != null && isDragging) {

            ViewGroup.LayoutParams params = mExpandOnDragView.getLayoutParams();
            if(params.height ==0){
                isPrecessed = false;
            }
            if (remainingDistance > 0 && params.height > 0) {
                if(params.height < mExpandOnDragHeight*2/3 ){
                    float per = (params.height/99 );
                    for(int i = 0;i<100;i++){
                        params.height -= per;
                        if (params.height < 0) {
                            remainingDistance = -params.height;
                            params.height = 0;
                        } else {
                            remainingDistance = 0;
                        }
                        mExpandOnDragView.setLayoutParams(params);
                    }
                }else{
                    params.height -= 3*remainingDistance;
                    if (params.height < 0) {
                        remainingDistance = -params.height;
                        params.height = 0;
                    } else {
                        remainingDistance = 0;
                    }
                    mExpandOnDragView.setLayoutParams(params);
                }



            } else if (distance < 0 && params.height < mExpandOnDragHeight && isExpand &&isListTop ) {
                if(params.height ==mExpandOnDragHeight){
                    isPrecessed = false;
                }
                if(params.height > mExpandOnDragHeight/3 ){
                    float per = ((mExpandOnDragHeight - params.height)/99 );
                    for(int i = 0;i<100;i++){
                        params.height += per;
                        if (params.height > mExpandOnDragHeight) {
                            remainingDistance = -(params.height - mExpandOnDragHeight);
                            params.height = mExpandOnDragHeight;
                        } else {
                            remainingDistance = 0;
                        }
                        mExpandOnDragView.setLayoutParams(params);
                    }
                }else{
                    params.height -= 3*remainingDistance;
                if (params.height > mExpandOnDragHeight) {
                    remainingDistance = -(params.height - mExpandOnDragHeight);
                    params.height = mExpandOnDragHeight;
                } else {
                    remainingDistance = 0;
                }
                mExpandOnDragView.setLayoutParams(params);
                }

            }
        }else{
            isPrecessed  = false;
        }

        remainingDistance = scrollThis(remainingDistance);
        scrollList(remainingDistance);
        return isPrecessed;
    }

    private int scrollThis(int distance) {
        int lastScroll = getScrollY();
        scrollBy(0, distance);
        if (isCollapsed()) {
            if (mPinnedView == null) {
                scrollTo(0, mCollapsibleView.getBottom());
            } else {
                scrollTo(0, mPinnedView.getTop());
            }
            return distance + lastScroll - getScrollY();
        } else if (isExpanded()) {
            return distance + lastScroll;
        }
        return 0;
    }

    private void scrollList(int dist) {
        mLv.smoothScrollBy(dist, 0);
    }
    private boolean isFirstShown(){
        if(mLv.getLayoutManager() != null){
            return ((StaggeredGridLayoutManager)mLv.getLayoutManager()).findFirstVisibleItemPositions(null)[0] == 0;
        }else{
            return false;
        }
    }
    private boolean isListReachedTop() {
        boolean a=isFirstShown() ;
        boolean b =
                    true
//                mLv.getChildAt(0).getTop() == 0
                ;
        return a && b;
    }

    private class Flinger implements Runnable {

        private OverScroller mScroller;
        private float mLastY;

        public Flinger() {
            mScroller = new OverScroller(getContext());
        }

        public void start(int initialVelocityY) {
            mLastY = 0;
            mScroller.fling(0, 0, 0, initialVelocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation(this);
        }

        @Override
        public void run() {

            if (mScroller.isFinished()) {
                return;
            }

            mScroller.computeScrollOffset();
            float dist = mLastY - mScroller.getCurrY();
            scroll((int) dist, false);
            mLastY = mScroller.getCurrY();
            postOnAnimation(this);
        }

        private void stopFling() {
            mScroller.forceFinished(true);
        }

        public boolean isFinished() {
            return mScroller.isFinished();
        }
    }
}