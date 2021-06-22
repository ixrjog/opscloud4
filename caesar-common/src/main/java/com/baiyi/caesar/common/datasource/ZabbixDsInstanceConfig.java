package com.baiyi.caesar.common.datasource;

import com.baiyi.caesar.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsZabbixConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/6/22 1:38 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ZabbixDsInstanceConfig extends BaseDsInstanceConfig {

    private DsZabbixConfig.Zabbix zabbix;
}

