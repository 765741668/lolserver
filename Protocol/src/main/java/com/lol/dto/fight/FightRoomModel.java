package com.lol.dto.fight;

public class FightRoomModel {
    private AbsFightModel[] teamOne;
    private AbsFightModel[] teamTwo;

    public AbsFightModel[] getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(AbsFightModel[] teamOne) {
        this.teamOne = teamOne;
    }

    public AbsFightModel[] getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(AbsFightModel[] teamTwo) {
        this.teamTwo = teamTwo;
    }
}