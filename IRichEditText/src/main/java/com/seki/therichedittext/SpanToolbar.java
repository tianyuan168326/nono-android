package com.seki.therichedittext;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Seki on 2016/5/2.
 * 用来放置样式按钮，有新样式时请更新init
 */
public class SpanToolbar extends HorizontalScrollView implements View.OnClickListener,View.OnLongClickListener{

    public SpanToolbar(Context context) {
        this(context, null);
    }

    public SpanToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpanToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color=typedValue.data;
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(new SpanButton.SpanTextSizeButton(getContext()).setOnClick(this));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_UNDERLINE).setColor(0xff757575).setImage(R.mipmap.ic_format_underline_grey600_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_STRIKETHROUGH).setColor(0xff757575).setImage(R.mipmap.ic_format_strikethrough_grey600_24dp));
       // linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_TEXTSIZE).setColor(0xff757575).setImage(R.mipmap.ic_format_));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_BOLD).setColor(0xff757575).setImage(R.mipmap.ic_format_bold_grey600_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_ITALIC).setColor(0xff757575).setImage(R.mipmap.ic_format_italic_grey600_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_SUBSCRIPT).setColor(0xff757575).setImage(R.mipmap.ic_vertical_align_bottom_grey600_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_SUPERSCRIPT).setColor(0xff757575).setImage(R.mipmap.ic_vertical_align_top_grey600_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setOnLongClick(this).setSpan(SpanButton.SPAN_BACKGROUND).setColor(0xff757575).setGroundColor(color).setImage(R.mipmap.ic_format_color_fill_black_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setOnLongClick(this).setSpan(SpanButton.SPAN_FOREGROUND).setColor(0xff757575).setGroundColor(color).setImage(R.mipmap.ic_format_color_text_grey600_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_TODO).setColor(0xff757575).setImage(R.mipmap.ic_check_box_128px));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_DOT).setColor(0xff757575).setImage(R.mipmap.ic_format_list_bulleted_black_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_NUMERIC).setColor(0xff757575).setImage(R.mipmap.ic_format_list_numbered_black_24dp));
        linearLayout.addView(new SpanButton(getContext()).setOnClick(this).setSpan(SpanButton.SPAN_PHOTO).setColor(0xff757575).setImage(R.mipmap.ic_insert_photo_grey600_24dp));
        this.addView(linearLayout);
    }

    interface OnSpanClickListener{
        void onSpanClick(SpanButton v);
    }

    private OnSpanClickListener spanClickListener=null;

    public void setOnSpanClickListener(OnSpanClickListener listener){
        spanClickListener=listener;
    }

    interface OnTextSpanClickListener{
        void onTextSpanClick(SpanButton.SpanTextSizeButton v);
    }

    private OnTextSpanClickListener textSpanClickListener=null;

    public void setOnTextSpanClickListener(OnTextSpanClickListener listener){
        textSpanClickListener=listener;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof SpanButton) {
            if (spanClickListener != null) {
                spanClickListener.onSpanClick((SpanButton) v);
            }
        }else if(v instanceof SpanButton.SpanTextSizeButton){
            if(textSpanClickListener!=null){
                textSpanClickListener.onTextSpanClick((SpanButton.SpanTextSizeButton)v);
            }
        }
    }

    interface OnGroundLongClickListener{
        boolean onGroundLongClick(SpanButton v);
    }

    private OnGroundLongClickListener groundLongClickListener=null;

    public void setOnGroundLongClickListener(OnGroundLongClickListener listener){
        groundLongClickListener=listener;
    }

    @Override
    public boolean onLongClick(View v) {
        return groundLongClickListener!=null&& groundLongClickListener.onGroundLongClick((SpanButton)v);
    }
}
