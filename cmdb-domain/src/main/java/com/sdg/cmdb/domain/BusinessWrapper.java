package com.sdg.cmdb.domain;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/1.
 */
public class BusinessWrapper<T> implements Serializable {
    private static final long serialVersionUID = 4883398637828372826L;

    private boolean success;

    private String msg;

    private String code;

    private T body;

    public BusinessWrapper(ErrorCode errorCode) {
        this.success = false;
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public BusinessWrapper(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessWrapper(T body) {
        this.success = true;
        this.body = body;
    }

    public BusinessWrapper(boolean success, String msg) {
        this.success = false;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "BusinessWrapper{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", body=" + body +
                '}';
    }
}
