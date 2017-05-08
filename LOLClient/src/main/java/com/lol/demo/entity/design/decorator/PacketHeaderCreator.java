/******************************************************************************
 *
 * Module Name:  entity.design.decorator - PecketHeaderCreator.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 3, 2016
 * Last Updated By: java
 * Last Updated Date: May 3, 2016
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
package com.lol.demo.entity.design.decorator;

public class PacketHeaderCreator extends PacketDecorator {

    public PacketHeaderCreator(IPacketCreator c) {
        super(c);
    }

    @Override
    public String handleContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cache-Control:non-cache\n");
        sb.append("Date:Mon,2016-11-12\n");
        sb.append(super.creator.handleContent());

        return sb.toString();
    }

}
