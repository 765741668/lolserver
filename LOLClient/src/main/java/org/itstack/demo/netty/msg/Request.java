package org.itstack.demo.netty.msg;

/**
 * Created by fuzhengwei1 on 2016/10/20.
 * 请求 Request
 */
public class Request {

    private String requestId;
    private String param;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
