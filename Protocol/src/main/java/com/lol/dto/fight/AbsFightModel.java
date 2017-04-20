package com.lol.dto.fight;

public class AbsFightModel {
    private int id; //战斗区域中 唯一识别码
    private ModelType type; //标识当前生命体是属于什么类别
    private int code; //模型唯一识别码 但是战斗中会有多个相同兵种出现 所以这里只用于标识形象急获取对应的数据
    private int hp; //当前血量
    private int maxHp; //最大血量
    private int atk; //攻击
    private int def; //防御
    private String name; //名称
    private float speed; //移动速度
    private float aSpeed; //攻击速度
    private float aRange; //攻击范围
    private float eyeRange; //视野范围
    private int team; //单位所在的队伍

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModelType getType() {
        return type;
    }

    public void setType(ModelType type) {
        this.type = type;
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

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getaRange() {
        return aRange;
    }

    public void setaRange(float aRange) {
        this.aRange = aRange;
    }

    public float getEyeRange() {
        return eyeRange;
    }

    public void setEyeRange(float eyeRange) {
        this.eyeRange = eyeRange;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}