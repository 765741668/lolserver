/******************************************************************************
 *
 * Module Name:  env.netty.encode - Test.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 25, 2016
 * Last Updated By: java
 * Last Updated Date: May 25, 2016
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
package com.lol.demo.test;

import io.netty.handler.codec.http.HttpHeaderValues;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {

    public static void main(String[] args) throws UnknownHostException {
        System.out.println((CharSequence) InetAddress.getByName("127.0.0.1").getHostAddress());
        System.out.println(HttpHeaderValues.GZIP + "," + HttpHeaderValues.DEFLATE);

    }

}
