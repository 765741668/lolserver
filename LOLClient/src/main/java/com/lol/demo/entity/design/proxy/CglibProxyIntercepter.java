/******************************************************************************
 *
 * Module Name:  entity.proxy - CglibProxyIntercepter.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: Apr 29, 2016
 * Last Updated By: randyzhyang
 * Last Updated Date: Apr 29, 2016
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
package com.lol.demo.entity.design.proxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyIntercepter implements MethodInterceptor {
    IDBQuery real = null;

    @Override
    public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
        if (real == null) {
            real = new DBQueryImpl();
        }

        return real.findUserName();
    }

}
