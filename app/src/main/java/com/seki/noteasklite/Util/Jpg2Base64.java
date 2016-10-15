package com.seki.noteasklite.Util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Jpg2Base64 {
    public static String bitmaptoString(Bitmap bitmap) {



        // 将Bitmap转换成字符串

        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(CompressFormat.JPEG, 100, bStream);
        byte[] bytes = bStream.toByteArray();

        string = Base64.encodeToString(bytes, Base64.DEFAULT);

        return string;

}
    public static Bitmap stringtoBitmap(String string) {

        // 将字符串转换成Bitmap类型

        Bitmap bitmap = null;

        try {

                byte[] bitmapArray;

                bitmapArray = Base64.decode(string, Base64.DEFAULT);

                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,

                        bitmapArray.length);

        } catch (Exception e) {

                e.printStackTrace();

        }



        return bitmap;

}


}
