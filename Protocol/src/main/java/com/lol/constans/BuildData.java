package com.lol.constans;

/**
 * 建筑属性配置表
 */
public class BuildData {
    public static final java.util.HashMap<Integer, BuildDataModel> buildMap = new java.util.HashMap<Integer, BuildDataModel>();

    /**
     静态构造 初次访问的时候自动调用

     */
    static {
        create(1, "主基地", 5000, 0, 50, false, true, false, 0);
        create(2, "高级箭塔", 3000, 200, 50, false, true, true, 30);//高地水晶
        create(3, "中级箭塔", 2000, 150, 30, true, true, false, 0);
        create(4, "初级箭塔", 1000, 100, 20, true, true, false, 0);
    }

    /**
     *
     * @param id
     * @param name
     * @param hp
     * @param atk
     * @param def
     * @param initiative
     * @param infrared
     * @param reborn
     * @param rebornTime
     */
    private static void create(int id, String name, int hp, int atk, int def, boolean initiative,
                               boolean infrared, boolean reborn, int rebornTime) {
        BuildDataModel model = new BuildDataModel(id, name, hp, atk, def, initiative, infrared, reborn, rebornTime);
        buildMap.put(id, model);
    }
}