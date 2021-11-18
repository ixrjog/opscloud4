package com.baiyi.opscloud.zabbix.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/8/3 10:02 上午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixHostInterface implements Serializable {

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


    /**
     * 是否应通过IP进行连接。
     *
     * 可能的值：
     * 0 - 使用主机DNS名称连接；
     * 1 - 使用该主机接口的主机IP地址进行连接。
     */
    private Integer useip;
}
