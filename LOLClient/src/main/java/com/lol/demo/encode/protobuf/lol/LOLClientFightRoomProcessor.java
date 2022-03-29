package com.lol.demo.encode.protobuf.lol;

import com.lol.SelectProtocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOLClientFightRoomProcessor extends BaseProcessor {

    private Logger logger = LoggerFactory.getLogger(LOLClientFightRoomProcessor.class);

    @Override
    public void process(MessageDownProto.MessageDown resp) {
        switch (resp.getHeader().getCmd()){
            case SelectProtocol.FIGHT_BRO:
                logger.info("欢迎来到英雄联盟~~~准备开始battle吧");
                break;
        }
    }
}