package com.mokylin.cater_server.handler;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.dao.SessionInfoRepository;
import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.game.User;
import com.mokylin.cater_server.service.WorldService;
import com.mokylin.cater_server.socket.handler.Command;
import com.mokylin.cater_server.socket.handler.MsgHandler;
import com.mokylin.cater_server.socket.message.MessageEntity;
import com.mokylin.cater_server.socket.message.MessageWorker;
import com.mokylin.cater_server.socket.message.SocketMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Command(SocketMessages.C2S_LOGIN)
public class LoginHandler implements MsgHandler<User, MessageWorker> {
    @Autowired
    private SessionInfoRepository sessionInfoRepository;
    @Autowired
    private WorldService worldService;

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
        worldService.getUser(sessionInfo.getOpenId());
        JSONObject result = new JSONObject();
        result.put("score", 10000);
        return new MessageEntity("login_success", result);
    }
}
