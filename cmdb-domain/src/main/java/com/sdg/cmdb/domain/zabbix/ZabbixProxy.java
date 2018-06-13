package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;


public class ZabbixProxy implements Serializable{
    private static final long serialVersionUID = 1593459167538719668L;
    private String host;

    private String proxyid;

    private boolean selected = false;


    @Override
    public String toString() {
        return "ZabbixProxy{" +
                "host='" + host + '\'' +
                ", proxyid='" + proxyid + '\'' +
                '}';
    }

    public ZabbixProxy() {

    }

    public ZabbixProxy(JSONObject proxy) {
        try {
            this.host = proxy.getString("host");
            this.proxyid = proxy.getString("proxyid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProxyid() {
        return proxyid;
    }

    public void setProxyid(String proxyid) {
        this.proxyid = proxyid;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

