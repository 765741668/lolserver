package com.lol.dto;

public class PlayerDTO {
    /**
     * 玩家ID 唯一主键
     */
    private int id;
    /**
     * 玩家昵称
     */
    private String nickName;
    /**
     * 玩家等级
     */
    private int level;
    /**
     * 玩家经验
     */
    private int exp;
    /**
     * 胜利场次
     */
    private int winCount;
    /**
     * 失败场次
     */
    private int loseCount;
    /**
     * 逃跑场次
     */
    private int ranCount;
    /**
     * 玩家拥有的英雄列表
     */
    private Integer[] heroList;

    public PlayerDTO() {
    }

    public PlayerDTO(String nickName, int id, int level, int win, int lose, int ran, Integer[] heroList) {
        this.id = id;
        this.nickName = nickName;
        this.winCount = win;
        this.loseCount = lose;
        this.ranCount = ran;
        this.level = level;
        this.heroList = heroList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
    }

    public int getRanCount() {
        return ranCount;
    }

    public void setRanCount(int ranCount) {
        this.ranCount = ranCount;
    }

    public Integer[] getHeroList() {
        return heroList;
    }

    public void setHeroList(Integer[] heroList) {
        this.heroList = heroList;
    }
}