package com.lol.fwk.anotation;


import java.lang.annotation.*;

/**
 * 用于删除缓存 
 * @author ajun 
 * @email zhaojun2066@gmail.com 
 * @blog http://blog.csdn.net/ajun_studio 
 * 2012-2-27 上午10:53:03 
 */  
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Flush {
    String prefix();//key的前缀，如咨询：zx
}  