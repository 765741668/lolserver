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
    private Logger looger = LoggerFactory.getLogger(AcountServiceImpl.class);
    @Autowired
    private IAcountDAO acountDAO;
    /**
     * 账号与自身具体属性的映射绑定
     */
    private Map<String, Acount> accMap = new HashMap<>();

    public final int create(Connection connection, String acount, String password) throws ServiceException {
        if (hasAcount(acount)) {
            return 1;
        }

        //创建账号实体并进行绑定
        Acount model = new Acount();
        model.setAcount(acount);
        model.setPassword(encoder.encode(password));

        try {
            acountDAO.saveAcount(model);
        } catch (DAOException e) {
            throw new ServiceException(ServiceErrorCode.ADD.toString(), "新增Acount对象失败", e);
        }

        accMap.put(acount, model);

        return 0;
    }

    public final int login(Connection connection, String acount, String password) throws ServiceException {
        //账号密码为空 输入不合法
        if (acount == null || password == null) {
            return -4;
        }
        //判断账号是否存在  不存在则无法登陆
        if (!hasAcount(acount)) {
            return -1;
        }
        //判断此账号当前是否在线
        boolean isOnline = ConnectionManager.getInstance().getConnection(acount) != null;
        if (isOnline) {
            return -2;
        }
        //判断账号密码是否匹配
        if (!match(acount, password)) {
            return -3;
        }

        //验证都通过 说明可以登录  让客户端上线
        ChannelHandlerContext ctx = connection.getChannelHandlerContext();
        ctx.attr(Constans.conn).set(connection);
        ConnectionManager.getInstance().addConnection(acount, connection);
        GameOnlineChannelManager.getInstance().addOnlineChannel(acount).addOnlineConnection(connection);

        return 0;
    }

    public final void close(Connection connection, String acount) throws ServiceException {
        //如果当前连接有登陆 进行移除
        ChannelHandlerContext ctx = connection.getChannelHandlerContext();
        ctx.attr(Constans.conn).remove();
        ConnectionManager.getInstance().removeConnection(connection);
        GameOnlineChannelManager.getInstance().getOnlineChannel(acount).removeOnlineConnection(connection);
    }

    public final int getAcountId(Connection connection) throws ServiceException {
        //判断在线字典中是否有此连接的记录  没有说明此连接没有登陆 无法获取到账号id
        if (!isOnline(connection.getAcount())) {
            return -1;
        }
        //返回绑定账号的id
        return accMap.get(connection.getAcount()).getId();
    }

    private boolean match(String acount, String password) {
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

    private boolean hasAcount(String acount) {
        init(acount);
        return accMap.containsKey(acount);
    }

    private void init(String acount) {
        if (accMap.containsKey(acount)) {
            return;
        }
        Map<String, String> condiction = new HashMap<>();
        condiction.put("acount", acount);
        Acount acc = null;
        try {
            acc = acountDAO.getAcountByCondiction(condiction);
        } catch (DAOException e) {
            looger.error(e.getMessage(), "根据acount查询Acount对象失败", e);
        }
        if (acc != null) {
            accMap.put(acount, acc);
        }
    }
}