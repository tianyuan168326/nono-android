package com.seki.noteasklite.CustomControl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.textservice.TextInfo;
import android.widget.ImageView;

import com.seki.noteasklite.R;

/**
 * Created by yuan-tian01 on 2016/2/25.
 */
public class TextInputLayoutWithDrawable extends TextInputLayout{
    private ImageView drawableImageView;
    private int drawableColor = 0;
    public TextInputLayoutWithDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawableImageView = new ImageView(context,attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextInputLayoutWithDrawable);
        int drawableColor = a.getColor(R.styleable.TextInputLayoutWithDrawable_drawableColor, 0xFFFFFF);
        Drawable drawable =  a.getDrawable(R.styleable.TextInputLayoutWithDrawable_drawableSrc);
        setDrawableColor(drawableColor);
        setDrawable(drawable);
    }
    public void setDrawableColor(int color){
        drawableColor = color;
    }
    public void setDrawable(Drawable drawable){
        drawableImageView.setImageDrawable(drawable);
        addView(drawableImageView);
        if(getEditText() != null) {
            getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if(drawableColor !=0){
                        drawableImageView.setColorFilter(drawableColor);
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }
    }
}
