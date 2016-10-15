package com.seki.noteasklite.CustomControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.seki.noteasklite.R;

import java.util.List;

/**
 * Created by yuan on 2016/1/16.
 */
public class LoadingAnimationSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    volatile boolean running = false;
    List<String> framePathList;
    SurfaceHolder holder;
    DrawThread drawThread = new DrawThread();
    Paint paint;

    private static int[] pngIdArray = {R.mipmap.explode_0_new,
            R.mipmap.explode_1_new,R.mipmap.explode_2_new,R.mipmap.explode_3_new,R.mipmap.explode_4_new
    ,R.mipmap.explode_5_new,R.mipmap.explode_6_new,R.mipmap.explode_7_new,R.mipmap.explode_8_new,R.mipmap.explode_9_new
,R.mipmap.explode_10_new,R.mipmap.explode_11_new,R.mipmap.explode_12_new,R.mipmap.explode_13_new,R.mipmap.explode_14_new
,R.mipmap.explode_15_new};
//private static int[] pngIdArray = {R.mipmap.ic_add_grey600_24dp};
    private static int pngNum = 16;
//    private static int pngNum = 1;
    public void start(){
        if(isCreatedFlag){
            running = true;
            drawThread = new DrawThread();
            drawThread.start();
        }
    }
    public void stop(){
        running = false;
        try{
            drawThread.join();
        }catch (Exception e){}

    }
    public LoadingAnimationSurfaceView(Context context) {
        super(context);
        ini();
    }
    public LoadingAnimationSurfaceView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        ini();
    }
    public LoadingAnimationSurfaceView(Context context, AttributeSet attrs){
        super(context,attrs);
        ini();
    }
    void ini(){
        paint = new Paint();
        this.holder = getHolder();
        this.holder.setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        getHolder().addCallback(this);
    }
    boolean isCreatedFlag = false;
    @Override
   public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        holder.setFormat(PixelFormat.TRANSLUCENT);
        isCreatedFlag = true;
        Log.d("Loading","surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.holder = holder;
        holder.setFormat(PixelFormat.TRANSLUCENT);

        Log.d("Loading","surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
        isCreatedFlag = false;
        Log.d("Loading", "surfaceDestroyed");
    }
    private class DrawThread extends  Thread{
        int bitmapWidth ;
        int bitmapHeight ;
        int left ;
        int top;
        Bitmap bitmap;
        Matrix matrix = new Matrix();
        Rect oldRect = new Rect(0,0,getWidth(),getHeight());
        Rect windowRect = new Rect(0,0,getWidth(),getHeight());
        Canvas canvas ;
        @Override
        public void run() {

            matrix.postScale(1,1);
            //paint.setAlpha(0);
            //paint.setARGB(255, 255, 255, 100);
            //canvas.drawRect(oldRect, paint);
            //holder.unlockCanvasAndPost(canvas);
            while(running){
                for(int index = 0;index<pngNum;index++){
                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    bitmap = BitmapFactory.decodeResource(getResources(), pngIdArray[index]);
                    bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                    bitmapWidth = bitmap.getWidth();
                    bitmapHeight = bitmap.getHeight();
                    left = (getWidth()-bitmapWidth)/2;
                    top = (getHeight()-bitmapHeight)/2;
                    oldRect = new Rect(left,top,left+bitmapWidth,top+bitmapHeight);
                    canvas.drawBitmap(bitmap, left, top, null);
                    holder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(5);
                    }catch (Exception e){}
                }
            }
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            holder.unlockCanvasAndPost(canvas);

        }
    }

}
