/******************************************************************************
 *
 * Module Name:  netty - Hero.java
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

public class Hero {
    private Integer attack;
    private Integer defense;
    private Integer live;
    private boolean isDeath;

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getLive() {
        return live;
    }

    public void setLive(Integer live) {
        this.live = live;
    }

    public boolean isDeath() {
        return isDeath;
    }

    public void setDeath(boolean isDeath) {
        this.isDeath = isDeath;
    }

    @Override
    public String toString() {
        return "Hero [attack=" + attack + ", defense=" + defense + ", live=" + live + ", isDeath=" + isDeath + "]";
    }

}
