package com.example.zhukai.downloaddemo;

import java.util.List;

public class AppInfo {
    public int id;
    public String pkg;
    public String icon;
    public String name;
    public String sdesc;
    public String fileurl;
    public int isfree;
    public int vercode;
    public String vername;
    public String size;
    public String updatetime;
    public long downloads;
    public int type;
    public int islocal;
    public String src;
    public List<Tag> tags;
    /**
     * 定义下载的状态及下载进度，Json数据中没有这两项数据，不影响Json解析
     * currentState，progress解析时返回为零
     */
    public int currentState;
    public int progress;

    class Tag {
        public String name;
        public String color;
        public String bgcolor;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "id=" + id +
                ", pkg='" + pkg + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", sdesc='" + sdesc + '\'' +
                ", fileurl='" + fileurl + '\'' +
                ", isfree=" + isfree +
                ", vercode=" + vercode +
                ", vername='" + vername + '\'' +
                ", size='" + size + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", downloads=" + downloads +
                ", type=" + type +
                ", islocal=" + islocal +
                ", src='" + src + '\'' +
                ", tags=" + tags +
                ", currentState=" + currentState +
                ", progress=" + progress +
                '}';
    }
}
