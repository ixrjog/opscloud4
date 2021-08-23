package com.baiyi.opscloud.zabbix.entry.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/8/23 4:09 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixTag implements Serializable {

    private String tag;
    private String value;

}
