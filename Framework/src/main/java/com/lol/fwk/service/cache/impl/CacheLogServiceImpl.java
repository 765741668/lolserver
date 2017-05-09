/*
 * @(#)SystemServiceImpl.java        1.0 2011-4-19
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

package com.lol.fwk.service.cache.impl;

import com.lol.fwk.dao.bean.cache.CacheLog;
import com.lol.fwk.dao.cache.ICacheLogManager;
import com.lol.fwk.db.DAOException;
import com.lol.fwk.db.ServiceErrorCode;
import com.lol.fwk.db.ServiceException;
import com.lol.fwk.service.bean.cache.CacheLogBean;
import com.lol.fwk.service.cache.ICacheLogService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description goes here.
 *
 * @version 1.0 2011-4-19
 * @author thinkpad
 * @history
 *
 */
@SuppressWarnings("unchecked")
@Service("cacheLogService")
public class CacheLogServiceImpl implements ICacheLogService {
	public Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private ICacheLogManager cacheLogManager;

    @Override
    public int deleteByPrefix(String prefix) throws ServiceException {
        try {
            List<CacheLog> cacheLogs = this.cacheLogManager.queryCacheLogListByPrefix(prefix);
            for(CacheLog bean:cacheLogs){
                try {
                    cacheLogManager.deleteCacheLog(bean);
                } catch (DAOException e) {
                    log.error("删除缓存数据失败，ID："+bean.getUuid());
                    throw new ServiceException("删除缓存数据失败，ID：",e);
                }
            }
            return cacheLogs.size();
        } catch (DAOException e) {
            log.error("查询缓存数据失败，prefix ："+prefix);
            throw new ServiceException("查询缓存数据失败，prefix ：",e);
        }
    }

    @Override
    public List<CacheLogBean> queryCacheLogBeanListByPrefix(String prefix) throws ServiceException {
        List<CacheLog> cacheLogs ;
        List<CacheLogBean> cacheLogBeans = new ArrayList<>();
        try {
            cacheLogs = this.cacheLogManager.queryCacheLogListByPrefix(prefix);
            for (CacheLog cacheLog : cacheLogs) {
                CacheLogBean cacheLogBean = new CacheLogBean();
                BeanUtils.copyProperties(cacheLogBean, cacheLog);
                cacheLogBeans.add(cacheLogBean);
            }
        } catch (Exception e) {
            log.error(e, e);
            throw new ServiceException(ServiceErrorCode.UPDATE.toString(),
                    "查询缓存列表信息失败");
        }
        return cacheLogBeans;
    }

    @Override
    public void addCacheLog(CacheLogBean cacheLogBean) throws ServiceException {
        if (cacheLogBean == null) {
            throw new ServiceException(ServiceErrorCode.ADD.toString(),
                    "缓存信息不能为空");
        }

        CacheLog cacheLog = new CacheLog();
        try {
            BeanUtils.copyProperties(cacheLog, cacheLogBean);
            cacheLogManager.saveCacheLog(cacheLog);
        } catch (Exception e) {
            log.error(e, e);
            throw new ServiceException(ServiceErrorCode.ADD.toString(),
                    "新增缓存信息失败");
        }
    }
}
