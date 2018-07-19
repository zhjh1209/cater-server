package com.mokylin.cater_server.handler;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.game.User;
import com.mokylin.cater_server.socket.handler.Command;
import com.mokylin.cater_server.socket.handler.MsgHandler;
import com.mokylin.cater_server.socket.message.MessageEntity;
import com.mokylin.cater_server.socket.message.MessageWorker;
import com.mokylin.cater_server.socket.message.SocketMessages;

import org.springframework.stereotype.Component;

@Component
@Command(SocketMessages.C2S_SYNC_TIME)
public class SyncTimeHandler implements MsgHandler<User, MessageWorker> {

    @Override
    public MessageEntity handler(MessageWorker worker, JSONObject data) throws Exception {
        JSONObject json = new JSONObject();
        json.put("time", (int) (System.currentTimeMillis() / 1000));
        MessageEntity messageEntity = new MessageEntity(SocketMessages.S2C_TIME_RESP, json);
        return messageEntity;
    }
}
