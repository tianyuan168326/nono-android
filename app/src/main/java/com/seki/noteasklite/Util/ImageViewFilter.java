package com.seki.noteasklite.Util;

import android.graphics.PorterDuff;
import android.widget.ImageView;

/**
 * Created by yuan on 2016/1/29.
 */
public class ImageViewFilter {
    public static  void filterImageView(ImageView imageView,int color,boolean isColorId){
        if(isColorId)
        {
            imageView.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }else{
            imageView.getDrawable().setColorFilter(imageView.getResources().getColor(color), PorterDuff.Mode.SRC_IN);
        }
    }
}
