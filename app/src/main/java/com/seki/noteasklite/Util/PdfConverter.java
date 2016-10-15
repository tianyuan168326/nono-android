package com.seki.noteasklite.Util;

import android.os.Handler;
import android.os.Looper;

import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.RetrofitHelper.RequestBody.RetrofitWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yuan-tian01 on 2016/4/20.
 */
public class PdfConverter {
    public static interface OnGetPdfListener{
        public void onGetPdfFile(String path);
        public void prepareGetPdfFile();
    }
    public static void convert(final String title, String srcContent, final OnGetPdfListener l){
        l.prepareGetPdfFile();
        OkHttpClient client = new OkHttpClient();
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("src_content", srcContent)
                .build();
        Request request = new Request.Builder()
                .url("http://2.diandianapp.sinaapp.com/Tools/Html2Pdf/Html2PdfConverter.php")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                l.onGetPdfFile(null);
                            }
                        });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //将返回结果转化为流，并写入文件
                int len;
                byte[] buf = new byte[2048];
                InputStream inputStream = response.body().byteStream();
                //可以在这里自定义路径
                final File file1 = new File(NONoConfig.getNONoPdfDir(),title+".pdf");
                FileOutputStream fileOutputStream = new FileOutputStream(file1);

                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                l.onGetPdfFile(file1.getAbsolutePath());
                            }
                        });

            }
        });
    }
}
