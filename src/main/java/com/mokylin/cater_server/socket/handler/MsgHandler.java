package com.mokylin.cater_server.socket.handler;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.socket.Worker;
import com.mokylin.cater_server.socket.message.MessageEntity;

import org.jboss.netty.buffer.ChannelBuffer;

public interface MsgHandler<T, U extends Worker<T>> {
    /**
     * String | ChannelBuffer
     * @param worker
     * @param data
     * @return
     * @throws Exception
     */
    MessageEntity handler(U worker, JSONObject data) throws Exception;
}
