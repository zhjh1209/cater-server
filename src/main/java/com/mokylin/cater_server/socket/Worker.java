package com.mokylin.cater_server.socket;

public interface Worker<T> extends MessageProcessor {
    void onDisconnect();

    void onConnected();

    void setHolder(T t);

    T getHolder();
}
