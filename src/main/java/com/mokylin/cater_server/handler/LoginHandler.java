package com.mokylin.cater_server.handler;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.dao.SessionInfoRepository;
import com.mokylin.cater_server.dao.UserDataRepository;
import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.entity.UserData;
import com.mokylin.cater_server.game.User;
import com.mokylin.cater_server.service.WorldService;
import com.mokylin.cater_server.socket.handler.Command;
import com.mokylin.cater_server.socket.handler.MsgHandler;
import com.mokylin.cater_server.socket.message.MessageEntity;
import com.mokylin.cater_server.socket.message.MessageWorker;
import com.mokylin.cater_server.socket.message.SocketMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
@Command(SocketMessages.C2S_LOGIN)
public class LoginHandler implements MsgHandler<User, MessageWorker> {
    @Autowired
    private SessionInfoRepository sessionInfoRepository;
    @Autowired
    private WorldService worldService;
    @Autowired
    private UserDataRepository userDataRepository;

    @Override
    public MessageEntity handler(MessageWorker worker, JSONObject data) throws Exception {
        String skey = data.getString("skey");
        Optional<SessionInfo> sessionInfoOpt = sessionInfoRepository.findBySkey(skey);
        if (!sessionInfoOpt.isPresent()) {
            JSONObject json = new JSONObject();
            json.put("msg", "登录失效");
            return new MessageEntity(SocketMessages.S2C_LOGIN_FAIL, json);
        }
        SessionInfo sessionInfo = sessionInfoOpt.get();
        User user = worldService.getUser(sessionInfo.getOpenId());
        if (user != null) {
            MessageWorker oldWorker = user.getWorker();
            if (oldWorker != null) {
                oldWorker.sendMessage(new MessageEntity(SocketMessages.S2C_LOGIN_ELSE, ""));
                oldWorker.closeChannel();
            }
        } else {
            Optional<UserData> userDataOpt = userDataRepository.findById(sessionInfo.getOpenId());
            if (userDataOpt.isPresent()) {
                UserData userData = userDataOpt.get();
                user = new User(userData, userDataRepository, worldService);
            } else {
                UserData userData = new UserData();
                userData.setOpenId(sessionInfo.getOpenId());
                userData.setScore(0);
                userData.setUserInfo(sessionInfo.getUserInfo());
                userData.setKvData(new HashMap<>());
                user = new User(userData, userDataRepository, worldService);
            }
            worldService.addUser(user);
        }
        user.getUserData().setUserInfo(sessionInfo.getUserInfo()); // 更新玩家信息
        user.setWorker(worker);
        worker.setHolder(user);
        user.online();
        JSONObject json = new JSONObject();
        json.put("score", user.getUserData().getScore());
        return new MessageEntity(SocketMessages.S2C_LOGIN_SUCCESS, json);
    }
}
