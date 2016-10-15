package com.seki.noteasklite.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.ViewGroup;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import javax.annotation.Nullable;


/**
 * Created by yuan on 2015/7/21.
 */
public class FrescoImageloadHelper {
    //must caled befor postRun
    public static void LoadImageFromURLAndCallBack(SimpleDraweeView destImageView , String URL,Context context,BaseBitmapDataSubscriber bbds)
    {
        int w = destImageView.getWidth();
        int h  =destImageView.getHeight();
        if(w<1){
            w = destImageView.getLayoutParams().width;
        }
        if(h<1){
            h  =destImageView.getLayoutParams().height;
        }
        ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(URL))
                        .setResizeOptions(new ResizeOptions(w,h))
                        .setProgressiveRenderingEnabled(true)
                        .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(bbds, CallerThreadExecutor.getInstance());
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(destImageView.getController())
                .setAutoPlayAnimations(true)
                .build();
        destImageView.setController(draweeController);
    }
    public  static class  GaussProcessor extends  BasePostprocessor{
        int radius;
        public GaussProcessor(int radius) {
            super();
            this.radius = radius;
        }

        @Nullable
        @Override
        public CacheKey getPostprocessorCacheKey() {
            return super.getPostprocessorCacheKey();
        }

        @Override
        public void process(Bitmap bitmap) {
            super.process(bitmap);
           ImageHelper.fastblurSrc(bitmap,radius);
        }

        @Override
        public String getName() {
            return "GaussProcessor";
        }
    }
    public static void LoadImageFromURLAndCallBack(SimpleDraweeView destImageView , String URL, Context context, BaseBitmapDataSubscriber bbds
    , BasePostprocessor postprocessor)
    {
        int w = destImageView.getWidth();
        int h  =destImageView.getHeight();
        if(w<1){
            w = destImageView.getLayoutParams().width;
        }
        if(h<1){
            h  =destImageView.getLayoutParams().height;
        }
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(URL))
                .setResizeOptions(new ResizeOptions(w,h))
                .setProgressiveRenderingEnabled(true);
        if(postprocessor!=null){
            builder.setPostprocessor(postprocessor);
        }
        ImageRequest imageRequest =
                builder
                        .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(bbds, CallerThreadExecutor.getInstance());
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(destImageView.getController())
                .setAutoPlayAnimations(true)
                .build();
        destImageView.setController(draweeController);
    }
    //must caled befor postRun
    public static void LoadImageFromURIAndCallBack(SimpleDraweeView destImageView , Uri uri,Context context,BaseBitmapDataSubscriber bbds)
    {
        int w = destImageView.getWidth();
        int h  =destImageView.getHeight();
        if(w<1){
            w = destImageView.getLayoutParams().width;
        }
        if(h<1){
            h  =destImageView.getLayoutParams().height;
        }
        ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(w,h))
                        .setProgressiveRenderingEnabled(true)
                        .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(bbds, CallerThreadExecutor.getInstance());
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(destImageView.getController())
                .setAutoPlayAnimations(true)
                .build();
        destImageView.setController(draweeController);
    }
    public static void LoadFullImageFromURL(final SimpleDraweeView destImageView , final String URL)
    {
        destImageView.post(new Runnable() {
            @Override
            public void run() {
                int w = destImageView.getWidth();
                int h  =destImageView.getHeight();
                ViewGroup.LayoutParams  params = destImageView.getLayoutParams();
                if(w<1){
                    w = params.width;
                }
                if(h<1){
                    h  = params.height;
                }
                ImageRequest imageRequest =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(URL))
                                .setResizeOptions(new ResizeOptions(w, h))
                                .setProgressiveRenderingEnabled(true)
                                .build();
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(imageRequest)
                        .setOldController(destImageView.getController())
                        .setAutoPlayAnimations(true)
                        .build();
                destImageView.setController(draweeController);
            }
        });

    }
    /*
    *   简单的图片下载，用于长宽策略不固定的情况
     */
    public static  void simpleLoadImageFromURL(SimpleDraweeView destImageView , String URL)
    {
        Uri uri = Uri.parse(URL);
        if(URL !=null && uri!=null){
            destImageView.setImageURI(uri);
        }
    }
    public static  void simpleLoadImageFromURI(SimpleDraweeView destImageView , Uri uri)
    {
        if(uri!=null){
            destImageView.setImageURI(uri);
        }
    }
    /**
     * 有些图片在网络上会变化，我们需要首现清除缓存从磁盘
     */
    public static  void removeCacheFromDisk(String imageURL)
    {
        Fresco.getImagePipelineFactory().getMainDiskStorageCache().remove(new SimpleCacheKey(imageURL));
        Fresco.getImagePipelineFactory().getSmallImageDiskStorageCache().remove(new SimpleCacheKey(imageURL));
    }
}
