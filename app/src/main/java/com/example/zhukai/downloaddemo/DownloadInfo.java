package com.example.zhukai.downloaddemo;

import android.os.Environment;

public class DownloadInfo {
    public int appId;
    public String appName;
    public int currentSize;
    public int totalSize;
    public int currentState;
    public String downloadUrl;
    public String saveUrl;

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "appId=" + appId +
                ", appName='" + appName + '\'' +
                ", currentSize=" + currentSize +
                ", totalSize=" + totalSize +
                ", currentState=" + currentState +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", saveUrl='" + saveUrl + '\'' +
                '}';
    }


    public static DownloadInfo clone(AppInfo appInfo) {
        DownloadInfo downloadInfo = new DownloadInfo();

        downloadInfo.appId = appInfo.id;
        downloadInfo.appName = appInfo.name;
        downloadInfo.currentSize = 0;
        downloadInfo.totalSize = Integer.parseInt(appInfo.size);
        downloadInfo.currentSize = 0;
        downloadInfo.downloadUrl = appInfo.fileurl;
        downloadInfo.saveUrl = Environment.getExternalStorageDirectory() + "/" + appInfo.name + ".apk";

        return downloadInfo;
    }

    public float getProgress() {
        if (totalSize == 0) {
            return 0;
        }
//        Log.i("Totalsize",totalSize+"");
//        Log.i("Totalsize",currentSize+"");
        return (currentSize + 0.0f) / totalSize;
    }
}
