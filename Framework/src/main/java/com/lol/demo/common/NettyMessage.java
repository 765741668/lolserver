/******************************************************************************
 *
 * Module Name:  env.netty - NeetyMessage.java
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
package com.lol.demo.common;

import com.lol.demo.game.Header;

public class NettyMessage {
    private Header header;
    private Object body;

    public Header getHeader() {
        return header;
    }


    public void setHeader(Header header) {
        this.header = header;
    }


    public Object getBody() {
        return body;
    }


    public void setBody(Object body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "NeetyMessage [header=" + header + ", body=" + body + "]";
    }


}
