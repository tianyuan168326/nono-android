package com.seki.noteasklite.CustomControl;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by yuan-tian01 on 2016/3/11.
 */
public class TintHelper {
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
                 final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                 DrawableCompat.setTintList(wrappedDrawable, colors);
                 return wrappedDrawable;
             }
    public static Drawable tintDrawable(Drawable drawable, int color) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
    public static void tintView(ImageView view, ColorStateList colors){
        view.setImageDrawable(tintDrawable(view.getDrawable(), colors));
    }
    public static void tintView(ImageView view, int color){
        view.setImageDrawable(tintDrawable(view.getDrawable(),color));
    }
}
