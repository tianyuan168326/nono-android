package com.seki.noteasklite.Util;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.Random;

/**
 * Created by yuan-tian01 on 2016/4/21.
 */
public class ColorUtils {
    private static  String[] Colors = {
            "#F44336",
            "#E91E63",
            "#9C27B0",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#9E9E9E",
            "#607D8B"};

    public static int getRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        return Color.parseColor(Colors[p]);
    }
    public static  int getLighterColor(int color,float tint_factor){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.0f + tint_factor; // value component
        if(hsv[2]<0){
            hsv[2] = 0;
        }
        if(hsv[2]>1){
            hsv[2] = 1;
        }
        return Color.HSVToColor(hsv);
    }
    //tint_factor need be in 0-1
    public static  int getDarkerColor(int color,float tint_factor){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.0f - tint_factor; // value component
        if(hsv[2]<0){
            hsv[2] = 0;
        }
        if(hsv[2]>1){
            hsv[2] = 1;
        }
        return Color.HSVToColor(hsv);
    }
    public static int getAlphaColor(@ColorInt int color, float alphaFactor){
        int r  = Color.red(color);
        int g  = Color.green(color);
        int b  = Color.blue(color);
        int a = Color.alpha(color);
        a*=alphaFactor;
        return Color.argb(a,r,g,b);
    }
}
