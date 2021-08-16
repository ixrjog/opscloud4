package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
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

