package com.lol.fwk.exception;


import com.lol.fwk.enums.ResultCodeEnum;

/**
 * @description: 自定义异常
 * @author: linhaibo
 * @since: 2019-05-20
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;

    public ServiceException(ResultCodeEnum resultCode){
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }


    public ServiceException(Integer code,String msg) {
        super(msg);
        this.code = code;
    }


    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
