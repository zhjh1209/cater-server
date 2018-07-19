package com.mokylin.cater_server.socket.message;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.game.User;
import com.mokylin.cater_server.service.WorldService;
import com.mokylin.cater_server.socket.Worker;
import com.mokylin.cater_server.socket.handler.MsgHandler;
import com.mokylin.cater_server.socket.handler.MsgHandlerManager;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageWorker implements Worker<User> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Channel channel;
    private final WorldService worldService;
    private final MsgHandlerManager msgHandlerManager;
    private User user;


    public MessageWorker(Channel channel, WorldService worldService,
            MsgHandlerManager msgHandlerManager) {
        this.channel = channel;
        this.worldService = worldService;
        this.msgHandlerManager = msgHandlerManager;
    }

    @Override
    public void setHolder(User user) {
        this.user = user;
    }

    @Override
    public User getHolder() {
        return user;
    }

    public void onMessage(MessagePacket messagePacket) {
        // 第心跳协议
        if ("ping".equals(messagePacket.getType())) {
            channel.write(PongPacket.INSTANCE);
            return;
        }
        if (!"message".equals(messagePacket.getType())) {
            logger.error("不是正常的通讯协议：dataType=" + messagePacket.getType());
            return;
        }
        MessageEntity json = messagePacket.getMessage();
        if (json == null) {
            return;
        }
        String type = json.getType();
        JSONObject data;
        if (json.getContent() instanceof JSONObject) {
            data = (JSONObject) json.getContent();
        } else {
            data = new JSONObject();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("received msg: type = " + type + ", content = " + data);
        }
        if (!SocketMessages.C2S_LOGIN.equals(type) && !SocketMessages.C2S_SYNC_TIME.equals(type) &&
                user == null) {
            logger.error("未登录就发送其他协议：type =" + type);
            return;
        }
        MsgHandler handler = msgHandlerManager.getHandler(type);
        if (handler == null) {
            logger.error("未知的协议号：type=" + type);
            return;
        }
        if (user == null) {
            worldService.getExecutorService().getCommon().execute(() -> {
                handlerMessage(data, type, handler);
            });
        } else {
            worldService.getExecutorService().get(user.getId().hashCode()).execute(() -> {
                handlerMessage(data, type, handler);
            });
        }

    }

    private void handlerMessage(JSONObject data, String type, MsgHandler handler) {
        MessageEntity object = null;
        try {
            object = handler.handler(this, data);
        } catch (Throwable e) {
            logger.error("handler msg error: type=" + type, e);
        }
        if (object != null) {
            sendMessage(object);
        }
    }

    public void onDisconnect() {
        if (user != null) {
            user.onOffline();
            worldService.removeUser(user.getId());
            user.setWorker(null);
            user = null;
        }
    }

    public void onConnected() {
        logger.info("channel connected: channel = " + channel);
    }

    public void sendMessage(MessageEntity messageEntity) {
        this.channel.write(new MessagePacket("message", messageEntity));
    }

    public WorldService getWorldService() {
        return worldService;
    }

}
