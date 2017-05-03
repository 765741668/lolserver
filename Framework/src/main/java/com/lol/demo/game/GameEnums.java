/******************************************************************************
 *
 * Module Name:  netty - Enums.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Apr 21, 2016
 * Last Updated By: java
 * Last Updated Date: Apr 21, 2016
 * Description:
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
package com.lol.demo.game;

public enum GameEnums {
    HERO_ATTACK("hero_attack", "attack monster", 1),
    HERO_DEFENSE("hero_defense", "defense from monster", 2),
    HERO_RUN("hero_run", "run away", 3),
    HERO_SKILLS("hero_skills", "perform skills on monster", 4),

    MONSTER_ATTACK("monster_attack", "monster attack hero", 5),
    MONSTER_DEFENSE("monster_defense", "monster defense from hero", 6),
    MONSTER_RUN("monster_run", "monster run away", 7),
    MONSTER_SKILLS("monster_skills", "monster perform skills on hero", 8);

    private final String code;
    private final String description;
    private final int seq;

    GameEnums(String code, String description, int seq) {
        this.code = code;
        this.description = description;
        this.seq = seq;
    }

    public static GameEnums getEnumsByCode(String code) {
        for (GameEnums e : GameEnums.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }

        return null;
    }

    public static GameEnums getEnumsBySeq(int seq) {
        for (GameEnums e : GameEnums.values()) {
            if (e.seq == seq) {
                return e;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }


}
