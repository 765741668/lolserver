package com.lol.demo.encode.protobuf.lol;

import com.lol.PlayerProtocol;
import com.lol.Protocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOLClientPlayerProcessor extends BaseProcessor {

    private Logger logger = LoggerFactory.getLogger(LOLClientPlayerProcessor.class);

    @Override
    public void process(MessageDownProto.MessageDown resp) {
        String respStr;
        switch (resp.getHeader().getCmd()) {
            case PlayerProtocol.CREATE_SRES:
                switch (resp.getBody().getPlayer().getCreate()) {
                    case -1:
                        respStr = "帐号未登陆，创建新玩家角色失败";
                        break;
                    case -2:
                        respStr = "当前帐号已经拥有该玩家角色";
                        break;
                    default:
                        respStr = "创建新玩家角色成功";
                        break;
                }
                logger.info("收到服务器创建新玩家角色响应: {}", respStr);
                break;
            case PlayerProtocol.INFO_SRES:
                if (resp.getBody().getPlayer().getPlayerModel() == null) {
                    logger.info("收到服务器获取玩家角色信息响应: 帐号未否登陆,获取失败");
                } else {
                    logger.info("收到服务器获取玩家角色信息响应: {}", resp.getBody().getPlayer().getPlayerModel().getNickName());
                }
                break;
            case PlayerProtocol.ONLINE_SRES:
                if (resp.getBody().getPlayer().getPlayerModel() == null) {
                    logger.info("收到服务器玩家角色上线响应: {}", "玩家角色不存在,无法获取信息，上线失败");
                } else {
                    logger.info("收到服务器玩家角色上线响应: {}", resp.getBody().getPlayer().getPlayerModel().getNickName());
                }
                break;
        }
    }
}