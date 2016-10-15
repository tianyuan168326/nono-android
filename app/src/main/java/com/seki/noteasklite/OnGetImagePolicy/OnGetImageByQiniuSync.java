package com.seki.noteasklite.OnGetImagePolicy;

import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.qiniu.android.utils.AsyncRun;
import com.qiniu.android.utils.UrlSafeBase64;
import com.seki.noteasklite.Config.QiniuLabConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by yuan-tian01 on 2016/2/29.
 */
public class OnGetImageByQiniuSync extends IOnGetImagePolicy {
    private UploadManager uploadManager;

    public OnGetImageByQiniuSync(OnRealPath onRealPath) {
        super(onRealPath);
    }



    @Override
    public void getRealImagePath(String path, int src_pathType) {
        switch(src_pathType){
            case IOnGetImagePolicy.SRC_TYPE_INTERNET:
                throw new IllegalArgumentException("the src on the internet,we have not implement ");
            case IOnGetImagePolicy.SRC_TYPE_LOCAL:
                this.getRealImagePath(path);
            default:
                throw new IllegalArgumentException("ERROR PathType! ");
        }
    }

    @Override
    public void getRealImagePath(final String srcPath) {
        if(TextUtils.isEmpty(srcPath)){
            onRealPath.realPath(srcPath);
            return;
        }
        final String path = this.preImageProcess(srcPath);
        //从业务服务器获取上传凭证
                final SyncHttpClient httpClient = new SyncHttpClient();
                httpClient.get(QiniuLabConfig.makeUrl(
                        QiniuLabConfig.REMOTE_SERVICE_SERVER,
                        QiniuLabConfig.RESUMABLE_UPLOAD_WITHOUT_KEY_PATH), null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        try {
                            String uploadToken = response.getString("uptoken");
                            upload(uploadToken,path,onRealPath);
                        } catch (JSONException e1) {
                            AsyncRun.run(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    }
                });
    }

    private void upload(String uploadToken, String path, final OnRealPath onRealPath) {
        if (this.uploadManager == null) {
            this.uploadManager = new UploadManager();
        }
        File uploadFile = new File(path);
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                    }
                }, null);
        final long startTime = System.currentTimeMillis();
        final long fileLength = uploadFile.length();

        //因为是无key上传，所以key参数指定为null
        this.uploadManager.put(uploadFile, null, uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo respInfo,
                                         JSONObject jsonData) {

                        long lastMillis = System.currentTimeMillis()
                                - startTime;
                        if (respInfo.isOK()) {
                            try {
                                String fileKey = jsonData.getString("key");
                                String fileHash = jsonData.getString("hash");
                                onRealPath.realPath("http://"+QiniuLabConfig.Qiniu_Public_Bucket_Domain+"/"
                                        +fileKey);
                            } catch (JSONException e) {
                                onRealPath.realPath(null);
                            }
                        }
                    }

                }, uploadOptions);
    }
}
