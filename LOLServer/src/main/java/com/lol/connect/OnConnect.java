package com.lol.connect;

import com.lol.buffer.GameUpBuffer;
import com.lol.handler.GameProcessor;
import org.apache.log4j.Logger;

/**
 * 玩家上线
 *
 * @author Randy
 */
public class OnConnect implements GameProcessor {

    private static Logger logger = Logger.getLogger(OnConnect.class);

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

    }
}
