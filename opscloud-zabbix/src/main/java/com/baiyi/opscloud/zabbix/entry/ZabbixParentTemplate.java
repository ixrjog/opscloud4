package com.baiyi.opscloud.zabbix.entry;

import lombok.Data;

import java.util.List;

@Data
public class ZabbixParentTemplate {


    private String hostid;
    private List<ZabbixTemplate> parentTemplates;


}
