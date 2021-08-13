package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 1:54 下午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixHost {

    //@JsonProperty("hostid")
    private String hostid;

    /**
     * 主机的正式名称
     */
    private String host;

    /**
     * Zabbix agent的可用性。
     * 0 - （默认）未知
     * 1 - 可用；
     * 2 - 不可用。
     */
    private Integer available;

    private String description;

    /**
     * 主机的来源。
     * 0 - 普通主机；
     * 4 - 自动发现的主机。
     */
    private Integer flags;

    /**
     * 主机可见名
     */
    private String name;

    /**
     * 主机的状态。
     * 0 - (默认) 已监控的主机；
     * 1 - 未监控的主机。
     */
    private Integer status;

    private List<ZabbixHostInterface> interfaces;

}
