package com.mokylin.cater_server.web.result;

import com.mokylin.cater_server.entity.UserInfo;

public class ResultRank {
    private int rank;
    private UserInfo userInfo;
    private int score;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
