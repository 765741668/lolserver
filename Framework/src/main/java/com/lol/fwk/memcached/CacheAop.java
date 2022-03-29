//package com.lol.fwk.memcached;
//
//import com.lol.fwk.anotation.Cache;
//import com.lol.fwk.anotation.Flush;
//import com.lol.fwk.db.ServiceException;
//import com.lol.fwk.entity.CacheLogBean;
//import com.lol.fwk.entity.cache.CacheLog;
//import com.lol.fwk.service.ICacheLogService;
//import com.lol.fwk.util.DateUtils;
//import com.lol.fwk.util.JsonUtil;
//import com.lol.fwk.util.XMemcaheUtil;
//import net.rubyeye.xmemcached.MemcachedClient;
//import net.rubyeye.xmemcached.exception.MemcachedException;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.concurrent.TimeoutException;
//
//
///**
// * 拦截缓存
// * ClassName: CacheAop <br/>
// * Function: TODO ADD FUNCTION. <br/>
// * Reason: TODO ADD REASON(可选). <br/>
// * date: 2014-4-8 上午10:28:38 <br/>
// *
// * @author Administrator
// * @since JDK 1.6
// */
//@Component
//@Aspect
//public class CacheAop {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private ICacheLogService cacheLogService;
//
//
////    @Resource(name = "memcachedClient")
////    private MemcachedClient memcachedClient;
//
//
//    //定义切面:切入点
//    @Pointcut("execution(* com.lol.fwk.service.*.impl..*.*(..))")
//    public void cachedPointcut() {
//
//    }
//
//    /**
//     * 环绕装备 用于拦截查询 如果缓存中有数据，直接从缓存中读取；否则从数据库读取并将结果放入缓存
//     *
//     * @param call
//     * @return
//     */
//    @Around("cachedPointcut()")
//    public Object doAround(ProceedingJoinPoint call) {
//        Object result = null;
//        Method[] methods = call.getTarget().getClass().getDeclaredMethods();
//        Signature signature = call.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//        for (Method m : methods) {//循环方法，找匹配的方法进行执行
//            if (m.getName().equals(method.getName())) {
//                MemcachedClient memcachedClient = XMemcaheUtil.getClient();
//                if (m.isAnnotationPresent(Cache.class)) {
//                    Cache cache = m.getAnnotation(Cache.class);
//                    if (cache != null) {
//                        String tempKey = this.getKey(method, call.getArgs());
//                        String prefix = cache.prefix();
//                        String key = prefix + "_" + tempKey;
//                        logger.info("开始获取Memcached缓存");
//                        try {
//                            result = memcachedClient.get(key);
//                        } catch (TimeoutException e) {
//                            logger.error("获取Memcached缓存超时，缓存Key值：" + key, e);
//
//                        } catch (InterruptedException e) {
//                            logger.error("获取Memcached缓存时线程其它线程被中断，缓存Key值:" + key, e);
//
//                        } catch (MemcachedException e) {
//                            logger.error("获取Memcached缓存失败，缓存Key值:" + key, e);
//                        }
//
//                        logger.info("获取Memcached缓存完毕，缓存值：" + result);
//
//                        Class<?> clazz = m.getReturnType();
//                        try {
//                            if (null == result) {
//                                logger.info("缓存为空，开始新增Memcached缓存");
//                                try {
//                                    logger.info("获取方法返回值");
//                                    result = call.proceed();
//                                } catch (Throwable e) {
//                                    logger.error("获取方法返回值出错，方法名：" + m.getName(), e);
//
//                                }
//                                //返回类型为集合
//                                Object json;
//                                if (clazz.isAssignableFrom(List.class)
//                                        || clazz.isAssignableFrom(Set.class)
//                                        || clazz.isAssignableFrom(ArrayList.class)
//                                        || clazz.isAssignableFrom(HashSet.class)) {
//
//                                    json = JSONArray.fromObject(result);
//                                } else {
//
//                                    json = JSONObject.fromObject(result);
//                                }
//                                long expiration = cache.expiration();//3 * 60==2小时过期
//                                int keeptime = (int) (expiration);
//                                memcachedClient.set(key, keeptime, json);
//                                logger.info("新增Memcached缓存成功，缓存key值： " + key);
//
//                                logger.info("将Memcached缓存信息存入数据库作记录");
//                                CacheLog cacheLogBean = new CacheLog();
//                                cacheLogBean.setUuid(UUID.randomUUID().toString());
//                                cacheLogBean.setPrefix(prefix);
//                                cacheLogBean.setCacheKey(key);
//                                cacheLogBean.setCreateTime(DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
//                                this.cacheLogService.addCacheLog(cacheLogBean);
//
//                                logger.info("新增数据库缓存记录完成，ID：" + cacheLogBean.getUuid() + " cacheKey: " + key);
//                            } else {
//                                logger.info("开始直接读取已有缓存");
//                                if (clazz.isAssignableFrom(List.class)
//                                        || clazz.isAssignableFrom(Set.class)
//                                        || clazz.isAssignableFrom(ArrayList.class)
//                                        || clazz.isAssignableFrom(HashSet.class)) {
//                                    logger.info("开始处理方法返回值，将json缓存值转换成对应的集合");
//                                    result = JsonUtil.json2ListBeanEx(result.toString(), clazz);
//                                } else {
//                                    logger.info("开始处理方法返回值，将json缓存值转换成对应的实体");
//                                    result = JsonUtil.json2BeanEx(result.toString(), clazz);
//                                }
//
//                                logger.info("直接读取已有缓存完毕");
//                            }
//                        } catch (TimeoutException e) {
//                            logger.error("新增缓存超时，缓存Key值：" + key, e);
//
//                        } catch (InterruptedException e) {
//                            logger.error("新增缓存时线程其它线程被中断，缓存Key值:" + key, e);
//
//                        } catch (MemcachedException e) {
//                            logger.error("新增缓存失败，缓存Key值: {}", key, e);
//                        } catch (ServiceException e) {
//                            logger.error(e.getMessage(), e);
//
//                        }
//                    }
//                } else if (m.isAnnotationPresent(Flush.class)) {
//                    logger.info("开始删除缓存");
//                    Flush flush = m.getAnnotation(Flush.class);
//                    if (flush != null) {
//                        logger.info("开始查询缓存数据");
//                        String prefix = flush.prefix();
//                        List<CacheLogBean> logs = null;
//                        try {
//                            logs = cacheLogService.queryCacheLogBeanListByPrefix(prefix);
//                            for (CacheLogBean bean : logs) {
//                                logger.info("获取要从数据库中移除的缓存记录数据成功：ID：" + bean.getUuid() + " cacheKey: " + bean.getCacheKey());
//                            }
//                        } catch (ServiceException e) {
//                            logger.error(e.getMessage(), e);
//                            logger.error(e.getErrorMsg(), e);
//                        }
//                        if (logs != null && !logs.isEmpty()) {
//                            logger.info("开始删除数据库缓存数据");
//                            int rows = 0;
//                            try {
//                                rows = cacheLogService.deleteByPrefix(prefix);
//                                logger.info("删除全部数据库缓存prefix为【" + prefix + "】的数据成功");
//                            } catch (ServiceException e) {
//                                logger.error(e.getErrorMsg(), e);
//                            }
//                            if (rows > 0) {
//                                //删除缓存
//                                logs.stream().filter(logbean -> logger != null).forEach(logbean -> {
//                                    String key = logbean.getCacheKey();
//                                    try {
//                                        logger.info("开始删除Memcahed缓存数据");
//                                        memcachedClient.delete(key);//删除缓存
//                                        logger.info("删除Memcahed缓存数据成功，缓存key值：" + key);
//                                    } catch (TimeoutException e) {
//                                        logger.error("删除缓存超时，缓存Key值：" + key, e);
//
//                                    } catch (InterruptedException e) {
//                                        logger.error("删除缓存时线程其它线程被中断，缓存Key值:" + key, e);
//
//                                    } catch (MemcachedException e) {
//                                        logger.error("删除缓存失败，缓存Key值:" + key, e);
//
//                                    }
//                                });
//                            }
//                        }
//                    }
//                } else {
//                    try {
//                        logger.info("没有任何缓存注解，直接获取方法返回值");
//                        result = call.proceed();
//                    } catch (Throwable e) {
//                        logger.error("获取方法返回值出错，方法名：" + m.getName(), e);
//
//                    }
//                }
//
//                logger.info("关闭缓存链接");
//                try {
//                    if (!memcachedClient.isShutdown()) {
//                        memcachedClient.shutdown();
//                        logger.info("关闭缓存链接成功");
//                    }
//                } catch (IOException e) {
//                    logger.error("关闭缓存链接失败", e);
//
//                }
//                break;
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 组装key值
//     *
//     * @param method
//     * @param args
//     * @return
//     */
//    private String getKey(Method method, Object[] args) {
//        StringBuilder key = new StringBuilder();
//        String methodName = method.getName();
//        key.append(methodName);
//        if (args != null && args.length > 0) {
//            for (Object arg : args) {
//                key.append("_").append(arg);
//            }
//        }
//
//        return key.toString();
//
//    }
//}