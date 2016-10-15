package com.seki.noteasklite.Util;

import android.text.Html;

/**
 * Created by yuan-tian01 on 2016/4/2.
 */
public class EmojiPatch {
    public static String fuckEmoji(String src){
        return Html.fromHtml(src,null,null).toString();
    }
}
