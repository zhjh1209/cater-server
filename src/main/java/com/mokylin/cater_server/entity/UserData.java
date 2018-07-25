package com.mokylin.cater_server.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class UserData {
    @Id
    private String openId;
    private UserInfo userInfo;
    private Map<String, Object> kvData;
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Map<String, Object> getKvData() {
        return kvData;
    }

    public void setKvData(Map<String, Object> kvData) {
        this.kvData = kvData;
    }
}
