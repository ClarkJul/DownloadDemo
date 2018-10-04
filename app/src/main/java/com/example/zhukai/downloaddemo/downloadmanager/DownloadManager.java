package com.example.zhukai.downloaddemo.downloadmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.zhukai.downloaddemo.DownloadInfo;
import com.example.zhukai.downloaddemo.AppInfo;
import com.example.zhukai.downloaddemo.message.DownloadInfoObserver;
import com.example.zhukai.downloaddemo.message.DownloadInfoPublisher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DownloadManager implements DownloadInfoPublisher {
    public static final int STATE_NONE = 0;
    public static final int STATE_WAITING = 1;
    public static final int STATE_DOWN_LOADING = 2;
    public static final int STATE_PAUSED = 3;
    public static final int STATE_DOWN_LOADED = 4;
    public static final int STATE_ERROR = 5;

    public static DownloadManager instance = null;

    /**
     * 单例
     *
     * @return
     */
    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }

        return instance;
    }

    public static Map<Integer, DownloadInfo> downloadInfoMap = new HashMap<>();
    public static Map<Integer, DownloadTask> downloadTaskMap = new HashMap<>();

    public static List<DownloadInfoObserver> downloadInfoObservers = new ArrayList<>();

    /**
     * 下载数据的线程
     */
    public class DownloadTask implements Runnable {
        DownloadInfo downloadInfo;
        private FileOutputStream fos;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            Log.i("Download", downloadInfo.downloadUrl);
            downloadInfo.currentState = STATE_DOWN_LOADING;
            notifyObserversOnStateChange(downloadInfo);

            URL url = null;
            try {
                url = new URL(downloadInfo.downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    InputStream inputStream = connection.getInputStream();

                    File file = new File(downloadInfo.saveUrl);
                    byte[] buffer = new byte[1024];
                    fos = new FileOutputStream(file);

                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();

                        downloadInfo.currentSize += len;
                        notifyObserversOnProgressChange(downloadInfo);

                    }

                    downloadInfo.currentState = STATE_DOWN_LOADED;
                    notifyObserversOnStateChange(downloadInfo);

                } else {
                    Log.i("Download", "请求失败");
                }
            } catch (Exception e) {
                e.printStackTrace();

                downloadInfo.currentState = STATE_ERROR;
                notifyObserversOnStateChange(downloadInfo);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    public void addObserver(DownloadInfoObserver downloadInfoObserver) {
        downloadInfoObservers.add(downloadInfoObserver);
    }

    @Override
    public void removeObserver(DownloadInfoObserver downloadInfoObserver) {
        downloadInfoObservers.remove(downloadInfoObserver);
    }

    @Override
    public void notifyObserversOnProgressChange(DownloadInfo downloadInfo) {
        for (DownloadInfoObserver downloadInfoObserver : downloadInfoObservers) {
            downloadInfoObserver.notifyDownloadProgress(downloadInfo);
        }
    }

    @Override
    public void notifyObserversOnStateChange(DownloadInfo downloadInfo) {
        for (DownloadInfoObserver downloadInfoObserver : downloadInfoObservers) {
            downloadInfoObserver.notifyDownloadState(downloadInfo);
        }
    }

    /**
     * 下载的方法
     *
     * @param appInfo
     */
    public void download(AppInfo appInfo) {
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.id);

        if (downloadInfo == null) {
            downloadInfo = DownloadInfo.clone(appInfo);
            downloadInfoMap.put(appInfo.id, downloadInfo);
        }


        if (downloadInfo.currentState == STATE_NONE || downloadInfo.currentState == STATE_PAUSED || downloadInfo.currentState == STATE_ERROR) {
            downloadInfo.currentState = STATE_WAITING;

            DownloadTask downloadTask = new DownloadTask(downloadInfo);
            downloadTaskMap.put(downloadInfo.appId, downloadTask);
            ThreadManager.getDownloadPool().execute(downloadTask);

            notifyObserversOnStateChange(downloadInfo);
        }
    }

    /**
     * 暂停的方法
     *
     * @param appInfo
     */
    public void stop(AppInfo appInfo) {

        DownloadInfo downloadInfo = downloadInfoMap.remove(appInfo.id);
        if (downloadInfo != null) {
            downloadInfo.currentState = STATE_PAUSED;
            notifyObserversOnStateChange(downloadInfo);
        }

        DownloadTask downloadTask = downloadTaskMap.remove(appInfo.id);
        if (downloadTask != null) {
            ThreadManager.getDownloadPool().cancel(downloadTask);
        }


    }

    /**
     * 下载完安装
     *
     * @param appInfo
     * @param context
     */
    public void install(AppInfo appInfo, Context context) {

        DownloadInfo downloadInfo = DownloadInfo.clone(appInfo);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + downloadInfo.saveUrl), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
