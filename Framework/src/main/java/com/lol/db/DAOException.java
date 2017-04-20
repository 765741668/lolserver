/*
 * @(#)DBException.java        1.0 2011-3-26
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
 * DAO异常
 *
 * @version 1.0 2011-3-26
 * @author YangZH
 * @history
 */
@SuppressWarnings("serial")
public class DAOException extends Exception {

    /**
     * 根异常，保持异常链
     */
    protected Throwable caused;
    /**
     * 异常代码
     */
    ErrorCode.DaoCode errorCode;
    /**
     * 异常信息
     */
    String errorMsg;

    public DAOException(ErrorCode.DaoCode errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public DAOException(ErrorCode.DaoCode errorCode, Throwable caused) {
        super(caused);
        this.errorCode = errorCode;
        this.caused = caused;
    }

    public DAOException(ErrorCode.DaoCode errorCode, String errorMsg,
                        Throwable caused) {
        super(errorMsg, caused);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.caused = caused;
    }

    public ErrorCode.DaoCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Throwable getCaused() {
        return caused;
    }


}
