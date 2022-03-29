package com.lol.demo.encode.protobuf.lol;

import com.lol.MatchProtocol;
import com.lol.Protocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOLClientSelectProcessor extends BaseProcessor {

    private Logger logger = LoggerFactory.getLogger(LOLClientSelectProcessor.class);

    @Override
    public void process(MessageDownProto.MessageDown resp) {
        switch (resp.getHeader().getCmd()) {
            case MatchProtocol.MATCH_COMPLETED:
                logger.info("匹配已找到。");
                break;
        }
    }
}