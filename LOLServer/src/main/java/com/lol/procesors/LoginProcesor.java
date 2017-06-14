package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.LoginProtocol;
import com.lol.dto.AcountInfoDTO;
import com.lol.fwk.buffer.GameDownBuffer;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.channel.GameOnlineChannelManager;
import com.lol.fwk.common.Constans;
import com.lol.fwk.core.Connection;
import com.lol.fwk.core.ConnectionManager;
import com.lol.fwk.db.ServiceException;
import com.lol.fwk.handler.GameProcessor;
import com.lol.fwk.protobuf.MessageDownProto;
import com.lol.fwk.service.acount.IAcountService;
import com.lol.fwk.util.Utils;
import com.lol.tool.EventUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
@Component
public class LoginProcesor implements GameProcessor {

    private Logger logger = LoggerFactory.getLogger(LoginProcesor.class);

    @Autowired
    private IAcountService acountService;

    @Override
    public void process(GameUpBuffer buffer) {
        try {
            Connection connection = buffer.getConnection();
            if (connection == null) {
                logger.info("客户端未登陆或已下线。忽略此次请求");
                return;
            }

            AcountInfoDTO acountDTO = buffer.getAcountInfo();
            int result = 0;
            int res_cmd = 0;
            String acount = acountDTO.getacount();
            String password = acountDTO.getPassword();
            switch (buffer.getCmd()) {
                case LoginProtocol.REG_CREQ:
                    result = acountService.create(acount, password);
                    res_cmd = LoginProtocol.REG_SRES;
                    break;
                case LoginProtocol.LOGIN_CREQ:
                    result = acountService.login(connection, acount, password);
                    res_cmd = LoginProtocol.LOGIN_SRES;
                    break;
                case LoginProtocol.LOGIN_INACTIVE:
                    EventUtil.disconnect = this::close;
                    res_cmd = LoginProtocol.LOGIN_INACTIVE;
                    break;
            }

            if (res_cmd != LoginProtocol.LOGIN_INACTIVE) {
                MessageDownProto.LoginDownBody.Builder login = MessageDownProto.LoginDownBody.newBuilder();
                login.setResult(result);

                GameDownBuffer message = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), res_cmd, login.build());
                        connection.writeDown(message);
                logger.debug("发送下游消息成功：{}", message.getBuffer().getBody().getLogin().getResult());
            }
        } catch (ServiceException e) {
            logger.error("处理登陆模块消息异常: {}", e.getMessage(), e);
        }

    }

    /**
     * 断开链接后的预处理集合,先后顺序
     * 1.玩家角色下线
     * 2.取消匹配（如果有）
     * 3.取消选人（如果已经进入选人模块）
     * 4.如果已经开始战斗，移除客户端链接(这个操作放在登陆模块一并处理,第4步回调此函数 做最终善后工作)
     *
     */
    public void close(Connection connection) {
        logger.info("所有预处理已经完成，开始移除客户端相关链接 : {}", connection.getChannel().remoteAddress());
        ChannelHandlerContext ctx = connection.getChannelHandlerContext();
        ctx.attr(Constans.conn).remove();
        logger.info("从 Channel AttributeKey 移除链接成功。");
        ConnectionManager.getInstance().removeConnection(connection);
        logger.info("从 连接管理器ConnectionManager 移除链接成功。");
        GameOnlineChannelManager.getInstance().getOnlineChannel(connection.getAcount()).removeOnlineConnection(connection);
        logger.info("从在 线游戏房间频道管理器GameOnlineChannelManager 移除链接成功。");
        logger.info("移除 客户端相关链 接完毕。");
    }

}
