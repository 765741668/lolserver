package com.lol.fwk.service.impl;

import com.lol.fwk.channel.GameOnlineChannelManager;
import com.lol.fwk.common.Constans;
import com.lol.fwk.core.Connection;
import com.lol.fwk.core.ConnectionManager;
import com.lol.fwk.entity.acount.Acount;
import com.lol.fwk.exception.ServiceException;
import com.lol.fwk.service.IAccountService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("accountService")
public class AccountServiceImpl implements IAccountService {
    PasswordEncoder encoder = new StandardPasswordEncoder();
    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    /**
     * 账号与自身具体属性的映射绑定
     */
    private static Map<String, Acount> accMap = new HashMap<>();

    @Override
    public final int create(String acount, String password) throws ServiceException {
        if (hasAcount(acount)) {
            logger.warn("账号已存在，创建新账号失败");
            return 1;
        }

        Acount model = new Acount(acount, encoder.encode(password));

        try {
            //TODO
            logger.info("创建新账号成功");
        } catch (Exception e) {
            logger.error("创建新账号失败:{}",e.getMessage(), e);
            throw new ServiceException("创建新账号失败", e);
        }

        accMap.put(acount, model);

        return 0;
    }

    @Override
    public final int login(Connection connection, String account, String password) throws ServiceException {
        if (account == null || password == null) {
            logger.warn("账号或密码为空，输入不合法");
            return -4;
        }

        boolean isOnline = ConnectionManager.getInstance().getConnection(account) != null;
        if (isOnline) {
            logger.warn("当前账号已经在线");
            return -2;
        }

        if (!match(account, password)) {
            logger.warn("账号或密码不正确");
            return -3;
        }

        //验证都通过 说明可以登录  让客户端上线
        logger.info("Channel AttributeKey 新增链接：{},玩家角色名：{}",connection.getChannel().remoteAddress(),connection.getAcount());
        ConnectionManager.getInstance().addConnection(account, connection);
        logger.info("连接管理器ConnectionManager 新增链接：{}-{}",connection.getChannel().remoteAddress(),connection.getAcount());
        //如果要世界频道就加入 TODO
        GameOnlineChannelManager.getInstance().addOnlineChannel(account).addOnlineConnection(connection);
        logger.info("在线游戏房间频道管理器GameOnlineChannelManager 新增链接：{}-{}",connection.getChannel().remoteAddress(),
                connection.getAcount());

        return 0;
    }

    @Override
    public final int getAccountId(Connection connection) {
        //判断在线字典中是否有此连接的记录  没有说明此连接没有登陆 无法获取到账号id
        if (!isOnline(connection.getAcount())) {
            logger.warn("连接管理器中没有此链接，无法获取账号ID ：{}",connection.getChannel().remoteAddress());
            return -1;
        }
        //返回绑定账号的id
        return accMap.get(connection.getAcount()).getId();
    }

    private boolean match(String acount, String password) throws ServiceException {
        //判断账号是否存在 不存在就谈不上匹配了
        if (!hasAcount(acount)) {
            return false;
        }
        //获取账号的信息 判断密码是否匹配并返回
        return encoder.matches(password, accMap.get(acount).getPassword());
    }

    private boolean isOnline(String acount) {
        return ConnectionManager.getInstance().getConnection(acount) != null;
    }

    private boolean hasAcount(String acount) throws ServiceException {
        return accMap.containsKey(acount);
    }

}