package com.lol.dto.fight;

public class FightBuildModel extends AbsFightModel {
    private boolean born; //是否重生
    private int bornTime; //重生时间
    private boolean initiative; //是否攻击
    private boolean infrared; //红外线 （是否反隐）

    public FightBuildModel() {
    }

    public FightBuildModel(int id, int code, int hp, int hpMax, int atk, int def, boolean reborn, int rebornTime,
                           boolean initiative, boolean infrared, String name) {
        setId(id);
        setCode(code);
        setHp(hp);
        setMaxHp(hpMax);
        setAtk(atk);
        setDef(def);
        this.born = reborn;
        this.bornTime = rebornTime;
        this.initiative = initiative;
        this.infrared = infrared;
        setName(name);
    }

    public boolean isBorn() {
        return born;
    }

    public void setBorn(boolean born) {
        this.born = born;
    }

    public int getBornTime() {
        return bornTime;
    }

    public void setBornTime(int bornTime) {
        this.bornTime = bornTime;
    }

    public boolean isInitiative() {
        return initiative;
    }

    public void setInitiative(boolean initiative) {
        this.initiative = initiative;
    }

    public boolean isInfrared() {
        return infrared;
    }

    public void setInfrared(boolean infrared) {
        this.infrared = infrared;
    }
}