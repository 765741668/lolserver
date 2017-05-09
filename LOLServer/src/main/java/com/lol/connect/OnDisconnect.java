package com.lol.connect;


import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.handler.GameProcessor;
import org.springframework.stereotype.Component;

/**
 * 玩家角色离线
 *
 * @author Randy
 */
@Component
public class OnDisconnect implements GameProcessor {

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

    }
}
