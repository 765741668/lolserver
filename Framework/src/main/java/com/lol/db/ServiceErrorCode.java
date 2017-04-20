/*
 * @(#)ServiceErrorCode.java        1.0 2011-4-14
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

/**
 * 服务异常代码类
 *
 * @author YangZH
 * @version 1.0 2011-4-14
 * @history
 */
public enum ServiceErrorCode {

    QUERY("001"), ADD("002"), UPDATE("003"), DELETE("004"),

    /**
     * 增加分类错误
     */
    ADDCATEGORY(ServiceErrorCode.CATEGORY + "001"), UPDATECATEGORY(
            ServiceErrorCode.CATEGORY + "002"), FINDCATEGORY(
            ServiceErrorCode.CATEGORY + "003");

    private final static String CATEGORY = "CATEGORY";

    /**
     * 错误代码
     */
    private final String ERRORCODE;

    private ServiceErrorCode(String code) {
        ERRORCODE = code;
    }

    @Override
    public String toString() {
        return ERRORCODE;
    }

}
