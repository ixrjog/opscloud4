package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsNexusConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/8/5 5:47 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NexusDsInstanceConfig extends BaseDsInstanceConfig {

    private DsNexusConfig.Nexus nexus;
}

