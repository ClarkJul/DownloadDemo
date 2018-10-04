package com.example.zhukai.downloaddemo.message;

import com.example.zhukai.downloaddemo.DownloadInfo;

public interface DownloadInfoPublisher {
    void addObserver(DownloadInfoObserver downloadInfoObserver);

    void removeObserver(DownloadInfoObserver downloadInfoObserver);

    void notifyObserversOnProgressChange(DownloadInfo downloadInfo);

    void notifyObserversOnStateChange(DownloadInfo downloadInfo);
}
