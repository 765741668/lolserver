/**
 * Project Name:springMVC3
 * File Name:IMissionService.java
 * Package Name:com.zds.service
 * Date:2014-4-8上午10:28:38
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved.
 *
 */
package com.lol.fwk.service;

import com.lol.fwk.entity.cache.CacheLog;
import com.lol.fwk.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: IMissionService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014-4-8 上午10:28:38 <br/>
 *
 * @author Administrator
 * @version
 * @since JDK 1.6
 */
@Service
public interface ICacheLogService {

    void addCacheLog(CacheLog cacheLogBean) throws ServiceException;
    List<CacheLog> queryCacheLogBeanListByPrefix(String prefix) throws ServiceException;
    int deleteByPrefix(String prefix) throws ServiceException;

}
