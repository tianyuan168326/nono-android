package com.seki.noteasklite.TestRom;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by yuan-tian01 on 2016/2/27.
 */
public class BallViewDrawable extends Drawable {
    int color;
    Paint mPaint;
    public BallViewDrawable(int color){
        this.color = color;
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
    }
    public void setColor(int color){
        this.color = color;
        mPaint.setColor(color);
        invalidateSelf();
    }
    @Override
    public void draw(Canvas canvas) {
        Rect bound = getBounds();
        int w = -1;
        int h  =-1;
        w= h = Math.min(bound.width(),bound.height());
        canvas.drawCircle(w/2,h/2,w/2,mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        mPaint.setStyle(Paint.Style.FILL);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(int color, PorterDuff.Mode mode) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
