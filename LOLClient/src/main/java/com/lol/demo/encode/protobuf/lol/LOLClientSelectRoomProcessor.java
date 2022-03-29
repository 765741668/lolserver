package com.lol.demo.encode.protobuf.lol;

import com.lol.SelectProtocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOLClientSelectRoomProcessor extends BaseProcessor {

    private Logger logger = LoggerFactory.getLogger(LOLClientSelectRoomProcessor.class);

    @Override
    public void process(MessageDownProto.MessageDown resp) {
        switch (resp.getHeader().getCmd()) {
            case SelectProtocol.ENTER_SRES:
                logger.info("欢迎自己进入选人房间");
                break;
            case SelectProtocol.ENTER_EXBRO:
                logger.info("欢迎广播别人进入选人房间");
                break;
            case SelectProtocol.SELECT_CREQ:
                logger.info("客户端选择了英雄");
                break;
            case SelectProtocol.SELECT_SRES:
                logger.info("服务器返回没有此英雄");
                break;
            case SelectProtocol.SELECT_BRO:
                logger.info("选择成功 通知房间所有人变更数据");
                break;
            case SelectProtocol.TALK_BRO:
                logger.info("服务器广播聊天");
                break;
            case SelectProtocol.READY_BRO:
                logger.info("有玩家准备OK了");
                break;
            default:
        }
    }
}