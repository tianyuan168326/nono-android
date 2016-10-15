package com.seki.noteasklite.Util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

/**
 * Created by yuan-tian01 on 2016/3/9.
 */
public class DownloadUtils {
    public static String MINETYPE_APPLCATION = "application/vnd.android.package-archive";

    public static long DownloadApkWithProgress(Context context, String url) {

        DownloadManager downloadManager =
                (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Updating" + context.getPackageName());
        request.setMimeType(MINETYPE_APPLCATION);
        long downloadId = downloadManager.enqueue(request);
        return downloadId;
    }
}
