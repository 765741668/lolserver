package com.lol.service.bean;/**
 * Description : 
 * Created by YangZH on 2017/4/17
 *  19:54
 */

/**
 * Description :
 * Created by YangZH on 2017/4/17
 * 19:54
 */

public class PlayerBean {
    private int id;
    private String name;
    private int level;
    private int exp;
    private int winCount;
    private int loseCount;
    private int ranCount;
    private int acountid;
    private Integer[] herolist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getAcountid() {
        return acountid;
    }

    public void setAcountid(int acountid) {
        this.acountid = acountid;
    }

    public Integer[] getHerolist() {
        return herolist;
    }

    public void setHerolist(Integer[] herolist) {
        this.herolist = herolist;
    }

}
