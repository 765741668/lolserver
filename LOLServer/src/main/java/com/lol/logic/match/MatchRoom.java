package com.lol.logic.match;

/**
 * 战斗匹配房间模型
 */
public class MatchRoom {
    public int id; //房间唯一ID
    public int teamMax = 1; //每支队伍需要匹配到的人数
    public java.util.ArrayList<Integer> teamOne = new java.util.ArrayList<>(); //队伍一 人员ID
    public java.util.ArrayList<Integer> teamTwo = new java.util.ArrayList<>(); //队伍二 人员ID
}