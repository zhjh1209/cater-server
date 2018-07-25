package com.mokylin.cater_server.service;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.dao.UserDataRepository;
import com.mokylin.cater_server.entity.UserData;
import com.mokylin.cater_server.game.User;
import com.mokylin.cater_server.util.Tuples.Tuple2;
import com.mokylin.cater_server.web.result.ResultRank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class WorldService {
    private static long RANK_CACHE_TIME = 300000; // 排行缓存5分钟
    private static long REMOVE_OFFLINE_TIME = 600000; // 十分钟没上线充世界移除
    private ThreadExecutorService executorService;
    @Autowired
    private UserDataRepository userDataRepository;

    private Map<String, User> userMap;

    @Autowired
    public WorldService(ThreadExecutorService executorService) {
        this.executorService = executorService;
        userMap = new ConcurrentHashMap<>(512);// 10-秒后开始，每5分钟保存一次
        this.executorService.getSchedule()
                .scheduleWithFixedDelay(() -> saveTask(), 10000, 300000, TimeUnit.MILLISECONDS);
    }

    public void saveTask() {
        long now = System.currentTimeMillis();
        List<User> toRemoveUser = new ArrayList<>(1024);
        for (User user : userMap.values()) {
            user.save();
            if (!user.isOnline() && user.getOfflineTime() + REMOVE_OFFLINE_TIME < now) {
                toRemoveUser.add(user);
            }
        }
        for (User user : toRemoveUser) {
            removeUser(user.getId());
        }
    }

    public void removeUser(String id) {
        userMap.remove(id);
    }

    public void addUser(User user) {
        userMap.put(user.getId(), user);
    }

    public ThreadExecutorService getExecutorService() {
        return executorService;
    }

    public User getUser(String openId) {
        return userMap.get(openId);
    }

    private volatile Tuple2<JSONObject, Long> rankCache;

    public JSONObject getWorldRank() {
        long now = System.currentTimeMillis();
        if (rankCache == null || rankCache._2() + RANK_CACHE_TIME < now) {
            synchronized (this) {
                if (rankCache == null || rankCache._2() + RANK_CACHE_TIME < now) {
                    Pageable page = PageRequest.of(0, 50, Direction.DESC, "score");
                    Page<UserData> pageInfo = userDataRepository.findAll(page);
                    List<UserData> list = pageInfo.getContent();
                    List<ResultRank> result = new ArrayList<>(list.size());
                    int index = 0;
                    for (UserData userData : list) {
                        index++;
                        ResultRank rank = new ResultRank();
                        rank.setRank(index);
                        rank.setUserInfo(userData.getUserInfo());
                        rank.setScore(userData.getScore());
                        result.add(rank);
                    }
                    JSONObject json = new JSONObject();
                    json.put("data", result);
                    rankCache = Tuple2.of(json, now);
                }
            }
        }
        return rankCache._1();
    }
}
