package com.baiyi.opscloud.zabbix.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/8/24 4:35 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixAction implements Serializable {

    private String actionid;
    private String name;
    private String eventsource;
    private String status;

}
