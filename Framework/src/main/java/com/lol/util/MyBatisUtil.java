package com.lol.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * MyBatis工具类
 *
 * @author Randy
 *         2015-2-6
 */
public class MyBatisUtil {

    private static MyBatisUtil instance = new MyBatisUtil();
    /**
     * 数据库会话
     */
    private SqlSession sqlSession;

    public static MyBatisUtil getInstance() {
        return instance;
    }

    /**
     * 取得会话
     *
     * @param autoCommit
     * @return
     */
    public SqlSession getSession(boolean autoCommit) {
        try {
            SqlSessionFactoryBuilder FactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = FactoryBuilder.build(ProReaderUtil.getInstance().getMyBatisPro());
            sqlSession = sqlSessionFactory.openSession(autoCommit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sqlSession;
    }

    /**
     * 关闭会话
     */
    public void closeSession() {
        if (sqlSession != null)
            sqlSession.close();
    }
}
