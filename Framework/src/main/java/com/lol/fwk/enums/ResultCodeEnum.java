package com.lol.fwk.enums;

/**
 * @description: 自定义状态码枚举类
 * 参考： http://www.cnblogs.com/zhanghengscnc/p/8824820.html
 * @author: linhaibo
 * @date: 2019-04-30 15:16
 */
public enum ResultCodeEnum {


    /**
     * 成功状态码
     */
    SUCCESS(1, "请求成功"),

    /**
     * 失败状态码
     */
    FAIL(0, "失败"),

    ;


    private int code;
    private String msg;

    ResultCodeEnum() {
    }

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    @Override
    public String toString() {
        return "ResultCodeEnum [code=" + code + ", msg=" + msg + "]";
    }
}
