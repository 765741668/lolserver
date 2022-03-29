package com.lol.logic.player;/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */

import com.lol.PlayerProtocol;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.core.Connection;
import com.lol.fwk.entity.player.Player;
import com.lol.fwk.handler.GameProcessor;
import com.lol.fwk.protobuf.MessageDownProto;
import com.lol.logic.BaseProcessor;
import com.lol.tool.EventUtil;
import com.lol.fwk.util.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
@Component
public class PlayerProcessor extends BaseProcessor implements GameProcessor {

    private Logger logger = LoggerFactory.getLogger(PlayerProcessor.class);

    @Override
    public void process(GameUpBuffer buffer) throws Exception {
        Connection connection = buffer.getConnection();
        if (connection == null) {
            logger.info("客户端未登陆或已下线。忽略此次请求");
            return;
        }

        Channel channel = connection.getChannel();
        logger.info("收到玩家模块消息: {}", channel.remoteAddress());

        int res_cmd = 0;
        MessageDownProto.PlayerDownBody.Builder playerBulder = MessageDownProto.PlayerDownBody.newBuilder();
        switch (buffer.getCmd()) {
            case PlayerProtocol.CREATE_CREQ:
                int result = playerService.create(connection, buffer.getPlayerNickName());
                playerBulder.setCreate(result);
                res_cmd = PlayerProtocol.CREATE_SRES;
                break;
            case PlayerProtocol.INFO_CREQ:
                Player player = playerService.getByAcount(connection);
                playerBulder.setPlayerModel(convert(player));

                res_cmd = PlayerProtocol.INFO_SRES;
                break;
            case PlayerProtocol.ONLINE_CREQ:
                Player onlinePlayer = playerService.online(connection);
                playerBulder.setPlayerModel(convert(onlinePlayer));
                logger.info("玩家角色上线成功：{}", connection.getAcount());

                res_cmd = PlayerProtocol.ONLINE_SRES;
                break;
            case PlayerProtocol.OFFLINE_CREQ:

                String acount = connection.getAcount();
                Player online = playerService.getByAcount(connection);
                if (online == null) {
                    logger.info("玩家角色角色[{}]没有选区进入游戏上线,直接请求最终操作。", acount);
//                    EventUtil.disconnect.accept(connection);
                } else {
                    playerService.offline(connection);
                    logger.info("玩家角色角色已下线：{}", acount);

//                    if (!isInMatch()) {
//                        logger.info("玩家角色[{}]没有在寻找匹配,直接请求最终操作。", acount);
//                        EventUtil.disconnect.accept(connection);
//                    }
                }
                break;
            default:
                logger.warn("示知指令,丢弃...");
                break;
        }

        if (res_cmd != PlayerProtocol.OFFLINE_CREQ) {
            MessageDownProto.MessageDown message = Utils.packgeDownData2(buffer.getMsgType(), buffer.getArea(), res_cmd, playerBulder.build());
            connection.writeDown(message);
            logger.debug("PlayerProcessor-发送下游消息成功：{}", message.getBody().getLogin().getResult());
        }
    }


    private MessageDownProto.Player convert(Player player) {
        if (player == null) {
            return null;
        }
        MessageDownProto.Player.Builder playerDown = MessageDownProto.Player.newBuilder();

        playerDown.setId(player.getId());
        playerDown.setNickName(player.getName());
        playerDown.setLevel(player.getLevel());
        playerDown.setWinCount(player.getWinCount());
        playerDown.setLoseCount(player.getLoseCount());
        playerDown.setRanCount(player.getRanCount());
        //TODO:英雄表
        playerDown.setHero(player.getHerolist());

        return playerDown.build();
    }
}
