package com.lol.constans;


import com.lol.constans.Skill.SkillAttack;

public class SkillProcessMap {
    private static java.util.HashMap<Integer, ISkill> skills = new java.util.HashMap<Integer, ISkill>();

    static {
        put(-1, new SkillAttack());
    }

    private static void put(int code, ISkill skill) {
        skills.put(code, skill);
    }

    public static boolean has(int code) {
        return skills.containsKey(code);
    }

    public static ISkill get(int code) {
        return skills.get(code);
    }
}