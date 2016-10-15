package com.seki.therichedittext;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Seki on 2016/5/2.
 * 这是SpanToolbar的项
 * 有新样式时，请添加下面的final SPAN_**
 */
public class SpanButton extends AppCompatImageView{

    private int span;

    public final static int SPAN_TEXTSIZE=0x30;
    public final static int SPAN_BOLD=0x31;
    public final static int SPAN_ITALIC=0x32;
    public final static int SPAN_STRIKETHROUGH=0x33;
    public final static int SPAN_UNDERLINE=0x34;
    public final static int SPAN_SUBSCRIPT=0x35;
    public final static int SPAN_SUPERSCRIPT=0x36;
    public final static int SPAN_FOREGROUND=0x37;
    public final static int SPAN_BACKGROUND=0x38;
    public final static int SPAN_PHOTO=0x39;
    public final static int SPAN_TODO=0x3a;
    public final static int SPAN_DOT=0x3b;
    public final static int SPAN_NUMERIC=0x3c;

    private int groundColor;//only use in Foreground and Background;

    public SpanButton(Context context) {
        this(context, null);
    }

    public SpanButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpanButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
       // View view=View.inflate(getContext(),R.layout.span_button,null);
        setClickable(true);
        setScaleType(ScaleType.CENTER_INSIDE);
        int size= (int)(getResources().getDisplayMetrics().density*36);
        setLayoutParams(new LinearLayout.LayoutParams(size,size));
        setPadding(DisplayUtil.dip2px(getContext(),4),
                DisplayUtil.dip2px(getContext(),8),
                DisplayUtil.dip2px(getContext(),4),
                DisplayUtil.dip2px(getContext(),8)
        );
    }

    public SpanButton setImage(int resId){
        setImageResource(resId);
        return this;
    }
    public SpanButton setScale(ScaleType type){
        setScaleType(type);
        return this;
    }
    public SpanButton setColor(@ColorInt int color){
        setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return this;
    }

    public SpanButton setSpan(int span){
        this.span=span;
        this.setTag(span);
//        if(this.span==SPAN_FOREGROUND||this.span==SPAN_BACKGROUND){
////            this.setOnLongClickListener(new OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View v) {
////                    new ColorPanel(getContext(),groundColor).setOnColorChoseCallback(new ColorPanel.OnColorChoseCallback() {
////                        @Override
////                        public void onColoChose(@ColorInt int color) {
////                            setGroundColor(color);
////                            setColor(color);
////                        }
////                    });
////                    return false;
////                }
////            });
//        }
        return this;
    }

    public SpanButton setOnLongClick(OnLongClickListener listener){
        setOnLongClickListener(listener);
        return this;
    }

    public int getSpan(){
        return span;
    }

    public SpanButton setOnClick(OnClickListener listener){
        setOnClickListener(listener);
        return this;
    }

    public SpanButton setGroundColor(@ColorInt int color){
        groundColor=color;
        return this;
    }

    @ColorInt
    public int getGroundColor(){
        return groundColor;
    }

    static class SpanTextSizeButton extends AppCompatTextView{

        public SpanTextSizeButton(Context context) {
            this(context, null);
        }

        public SpanTextSizeButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public SpanTextSizeButton(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init(){
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
            int color=typedValue.data;
            setClickable(true);
            int size= (int)(getResources().getDisplayMetrics().density*36);
            setLayoutParams(new LinearLayout.LayoutParams(size,size));
            setText("M");
            setTextAppearance(getContext(),R.style.SpanTextAppearance);
            setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            setTextColor(color);
            setGravity(Gravity.CENTER);
            setTag(SPAN_TEXTSIZE);
        }

        public SpanTextSizeButton setOnClick(OnClickListener listener){
            setOnClickListener(listener);
            return this;
        }

        public SpanTextSizeButton setTextSize(int sizeFlag){
            switch (sizeFlag){
                case BaseRichEditText.SMALL_TEXT:
                    setText("S");
                    break;
                case BaseRichEditText.LARGE_TEXT:
                    setText("L");
                    break;
                case BaseRichEditText.MEDIUM_TEXT:
                    setText("M");
                    break;
            }
            return this;
        }
    }
}