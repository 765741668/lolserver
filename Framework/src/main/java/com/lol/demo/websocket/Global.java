/******************************************************************************
 *
 * Module Name:  env.netty.websocket - Global.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Jun 2, 2016
 * Last Updated By: java
 * Last Updated Date: Jun 2, 2016
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
package com.lol.demo.websocket;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

public class Global {
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static Map<String, String> f2f = new HashMap<>();
    public static Map<String, String> CACHEMSG_ONEXCEPTION = new HashMap<>();
}
