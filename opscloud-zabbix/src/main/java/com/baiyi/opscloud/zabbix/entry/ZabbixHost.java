package com.baiyi.opscloud.zabbix.entry;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/1/2 2:55 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZabbixHost extends ResultMapper implements Serializable {

    private static final long serialVersionUID = -4755960186281669971L;
}
