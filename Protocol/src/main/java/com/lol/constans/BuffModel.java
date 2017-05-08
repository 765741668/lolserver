package com.lol.constans;

public class BuffModel {
    private int code; //策划定义的唯一编号
    private String name; //BUFF名称
    private int time; //持续时间
    private int playerId; //拥有buff的玩家id
    private boolean dieMiss; //玩家死后是否 还拥有buff
    private boolean isSingle; //是否单人buff
    private int count; //buff 层数
    private BuffEnum buffEnum; //buff 类别

    public BuffModel() {
    }

    public BuffModel(int code, String name, int time, int playerId, boolean dieMiss, boolean isSingle, int count, BuffEnum buffEnum) {
        this.code = code;
        this.name = name;
        this.time = time;
        this.playerId = playerId;
        this.dieMiss = dieMiss;
        this.isSingle = isSingle;
        this.count = count;
        this.buffEnum = buffEnum;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isDieMiss() {
        return dieMiss;
    }

    public void setDieMiss(boolean dieMiss) {
        this.dieMiss = dieMiss;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BuffEnum getBuffEnum() {
        return buffEnum;
    }

    public void setBuffEnum(BuffEnum buffEnum) {
        this.buffEnum = buffEnum;
    }
}
