package com.lol.constans;


import com.lol.util.Utils;

public class MonsterData {

    public static final java.util.HashMap<Integer, MonsterModel> monsterMap = new java.util.HashMap<Integer, MonsterModel>();

    /**
     * 静态构造 初次访问的时候自动调用
     */
    static {
        int random = Utils.getRandomNum(4, 7);
        BuffModel river = new BuffModel();
        BuffModel red = new BuffModel();
        BuffModel blue = new BuffModel();
        BuffModel wind_gragon = new BuffModel();
        BuffModel fire_gragon = new BuffModel();
        BuffModel water_gragon = new BuffModel();
        BuffModel soil_gragon = new BuffModel();
        BuffModel ancient_dragon = new BuffModel();
        BuffModel pioneer_valley = new BuffModel();
        BuffModel nash_baron = new BuffModel();


        create(1, "红BUFF怪",0,70,4000,300,0,0,0,5,15,new int[]{},true,60,true,red,true);
        create(2, "蓝BUFF怪",0,70,4000,300,0,0,0,0,0,new int[]{},true,60,true,blue,true);
        create(3, "峡谷先锋",250,200,12000,300,0,0,0,0,0,new int[]{},false,0,true,pioneer_valley,true);
        create(4, "纳什男爵",600,500,100000,300,0,0,0,0,0,new int[]{},true,3000,true,nash_baron,true);
        //4龙 随机选1
        create(5,BuffEnum.fromCode(random).getDescription()
                ,0,0,8000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(6,"三狼",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(6,"三狼儿子1",120,30,500,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(7,"三狼儿子2",120,30,500,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(8,"F4老爸",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(9,"F4儿子1",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(10,"F4儿子2",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(11,"F4儿子3",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(12,"F4儿子4",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(13,"石头老爸",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(14,"石头儿子1",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(15,"石头儿子2",120,50,1000,300,0,0,0,0,0,new int[]{},true,30,false,null,true);
        create(16,"蛤蟆",150,50,2000,300,0,0,0,5,15,new int[]{},true,30,false,null,true);
        create(17,"河蟹",0,0,4000,300,0,0,0,0,0,new int[]{},true,45,true,river,true);

    }

    /**
     * 创建模型并添加进字典
     * @param code
     * @param name
     * @param atkBase
     * @param defBase
     * @param hpBase
     * @param speed
     * @param atkArr
     * @param defArr
     * @param hpArr
     * @param atkRange
     * @param tailRange
     * @param skills
     * @param reborn
     * @param rebornTime
     * @param provideBuff
     * @param buff
     * @param neutrality
     */
    private static void create(int code, String name, int atkBase, int defBase, int hpBase,int speed,int atkArr,
                               float defArr, int hpArr, float atkRange, float tailRange, int[] skills,
                               boolean reborn,int rebornTime,boolean provideBuff,BuffModel buff, boolean neutrality) {
        MonsterModel model = new MonsterModel();

        model.setCode(code); //策划定义的唯一编号
        model.setName(name); //怪物名称
        model.setAtkBase(atkBase); //初始(基础)攻击力
        model.setDefBase(defBase); //初始防御
        model.setHpBase(hpBase); //初始血量
        model.setSpeed(speed); //移动速度
        model.setAtkArr(atkArr); //攻击成长
        model.setDefArr(defArr); //防御成长
        model.setHpArr(hpArr); //血量成长
        model.setAtkRange(atkRange); //攻击距离
        model.setTailRange(tailRange); //跟踪范围
        model.setSkills(skills); //拥有技能
        model.setReborn(reborn); //是否复活
        model.setRebornTime(rebornTime); //复活时间
        model.setProvideBuff(provideBuff); //杀死后是否提供增益BUF
        model.setBuff(buff); //buff;
        model.setNeutrality(neutrality); //是否中立

        monsterMap.put(code, model);
    }


}
