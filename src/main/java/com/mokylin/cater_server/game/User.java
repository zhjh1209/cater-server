package com.mokylin.cater_server.game;

import com.mokylin.cater_server.entity.UserInfo;
import com.mokylin.cater_server.socket.message.MessageWorker;

public class User {
    private UserInfo userInfo;

    public void onOffline() {

    }

    public String getId() {
        return userInfo.getOpenId();
    }

    public void setWorker(MessageWorker worker) {
    }
}
