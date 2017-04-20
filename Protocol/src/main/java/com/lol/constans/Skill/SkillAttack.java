package com.lol.constans.Skill;


import com.lol.constans.ISkill;
import com.lol.dto.fight.AbsFightModel;

import java.util.List;

public class SkillAttack implements ISkill {
    //TODO: 伤害值 算法
    @Override
    public final void damage(int level, AbsFightModel atk, AbsFightModel target, List<Integer[]> damages) {
        int value = atk.getAtk() - target.getDef();
        value = value > 0 ? value : 1;
        target.setHp(target.getHp() - value <= 0 ? 0 : target.getHp() - value);
        damages.add(new Integer[]{target.getId(), value, target.getHp() == 0 ? 0 : 1});
    }
}