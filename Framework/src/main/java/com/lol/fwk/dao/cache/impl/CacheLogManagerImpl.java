package com.lol.fwk.dao.cache.impl;

import com.lol.fwk.dao.bean.cache.CacheLog;
import com.lol.fwk.dao.cache.ICacheLogManager;
import com.lol.fwk.db.BaseDao;
import com.lol.fwk.db.DAOException;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository("cacheLogManager")
public class CacheLogManagerImpl extends BaseDao<CacheLog> implements ICacheLogManager {

    @Override
    public List<CacheLog> queryCacheLogListByPrefix(String prefix) throws DAOException {
        String hql = "from CacheLog where prefix = ? ";
        Object[] params = new Object[] { prefix };
        return this.findByHql(hql,
                params);
    }

    @Override
    public void saveCacheLog(CacheLog cacheLog) throws DAOException {
        this.save(cacheLog);
    }

    @Override
    public void deleteCacheLog(CacheLog cacheLog) throws DAOException {
        this.delete(cacheLog);
    }
}