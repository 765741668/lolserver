package com.lol.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class InvokerHandler extends ChannelInboundHandlerAdapter {
    public static ConcurrentHashMap<String, Object> classMap = new ConcurrentHashMap<String,Object>();
    @Override    
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo)msg;  
        Object claszz = null;
        if(!classMap.containsKey(classInfo.getClassName())){  
            try {  
                claszz = Class.forName(classInfo.getClassName()).newInstance();  
                classMap.put(classInfo.getClassName(), claszz);  
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {  
                e.printStackTrace();  
            }
        }else {
            claszz = classMap.get(classInfo.getClassName());  
        }
        Method method = claszz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
        Object result = method.invoke(claszz, classInfo.getObjects());
        System.out.println("server 收到: " + classInfo.getObjects()[0].toString());
        System.out.println("server 返回: " + result);
        ctx.write(result);
        ctx.flush();    
        ctx.close();  
    }    
    @Override    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {    
         cause.printStackTrace();    
         ctx.close();    
    }    
  
} 