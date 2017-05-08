package com.lol.demo.util;/**
 * Description : 
 * Created by YangZH on 2017/3/24
 *  0:45
 */

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.lol.demo.common.NettyMessage;
import com.lol.demo.enums.MessageType;
import com.lol.demo.game.Header;

/**
 * Description :
 * Created by YangZH on 2017/3/24
 * 0:45
 */

public class SerializeProtobufUtil {
    public static <T> byte[] encode(T t, Class<T> clazz) {
        return ProtobufIOUtil.toByteArray(t, RuntimeSchema.createFrom(clazz),
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    public static <T> T decode(byte[] data, Class<T> clazz) {
        RuntimeSchema<T> runtimeSchema = RuntimeSchema.createFrom(clazz);
        T t = runtimeSchema.newMessage();
        ProtobufIOUtil.mergeFrom(data, t, runtimeSchema);
        return t;
    }

    public static void main(String[] args) {
        NettyMessage message = buildMessage();

        for (int i = 0; i < 2; i++) {
            System.out.println("Serialize....");
            byte[] bytes = encode(message, NettyMessage.class);
            for (byte b : bytes) {
                System.out.print(b);
            }
            System.out.println();
            System.out.println("DeSerialize...");
            NettyMessage decode = decode(bytes, NettyMessage.class);
            System.out.println(decode.toString());
        }

    }

    static NettyMessage buildMessage() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.getValue());
        header.setLength(100);
        header.setOrcCode(100);
        header.setPriority((byte) 1);
        header.setSessionId(100);
        header.setType((byte) 1);
        message.setBody("It is a test message...");
        message.setHeader(header);

        return message;
    }
}
