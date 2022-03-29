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

package com.lol.fwk.service.impl;

import com.lol.fwk.entity.cache.CacheLog;
import com.lol.fwk.exception.ServiceException;
import com.lol.fwk.service.ICacheLogService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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

    @Override
    public int deleteByPrefix(String prefix) throws ServiceException {
        return 0;
    }

    @Override
    public void addCacheLog(CacheLog CacheLog) throws ServiceException {

    }

    @Override
    public List<CacheLog> queryCacheLogBeanListByPrefix(String prefix) throws ServiceException {
        return null;
    }
}
