package com.seki.therichedittext;

/**
 * Created by yuan on 2015/10/28.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 加载网络图片异步类
 * @author Susie
 */
public final class AsyncLoadNetworkPic extends AsyncTask<String, Integer, Boolean> {
    TextView editText;
    String content;
    Html.ImageGetter imageGetter;
    Html.TagHandler htmlTagHandler;
    Context context;
    public AsyncLoadNetworkPic(Context context, TextView editText, String content, Html.ImageGetter imageGetter, Html.TagHandler htmlTagHandler){
        this.context=context;
        this.editText = editText;
        this.content = content;
        this.imageGetter = imageGetter;
        this.htmlTagHandler = htmlTagHandler;
    }
    @Override
    protected Boolean doInBackground(String... params) {
        // 加载网络图片
        return loadNetPic(params);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        // 当执行完成后再次为其设置一次
        if (result) {
            if(editText instanceof BaseRichEditText){
            }
            editText.setText(Html.fromHtml(content, imageGetter, htmlTagHandler), TextView.BufferType.SPANNABLE);
            if(editText instanceof BaseRichEditText){
                ((BaseRichEditText)editText).setSelection(editText.length());
            }
            //setSpanClickable();
        }
    }
    /**加载网络图片*/
    private boolean loadNetPic(String... params) {
        boolean is=false;
        String path = params[0];

        //File file = new File(Environment.getExternalStorageDirectory(), params[1]);
        File file = new File(context.getExternalFilesDir(null),params[1]);


        InputStream in = null;

        OutputStream out = null;

        try {
            URL url = new URL(path);

            HttpURLConnection connUrl = (HttpURLConnection) url.openConnection();

            connUrl.setConnectTimeout(5000);

            connUrl.setRequestMethod("GET");

            if(connUrl.getResponseCode() == 200) {

                in = connUrl.getInputStream();

                out = new BufferedOutputStream(new FileOutputStream(file));

                byte[] buffer = new byte[1024];

                int len;
                while((len = in.read(buffer))!= -1){
                    out.write(buffer, 0, len);
                }
                is=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(is){
            try{
                content = content.replace(params[0],file.getCanonicalPath());
            }catch (Exception e){}
        }
        return is;
    }
}
