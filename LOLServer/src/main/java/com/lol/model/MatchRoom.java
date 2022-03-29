package com.lol.model;

import java.util.ArrayList;

/**
 * 战斗匹配房间模型
 */
public class MatchRoom {
    private int id; //房间唯一ID
    private int teamMax; //每支队伍需要匹配到的人数 1 3 5
    private java.util.ArrayList<Integer> teamOne = new java.util.ArrayList<>(); //队伍一 人员ID
    private java.util.ArrayList<Integer> teamTwo = new java.util.ArrayList<>(); //队伍二 人员ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamMax() {
        return teamMax;
    }

    public void setTeamMax(int teamMax) {
        this.teamMax = teamMax;
    }

    public ArrayList<Integer> getTeamOne() {
        return teamOne;
    }


    public ArrayList<Integer> getTeamTwo() {
        return teamTwo;
    }

}