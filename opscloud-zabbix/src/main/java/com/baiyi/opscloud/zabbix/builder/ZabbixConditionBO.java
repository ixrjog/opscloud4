package com.baiyi.opscloud.zabbix.builder;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/3 2:42 下午
 * @Version 1.0
 */
@Data
@Builder
public class ZabbixConditionBO {

    private Integer conditiontype;
    private Integer operator;
    private String value;
    private String formulaid;

}
