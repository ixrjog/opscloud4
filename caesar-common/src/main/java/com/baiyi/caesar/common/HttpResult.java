package com.baiyi.caesar.common;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.common.exception.BaseException;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import lombok.Data;


@Data
public class HttpResult<T> {

    public static final HttpResult<Boolean> SUCCESS = new HttpResult<>(true);

    private T body;

    private boolean success;

    private String msg;

    private int code;

    public HttpResult(T body) {
        this.body = body;
        this.msg = "success";
        this.success = true;
    }

    public HttpResult(BaseException ex) {
        this.msg = ex.getMessage();
        this.code = ex.getCode();
        this.success = false;
    }

    public HttpResult(BusinessWrapper<T> wrapper) {
        this.success = wrapper.isSuccess();
        if (wrapper.isSuccess()) {
            this.body = wrapper.getBody();
            this.msg = "success";
        } else {
            this.code = wrapper.getCode();
            this.msg = wrapper.getDesc();
        }
    }

    public HttpResult(ErrorEnum errorEnum) {
        this.success = false;
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMessage();
    }

    public HttpResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.success = false;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
