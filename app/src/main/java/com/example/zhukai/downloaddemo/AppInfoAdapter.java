package com.example.zhukai.downloaddemo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhukai.downloaddemo.downloadmanager.DownloadManager;

import java.util.ArrayList;
import java.util.List;

public class AppInfoAdapter extends BaseAdapter {
    List<AppInfo> appInfos=new ArrayList<>();
    Context mContext;

    public AppInfoAdapter(List<AppInfo> appInfos, Context mContext) {
        this.appInfos = appInfos;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AppInfo appInfo = (AppInfo) getItem(position);
        View view;
        Viewholder viewholder;
/*        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.home_list_item_app, parent, false);
            viewholder = new Viewholder();
            viewholder.imageView = view.findViewById(R.id.home_list_item_image);
            viewholder.nameText = view.findViewById(R.id.home_list_item_name);
            viewholder.contentText = view.findViewById(R.id.home_list_item_content);
            viewholder.descText = view.findViewById(R.id.home_list_item_description);

            //下载相关
            viewholder.downloadApp = view.findViewById(R.id.download_app);
            viewholder.downloadProgress = view.findViewById(R.id.progressBar);
            viewholder.downloadButton = view.findViewById(R.id.btn_install);

            view.setTag(viewholder);//将viewholder存储在view中
        } else {
            view = convertView;
            viewholder = (Viewholder)view.getTag();//重新获取viewholder
        }*/
        view = LayoutInflater.from(mContext).inflate(R.layout.home_list_item_app, parent, false);
        viewholder = new Viewholder();
        viewholder.imageView = view.findViewById(R.id.home_list_item_image);
        viewholder.nameText = view.findViewById(R.id.home_list_item_name);
        viewholder.contentText = view.findViewById(R.id.home_list_item_content);
        viewholder.descText = view.findViewById(R.id.home_list_item_description);

        //下载相关
        viewholder.downloadApp = view.findViewById(R.id.download_app);
        viewholder.downloadProgress = view.findViewById(R.id.progressBar);
        viewholder.downloadButton = view.findViewById(R.id.btn_install);

        Glide.with(mContext).load(appInfo.icon).into(viewholder.imageView);
        viewholder.nameText.setText(appInfo.name);
        viewholder.contentText.setText("下载" + appInfo.downloads + "次   ");
        viewholder.descText.setText(appInfo.sdesc);

        //下载相关
        updateProgressBarState(viewholder.downloadProgress,appInfo.currentState,appInfo.progress,viewholder.downloadButton);
        viewholder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appInfo.currentState == DownloadManager.STATE_NONE ||appInfo.currentState == DownloadManager.STATE_PAUSED || appInfo.currentState == DownloadManager.STATE_ERROR) {
                    DownloadManager.getInstance().download(appInfo);
                } else if (appInfo.currentState == DownloadManager.STATE_DOWN_LOADING) {
                    DownloadManager.getInstance().stop(appInfo);
                } else if (appInfo.currentState == DownloadManager.STATE_DOWN_LOADED) {
                    DownloadManager.getInstance().install(appInfo, mContext);
                }
            }
        });
        return view;
    }

    static class Viewholder {
        public ImageView imageView;
        public TextView nameText;
        public TextView contentText;
        public TextView descText;

        //下载相关的控件
        public FrameLayout downloadApp;
        public ProgressBar downloadProgress;
        public Button downloadButton;
    }

    public void updateProgressBarState(ProgressBar bar, int state, int progress, Button installButton) {
        if (state == DownloadManager.STATE_NONE) {

        } else if (state == DownloadManager.STATE_WAITING) {
            //等待状态：超过三个下载任务时，其他任务等待下载
            //显示“等待”，字体颜色为黑色
            installButton.setText("等待");
            installButton.setTextColor(installButton.getResources().getColor(R.color.black));
        } else if (state == DownloadManager.STATE_DOWN_LOADING) {
            bar.setProgress(progress);
            installButton.setTextColor(installButton.getResources().getColor(R.color.black));
            installButton.setText(progress+"%");
        } else if (state ==DownloadManager.STATE_PAUSED) {
            installButton.setText("继续");
        } else if (state == DownloadManager.STATE_DOWN_LOADED) {
            installButton.setText("安装");
        } else if (state == DownloadManager.STATE_ERROR) {
            installButton.setText("下载");
            installButton.setTextColor(installButton.getResources().getColor(R.color.black));
        }
    }
}
