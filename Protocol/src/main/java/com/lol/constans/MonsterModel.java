package com.lol.constans;

public class MonsterModel {
    private int code; //策划定义的唯一编号
    private String name; //怪物名称
    private int atkBase; //初始(基础)攻击力
    private int defBase; //初始防御
    private int hpBase; //初始血量
    private float atkArr; //攻击成长
    private float defArr; //防御成长
    private int hpArr; //血量成长
    private int speed; //移动速度
    private float atkRange; //攻击距离
    private float tailRange; //跟踪范围
    private int[] skills; //拥有技能
    private boolean reborn; //是否复活
    private int rebornTime; //复活时间
    private boolean provideBuff; //杀死后是否提供增益BUF
    private BuffModel buff; //buff;
    private boolean neutrality; //是否中立

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

    public float getAtkArr() {
        return atkArr;
    }

    public void setAtkArr(float atkArr) {
        this.atkArr = atkArr;
    }

    public float getDefArr() {
        return defArr;
    }

    public void setDefArr(float defArr) {
        this.defArr = defArr;
    }

    public int getHpArr() {
        return hpArr;
    }

    public void setHpArr(int hpArr) {
        this.hpArr = hpArr;
    }

    public float getAtkRange() {
        return atkRange;
    }

    public void setAtkRange(float atkRange) {
        this.atkRange = atkRange;
    }

    public float getTailRange() {
        return tailRange;
    }

    public void setTailRange(float tailRange) {
        this.tailRange = tailRange;
    }

    public int[] getSkills() {
        return skills;
    }

    public void setSkills(int[] skills) {
        this.skills = skills;
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

    public boolean isProvideBuff() {
        return provideBuff;
    }

    public void setProvideBuff(boolean provideBuff) {
        this.provideBuff = provideBuff;
    }

    public BuffModel getBuff() {
        return buff;
    }

    public void setBuff(BuffModel buff) {
        this.buff = buff;
    }

    public boolean isNeutrality() {
        return neutrality;
    }

    public void setNeutrality(boolean neutrality) {
        this.neutrality = neutrality;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
