package com.lol.demo.encode.protobuf;

import com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoBody;
import com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoHeader;
import com.lol.demo.encode.protobuf.SubscribeRespProto.SubscribeResp;
import com.lol.demo.encode.protobuf.SubscribeReqProto.SubscribeReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SubReqClientHandler extends SimpleChannelInboundHandler<SubscribeResp> {

    private static final Logger logger = LoggerFactory.getLogger(SubReqClientHandler.class.getName());

    public SubReqClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println("send msg to server....");
            SubscribeReq req = req(i);
            ctx.writeAndFlush(req);
        }
        ctx.flush();
    }

    private SubscribeReq req(int i) {
        ProtoHeader.Builder header = ProtoHeader.newBuilder();
        header.setSubReqID(i);
        header.setMsgType(ProtoHeader.MsgType.LOGIN_OK);

        List<String> address = new ArrayList<String>();
        address.add("Nanjing");
        address.add("Beijing");

        ProtoBody.Builder body = ProtoBody.newBuilder();
        body.setProductName("Netty Book" + i);
        body.setUserName("leeka");
        body.addAllAddress(address);
        body.getMutableHeaderMap().put("head1", header.build());

        SubscribeReq.Builder r = SubscribeReq.newBuilder();
        r.setHeader(header);
        r.setBody(body);

        return r.build();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SubscribeResp msg)
            throws Exception {
        System.out.println("receive server response:[");
        System.out.println(msg);
        System.out.println("]");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.warn("unexpected exception from downstream: {}",cause.getMessage());
        ctx.close();
    }

}