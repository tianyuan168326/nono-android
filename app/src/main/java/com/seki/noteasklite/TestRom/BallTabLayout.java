package com.seki.noteasklite.TestRom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.DisplayUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.LayoutParams.*;

/**
 * Created by yuan-tian01 on 2016/2/27.
 */
public class BallTabLayout extends LinearLayout {
    private List<View> tabViews;
    private int currentSelectedTabIndex;
    private Context context;
    //private int normalColor;
    private ViewPagerOnBallTabSelectedListener mViewPagerOnBallTabSelectedListener;

    private WeakReference<PagerAdapter> mViewPagerAdapterWrapper;
    private float ballScale = 1.3f;
    int primaryBallColor = getResources().getColor(R.color.colorAccent);
    int selectedBallColor = getResources().getColor(R.color.colorPrimary);
    public BallTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        tabViews = new ArrayList<>();
        this.context = context;
        currentSelectedTabIndex = 0;
    }
    public void setupWithViewPager(@NonNull ViewPager viewPager){
        final PagerAdapter adapter = viewPager.getAdapter();
        mViewPagerAdapterWrapper = new WeakReference<PagerAdapter>(adapter);
        if (adapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }
        // First we'll add Tabs, using the adapter's page titles
        setTabsFromPagerAdapter(adapter);

        // Now we'll add our page change listener to the ViewPager
        viewPager.addOnPageChangeListener(new BallTabLayoutOnPageChangeListener(this));

        // Now we'll add a tab selected listener to set ViewPager's current item
        setOnTabSelectedListener(new ViewPagerOnBallTabSelectedListener(viewPager));

        // Make sure we reflect the currently set ViewPager item
        if (adapter.getCount() > 0) {
            final int curItem = viewPager.getCurrentItem();
            if (getSelectedTabPosition() != curItem) {
                selectTab(getTabAt(curItem));
            }
        }
    }

    private void setOnTabSelectedListener(ViewPagerOnBallTabSelectedListener viewPagerOnBallTabSelectedListener) {
        mViewPagerOnBallTabSelectedListener = viewPagerOnBallTabSelectedListener;
    }

    private void selectTab(View tabAt) {

    }
    int normalBallWidth = -1;
    int normalBallHeight = -1;
    private void setTabsFromPagerAdapter(final PagerAdapter adapter) {
        tabViews.clear();
        currentSelectedTabIndex = 0;
        for(int i = 0;i<adapter.getCount();i++){
            SquareTextView tabView = new SquareTextView(context);

            tabView.setText(String.valueOf(i + 1) );
            tabView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimensionPixelSize(R.dimen.md_ball_text_size)
            );
            tabView.setTextColor(Color.WHITE);
            tabViews.add(tabView);
            BallTabLayout.this.addView(tabView, WRAP_CONTENT, WRAP_CONTENT);
            tabView.setGravity(Gravity.CENTER);
            tabView.setPadding(
                    context.getResources().getDimensionPixelSize(R.dimen.ball_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.ball_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.ball_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.ball_margin)
            );
            BallViewDrawable ballViewDrawable = new BallViewDrawable(primaryBallColor);
            tabView.setBackgroundDrawable(ballViewDrawable);
        }
        post(new Runnable() {
            @Override
            public void run() {
                int tabViewWidth = tabViews.get(0).getWidth();
                normalBallWidth  =  tabViews.get(0).getWidth();
                normalBallHeight  =  tabViews.get(0).getHeight();
                int sumW =
                        (int)(
                                BallTabLayout.this.getMeasuredWidth() -
                                        BallTabLayout.this.getPaddingLeft() - BallTabLayout.this.getPaddingRight()-(ballScale-1)*normalBallWidth

                                );
                int tabViewMarginRight = (int) (
                        (sumW) / (tabViews.size() - 1) - (1f + 1f / (tabViews.size() - 1)) * tabViewWidth
                );
                for (int i = 0; i < adapter.getCount() - 1; i++) {
                    ViewGroup.MarginLayoutParams tabViewLayoutParams = (ViewGroup.MarginLayoutParams) tabViews.get(i).getLayoutParams();
                    tabViewLayoutParams.rightMargin = tabViewMarginRight;
                }
            }
        });
        if(adapter.getCount()>0){

        }
    }




    private class BallTabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<BallTabLayout> mTabLayoutRef;
        private int mPreviousScrollState;
        private int mScrollState;
        public BallTabLayoutOnPageChangeListener(BallTabLayout ballTabLayout) {
            mTabLayoutRef = new WeakReference<>(ballTabLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            final BallTabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                // Update the scroll position, only update the text selection if we're being
                // dragged (or we're settling after a drag)
                final boolean updateText = (mScrollState == ViewPager.SCROLL_STATE_DRAGGING)
                        || (mScrollState == ViewPager.SCROLL_STATE_SETTLING
                        && mPreviousScrollState == ViewPager.SCROLL_STATE_DRAGGING);
                tabLayout.setScrollPosition(position, positionOffset, updateText);
            }
        }

        @Override
        public void onPageSelected(int position) {
            final BallTabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                tabLayout.selectTab(tabLayout.getTabAt(position),
                        mScrollState == ViewPager.SCROLL_STATE_IDLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
        }
    }

    private void selectTab(View tabAt, boolean doAnim) {

    }

    private View getTabAt(int position) {
        return tabViews.get(position);
    }

    private int getSelectedTabPosition() {
        return currentSelectedTabIndex;
    }

    private void setScrollPosition(final int position, final float positionOffset, final boolean updateText) {
        if(position<0 || position >= tabViews.size()){
            return ;
        }
        if(positionOffset == 0){
            return ;
        }
        ViewGroup.LayoutParams tabViewparams = tabViews.get(0).getLayoutParams();
        int tabViewparamsW = tabViewparams.width;
        int tabViewparamsH = tabViewparams.height;
        ViewGroup.LayoutParams currentTabViewParams = tabViews.get(position).getLayoutParams();
        //from [0,1) to [ballScale,1)
        currentTabViewParams.width =(int) (
                normalBallWidth*(
                        1+(1-positionOffset)*(ballScale-1)
                )
        );
        currentTabViewParams.height =(int) (
                normalBallHeight*(
                        1+(1-positionOffset)*(ballScale-1)
                )
        );
        tabViews.get(position).requestLayout();
        tabViews.get(position).getBackground().setColorFilter(getMidColor(selectedBallColor,primaryBallColor,positionOffset), PorterDuff.Mode.SRC);
        ViewGroup.LayoutParams nextTabViewParams = tabViews.get(position+1).getLayoutParams();
        //from (0,1] to (1,ballScale]
        nextTabViewParams.width =(int) (
                normalBallWidth*
                (1 + (ballScale-1)*(positionOffset))
        );
        nextTabViewParams.height =(int) (
                normalBallHeight*
                        (1 + (ballScale-1)*(positionOffset))
        );
        tabViews.get(position+1).requestLayout();
        tabViews.get(position+1).getBackground().setColorFilter(getMidColor(primaryBallColor, selectedBallColor, positionOffset), PorterDuff.Mode.SRC);
        if(updateText){
            currentSelectedTabIndex = Math.round(position*(1f+positionOffset));
        }
    }
    private static int getMidColor(int beginColor,int endColor,float offset){
        float rA  =Color.alpha(beginColor) +( Color.alpha(endColor) - Color.alpha(beginColor))*offset;
        float rR  =Color.red(beginColor) +( Color.red(endColor) - Color.red(beginColor))*offset;
        float rG  =Color.green(beginColor) +( Color.green(endColor) - Color.green(beginColor))*offset;
        float rB  =Color.blue(beginColor) +( Color.blue(endColor) - Color.blue(beginColor))*offset;
        return Color.argb((int)rA,(int)rR,(int)rG,(int)rB);
    }
    /**
     * Callback interface invoked when a tab's selection state changes.
     */
    public interface OnTabSelectedListener {

        /**
         * Called when a tab enters the selected state.
         *
         * @param tab The tab that was selected
         */
        public void onTabSelected(View tab);

        /**
         * Called when a tab exits the selected state.
         *
         * @param tab The tab that was unselected
         */
        public void onTabUnselected(View tab);

        /**
         * Called when a tab that is already selected is chosen again by the user. Some applications
         * may use this action to return to the top level of a category.
         *
         * @param tab The tab that was reselected.
         */
        public void onTabReselected(View tab);
    }

    private class ViewPagerOnBallTabSelectedListener implements OnTabSelectedListener{
        private final ViewPager mViewPager;
        public ViewPagerOnBallTabSelectedListener(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onTabSelected(View tab) {
            mViewPager.setCurrentItem(tabViews.indexOf(tab));

        }

        @Override
        public void onTabUnselected(View tab) {

        }

        @Override
        public void onTabReselected(View tab) {

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = 0;
        int h = 0;
        int w_mode = MeasureSpec.getMode(widthMeasureSpec);
        int w_size = MeasureSpec.getSize(widthMeasureSpec);
        int h_mode = MeasureSpec.getMode(heightMeasureSpec);
        int h_size = MeasureSpec.getSize(heightMeasureSpec);
        //we find the most long title of viewPagers
        String mostLongTitle="元";
        if(mViewPagerAdapterWrapper!=null&&mViewPagerAdapterWrapper.get() !=null){
            for(int i = 0;i<mViewPagerAdapterWrapper.get().getCount();i++){
                String currentTitle = (String) (mViewPagerAdapterWrapper.get().getPageTitle(i));
                if(currentTitle!=null && currentTitle.length()>mostLongTitle.length()){
                    mostLongTitle = currentTitle;
                }
            }
        }

        Rect titleRect = new Rect();
        Paint mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(getResources().getDimension(R.dimen.md_regular_text_size));
        mPaint.getTextBounds(mostLongTitle,0,mostLongTitle.length(),titleRect);
        switch (w_mode){
            case MeasureSpec.EXACTLY:
                w = w_size;
                break;
            default:
                //an very easy implemention
                w = mViewPagerAdapterWrapper.get().getCount() *(titleRect.width() +10);
                break;
        }
        switch (h_mode){
            case MeasureSpec.EXACTLY:
                h = h_size;
                break;
            default:
                //an very easy implemention
                h =(int)(
                                Math.max(titleRect.width(),titleRect.height())+(ballScale+1.5)*context.getResources().getDimensionPixelSize(R.dimen.ball_margin)
                                ) ;
                break;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
    }
}
