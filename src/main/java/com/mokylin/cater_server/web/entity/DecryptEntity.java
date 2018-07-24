package com.mokylin.cater_server.web.entity;

public class DecryptEntity {
    private String iv;
    private String ed;

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEd() {
        return ed;
    }

    public void setEd(String ed) {
        this.ed = ed;
    }
}
