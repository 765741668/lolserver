package com.lol.service.impl;

import com.lol.channel.GameOnlineChannelManager;
import com.lol.common.Constans;
import com.lol.core.Connection;
import com.lol.core.ConnectionManager;
import com.lol.dao.IAcountDAO;
import com.lol.dao.bean.Acount;
import com.lol.db.DAOException;
import com.lol.db.ServiceErrorCode;
import com.lol.db.ServiceException;
import com.lol.service.IAcountService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("acountServiceImpl")
public class AcountServiceImpl implements IAcountService {
    PasswordEncoder encoder = new StandardPasswordEncoder();
    private Logger logger = LoggerFactory.getLogger(AcountServiceImpl.class);

    @Autowired
    private IAcountDAO acountDAO;
    /**
     * 账号与自身具体属性的映射绑定
     */
    private static Map<String, Acount> accMap = new HashMap<>();

    public final int create(String acount, String password) throws ServiceException {
        if (hasAcount(acount)) {
            logger.warn("账号已存在，创建新账号失败");
            return 1;
        }

        Acount model = new Acount();
        model.setAcount(acount);
        model.setPassword(encoder.encode(password));

        try {
            acountDAO.saveAcount(model);
            logger.info("创建新账号成功");
        } catch (DAOException e) {
            throw new ServiceException(ServiceErrorCode.ADD.toString(), "创建新账号失败", e);
        }

        accMap.put(acount, model);

        return 0;
    }

    public final int login(Connection connection, String acount, String password) throws ServiceException {
        if (acount == null || password == null) {
            logger.warn("账号或密码为空，输入不合法");
            return -4;
        }

        if (!hasAcount(acount)) {
            logger.warn("账号不存在，拒绝登陆");
            return -1;
        }
        boolean isOnline = ConnectionManager.getInstance().getConnection(acount) != null;
        if (isOnline) {
            logger.warn("当前账号已经在线");
            return -2;
        }

        if (!match(acount, password)) {
            logger.warn("账号或密码不正确");
            return -3;
        }

        //验证都通过 说明可以登录  让客户端上线
        ChannelHandlerContext ctx = connection.getChannelHandlerContext();
        ctx.attr(Constans.conn).set(connection);
        logger.info("Channel AttributeKey 新增链接：{},玩家角色名：{}",connection.getChannel().remoteAddress(),connection.getAcount());
        ConnectionManager.getInstance().addConnection(acount, connection);
        logger.info("连接管理器ConnectionManager 新增链接：{}",connection.getChannel().remoteAddress(),connection.getAcount());
        GameOnlineChannelManager.getInstance().addOnlineChannel(acount).addOnlineConnection(connection);
        logger.info("在线游戏房间频道管理器GameOnlineChannelManager 新增链接：{}",connection.getChannel().remoteAddress(),
                connection.getAcount());

        return 0;
    }

    public final int getAcountId(Connection connection) {
        //判断在线字典中是否有此连接的记录  没有说明此连接没有登陆 无法获取到账号id
        if (!isOnline(connection.getAcount())) {
            logger.warn("连接管理器中没有此链接，无法获取账号ID ：{}",connection.getChannel().remoteAddress());
            return -1;
        }
        //返回绑定账号的id
        return accMap.get(connection.getAcount()).getId();
    }

    private boolean match(String acount, String password) throws ServiceException {
        init(acount);
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
        init(acount);
        return accMap.containsKey(acount);
    }

    private void init(String acount) throws ServiceException {
        if (accMap.containsKey(acount)) {
            return;
        }
        Map<String, String> condiction = new HashMap<>();
        condiction.put("acount", acount);
        Acount acc = null;
        try {
            acc = acountDAO.getAcountByCondiction(condiction);
        } catch (DAOException e) {
            throw  new ServiceException("根据acount查询Acount对象失败",e);
        }
        if (acc != null) {
            accMap.put(acount, acc);
        }
    }
}