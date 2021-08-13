package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/8/3 10:02 上午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixHostInterface {

   // @JsonProperty("interfaceid")
    private String interfaceid;

    private String dns;

   // @JsonProperty("hostid")
    private String hostid;

    private String ip;

    /**
     * 该接口是否在主机上用作默认接口。主机上只能有一种类型的接口作为默认设置。
     * 0 - 不是默认；
     * 1 - 默认。
     */
    private Integer main;

    private String port;


    /**
     * 接口类型。
     * 1 - agent；
     * 2 - SNMP；
     * 3 - IPMI；
     * 4 - JMX。
     */
    private Integer type;


   // @JsonProperty("useip")
    private Integer useip;
}
