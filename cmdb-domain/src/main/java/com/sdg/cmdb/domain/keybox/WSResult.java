package com.sdg.cmdb.domain.keybox;

import com.sdg.cmdb.domain.ErrorCode;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/12/14.
 */
public class WSResult<T> implements Serializable {
    private static final long serialVersionUID = -6629727321446231548L;

    private String id;

    private String code;

    private String msg;

    private T data;

    /**
     * 0:正常响应;1:登录成功
     */
    private int type;

    public WSResult() {
    }

    public WSResult(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public WSResult(T data) {
        this.data = data;
        this.type = WSContentType.response.getCode();
    }

    public WSResult(String id, T data, int type) {
        this.id = id;
        this.data = data;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
