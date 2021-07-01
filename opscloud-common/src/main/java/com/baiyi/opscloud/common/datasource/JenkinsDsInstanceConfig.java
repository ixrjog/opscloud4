package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsJenkinsConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/7/1 1:51 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JenkinsDsInstanceConfig extends BaseDsInstanceConfig {

    private DsJenkinsConfig.Jenkins jenkins;
}
