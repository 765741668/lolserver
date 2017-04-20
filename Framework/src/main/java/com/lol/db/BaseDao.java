package com.lol.db;


import com.lol.util.Page;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DAO操作类
 *
 * @author YangZH
 * @version 1.0 2011-3-26
 * @history
 */
@SuppressWarnings("unchecked")
public class BaseDao<T> extends HibernateDaoSupport implements
        DBManager<T> {

    Logger logger = LoggerFactory.getLogger(BaseDao.class);

    /**
     * 数据插入
     *
     * @param obj
     * @return
     * @throws DAOException
     */
    public Object save(T obj) throws DAOException {
        try {
            return this.getHibernateTemplate().save(obj);
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.SAVE, ex.getMessage(), ex);
        }
    }

    /**
     * 数据更新
     *
     * @param obj
     * @throws DAOException
     */
    public void update(T obj) throws DAOException {
        try {
            this.getHibernateTemplate().saveOrUpdate(obj);
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.UPDATE, ex.getMessage(), ex);
        }
    }

    /**
     * 数据删除
     *
     * @param obj
     * @throws DAOException
     */
    public void delete(T obj) throws DAOException {
        try {
            this.getHibernateTemplate().delete(obj);
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.DELETE, ex.getMessage(), ex);
        }
    }

    /**
     * 立即加载数据
     */
    @Override
    public T get(Class clazz, Serializable id) throws DAOException {
        try {
            return (T) this.getHibernateTemplate().get(clazz, id);
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.QUERY, ex.getMessage(), ex);
        }
    }

    /**
     * 通用查找功能
     *
     * @param hql    hql语句
     * @param params 参数
     * @return
     * @throws DAOException
     */
    public List<T> findByHql(String hql, Object[] params) throws DAOException {
        try {
            List<T> list = (List<T>) this.getHibernateTemplate().find(hql, params);
            if (list == null || list.size() == 0)
                return new ArrayList<T>();
            return list;
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.QUERY, ex.getMessage(), ex);
        }
    }

    /**
     * 延迟加载实体bean
     *
     * @param clazz
     * @param id
     * @return
     */
    @Override
    public T load(Class clazz, Serializable id) throws DAOException {
        return (T) this.getHibernateTemplate().load(clazz, id);
    }

    /**
     * 通用查找功能
     *
     * @param sql    sql语句
     * @param params 参数
     * @return
     * @throws DAOException
     */
    public List<T> findBySql(final String sql, final Object[] params)
            throws DAOException {
        try {
            return (List<T>) this.getHibernateTemplate().execute(
                    new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                throws HibernateException {
                            Query query = session.createNativeQuery(sql);
                            if (params != null) {
                                for (int i = 0; i < params.length; i++) {
                                    if (params[i] instanceof AuditStateEnum) {
                                        query.setParameter(i, ((AuditStateEnum) params[i]).getValue());
                                    } else {
                                        query.setParameter(i, params[i]);
                                    }
                                }
                            }
                            return query.list();
                        }

                    }
            );
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.QUERY, ex.getMessage(), ex);
        }
    }

    /**
     * 通过sql查询多个对象
     *
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public List<T> findAllBySql(String sql, Class clazz, List params) {
        Query sqlQuery = (Query) this.getSessionFactory().getCurrentSession().createNativeQuery(sql, clazz);
        for (int i = 0, count = params.size(); i < count; i++) {
            sqlQuery = sqlQuery.setParameter(i, params.get(i).toString());
        }
        return sqlQuery.list();
    }

    public List<T> queryBySql(final String sql, final Object[] params)
            throws DAOException {
        try {
            final List list = new ArrayList();
            // 执行JDBC的数据批量保存
            Work jdbcWork = new Work() {
                public void execute(Connection connection) throws SQLException {
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    try {
                        ps = connection.prepareStatement(sql);
                        if (params != null) {
                            for (int i = 0; i < params.length; i++) {
                                ps.setObject(i + 1, params[i]);
                            }
                        }
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            ResultSetMetaData md = rs.getMetaData();
//                            Map map = new HashMap();
                            for (int i = 1; i <= md.getColumnCount(); i++) {
//                                map.put(md.getColumnLabel(i), rs.getObject(i));
                                list.add(rs.getObject(i));
                            }
                        }
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                        if (ps != null) {
                            ps.close();
                        }
                    }
                }
            };

            this.getSessionFactory().getCurrentSession().doWork(jdbcWork);

            return list;
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.QUERY, ex.getMessage(), ex);
        }
    }

    /**
     * 通用的更新方法
     *
     * @param hql
     * @param params
     * @throws DAOException
     */
    public void updateByHql(String hql, Object[] params) throws DAOException {
        try {
            this.getHibernateTemplate().execute(
                    new UpdateExecuteBack(hql, params));
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.UPDATE, ex.getMessage(), ex);
        }
    }

    /**
     * 根据hql删除
     *
     * @param hql
     * @param params
     * @return 删除的记录数
     * @throws DAOException
     */
    public int deleteByHql(String hql, Object[] params) throws DAOException {
        try {
            return (Integer) this.getHibernateTemplate().execute(
                    new DeleteExecuteBack(hql, params));
        } catch (Exception ex) {
            throw new DAOException(ErrorCode.DaoCode.DELETE, ex.getMessage(), ex);
        }
    }

    /**
     * 通用查找功能,查找一个对象
     *
     * @param hql    hql语句
     * @param params 参数
     * @return
     * @throws DAOException
     */
    public T findUniqueObjectByHql(final String hql, final Object[] params)
            throws DAOException {

        try {
            return (T) this.getHibernateTemplate().execute(
                    new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                throws HibernateException {
                            Query query = session.createQuery(hql);
                            if (params != null) {
                                for (int i = 0; i < params.length; i++) {
                                    query.setParameter(i, params[i]);
                                }
                            }
                            Object obj = null;
                            obj = query.uniqueResult();
                            return obj;
                        }
                    }
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(ErrorCode.DaoCode.QUERY, "返回多条记录");
        }
    }

    /**
     * 分页查找功能
     *
     * @param hql      查找语句
     * @param params   查找参数
     * @param page     页号
     * @param pageSize 页大小
     * @return
     * @throws DAOException
     */
    public List<T> findPagedByHql(String hql, Object[] params, int page,
                                  int pageSize) throws DAOException {
        try {
            List<T> list = (List<T>) this.getHibernateTemplate().execute(
                    new PagedCallBack(hql, params, page, pageSize));
            if (list == null || list.size() == 0)
                return new ArrayList<T>();

            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(ErrorCode.DaoCode.QUERY, "返回多条记录");
        }
    }

    /**
     * 分页查找
     *
     * @param hql
     * @param params
     * @param page
     * @return
     * @throws DAOException
     */
    public List<T> findPagedByHql(String hql, Object[] params, Page page)
            throws DAOException {
        try {
            List<T> list = (List<T>) this.getHibernateTemplate().execute(
                    new PagedCallBack(hql, params, page.getCurrPage(), page
                            .getPageSize())
            );
            if (list == null) {
                list = new ArrayList();
            }

            /** 统计总的页数 */
            String countSql = getCountSql(hql2Sql(hql, this.getSessionFactory()));

            List temp = (List) this.findBySql(countSql, params);
            if (temp != null) {
                Object totalCount = temp.get(0);
                /*if (totalCount instanceof Integer) {
					page.setTotalRecord(((Integer) totalCount));
				} else if (totalCount instanceof BigInteger) {
					page.setTotalRecord(((BigInteger) totalCount).intValue());
				}*/
                String tempTotalCount = totalCount + "";
                if (tempTotalCount.length() >= 5) {
                    BigInteger count = new BigInteger(tempTotalCount);
                    page.setTotalRecord(count.intValue());
                } else {
                    page.setTotalRecord(Integer.parseInt(tempTotalCount));
                }


            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(ErrorCode.DaoCode.QUERY, "返回多条记录");
        }
    }

    /**
     * HQL转化成SQL语句
     *
     * @param originalHql
     * @param sessionFactory
     * @return
     * @throws Exception
     */
    protected String hql2Sql(String originalHql,
                             org.hibernate.SessionFactory sessionFactory) throws Exception {
        QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(
                originalHql, originalHql, Collections.EMPTY_MAP,
                (SessionFactoryImplementor) sessionFactory);

        queryTranslator.compile(Collections.EMPTY_MAP, false);
        return queryTranslator.getSQLString().toLowerCase();
    }

    /**
     * 生成查询记录条数的SQL
     *
     * @param sql
     * @return
     */
    protected String getCountSql(String sql) {
        // logger.info("去掉order by子句之前:" + sql);
        sql = sql.replaceAll("(order\\s+by)(.+)($)", "");
        // logger.info("去掉order by子句之后:" + sql);

        return "select count(*) from (" + sql + ") tmp_count_t";
    }

    /**
     * 查询分页总记录数
     *
     * @param hql    HQL语句
     * @param params 参数
     */
    public int queryCount(String hql, Object[] params) {
        try {
            List<Object> list = (List<Object>) this.getHibernateTemplate().find(hql, params);
            if (list == null || list.size() == 0)
                return 0;
            return ((Long) list.get(0)).intValue();
        } catch (Exception e) {
            logger.error(
                    "查询分页总记录数[" + e.getMessage() + "]", e);
            return 0;
        }
    }

    /**
     * 根据sql语句查找
     *
     * @param sql
     * @return
     */
    public Object findBySql(final String sql) {
        Object list = this.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) {
                        Query query = session.createNativeQuery(sql);
                        return query.list();
                    }
                }
        );
        return list;
    }

    /**
     * 查询分页总记录数
     *
     * @param sql    sql语句
     * @param params 参数
     */
    public int findBySql(final StringBuffer sql, final Object[] params) {
        return (Integer) this.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException {
                        Query query = session.createNativeQuery(sql.toString());

                        for (int i = 0; i < params.length; i++) {
                            query.setParameter(i, params[i]);
                        }
                        if (query.list().size() > 0) {
                            return ((BigDecimal) query.list().get(0))
                                    .intValue();
                        }
                        return 0;
                    }

                }
        );
    }

    public void executeBySql(final String sql, final Object[] params) {
        this.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException {
                Query query = session.createNativeQuery(sql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                return query.executeUpdate();
            }

        });
    }

    /**
     * 通用查找功能
     *
     * @param sql    . sql语句
     * @param params 参数
     * @return
     * @throws java.sql.SQLException
     * @throws DAOException
     */
    public List<T> findPageBySql(String sql, Object[] params, int page,
                                 int pageSize) {

        List<T> list = (List<T>) this.getHibernateTemplate().execute(
                new PageCallBackSql(sql, params, page, pageSize));
        if (list == null || list.size() == 0)
            return new ArrayList<T>();

        return list;
    }

    public List<T> findPagedBySql(String sql, Object[] params, Page page)
            throws DAOException {
        try {
            List<T> list = (List<T>) this.getHibernateTemplate().execute(
                    new PageCallBackSql(sql, params, page.getCurrPage(), page
                            .getPageSize())
            );
            if (list == null) {
                list = new ArrayList();
            }

            /** 统计总的页数 */
            String countSql = getCountSql(sql);
            List<BigInteger> temp = (List<BigInteger>) this.findBySql(countSql,
                    params);
            if (temp != null) {
                Object totalCount = temp.get(0);
                if (totalCount instanceof Integer) {
                    page.setTotalRecord(((Integer) totalCount));
                } else if (totalCount instanceof BigInteger) {
                    page.setTotalRecord(((BigInteger) totalCount).intValue());
                }
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(ErrorCode.DaoCode.QUERY, "返回多条记录");
        }
    }

    public List<T> findPagedBySql(String sql, Object[] params, Class clazz,
                                  Page page) throws DAOException {
        try {
            List<T> list = (List<T>) this.getHibernateTemplate().execute(
                    new PageCallBackSql(sql, params, clazz, page.getCurrPage(),
                            page.getPageSize())
            );
            if (list == null) {
                list = new ArrayList();
            }

            /** 统计总的页数 */
            String countSql = getCountSql(sql);
            List<BigInteger> temp = (List<BigInteger>) this.findBySql(countSql,
                    params);
            if (temp != null) {
                Object totalCount = temp.get(0);
                if (totalCount instanceof Integer) {
                    page.setTotalRecord(((Integer) totalCount));
                } else if (totalCount instanceof BigInteger) {
                    page.setTotalRecord(((BigInteger) totalCount).intValue());
                } else if (totalCount instanceof BigDecimal) {
                    page.setTotalRecord(((BigDecimal) totalCount).intValue());
                }
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DAOException(ErrorCode.DaoCode.QUERY, "返回多条记录");
        }
    }

    public void bulkDelete(final String hql, final String[] ids) throws HibernateException {
        this.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);

                query.setParameterList("ids", ids);
                return query.executeUpdate();
            }
        });
    }

    /**
     * 根据自定义类查询 分页
     *
     * @param sql
     * @param params
     * @param clazz
     * @return
     */
    public List<T> queryPageBySql(final String sql, final Object[] params, final Class clazz, final Page page) {
        Object o = this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                Field[] fields = clazz.getDeclaredFields();
                for (Field f : fields) {
                    f.setAccessible(true);
                    if (f.getName().equalsIgnoreCase("SerialVersionUID")) {
                        continue;
                    }
                    query.addScalar(f.getName());
                }
                query.setResultTransformer(Transformers.aliasToBean(clazz));

                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                query.setFirstResult(page.getCurrPage() * page.getPageSize() - page.getPageSize());
                query.setMaxResults(page.getPageSize());
                List list = query.list();
                if (null == list) {
                    list = new ArrayList();
                }
                return list;
            }
        });
        /** 统计总的页数 */
        String countHql = getCountSql(sql);
        List<BigInteger> temp;
        try {
            temp = (List<BigInteger>) this.findBySql(countHql,
                    params);
            if (temp != null) {
                Object totalCount = temp.get(0);
                if (totalCount instanceof Integer) {
                    page.setTotalRecord(((Integer) totalCount));
                } else if (totalCount instanceof BigInteger) {
                    page.setTotalRecord(((BigInteger) totalCount).intValue());
                } else if (totalCount instanceof BigDecimal) {
                    page.setTotalRecord(((BigDecimal) totalCount).intValue());
                }
            }
        } catch (DAOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return (List<T>) o;
    }

    /**
     * 通过sql 查询返回自定义实体类  自定义实体表示 数据库无对应表的类
     *
     * @param sql
     * @param params
     * @param clazz
     * @return
     */
    public T getObjectBySql(final String sql, final Object[] params, final Class clazz) {
        Object o = this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                Field[] fields = clazz.getDeclaredFields();
                for (Field f : fields) {
                    f.setAccessible(true);
                    if (f.getName().equalsIgnoreCase("SerialVersionUID")) {
                        continue;
                    }
                    query.addScalar(f.getName());
                }
                query.setResultTransformer(Transformers.aliasToBean(clazz));

                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                List list = query.list();
                if (null == list || list.size() == 0) {
                    return null;
                }
                return list.get(0);
            }
        });
        return (T) o;
    }

    /**
     * 返回任意单个数据
     *
     * @param sql
     * @param params
     */
    public T getObjectBySql(final String sql, final Object[] params) {
        Object o = this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createNativeQuery(sql);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                List list = query.list();
                if (null == list || list.size() == 0) {
                    return null;
                }
                return list.get(0);
            }
        });
        return (T) o;
    }

    /**
     * 根据自定义类查询 不分页
     *
     * @param sql
     * @param params
     * @param clazz
     * @return
     */
    public List<T> queryBySql(final String sql, final Object[] params, final Class clazz) {
        Object o = this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(sql);
                Field[] fields = clazz.getDeclaredFields();
                for (Field f : fields) {
                    f.setAccessible(true);
                    if (f.getName().equalsIgnoreCase("SerialVersionUID")) {
                        continue;
                    }
                    query.addScalar(f.getName());
                }
                query.setResultTransformer(Transformers.aliasToBean(clazz));

                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        query.setParameter(i, params[i]);
                    }
                }
                List list = query.list();
                if (null == list) {
                    list = new ArrayList();
                }
                return list;
            }
        });
        return (List<T>) o;
    }

    /**
     * 批量查询
     *
     * @param hql
     * @param ids
     * @return
     */
    public List<T> batchQuery(final String hql, final String aliases, final String[] ids) {
        Object o = this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setParameterList(aliases, ids);
                List list = query.list();
                if (null == list) {
                    list = new ArrayList();
                }
                return list;
            }
        });
        return (List<T>) o;
    }

    private class PagedCallBack implements HibernateCallback {
        /**
         * 查询hql语句
         */
        private String hql;

        /**
         * 查询参数对象数组
         */
        private Object[] params;

        /**
         * 页号 从1开始
         */
        private int page;

        /**
         * 页大小
         */
        private int pageSize;

        public PagedCallBack(String hql, Object[] params, int page, int pageSize) {
            this.hql = hql;
            this.params = params;
            this.page = page;
            this.pageSize = pageSize;
        }

        /**
         * 分页查询
         */
        public Object doInHibernate(Session session) throws HibernateException {
            Query query = session.createQuery(hql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i, params[i]);
                }
            }
            query.setFirstResult(page * pageSize - pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        }
    }

    private class UpdateExecuteBack implements HibernateCallback {
        /**
         * 查询hql语句
         */
        private String hql;

        /**
         * 查询参数对象数组
         */
        private Object[] params;

        public UpdateExecuteBack(String hql, Object[] params) {
            this.hql = hql;
            this.params = params;
        }

        /**
         * 分页查询
         */
        public Object doInHibernate(Session session) throws HibernateException {
            Query query = session.createQuery(hql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i, params[i]);
                }
            }

            return query.executeUpdate();
        }
    }

    private class DeleteExecuteBack implements HibernateCallback {
        /**
         * 查询hql语句
         */
        private String hql;

        /**
         * 查询参数对象数组
         */
        private Object[] params;

        public DeleteExecuteBack(String hql, Object[] params) {
            this.hql = hql;
            this.params = params;
        }

        public Object doInHibernate(Session session) throws HibernateException {
            Query query = session.createQuery(hql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i, params[i]);
                }
            }

            return query.executeUpdate();
        }

    }

    /**
     * 分页查找callBack类.
     *
     * @author PageCallBackSql
     * @version 1.0 2009-8-4
     * @history
     */
    private class PageCallBackSql implements HibernateCallback {
        /**
         * 查询hql语句
         */
        private String hql;

        /**
         * 查询参数对象数组
         */
        private Object[] params;

        /**
         * 页号 从1开始
         */
        private int page;

        /**
         * 页大小
         */
        private int pageSize;

        /**
         * 转换成的实体类
         */
        private Class clazz = null;

        public PageCallBackSql(String sql, Object[] params, int page,
                               int pageSize) {
            this.hql = sql;
            this.params = params;
            this.page = page;
            this.pageSize = pageSize;
        }

        public PageCallBackSql(String sql, Object[] params, Class clazz,
                               int page, int pageSize) {
            this.hql = sql;
            this.params = params;
            this.clazz = clazz;
            this.page = page;
            this.pageSize = pageSize;
        }

        public PageCallBackSql(String sql, Object[] params, Class clazz) {
            this.hql = sql;
            this.params = params;
            this.clazz = clazz;
        }

        /**
         * 分页查询
         */
        public Object doInHibernate(Session session) throws HibernateException {
            Query query;
            if (clazz != null) {
                query = session.createQuery(hql, clazz);
            } else {
                query = session.createQuery(hql, clazz);
            }
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i, params[i]);
                }
            }
            query.setFirstResult(page * pageSize - pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        }
    }
}
