/******************************************************************************
 *
 * Module Name:  netty - NettyConstans.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 11, 2016
 * Last Updated By: java
 * Last Updated Date: May 11, 2016
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

import java.util.HashMap;
import java.util.Map;

public class NettyConstants {
    /**
     * key addr.
     * value 0: first start connet flag.
     * value 1: reconnet when offline.
     */
    public static volatile Map<String, Integer> CLIENT_CONNET_STATUS = new HashMap<String, Integer>();
    public static Map<String, String> CACHEMSG_ONEXCEPTION = new HashMap<>();
}
