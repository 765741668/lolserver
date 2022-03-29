package com.lol.connect;

import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.handler.GameProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 玩家角色上线
 *
 * @author Randy
 */
@Component
public class OnConnect implements GameProcessor {

    private static Logger logger = LoggerFactory.getLogger(OnConnect.class);

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

    }
}
