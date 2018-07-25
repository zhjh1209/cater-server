package com.mokylin.cater_server.game;

import com.mokylin.cater_server.dao.UserDataRepository;
import com.mokylin.cater_server.entity.UserData;
import com.mokylin.cater_server.service.WorldService;
import com.mokylin.cater_server.socket.message.MessageWorker;

import java.util.concurrent.atomic.AtomicBoolean;

public class User {
    private UserData userData;
    private AtomicBoolean saveMark;
    private UserDataRepository userDataRepository;
    private WorldService worldService;
    private MessageWorker worker;
    private AtomicBoolean state;
    private long offlineTime;

    public User(UserData userData, UserDataRepository userDataRepository,
            WorldService worldService) {
        this.userData = userData;
        this.saveMark = new AtomicBoolean(false);
        this.userDataRepository = userDataRepository;
        this.worldService = worldService;
        this.state = new AtomicBoolean(true);
    }

    public void onOffline() {
        state.compareAndSet(true, false); // 下线标记
        offlineTime = System.currentTimeMillis();
    }

    public String getId() {
        return userData.getOpenId();
    }

    public void setWorker(MessageWorker worker) {
        this.worker = worker;
    }

    public UserData getUserData() {
        return userData;
    }

    public void save() {
        if (this.saveMark.compareAndSet(false, true)) {
            try {
                userDataRepository.save(userData);
            } finally {
                this.saveMark.compareAndSet(true, false);
            }
        }
    }

    public MessageWorker getWorker() {
        return worker;
    }

    public void online() {
        state.compareAndSet(false, true); // 上线标记
    }

    public boolean isOnline() {
        return state.get();
    }

    public long getOfflineTime() {
        return offlineTime;
    }
}
