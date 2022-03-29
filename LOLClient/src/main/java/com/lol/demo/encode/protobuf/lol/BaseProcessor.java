package com.lol.demo.encode.protobuf.lol;/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  19:25
 */

import com.lol.demo.encode.protobuf.MessageDownProto;

/**
 * Description :
 * Created by YangZH on 2017/4/12
 * 19:25
 */

public abstract class BaseProcessor {
    public abstract void process(MessageDownProto.MessageDown message) throws Exception;
}
