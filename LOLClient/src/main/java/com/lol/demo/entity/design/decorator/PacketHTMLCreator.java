/******************************************************************************
 *
 * Module Name:  entity.design.decorator - PacketHTMLHeaderCreator.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: May 3, 2016
 * Last Updated By: randyzhyang
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

public class PacketHTMLCreator extends PacketDecorator {

    public PacketHTMLCreator(IPacketCreator c) {
        super(c);
    }

    @Override
    public String handleContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n");
        sb.append("<body>\n");
        sb.append("\t" + super.creator.handleContent() + "\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        return sb.toString();
    }

}
