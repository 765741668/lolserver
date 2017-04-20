/*
 * @(#)DBManager.java        1.0 2011-3-26
 *
 * Copyright (c) 2011-2011 Perfect Team(PT).
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * PT. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use 
 * it only in accordance with the terms of the license agreement you 
 * entered into with PT.
 */
package com.lol.db;

import java.io.Serializable;
import java.util.List;

/**
 * DAO操作接口.
 *
 * @param <T>
 * @version 1.0 2011-3-26
 * @author YangZH
 * @history
 */
public interface DBManager<T extends Object> {
    /**
     * 数据插入
     *
     * @param obj
     * @return
     * @throws DAOException
     */
    public Object save(T obj) throws DAOException;

    /**
     * 数据更新
     *
     * @param obj
     * @throws DAOException
     */
    public void update(T obj) throws DAOException;

    /**
     * 数据删除
     *
     * @param obj
     * @throws DAOException
     */
    public void delete(T obj) throws DAOException;

    /**
     * 读取对象
     *
     * @param clazz
     * @param id
     * @return
     * @throws DAOException
     */
    public T get(Class clazz, Serializable id) throws DAOException;

    /**
     * 通用查找功能
     *
     * @param hql    hql语句
     * @param params 参数
     * @return
     * @throws DAOException
     */
    public List<T> findByHql(String hql, Object[] params) throws DAOException;

    /**
     * 根据sql语句查找
     *
     * @param sql
     * @return
     */
    public Object findBySql(String sql) throws DAOException;

    /**
     * 通用查找功能
     *
     * @param sql    sql语句
     * @param params 参数
     * @return
     * @throws DAOException
     */
    public List<T> findBySql(String sql, Object[] params) throws DAOException;

    /**
     * 通用的更新方法
     *
     * @param hql
     * @param params
     * @throws DAOException
     */
    public void updateByHql(String hql, Object[] params) throws DAOException;

    /**
     * 通用查找功能,查找一个对象
     *
     * @param hql    hql语句
     * @param params 参数
     * @return
     * @throws DAOException
     */
    public T findUniqueObjectByHql(final String hql, final Object[] params)
            throws DAOException;

    /**
     * 加载实体bean
     *
     * @param clazz
     * @param id
     * @return
     */
    public T load(Class clazz, Serializable id) throws DAOException;

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
                                  int pageSize) throws DAOException;

/*	public String getDBTime();*/

}
