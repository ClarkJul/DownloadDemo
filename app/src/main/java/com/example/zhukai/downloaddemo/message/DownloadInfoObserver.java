package com.example.zhukai.downloaddemo.message;


import com.example.zhukai.downloaddemo.DownloadInfo;

public interface DownloadInfoObserver {
    void notifyDownloadProgress(DownloadInfo downloadInfo);

    void notifyDownloadState(DownloadInfo downloadInfo);
}
