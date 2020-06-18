package com.baiyi.opscloud.zabbix.builder;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/2 10:25 上午
 * @Version 1.0
 */
@Data
public class ZabbixParameter {

    private String name;
    private String value;

    public ZabbixParameter() {

    }

    public ZabbixParameter(String name,
                           String value) {
        this.name = name;
        this.value = value;

    }

}
