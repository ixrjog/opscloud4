package com.sdg.cmdb.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/1.
 */
public class HttpResult<T> implements Serializable {
    private static final long serialVersionUID = 620147525754115016L;

    private boolean success;

    private String code;

    private String msg;

    private boolean disableCircularReferenceDetect = false;

    private T body;

    public HttpResult(T body) {
        this.success = true;
        this.body = body;
    }

    public HttpResult(T body, boolean disableCircularReferenceDetect) {
        this.success = true;
        this.body = body;
        this.disableCircularReferenceDetect = true;
    }

    public HttpResult(String code, String msg) {
        this.success = false;
        this.code = code;
        this.msg = msg;
    }

    public HttpResult(String code, String msg,boolean disableCircularReferenceDetect) {
        this.success = false;
        this.code = code;
        this.msg = msg;
        this.disableCircularReferenceDetect = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public T getBody() {
        // return (T) JSONObject.parse(JSON.toJSONString(body, SerializerFeature.DisableCircularReferenceDetect));
        if(disableCircularReferenceDetect){
            return (T) JSONObject.parse(JSON.toJSONString(body, SerializerFeature.DisableCircularReferenceDetect));
        }
        return body;


    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", body=" + JSON.toJSONString(body, SerializerFeature.DisableCircularReferenceDetect) +
                '}';
    }
}
