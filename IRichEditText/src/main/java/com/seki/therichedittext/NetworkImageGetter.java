package com.seki.therichedittext;

/**
 * Created by yuan on 2016/1/30.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import java.io.File;


/**
 * 网络图片
 *
 * @author Susie
 */
public final class NetworkImageGetter implements Html.ImageGetter {
    BaseRichEditText textView;
    String content;
    MyHtmlTagHandler htmlTagHandler;

    public NetworkImageGetter(BaseRichEditText textView,String content,MyHtmlTagHandler htmlTagHandler){
        this.textView = textView;
        this.content = content;
        this.htmlTagHandler = htmlTagHandler;
    }

    @Override
    public Drawable getDrawable(String source) {

        Drawable drawable = null;
        File file;
        //file = new File(source);
        if(source.startsWith("http")){
            file = new File(textView.getContext().getApplicationContext().getExternalFilesDir(null), source.substring(source.lastIndexOf("/"), source.length()));
        }else{
            file = new File(source);
        }
        if (file.exists()) {
            // 存在即获取drawable
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //drawable = Drawable.createFromPath(file.getAbsolutePath());
           // drawable = BitmapDrawable.createFromPath(file.getAbsolutePath());
            if (bitmap != null) {
//                int w = drawable.getIntrinsicWidth();
//                int h = drawable.getIntrinsicHeight();
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                float ratio = (float) w / h;
                double displayW = textView.getContext().getResources().getDisplayMetrics().widthPixels
                        - textView.getPaddingLeft() - textView.getPaddingRight();
                if (w > displayW ) {
                    drawable = ImageProcessor.bitmap2Drawable(ImageProcessor.zoomImageMin(bitmap, displayW, displayW / ratio));
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                }else{
                    drawable = ImageProcessor.bitmap2Drawable(ImageProcessor.zoomImageMin(bitmap, displayW, displayW / ratio));
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                }

            }
        } else if (source.startsWith("http")) {
            // 不存在即开启异步任务加载网络图片
           new AsyncLoadNetworkPic(textView.getContext().getApplicationContext(), textView,
                    content, this, htmlTagHandler)
                    .execute(source, source.substring(source.lastIndexOf("/"), source.length()));
        }
        return drawable;
    }
}
