package com.lol.dto.fight;

public class AttackDTO {
    private int userId; //攻击者ID
    private int targetId; //被攻击者ID

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
}