package com.lol.demo.encode.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoBody;
import com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoHeader;

import java.util.ArrayList;
import java.util.List;

public class TestSubscribeReq {

    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body)
            throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        ProtoHeader.Builder header = ProtoHeader.newBuilder();
        header.setSubReqID(1);
        header.setMsgType(ProtoHeader.MsgType.LOGIN_OK);

        List<String> address = new ArrayList<String>();
        address.add("Nanjing");
        address.add("Beijing");

        ProtoBody.Builder body = ProtoBody.newBuilder();
        body.setProductName("Netty Book" + 1);
        body.setUserName("leeka");
        body.addAllAddress(address);
        body.getMutableHeaderMap().put("head1", header.build());

        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setHeader(header);
        builder.setBody(body);
        return builder.build();
    }

    public static void main(String[] args) throws Exception {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("before encode:" + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("after encode:" + req2.toString());
        System.out.println("Assert equal: " + req2.equals(req));

    }

}