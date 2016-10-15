package com.seki.noteasklite.OnGetImagePolicy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.IllegalFormatCodePointException;
import java.util.IllegalFormatException;

/**
 * Created by Seki on 2016/1/18.
 */
public class ImageProcessor extends BitmapFactory{

    public static byte[] bitmap2Bytes(Bitmap bitmap){
        ByteBuffer byteBuffer=ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }

    public static String bitmap2Base64(Bitmap bitmap){
        return Base64.encodeToString(bitmap2Bytes(bitmap), Base64.DEFAULT);
    }

    public static Bitmap bytes2Bitmap(byte[] bytes){
        return decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap base642Bitmap(String base){
        byte[] bytes=Base64.decode(base,Base64.DEFAULT);
        return bytes2Bitmap(bytes);
    }

    public static byte[] compressBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int nowL=baos.toByteArray().length;
        int options = 100;
        //int length=nowL/2;
        double threshold=bitmap.getWidth()*bitmap.getHeight()*20/81.0;//1920*1080 压缩至 500k 以此为基准进行压缩
        threshold=threshold>0x100000?0x100000:threshold;//压缩大小不超过1M
        while (nowL>threshold) {
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -=10;//每次都减少10
            nowL=baos.toByteArray().length;
        }
        return baos.toByteArray();
    }

    public static Bitmap compressBitmapAndReturn(Bitmap bitmap){
        return bytes2Bitmap(compressBitmap(bitmap));
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

    public static Drawable zoomImageMax(Drawable drawable,double newWidth,double newHeight){
        return bitmap2Drawable(zoomImageMax(drawable2Bitmap(drawable),newWidth,newHeight));
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

    public static Drawable zoomImageMin(Drawable drawable,double newWidth,double newHeight){
        return bitmap2Drawable(zoomImageMin(drawable2Bitmap(drawable), newWidth, newHeight));
    }

    public static byte[] decodeResizedImageFromBitmap(String filePath,int dw,int dh) {
        Options bmpFactoryOptions = new Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        if(!(bmpFactoryOptions.outHeight<dh && bmpFactoryOptions.outWidth <dw))
        {
            int heightRatio,widthRatio;
            heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) dh);
            widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) dw);
            if (heightRatio > 1 && widthRatio > 1) {
                if (heightRatio > widthRatio) {
                    bmpFactoryOptions.inSampleSize = heightRatio;
                } else {
                    bmpFactoryOptions.inSampleSize = widthRatio;
                }
            }
            bmpFactoryOptions.inJustDecodeBounds = false;
        }
        return compressBitmap(decodeFile(filePath, bmpFactoryOptions));
    }

    public static Bitmap decodeResizedImageFromBitmapAndReturn(String filePath,int dw,int dh) {
        return bytes2Bitmap(decodeResizedImageFromBitmap(filePath, dw, dh));
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(Resources.getSystem(),bitmap);
    }
    //compress the image loaded from srcPath,and return the dest path
    public static String compressImage(Context context,String srcPath){
        Bitmap bm = BitmapFactory.decodeFile(srcPath);
        if(bm==null){
            return "";
        }
        if(Math.min(bm.getWidth(),bm.getHeight())>1920){
            bm= ImageProcessor.zoomImageMax(bm, 1920, 1920);
        }
        byte[] bytes= ImageProcessor.compressBitmap(bm);
        File littleImageFIle = new File(srcPath);
        FileOutputStream outputStream;
        try{
            outputStream = context.openFileOutput(littleImageFIle.getName(), Context.MODE_PRIVATE);
            outputStream.write(bytes);
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        if (!bm.isRecycled()) {
            bm.recycle();
            System.gc();
        }
        return context.getFilesDir()+"/"+littleImageFIle.getName();
    }
}
