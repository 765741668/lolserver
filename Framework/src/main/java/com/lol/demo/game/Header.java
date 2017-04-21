/******************************************************************************
 *
 * Module Name:  env.netty - Header.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 12, 2016
 * Last Updated By: java
 * Last Updated Date: May 12, 2016
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

import java.util.HashMap;
import java.util.Map;

public class Header {
    private int orcCode = 0xabef0101;
    private byte type;
    private byte priority;
    private int length;
    private long sessionId;
    private Map<String, Object> attachment = new HashMap<>();


    public final int getOrcCode() {
        return orcCode;
    }


    public final void setOrcCode(int orcCode) {
        this.orcCode = orcCode;
    }


    public final byte getType() {
        return type;
    }


    public final void setType(byte type) {
        this.type = type;
    }


    public final byte getPriority() {
        return priority;
    }


    public final void setPriority(byte priority) {
        this.priority = priority;
    }


    public final int getLength() {
        return length;
    }


    public final void setLength(int length) {
        this.length = length;
    }


    public final long getSessionId() {
        return sessionId;
    }


    public final void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }


    public final Map<String, Object> getAttachment() {
        return attachment;
    }


    public final void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }


    @Override
    public String toString() {
        return "Header [orcCode=" + orcCode + ", length=" + length + ", sessionId=" + sessionId + ", type=" + type + ", priority=" + priority + ", attachment="
                + attachment + "]";
    }

}
