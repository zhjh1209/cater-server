package com.mokylin.cater_server.socket.message;

public class PongPacket extends MessagePacket {
    public static PongPacket INSTANCE = new PongPacket();

    private PongPacket() {
        super("pong", null);
    }

}
