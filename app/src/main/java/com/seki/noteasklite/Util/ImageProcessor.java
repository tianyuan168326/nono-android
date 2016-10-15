package com.seki.noteasklite.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by yuan on 2015/10/30.
 */
public class ImageProcessor {
    private String filePath;
    private Context context;
    public ImageProcessor(String filePath,Context context)
    {
        this.filePath = filePath;
        this.context = context;
    }
//    public Bitmap  decodeResizedImageFromFile()
//    {
//        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//        Display currentDisplay = windowManager.getDefaultDisplay();
//        int dw = currentDisplay.getWidth();
//        int dh = currentDisplay.getHeight();
//        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//        bmpFactoryOptions.inJustDecodeBounds = true;
//        Bitmap bmpBitmap = BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
//        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)dh);
//        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)dw);
//        if(heightRatio >1 && widthRatio >1)
//        {
//            if(heightRatio > widthRatio)
//            {
//                bmpFactoryOptions.inSampleSize = heightRatio;
//            }
//            else {
//                bmpFactoryOptions.inSampleSize = widthRatio;
//            }
//        }
//        bmpFactoryOptions.inJustDecodeBounds = false;
//        bmpBitmap = BitmapFactory .decodeFile(filePath,bmpFactoryOptions);
//        return compressImage(bmpBitmap);
//    }
//    public Bitmap compressImage(Bitmap image)
//    {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while ( baos.toByteArray().length / 1024>300) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }
//    public Bitmap decodeResizedImageFromBitmap(int dw,int dh)
//    {
//        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//        bmpFactoryOptions.inJustDecodeBounds = true;
//        Bitmap bmpBitmap = BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
//        int heightRatio , widthRatio;
//        if(bmpFactoryOptions.outHeight<dh && bmpFactoryOptions.outWidth <dw)
//        {
//            heightRatio = widthRatio = 1;
//        }
//        else {
//            heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) dh);
//            widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) dw);
//            if (heightRatio > 1 && widthRatio > 1) {
//                if (heightRatio > widthRatio) {
//                    bmpFactoryOptions.inSampleSize = heightRatio;
//                } else {
//                    bmpFactoryOptions.inSampleSize = widthRatio;
//                }
//            }
//            bmpFactoryOptions.inJustDecodeBounds = false;
//        }
//        bmpBitmap = BitmapFactory .decodeFile(filePath,bmpFactoryOptions);
//        return compressImage(bmpBitmap);
//    }

//
//    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
//        // 获取这个图片的宽和高
//        float width = bgimage.getWidth();
//        float height = bgimage.getHeight();
//        // 创建操作图片用的matrix对象
//        Matrix matrix = new Matrix();
//        // 计算宽高缩放率
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // 缩放图片动作
//        if(newWidth>newHeight) {
//            matrix.postScale(scaleWidth, scaleWidth);
//            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
//            return Bitmap.createBitmap(bitmap, 0, (int)(bitmap.getHeight()-newHeight)/2, (int) newWidth, (int) newHeight);
//        }else {
//            matrix.postScale(scaleHeight, scaleHeight);
//            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
//            return Bitmap.createBitmap(bitmap, 0, (int)(bitmap.getWidth()-newWidth)/2, (int) newWidth, (int) newHeight);
//        }
//    }
//
//    public static Bitmap drawableToBitmap(Drawable drawable) {
//        Bitmap bitmap = Bitmap.createBitmap(
//                drawable.getIntrinsicWidth(),
//                drawable.getIntrinsicHeight(),
//                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                        : Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        //canvas.setBitmap(bitmap);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        drawable.draw(canvas);
//        return bitmap;
//    }
}
