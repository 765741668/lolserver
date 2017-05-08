package com.lol.demo.util;

import com.lol.demo.encode.protobuf.MessageUpProto;

/**
 * 工具类
 *
 * @author Randy
 */
public class Utils {

    /**
     * 上行数据封装
     *
     * @param msgType
     * @param area
     * @param cmd
     * @param data
     * @return
     */
    public static MessageUpProto.MessageUp packgeUpData(int msgType, int area, int cmd, Object data) {

        MessageUpProto.MessageUp.Builder builder = MessageUpProto.MessageUp.newBuilder();
        MessageUpProto.MsgHeader.Builder header = MessageUpProto.MsgHeader.newBuilder();

        header.setMsgType(msgType);
        header.setArea(area);
        header.setCmd(cmd);
        builder.setHeader(header);

        if (data instanceof MessageUpProto.MsgUpBody) {
            builder.setBody((MessageUpProto.MsgUpBody) data);
        } else {
            MessageUpProto.MsgUpBody.Builder body = MessageUpProto.MsgUpBody.newBuilder();
            if (data instanceof MessageUpProto.LoginUpBody) {
                body.setLogin((MessageUpProto.LoginUpBody) data);
            } else if (data instanceof MessageUpProto.PlayerUpBody) {
                body.setPlayer((MessageUpProto.PlayerUpBody) data);
            } else if (data instanceof MessageUpProto.MatchUpBody) {
                body.setMatch((MessageUpProto.MatchUpBody) data);
            } else if (data instanceof MessageUpProto.SelectUpBody) {
                body.setSelect((MessageUpProto.SelectUpBody) data);
            } else if (data instanceof MessageUpProto.FightUpBody) {
                body.setFight((MessageUpProto.FightUpBody) data);
            }

            builder.setBody(body);
        }

        return builder.build();
    }
}
