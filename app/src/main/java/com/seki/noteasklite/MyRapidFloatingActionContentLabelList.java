package com.seki.noteasklite;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.ABViewUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABImageProcess;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionContent;
import com.wangjie.rapidfloatingactionbutton.constants.RFABSize;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.widget.CircleButtonDrawable;
import com.wangjie.rapidfloatingactionbutton.widget.CircleButtonProperties;

import java.util.List;

/**
 * Created by Seki on 2016/2/2.
 */
public class MyRapidFloatingActionContentLabelList extends RapidFloatingActionContent implements View.OnClickListener {
    private MyRapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener onRapidFloatingActionContentLabelListListener;
    private int rfacItemDrawableSizePx;
    private LinearLayout contentView;
    private List<RFACLabelItem> items;
    private int iconShadowRadius;
    private int iconShadowColor;
    private int iconShadowDx;
    private int iconShadowDy;
    private OvershootInterpolator mOvershootInterpolator = new OvershootInterpolator();
    public ScrollView scrollView;
    public void setOnRapidFloatingActionContentLabelListListener(MyRapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener onRapidFloatingActionContentLabelListListener) {
        this.onRapidFloatingActionContentLabelListListener = onRapidFloatingActionContentLabelListListener;
    }

    public MyRapidFloatingActionContentLabelList(Context context) {
        super(context);
    }

    public MyRapidFloatingActionContentLabelList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRapidFloatingActionContentLabelList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyRapidFloatingActionContentLabelList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void initInConstructor() {
        this.rfacItemDrawableSizePx = ABTextUtil.dip2px(this.getContext(), 24.0F);
        this.contentView = new LinearLayout(this.getContext());
        this.contentView.setLayoutParams(new LayoutParams(-1, -1));
        this.contentView.setOrientation(LinearLayout.VERTICAL);
        scrollView=new ScrollView(this.getContext());
        scrollView.addView(contentView);
        scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
        this.setRootView(scrollView);
    }

    protected void initAfterRFABHelperBuild() {
        this.refreshItems();
    }

    public List<RFACLabelItem> getItems() {
        return this.items;
    }

    public MyRapidFloatingActionContentLabelList setItems(List<RFACLabelItem> items) {
        if(!ABTextUtil.isEmpty(items)) {
            this.items = items;
        }

        return this;
    }

