package com.lol.fwk.anotation;

import java.lang.annotation.*;

/** 
 * 用于在查询的时候 ，放置缓存信息 
 * @author ajun 
 * @email zhaojun2066@gmail.com 
 * @blog http://blog.csdn.net/ajun_studio 
 * 2012-2-27 上午10:42:06 
 */  
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cache {
      
    String prefix();//key的前缀，如咨询：zx
  
    long expiration() default 0;//缓存有效期 0==永久 1000*60*60*2==2小时过期
      
      
}  