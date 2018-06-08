package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2016/12/19.
 */
public class ZabbixRequest {

    private String jsonrpc = "2.0";

    private Map<String, Object> params = new HashMap<>();

    private String method;

    private String auth;

    private Integer id;

    public void putParam(String key, Object value) {
        params.put(key, value);
    }

    public Object removeParam(String key) {
        return params.remove(key);
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Object getParams() {
        if (params.containsKey("params")) {
            return params.get("params");
        } else {
            return params;
        }
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
