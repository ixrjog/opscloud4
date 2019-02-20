package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于校验ZabbixAPI配置是否生效
 */
@Data
public class ZabbixVersion implements Serializable {

    private static final long serialVersionUID = 1656665563968452434L;

    private String version;
    // "name": "Zabbix server"
    private String name;
    private String hostid;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }



}
