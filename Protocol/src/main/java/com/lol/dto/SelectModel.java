package com.lol.dto;

public class SelectModel {
    /**
     * 用户ID
     */
    private int playerId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 所选英雄
     */
    private int hero;
    /**
     * 是否进入
     */
    private boolean enter;
    /**
     * 是否已准备
     */
    private boolean ready;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getHero() {
        return hero;
    }

    public void setHero(int hero) {
        this.hero = hero;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}