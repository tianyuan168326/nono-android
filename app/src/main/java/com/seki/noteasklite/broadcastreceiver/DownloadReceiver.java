package com.seki.noteasklite.broadcastreceiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;

/**
 *
 * @author 林楷鹏
 * @description 下载完成广播接收器
 * @create 2014-7-24下午3:28:34
 *
 */
public  class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
            DownloadManager.Query query = new DownloadManager.Query();
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            query.setFilterById(id);
            Cursor c = manager.query(query);
            if(c.moveToFirst()) {
                //获取文件下载路径
                String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                if(filename != null){
                    File file  = new File(filename);
                    Intent anotherIntent = new Intent();
                    anotherIntent.addFlags(anotherIntent.FLAG_ACTIVITY_NEW_TASK);
                    anotherIntent.setAction(android.content.Intent.ACTION_VIEW);
                    anotherIntent.setDataAndType(Uri.fromFile(file),
                            "application/vnd.android.package-archive");
                    context.startActivity(anotherIntent);
                }
            }
        }else if(DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())){
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //点击通知栏取消下载
            manager.remove(ids);
            //ShowToastUtil.showShortToast(context, "已经取消下载");
        }

    }

}