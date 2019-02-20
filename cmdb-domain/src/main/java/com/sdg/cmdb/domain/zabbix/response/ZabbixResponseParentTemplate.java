package com.sdg.cmdb.domain.zabbix.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ZabbixResponseParentTemplate implements Serializable {
    private static final long serialVersionUID = 1772195319471153839L;

    private String hostid;
    private List<ZabbixResponseTemplate> parentTemplates;


}
