package com.lol.constans;

public class BuildDataModel {
    public int code; //箭塔编码
    public int hp; //箭塔血量
    public int atk; //箭塔攻击
    public int def; //箭塔防御
    public boolean initiative; //是否攻击型建筑
    public boolean infrared; //不可否认，这货字面意思是红外线，但是红外代表夜视，所以这里咱用来表示是否反隐吧
    public String name; //还是给个名字 用来区分下吧
    public boolean reborn; //是否复活
    public int rebornTime; //复活时间，单位秒

    public BuildDataModel() {
    }

    public BuildDataModel(int code, String name, int hp, int atk, int def, boolean initiative, boolean infrared, boolean reborn, int rebornTime) {
        this.code = code;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.initiative = initiative;
        this.infrared = infrared;
        this.name = name;
        this.reborn = reborn;
        this.rebornTime = rebornTime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReborn() {
        return reborn;
    }

    public void setReborn(boolean reborn) {
        this.reborn = reborn;
    }

    public int getRebornTime() {
        return rebornTime;
    }

    public void setRebornTime(int rebornTime) {
        this.rebornTime = rebornTime;
    }
}