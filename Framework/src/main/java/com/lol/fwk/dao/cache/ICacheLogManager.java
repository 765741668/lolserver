/**
 * 
 */
package com.lol.fwk.dao.cache;

import com.lol.fwk.dao.bean.cache.CacheLog;
import com.lol.fwk.db.DAOException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YangZH
 * @version 1.0 2011-3-26
 * @history
 */
@Repository
public interface ICacheLogManager {

    /**
     * 根据prefix前缘查询所有缓存列表
     * @param orgId
     * @return
     * @throws DAOException
     */
	List<CacheLog> queryCacheLogListByPrefix(String orgId) throws DAOException;

    void saveCacheLog(CacheLog cacheLog) throws DAOException;

    void deleteCacheLog(CacheLog cacheLog) throws DAOException;

}
