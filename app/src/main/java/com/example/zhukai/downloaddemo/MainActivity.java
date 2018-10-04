package com.example.zhukai.downloaddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.zhukai.downloaddemo.downloadmanager.DownloadManager;
import com.example.zhukai.downloaddemo.message.DownloadInfoObserver;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadInfoObserver {

    private List<AppInfo> appInfos=new ArrayList<>();
    private AppInfoAdapter appInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initListeners();
        initViews();
    }

    public void initViews() {
        Log.i("AppInfo", appInfos.toString());
        ListView appList = findViewById(R.id.main_list);
        appInfoAdapter = new AppInfoAdapter(appInfos, MainActivity.this);
        appList.setAdapter(appInfoAdapter);

        DownloadManager.getInstance().addObserver(this);
    }

    public void initDatas() {
        final String appsData = LocalJsonToString.getJson("appInfo.json", MainActivity.this);//使用工具类将Json数据转换成字符串
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 解析本地Json数据，用AppInfo[]数组存储，再将数组转换为List
                 */
                AppInfo[] appsArr = new Gson().fromJson(appsData, AppInfo[].class);
                appInfos = Arrays.asList(appsArr);
            }
        });
    }

    public void initListeners() {

    }

    @Override
    public void notifyDownloadProgress(DownloadInfo downloadInfo) {
        for (AppInfo appInfo : appInfos) {
            if (appInfo.id == downloadInfo.appId) {
                appInfo.progress = (int) (downloadInfo.getProgress()*100);
//                Log.i("Totalsize",appInfo.progress+"progress");
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appInfoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void notifyDownloadState(DownloadInfo downloadInfo) {
        for (AppInfo appInfo : appInfos) {
            if (appInfo.id == downloadInfo.appId) {
                appInfo.currentState = downloadInfo.currentState;
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appInfoAdapter.notifyDataSetChanged();
            }
        });
    }
}
