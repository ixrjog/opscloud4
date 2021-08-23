package com.baiyi.opscloud.zabbix.entry;

import com.baiyi.opscloud.zabbix.entry.base.ZabbixTag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/23 4:12 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixHostTag implements Serializable {

    private String hostid;

    private String name;

    private List<ZabbixTag> tags;

    private List<ZabbixTag> inheritedTags;

}
