package com.sdg.cmdb.extend;

import java.io.Serializable;

/**
 * Created by zxxiao on 2017/5/15.
 */
public class InvokeResult<T> implements Result, Serializable {
    private static final long serialVersionUID = 2914380639843410836L;

    /**
     * 状态
     */
    private boolean success;

    /**
     * 消息
     */
    private String msg;

    /**
     * 异常
     */
    private Throwable throwable;

    /**
     * 业务响应
     */
    private T result;

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

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
