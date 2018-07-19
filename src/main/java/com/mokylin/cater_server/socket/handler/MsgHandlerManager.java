package com.mokylin.cater_server.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MsgHandlerManager {
    @Autowired
    private ApplicationContext context;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, MsgHandler> msgHandlers;

    public MsgHandlerManager() {
        msgHandlers = new HashMap<>();
    }

    public MsgHandler getHandler(String type) {
        if (type == null) {
            return null;
        }
        return msgHandlers.get(type);
    }

    public void initMsgHandlers() {
        Map<String, MsgHandler> handlers = context.getBeansOfType(MsgHandler.class);
        for (MsgHandler msgHandler : handlers.values()) {
            Command command = msgHandler.getClass().getAnnotation(Command.class);
            if (command == null) {
                logger.error("MsgHandler {} 未标记Command！", msgHandler.getClass().getCanonicalName());
                continue;
            }
            String type = command.value();
            MsgHandler oldMsghandler = msgHandlers.get(type);
            if (oldMsghandler != null) {
                logger.error("MsgHandler 使用了相同的消息号：{} -- {}",
                        msgHandler.getClass().getCanonicalName(),
                        oldMsghandler.getClass().getCanonicalName());
                continue;
            }
            msgHandlers.put(type, msgHandler);
        }
        logger.info("MsgHandler count: {}", msgHandlers.size());
    }
}
