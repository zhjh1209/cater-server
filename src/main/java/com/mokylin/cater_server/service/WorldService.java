package com.mokylin.cater_server.service;

import com.mokylin.cater_server.game.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorldService {
    @Autowired
    ThreadExecutorService executorService;

    public void removeUser(String id) {

    }

    public ThreadExecutorService getExecutorService() {
        return executorService;
    }

    public User getUser(String openId) {
        return null;
    }
}
