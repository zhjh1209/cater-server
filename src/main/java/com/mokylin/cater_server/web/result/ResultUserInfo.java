package com.mokylin.cater_server.web.result;

import com.mokylin.cater_server.entity.UserInfo;

public class ResultUserInfo {
    private String skey;
    private UserInfo userinfo;
    private int time;

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
