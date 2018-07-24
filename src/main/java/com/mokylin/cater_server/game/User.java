package com.mokylin.cater_server.game;

import com.mokylin.cater_server.entity.UserData;
import com.mokylin.cater_server.entity.UserInfo;
import com.mokylin.cater_server.socket.message.MessageWorker;
import com.mokylin.cater_server.web.GetKvDataController;

public class User {
    private UserInfo userInfo;

    public void onOffline() {

    }

    public String getId() {
        return userInfo.getOpenId();
    }

    public void setWorker(MessageWorker worker) {
    }

    public UserData getUserData() {
        return null;
    }
}
