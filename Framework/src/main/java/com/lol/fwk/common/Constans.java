package com.lol.fwk.common;/**
 * Description : 
 * Created by YangZH on 2017/4/10
 *  23:03
 */

import com.lol.fwk.core.Connection;
import io.netty.util.AttributeKey;

/**
 * Description :
 * Created by YangZH on 2017/4/10
 * 23:03
 */

public class Constans {
    /**
     * 连接属性
     */
    public static AttributeKey<Connection> conn = AttributeKey.valueOf("Connection.attr");

}
