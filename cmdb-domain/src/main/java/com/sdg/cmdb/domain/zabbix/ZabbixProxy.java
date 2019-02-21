package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.zabbix.response.ZabbixResponseProxy;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixProxy implements Serializable {
    private static final long serialVersionUID = 1593459167538719668L;
    private String host;
    private String proxyid;
    private boolean selected = false;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public ZabbixProxy() { }

    public ZabbixProxy(JSONObject proxy) {
        try {
            this.host = proxy.getString("host");
            this.proxyid = proxy.getString("proxyid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ZabbixProxy(ZabbixResponseProxy proxy) {
        this.host = proxy.getHost();
        this.proxyid = proxy.getProxyid();
    }

}

