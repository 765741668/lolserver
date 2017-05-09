/*
 * @(#)ServiceException.java        1.0 2011-3-26
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
package com.lol.fwk.db;

/**
 * �����쳣
 *
 * @version 1.0 2011-3-26
 * @author YangZH
 * @history
 */

@SuppressWarnings("serial")
public class ServiceException extends Exception {

    /**
     * 根异常，保持异常链
     */
    protected Throwable caused;
    /**
     * 错误代码
     */
    String errorCode;
    /**
     * 异常信息
     */
    String errorMsg;

    public ServiceException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ServiceException(String errorCode, Throwable caused) {
        super(caused);
        this.errorCode = errorCode;
        this.caused = caused;
    }

    public ServiceException(String errorCode, String errorMsg,
                            Throwable caused) {
        super(errorMsg, caused);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.caused = caused;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Throwable getCaused() {
        return caused;
    }


}
