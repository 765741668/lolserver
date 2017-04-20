/******************************************************************************
 *
 * Module Name:  entity.design.proxy - ProxyUtil.java
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

import org.springframework.cglib.proxy.Enhancer;

public class ProxyUtil {

    public static IDBQuery createCglibProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibProxyIntercepter());
        enhancer.setInterfaces(new Class[]{IDBQuery.class});

        IDBQuery result = (IDBQuery) enhancer.create();

        return result;
    }

}
