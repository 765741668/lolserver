package org.itstack.demo.netty.msg;

/**
 * Created by fuzhengwei1 on 2016/10/20.
 * 反馈 Response
 */
public class Response {

    private String requestId;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
