package com.lol.demo.encode.protobuf.lol;

import com.lol.MatchProtocol;
import com.lol.Protocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOLClientMatchProcessor extends BaseProcessor {

    private Logger logger = LoggerFactory.getLogger(LOLClientMatchProcessor.class);

    @Override
    public void process(MessageDownProto.MessageDown resp) {
        switch (resp.getHeader().getCmd()){
            case MatchProtocol.MATCH_COMPLETED:
                logger.info("匹配已找到。");
                break;
            case MatchProtocol.ENTER_SRES:
                logger.info("进入匹配池成功");
                break;
        }
    }
}