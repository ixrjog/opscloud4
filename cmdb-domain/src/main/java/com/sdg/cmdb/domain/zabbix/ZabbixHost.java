package com.sdg.cmdb.domain.zabbix;


import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerVO;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

/**
 * 新增Host
 */
@Data
public class ZabbixHost implements Serializable {
    private static final long serialVersionUID = 1701291335452566882L;

    private ServerVO serverVO;
    private String host;
    private String ip;
    /**
     * 使用代理
     */
    private boolean useProxy;
    // TODO 选中的代理
    private ZabbixProxy proxy;
    private List<ZabbixProxy> proxys;
    private List<ZabbixTemplateVO> templates;

    public ZabbixHost() { }
    public ZabbixHost(List<ZabbixTemplateVO> templates) {
        this.templates = templates;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
