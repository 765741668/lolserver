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

public enum En_DeCodeType {
    JBOSS_MARSHALL((byte) 1, "login request"),
    GOOGLE_PROTOBUF((byte) 2, "login response"),
    SUN_JDK_DEFAULT((byte) 3, "login is ok");

    private final byte value;
    private final String desc;

    private En_DeCodeType(byte value, String desc) {
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
