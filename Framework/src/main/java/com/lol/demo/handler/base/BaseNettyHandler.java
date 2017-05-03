/******************************************************************************
 *
 * Module Name:  netty.handle.base - BaseNettyHandler.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Jun 3, 2016
 * Last Updated By: java
 * Last Updated Date: Jun 3, 2016
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
package com.lol.demo.handler.base;

import com.lol.demo.entity.base.Base;
import com.lol.demo.parenttest.Ftest;

public class BaseNettyHandler<M extends Base, P extends Ftest> {
    private String properies;

    public String getProperies() {
        return properies;
    }

    public void setProperies(String properies) {
        this.properies = properies;
    }
}
