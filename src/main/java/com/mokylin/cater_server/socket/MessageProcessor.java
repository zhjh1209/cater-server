package com.mokylin.cater_server.socket;

import com.mokylin.cater_server.socket.message.MessagePacket;

public interface MessageProcessor {
    void onMessage(MessagePacket var1);
}
