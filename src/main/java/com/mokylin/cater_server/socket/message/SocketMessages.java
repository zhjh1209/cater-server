package com.mokylin.cater_server.socket.message;

public interface SocketMessages {
    String C2S_SYNC_TIME = "time";
    String S2C_TIME_RESP = "time_resp";
    String C2S_LOGIN = "login";
    String S2C_LOGIN_FAIL = "login_fail";
    String S2C_LOGIN_ELSE = "login_else";
    String S2C_LOGIN_SUCCESS = "login_success";
}
