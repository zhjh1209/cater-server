package com.mokylin.cater_server.socket;

import java.io.Closeable;

public interface Server extends Closeable {
    int start(); // 返还启动端口
}
