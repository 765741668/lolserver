package com.lol.util;

import com.lol.buffer.GameDownBuffer;
import com.lol.buffer.GameUpBuffer;
import com.lol.core.Connection;
import com.lol.protobuf.MessageDownProto;
import com.lol.protobuf.MessageUpProto;
import org.apache.commons.lang3.RandomUtils;

/**
 * 工具类
 *
 * @author Randy
 */
public class Utils {
    /**
     * 取得指定范围随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNum(int min, int max) {
        return RandomUtils.nextInt(min, max + 1);
    }

    /**
     * 取得指定范围随机浮点数
     *
     * @param min
     * @param max
     * @return
     */
    public static float getRandomNum(float min, float max) {
        return RandomUtils.nextFloat(min, max);
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static int getTimeStamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 下发数据封装
     * TODO:: 增加 class 参数
     *
     * @param msgType
     * @param area
     * @param cmd
     * @param data
     * @return
     */
    public static GameDownBuffer packgeDownData(int msgType, int area, int cmd, Object data) {

        GameDownBuffer buffer = new GameDownBuffer();
        MessageDownProto.MessageDown.Builder builder = MessageDownProto.MessageDown.newBuilder();
        MessageDownProto.MsgHeader.Builder header = MessageDownProto.MsgHeader.newBuilder();

        header.setMsgType(msgType);
        header.setArea(area);
        header.setCmd(cmd);

        if (data instanceof MessageDownProto.MsgDownBody) {
            builder.setBody((MessageDownProto.MsgDownBody) data);
        } else {
            MessageDownProto.MsgDownBody.Builder body = MessageDownProto.MsgDownBody.newBuilder();
            if (data instanceof MessageDownProto.LoginDownBody) {
                body.setLogin((MessageDownProto.LoginDownBody) data);
            } else if (data instanceof MessageDownProto.PlayerDownBody) {
                body.setPlayer((MessageDownProto.PlayerDownBody) data);
            } else if (data instanceof MessageDownProto.SelectDownBody) {
                body.setSelect((MessageDownProto.SelectDownBody) data);
            } else if (data instanceof MessageDownProto.FightDownBody) {
                body.setFight((MessageDownProto.FightDownBody) data);
            }
            builder.setBody(body);
        }

        builder.setHeader(header);
        buffer.setBuffer(builder.build());

        return buffer;

    }

    /**
     * 上行数据封装
     *
     * @param msgType
     * @param area
     * @param cmd
     * @param data
     * @return
     */
    public static GameUpBuffer packgeUpData(Connection connection, int msgType, int area, int cmd, Object data) {

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

        return new GameUpBuffer(builder.build(), connection);
    }

    public static void main(String[] args) {
        System.out.println(getRandomNum(1,4));
        System.out.println(getRandomNum(0.1f,4.0f));
    }
}
