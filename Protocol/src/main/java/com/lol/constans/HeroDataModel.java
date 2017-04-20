package com.lol.constans;

public class HeroDataModel {
    public int code; //策划定义的唯一编号
    public String name; //英雄名称
    public int atkBase; //初始(基础)攻击力
    public int defBase; //初始防御
    public int hpBase; //初始血量
    public int mpBase; //初始蓝
    public int atkArr; //攻击成长、
    public int defArr; //防御成长
    public int hpArr; //血量成长
    public int mpArr; //蓝成长
    public float speed; //移动速度
    public float aSpeed; //攻击速度
    public float range; //攻击距离
    public float eyeRange; //视野范围
    public int[] skills; //拥有技能

    public HeroDataModel() {
    }

    public HeroDataModel(int code, String name, int atkBase, int defBase, int hpBase, int mpBase, int atkArr,
                         int defArr, int hpArr, int mpArr, float speed, float aSpeed, float range, float eyeRange,
                         int[] skills) {
        this.code = code;
        this.name = name;
        this.atkBase = atkBase;
        this.defBase = defBase;
        this.hpBase = hpBase;
        this.mpBase = mpBase;
        this.atkArr = atkArr;
        this.defArr = defArr;
        this.hpArr = hpArr;
        this.mpArr = mpArr;
        this.speed = speed;
        this.aSpeed = aSpeed;
        this.range = range;
        this.eyeRange = eyeRange;
        this.skills = skills;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAtkBase() {
        return atkBase;
    }

    public void setAtkBase(int atkBase) {
        this.atkBase = atkBase;
    }

    public int getDefBase() {
        return defBase;
    }

    public void setDefBase(int defBase) {
        this.defBase = defBase;
    }

    public int getHpBase() {
        return hpBase;
    }

    public void setHpBase(int hpBase) {
        this.hpBase = hpBase;
    }

    public int getMpBase() {
        return mpBase;
    }

    public void setMpBase(int mpBase) {
        this.mpBase = mpBase;
    }

    public int getAtkArr() {
        return atkArr;
    }

    public void setAtkArr(int atkArr) {
        this.atkArr = atkArr;
    }

    public int getDefArr() {
        return defArr;
    }

    public void setDefArr(int defArr) {
        this.defArr = defArr;
    }

    public int getHpArr() {
        return hpArr;
    }

    public void setHpArr(int hpArr) {
        this.hpArr = hpArr;
    }

    public int getMpArr() {
        return mpArr;
    }

    public void setMpArr(int mpArr) {
        this.mpArr = mpArr;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getaSpeed() {
        return aSpeed;
    }

    public void setaSpeed(float aSpeed) {
        this.aSpeed = aSpeed;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getEyeRange() {
        return eyeRange;
    }

    public void setEyeRange(float eyeRange) {
        this.eyeRange = eyeRange;
    }

    public int[] getSkills() {
        return skills;
    }

    public void setSkills(int[] skills) {
        this.skills = skills;
    }
}