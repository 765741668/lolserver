package com.lol.constans;


import com.lol.dto.fight.AbsFightModel;

import java.util.List;

public interface ISkill {
    /**
     * TODO: 攻击伤害 算法
     *
     * @param level
     * @param atk
     * @param target
     * @param damages
     */
    void damage(int level, AbsFightModel atk, AbsFightModel target, List<Integer[]> damages);
}