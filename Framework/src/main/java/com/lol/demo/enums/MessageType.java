/******************************************************************************
 *
 * Module Name:  netty - MessageType.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 13, 2016
 * Last Updated By: java
 * Last Updated Date: May 13, 2016
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
package com.lol.demo.enums;

public enum MessageType {
    LOGIN_REQ((byte) 1, "login request"),
    LOGIN_RESP((byte) 2, "login response"),
    LOGIN_OK((byte) 3, "login is ok"),
    LOGIN_OUT_OF_WHITE_IP((byte) 4, "login is fail,out of white ip list"),
    LOGIN_DENY((byte) 5, "logined,deny login"),
    HEARTBEAT_REQ((byte) 6, "heartbeat request"),
    HEARTBEAT_RESP((byte) 7, "heartbeat response"),
    GAME_REQ((byte) 8, "game handle request"),
    GAME_RESP((byte) 9, "game handle response");

    private final byte value;
    private final String desc;

    private MessageType(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public byte getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
