package com.seki.noteasklite.Util;

import android.text.Html;

/**
 * Created by yuan on 2016/5/1.
 */
public class FuckBreaker  {
    public static String fuckBreakerAndSpace(String src){
        if(src == null)
        return null;
        String plainText = Html.fromHtml(src, null, null).toString();
        plainText  = plainText.trim().replace(" ","");
        return  plainText;
    }
    public static String fuckBreaker(String src){
        if(src == null)
            return "";
        String plainText = Html.fromHtml(src, null, null).toString();
        plainText  = plainText.trim();
        return  plainText;
    }
}
