package com.lol.constans;


import com.lol.dto.fight.SkillTarget;
import com.lol.dto.fight.SkillType;

public class SkillData {
    public static final java.util.HashMap<Integer, SkillDataModel> skillMap = new java.util.HashMap<Integer, SkillDataModel>();

    static {
        create(1, "欺诈宝珠", "消耗 65/70/75/80/85法力 \n冷却7/7/7/7/7 \n射程 880 \n效果 阿狸放出宝珠，" +
                        "造成40/65/90/115/140(+0.35)魔法伤害，随后将其收回，造成40/65/90/115/140(+0.35)真实伤害。",
                SkillType.POSITION, SkillTarget.E_N_B, skillLevelData(1, 0, 0, 0), skillLevelData(3, 7, 65, 880),
                skillLevelData(5, 7, 70, 880), skillLevelData(7, 7, 75, 880), skillLevelData(9, 7, 80, 880),
                skillLevelData(-1, 7, 85, 880)
        );
        create(2, "妖异狐火", "消耗 50/50/50/50/50法力 \n冷却 9/8/7/6/5 \n射程 550 \n效果 阿狸放出三团狐火，" +
                        "狐火会锁定附近的三名敌人（英雄优先）进行攻击，造成40/65/90/115/140(+0.4)魔法伤害。",
                SkillType.SELF_CENTER, SkillTarget.E_H, skillLevelData(1, 0, 0, 0), skillLevelData(3, 7, 65, 880),
                skillLevelData(5, 7, 70, 880), skillLevelData(7, 7, 75, 880), skillLevelData(9, 7, 80, 880),
                skillLevelData(-1, 7, 85, 880)
        );
        create(3, "魅惑妖术", "消耗 85/85/85/85/85法力 \n冷却 12/12/12/12/12 \n射程 975 \n效果 阿狸献出红唇热吻，" +
                        "对命中的第一个敌人造成60/90/120/150/200(+0.5)魔法伤害并将目标魅惑，让目标意乱情迷地走向阿狸。" +
                        "魅惑效果持续1/1.25/1.5/1.75/2秒。", SkillType.POSITION, SkillTarget.E_H, skillLevelData(1, 0, 0, 0),
                skillLevelData(3, 7, 65, 880), skillLevelData(5, 7, 70, 880), skillLevelData(7, 7, 75, 880),
                skillLevelData(9, 7, 80, 880), skillLevelData(-1, 7, 85, 880)
        );
        create(4, "灵魂突袭", "消耗 100/100/100法力 \n冷却 110/95/80 \n射程 450 \n效果 阿狸像妖魅一般向前冲锋，" +
                        "并向周围的3名敌人（英雄优先）发射元气弹，造成70/110/150(+0.3)魔法伤害。" +
                        "灵魄突袭可以在进入冷却阶段前的10秒内被施放最多3次。", SkillType.POSITION, SkillTarget.E_H,
                skillLevelData(6, 110, 0, 880), skillLevelData(11, 95, 65, 880), skillLevelData(16, 80, 70, 880),
                skillLevelData(-1, 80, 70, 880)
        );
    }

    private static SkillLevelData skillLevelData(int level, int time, int mp, float range) {
        return new SkillLevelData(level, time, mp, range);
    }

    private static void create(int code, String name, String info, SkillType type, SkillTarget target,
                               SkillLevelData... levels) {
        SkillDataModel model = new SkillDataModel(code, name, info, type, target, levels);
        skillMap.put(code, model);
    }

}