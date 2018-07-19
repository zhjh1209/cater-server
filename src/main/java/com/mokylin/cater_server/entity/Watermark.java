package com.mokylin.cater_server.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Watermark {
    private int timestamp;
    private String appid;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
