package com.seki.therichedittext;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Seki on 2016/1/18.
 */
public class ImageProcessor extends BitmapFactory{

    public static Bitmap decodeResizedImageFromBitmapAndReturn(String filePath,int dw,int dh) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        BitmapFactory.decodeFile(filePath, opts);
        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) (Math.ceil(imgWidth / (float) dw)+0.5);
        int heightRatio = (int) (Math.ceil(imgHeight / (float) dh)+0.5);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        return decodeFile(filePath, opts);
    }

    public static Bitmap zoomImageMax(Bitmap bitmap, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        if(scaleWidth>scaleHeight) {
            matrix.postScale(scaleWidth, scaleWidth);
            return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
        }else {
            matrix.postScale(scaleHeight, scaleHeight);
            return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
        }
    }
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(Resources.getSystem(),bitmap);
    }
    public static Bitmap zoomImageMin(Bitmap bitmap, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        if(scaleWidth<scaleHeight) {
            matrix.postScale(scaleWidth, scaleWidth);
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
            return bitmap1;
        }else {
            matrix.postScale(scaleHeight, scaleHeight);
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
            return bitmap1;
        }
    }

}
