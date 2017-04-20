package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.PlayerProtocol;
import com.lol.buffer.GameDownBuffer;
import com.lol.buffer.GameUpBuffer;
import com.lol.dao.bean.Player;
import com.lol.handler.GameProcessor;
import com.lol.protobuf.MessageDownProto;
import com.lol.service.IPlayerService;
import com.lol.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */

public class PlayerProcesor implements GameProcessor {

    @Autowired
    private IPlayerService playerService;

    @Override
    public void process(GameUpBuffer buffer) throws Exception {
        GameDownBuffer message = null;
        MessageDownProto.PlayerDownBody.Builder playerBulder = MessageDownProto.PlayerDownBody.newBuilder();
        switch (buffer.getCmd()) {
            case PlayerProtocol.CREATE_CREQ:
                boolean result = playerService.create(buffer.getConnection(), buffer.getPlayerNickName());

                playerBulder.setCreate(result);
                message = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), PlayerProtocol.CREATE_SRES, playerBulder);

                break;
            case PlayerProtocol.INFO_CREQ:
                Player player = playerService.getByAcount(buffer.getConnection());

                playerBulder.setPlayerModel(convert(player));
                message = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), PlayerProtocol.INFO_SRES, playerBulder);
                break;
            case PlayerProtocol.ONLINE_CREQ:
                Player onlinePlayer = playerService.online(buffer.getConnection());

                playerBulder.setPlayerModel(convert(onlinePlayer));
                message = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), PlayerProtocol.ONLINE_SRES, playerBulder);
                break;
            case PlayerProtocol.OFFLINE_CREQ:
                playerService.offline(buffer.getConnection());
                break;
        }

        buffer.getConnection().writeDown(message);
    }


    private MessageDownProto.Player convert(Player user) {
        if (user == null) {
            return null;
        }
        MessageDownProto.Player.Builder player = MessageDownProto.Player.newBuilder();

        player.setId(user.getId());
        player.setNickName(user.getName());
        player.setLevel(user.getLevel());
        player.setWinCount(user.getWinCount());
        player.setLoseCount(user.getLoseCount());
        player.setRanCount(user.getRanCount());
        Collections.addAll(player.getHeroList(), user.getHerolist());

        return player.build();
    }
}
