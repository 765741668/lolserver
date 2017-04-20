package com.lol.dto;

public class SelectRoomDTO {
    private String roomName;
    private SelectModel[] teamOne;
    private SelectModel[] teamTwo;

    public final int getTeam(int uid) {
        for (SelectModel item : teamOne) {
            if (item.getPlayerId() == uid) {
                return 1;
            }
        }
        for (SelectModel item : teamTwo) {
            if (item.getPlayerId() == uid) {
                return 2;
            }
        }
        return -1;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public SelectModel[] getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(SelectModel[] teamOne) {
        this.teamOne = teamOne;
    }

    public SelectModel[] getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(SelectModel[] teamTwo) {
        this.teamTwo = teamTwo;
    }
}