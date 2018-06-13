package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.annotation.JSONField;
import com.sdg.cmdb.domain.server.ServerVO;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * 新增Host
 */
public class ZabbixHost implements Serializable {
    private static final long serialVersionUID = 1701291335452566882L;

    private ServerVO serverVO;

    private String host;

    private String ip;


    public ZabbixHost() {
    }

    public ZabbixHost(List<ZabbixTemplateVO> templates) {
        this.templates = templates;
    }

    /**
     * 使用代理
     */
    private boolean useProxy;


    private List<ZabbixProxy> proxys;

    private List<ZabbixTemplateVO> templates;

    public ServerVO getServerVO() {
        return serverVO;
    }

    public void setServerVO(ServerVO serverVO) {
        this.serverVO = serverVO;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }


    public List<ZabbixProxy> getProxys() {
        return proxys;
    }

    public void setProxys(List<ZabbixProxy> proxys) {
        this.proxys = proxys;
    }

    public void setTemplates(List<ZabbixTemplateVO> templates) {
        this.templates = templates;
    }

    public List<ZabbixTemplateVO> getTemplates() {
        return templates;
    }
}
