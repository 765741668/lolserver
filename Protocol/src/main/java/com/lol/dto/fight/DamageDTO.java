package com.lol.dto.fight;

public class DamageDTO {
    private int userId;
    private int skill;
    private int[][] target;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int[][] getTarget() {
        return target;
    }

    public void setTarget(int[][] target) {
        this.target = target;
    }
}