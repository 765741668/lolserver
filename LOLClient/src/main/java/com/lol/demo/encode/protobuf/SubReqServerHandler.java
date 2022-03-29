package com.lol.demo.encode.protobuf;

import io.netty.channel.ChannelHandlerContext;
import com.lol.demo.encode.protobuf.SubscribeRespProto.SubscribeResp;
import com.lol.demo.encode.protobuf.SubscribeReqProto.SubscribeReq;
import io.netty.channel.SimpleChannelInboundHandler;

public class SubReqServerHandler extends SimpleChannelInboundHandler<SubscribeReq> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SubscribeReq req)
            throws Exception {
        //System.out.println("SubReqServerHandler channelRead:"+ req.getUserName());
        if ("leeka".equalsIgnoreCase(req.getBody().getUserName())) {
            System.out.println("service accept client subscribe req:[");
            System.out.println(req);
            System.out.println("]");
            ctx.writeAndFlush(resp(req.getHeader().getSubReqID()));
        }
    }

    private SubscribeResp resp(int subReqID) {
        SubscribeResp.Builder builder = SubscribeResp.newBuilder();
        builder.setSubReqID(subReqID);
        builder.setRespCode("0");
        builder.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}  