    private void refreshItems() {
        if(ABTextUtil.isEmpty(this.items)) {
            throw new RuntimeException(this.getClass().getSimpleName() + "[items] can not be empty!");
        } else {
            this.contentView.removeAllViews();
            int i = 0;

            for(int size = this.items.size(); i < size; ++i) {
                RFACLabelItem item = (RFACLabelItem)this.items.get(i);
                View itemView = LayoutInflater.from(this.getContext()).inflate(com.wangjie.rapidfloatingactionbutton.R.layout.rfab__content_label_list_item, (ViewGroup)null);
                View rootView = (ABViewUtil.obtainView(itemView, com.wangjie.rapidfloatingactionbutton.R.id.rfab__content_label_list_root_view));
                TextView labelTv = (TextView)ABViewUtil.obtainView(itemView, com.wangjie.rapidfloatingactionbutton.R.id.rfab__content_label_list_label_tv);
                ImageView iconIv = (ImageView)ABViewUtil.obtainView(itemView, com.wangjie.rapidfloatingactionbutton.R.id.rfab__content_label_list_icon_iv);
                rootView.setOnClickListener(this);
                labelTv.setOnClickListener(this);
                iconIv.setOnClickListener(this);
                rootView.setTag(com.wangjie.rapidfloatingactionbutton.R.id.rfab__id_content_label_list_item_position, Integer.valueOf(i));
                labelTv.setTag(com.wangjie.rapidfloatingactionbutton.R.id.rfab__id_content_label_list_item_position, Integer.valueOf(i));
                iconIv.setTag(com.wangjie.rapidfloatingactionbutton.R.id.rfab__id_content_label_list_item_position, Integer.valueOf(i));
                CircleButtonProperties circleButtonProperties = (new CircleButtonProperties()).setStandardSize(RFABSize.MINI).setShadowColor(this.iconShadowColor).setShadowRadius(this.iconShadowRadius).setShadowDx(this.iconShadowDx).setShadowDy(this.iconShadowDy);
                int shadowOffsetHalf = circleButtonProperties.getShadowOffsetHalf();
                int minPadding = ABTextUtil.dip2px(this.getContext(), 8.0F);
                int realItemSize;
                if(shadowOffsetHalf < minPadding) {
                    realItemSize = minPadding - shadowOffsetHalf;
                    rootView.setPadding(0, realItemSize, 0, realItemSize);
                }

                realItemSize = circleButtonProperties.getRealSizePx(this.getContext());
                android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams)iconIv.getLayoutParams();
                if(null == lp) {
                    lp = new android.widget.LinearLayout.LayoutParams(-2, -2);
                }

                int rfabRealSize = this.onRapidFloatingActionListener.obtainRFAButton().getRfabProperties().getRealSizePx(this.getContext());
                lp.rightMargin = (rfabRealSize - realItemSize) / 2;
                lp.width = realItemSize;
                lp.height = realItemSize;
                iconIv.setLayoutParams(lp);
                Integer normalColor = item.getIconNormalColor();
                Integer pressedColor = item.getIconPressedColor();
                CircleButtonDrawable rfacNormalDrawable = new CircleButtonDrawable(this.getContext(), circleButtonProperties, null == normalColor?this.getResources().getColor(com.wangjie.rapidfloatingactionbutton.R.color.rfab__color_background_normal):normalColor.intValue());
                CircleButtonDrawable rfacPressedDrawable = new CircleButtonDrawable(this.getContext(), circleButtonProperties, null == pressedColor?this.getResources().getColor(com.wangjie.rapidfloatingactionbutton.R.color.rfab__color_background_pressed):pressedColor.intValue());
                if(Build.VERSION.SDK_INT > 11) {
                    iconIv.setLayerType(1, rfacNormalDrawable.getPaint());
                }

                ABViewUtil.setBackgroundDrawable(iconIv, ABShape.selectorClickSimple(rfacNormalDrawable, rfacPressedDrawable));
                int padding = ABTextUtil.dip2px(this.getContext(), 8.0F) + shadowOffsetHalf;
                iconIv.setPadding(padding, padding, padding, padding);
                String label = item.getLabel();
                if(ABTextUtil.isEmpty(label)) {
                    labelTv.setVisibility(GONE);
                } else {
                    if(item.isLabelTextBold()) {
                        labelTv.getPaint().setFakeBoldText(true);
                    }

                    labelTv.setVisibility(VISIBLE);
                    labelTv.setText(label);
                    Drawable resId = item.getLabelBackgroundDrawable();
                    if(null != resId) {
                        ABViewUtil.setBackgroundDrawable(labelTv, resId);
                    }

                    Integer drawable = item.getLabelColor();
                    if(null != drawable) {
                        labelTv.setTextColor(drawable.intValue());
                    }

                    Integer labelSize = item.getLabelSizeSp();
                    if(null != labelSize) {
                        labelTv.setTextSize(2, (float)labelSize.intValue());
                    }
                }

                Drawable var23 = item.getDrawable();
                if(null != var23) {
                    iconIv.setVisibility(VISIBLE);
                    var23.setBounds(0, 0, this.rfacItemDrawableSizePx, this.rfacItemDrawableSizePx);
                    iconIv.setImageDrawable(var23);
                } else {
                    int var24;
                    if((var24 = item.getResId()) > 0) {
                        iconIv.setVisibility(VISIBLE);
                        iconIv.setImageDrawable(ABImageProcess.getResourceDrawableBounded(this.getContext(), var24, this.rfacItemDrawableSizePx));
                    } else {
                        iconIv.setVisibility(GONE);
                    }
                }

                this.contentView.addView(itemView);
            }

        }
    }

    protected void initialContentViews(View rootView) {
    }

    public void onClick(View v) {
        Integer position;
        if(null != this.onRapidFloatingActionContentLabelListListener && null != (position = (Integer)v.getTag(com.wangjie.rapidfloatingactionbutton.R.id.rfab__id_content_label_list_item_position))) {
            int i = v.getId();
            if(i == com.wangjie.rapidfloatingactionbutton.R.id.rfab__content_label_list_label_tv) {
                this.onRapidFloatingActionContentLabelListListener.onRFACItemLabelClick(position.intValue(), (RFACLabelItem)this.items.get(position.intValue()));
            } else if(i == com.wangjie.rapidfloatingactionbutton.R.id.rfab__content_label_list_icon_iv) {
                this.onRapidFloatingActionContentLabelListListener.onRFACItemIconClick(position.intValue(), (RFACLabelItem)this.items.get(position.intValue()));
            } else if(i == com.wangjie.rapidfloatingactionbutton.R.id.rfab__content_label_list_root_view) {
                this.onRapidFloatingActionListener.collapseContent();
            }

        }
    }

    public MyRapidFloatingActionContentLabelList setIconShadowRadius(int iconShadowRadius) {
        this.iconShadowRadius = iconShadowRadius;
        return this;
    }

    public MyRapidFloatingActionContentLabelList setIconShadowColor(int iconShadowColor) {
        this.iconShadowColor = iconShadowColor;
        return this;
    }

    public MyRapidFloatingActionContentLabelList setIconShadowDx(int iconShadowDx) {
        this.iconShadowDx = iconShadowDx;
        return this;
    }

    public MyRapidFloatingActionContentLabelList setIconShadowDy(int iconShadowDy) {
        this.iconShadowDy = iconShadowDy;
        return this;
    }

    public void onExpandAnimator(AnimatorSet animatorSet) {
        int count = this.contentView.getChildCount();

        for(int i = 0; i < count; ++i) {
            View rootView = this.contentView.getChildAt(i);
            ImageView iconIv = (ImageView)ABViewUtil.obtainView(rootView, com.wangjie.rapidfloatingactionbutton.R.id.rfab__content_label_list_icon_iv);
            if(null == iconIv) {
                return;
            }

            ObjectAnimator animator = new ObjectAnimator();
            animator.setTarget(iconIv);
            animator.setFloatValues(new float[]{45.0F, 0.0F});
            animator.setPropertyName("rotation");
            animator.setInterpolator(this.mOvershootInterpolator);
            //animator.setStartDelay((long)(count * i * 20));
            animatorSet.playTogether(new Animator[]{animator});
        }

    }

    public void onCollapseAnimator(AnimatorSet animatorSet) {
        int count = this.contentView.getChildCount();

        for(int i = 0; i < count; ++i) {
            View rootView = this.contentView.getChildAt(i);
            ImageView iconIv = (ImageView)ABViewUtil.obtainView(rootView, com.wangjie.rapidfloatingactionbutton.R.id.rfab__content_label_list_icon_iv);
            if(null == iconIv) {
                return;
            }

            ObjectAnimator animator = new ObjectAnimator();
            animator.setTarget(iconIv);
            animator.setFloatValues(new float[]{0.0F, 45.0F});
            animator.setPropertyName("rotation");
            animator.setInterpolator(this.mOvershootInterpolator);
            //animator.setStartDelay((long)(count * i * 20));
            animatorSet.playTogether(new Animator[]{animator});
        }

    }

    public interface OnRapidFloatingActionContentLabelListListener<T> {
        void onRFACItemLabelClick(int var1, RFACLabelItem<T> var2);

        void onRFACItemIconClick(int var1, RFACLabelItem<T> var2);
    }
}
