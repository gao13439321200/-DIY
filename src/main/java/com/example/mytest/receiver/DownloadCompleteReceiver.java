package com.example.mytest.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by gaopeng on 2016/11/15.
 *
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {
    private DownloadManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            //通过downloadId去查询下载的文件名
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d("UpdateActivity", "广播downloadId:" + downloadId);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor myDownload = manager.query(query);
            if (myDownload.moveToFirst()) {
                int fileNameIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                String fileName = myDownload.getString(fileNameIdx);
                installAPK(fileName, context);
            }
        }
    }

    //安装APK
    private void installAPK(String filePath, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//广播里面操作需要加上这句，存在于一个独立的栈里
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
