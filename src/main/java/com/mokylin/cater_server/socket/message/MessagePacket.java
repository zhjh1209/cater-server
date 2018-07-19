package com.mokylin.cater_server.socket.message;

public class MessagePacket {
    private final String type;
    private final MessageEntity message;

    public MessagePacket(String type, MessageEntity message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public MessageEntity getMessage() {
        return message;
    }
}
