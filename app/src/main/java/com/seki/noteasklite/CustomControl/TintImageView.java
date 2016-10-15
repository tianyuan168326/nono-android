package com.seki.noteasklite.CustomControl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.seki.noteasklite.R;

/**
 * Created by yuan-tian01 on 2016/3/11.
 */
public class TintImageView extends AppCompatImageView {
    public TintImageView(Context context) {
        super(context);
    }
    int tintColor;
    public TintImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini(context, attrs);
    }

    public TintImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini(context,attrs);
    }

    private void ini(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TintImageView);
         tintColor = a.getColor(R.styleable.TintImageView_tintColor, Color.BLACK);
        a.recycle();
        setImageDrawable(TintHelper.tintDrawable(getDrawable(), tintColor));
    }
    // color
    public void setTiniColor(int color){
        tintColor = color;
        setImageDrawable(TintHelper.tintDrawable(getDrawable(),color));

    }
    public int getTintColor(){
        return tintColor;
    }
}
